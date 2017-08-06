package space.funin.questBot.Listeners.Role;

import java.awt.Color;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.utils.CommandUtils;

public class RoleCreateCommand implements CommandExecutor {
	@Command(aliases = {"!!createRole", "!!cr"}, description = "Creates a mentionable role for quest creation. Moderator only.", usage = "!!cr [roleName]*", async = true)
	public void onCreateRoleCommand(String[] args, User user, Server server, Channel channel, Message message, DiscordAPI api) throws InterruptedException, ExecutionException {
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!purge : " + CommandResponses.errorPermissions);
			return;
		}
		//requires at least one argument
		if(args.length == 0) {
			channel.sendMessage(CommandResponses.errorArgAmount);
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String s : args) {
			Role r = server.createRole().get();
			Permissions permissions = api.getPermissionsBuilder().build();
			r.update(s, new Color(0), false, permissions, true).get();
			sb.append("Created Role: " + r.getMentionTag() + "\n");
		}
		
		channel.sendMessage(sb.toString());
	}
}
