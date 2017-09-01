package space.funin.questBot.Listeners.Channel;

import java.util.ArrayList;
import java.util.List;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class ChannelListUserCommand implements CommandExecutor {
	@Command(aliases = {
			"!!listUsers" }, description = "Prints a list of users that have access to the current channel", usage = "!!listUsers", async = true)
	public void onListUserCommand(Channel channel, Server server) {
		List<User> userList = new ArrayList<User>(server.getMembers());
		// List<User> allowedUsers = new ArrayList<User>();
		StringBuilder sb = new StringBuilder();

		for (User user : userList) {
			if (hasReadPermission(server, channel, user)) {
				// allowedUsers.add(user);
				sb.append(user.getName() + "\n");
			}
		}
		channel.sendMessage(sb.toString());
	}

	private boolean hasReadPermission(Server server, Channel channel, User user) {
		PermissionState everyonePerms = channel.getServer().getRoleById(channel.getServer().getId()).getPermissions()
				.getState(PermissionType.READ_MESSAGES);
		PermissionState everyoneChannelPerms = channelReadPermissionEveryone(channel);
		PermissionState rolePerms = PermissionState.NONE;
		PermissionState userPerms = channel.getOverwrittenPermissions(user).getState(PermissionType.READ_MESSAGES);
		PermissionState finalPermissions = PermissionState.DENIED;

		for (Role r : user.getRoles(server)) {
			PermissionState override = r.getOverwrittenPermissions(channel).getState(PermissionType.READ_MESSAGES);
			switch (override) {
			case ALLOWED:
				rolePerms = override;
				break;
			case DENIED:
				if (rolePerms.equals(PermissionState.NONE)) {
					rolePerms = override;
				}
				break;
			case NONE:
				break;
			default:
				break;
			}
		}

		if (everyonePerms.equals(PermissionState.ALLOWED)) {
			finalPermissions = PermissionState.ALLOWED;
		}
		if (!everyoneChannelPerms.equals(PermissionState.NONE)) {
			finalPermissions = everyoneChannelPerms;
		}
		if (!rolePerms.equals(PermissionState.NONE)) {
			finalPermissions = rolePerms;
		}
		if (!userPerms.equals(PermissionState.NONE)) {
			finalPermissions = userPerms;
		}
		if (finalPermissions.equals(PermissionState.DENIED)) {
			return false;
		} else {
			return true;
		}
	}

	private PermissionState channelReadPermission(Channel channel, Role role) {
		if (role.getPermissions().getState(PermissionType.ADMINISTRATOR).equals(PermissionState.ALLOWED)) {
			return PermissionState.ALLOWED;
		}
		return channel.getOverwrittenPermissions(role).getState(PermissionType.READ_MESSAGES);
	}

	private PermissionState channelReadPermissionEveryone(Channel channel) {
		Role everyone = channel.getServer().getRoleById(channel.getServer().getId());
		return channelReadPermission(channel, everyone);
	}

}
