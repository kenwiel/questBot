package space.funin.questBot.Listeners.Server;

import java.awt.Color;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.utils.CommandUtils;

public class ServerSetupMuteCommand implements CommandExecutor {
	DiscordAPI api;

	public ServerSetupMuteCommand(DiscordAPI api) {
		this.api = api;
	}

	@Command(aliases = { "!!setupMute", "!!muteSetup"}, description = "Sets up Muted permissions on all channels", usage = "!!setupMute", async = true)
	public void onSetupMuteCommand(User user, Server server, Channel channel) throws InterruptedException, ExecutionException {
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!mute : " + CommandResponses.errorPermissions);
			return;
		}
		Role muted = CommandUtils.getMutedRole(server);

		//muted role doesnt exist -> create it
		if (muted == null) {
			//create muted role
			muted = server.createRole().get();
			Permissions rolePermissions = api.getPermissionsBuilder().build();
			muted.update("Muted", new Color(0), false, rolePermissions, false, false).get();

			muted = CommandUtils.getMutedRole(server);
		}
		
		//channel permissions for muted role: send messages is denied
		Permissions channelPermissions = api.getPermissionsBuilder().setState(PermissionType.SEND_MESSAGES, PermissionState.DENIED).build();
		
		for(Channel c : server.getChannels()) {
			c.updateOverwrittenPermissions(muted, channelPermissions);
		}
	}
}
