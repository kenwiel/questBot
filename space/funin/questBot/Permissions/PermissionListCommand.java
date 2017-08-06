package space.funin.questBot.Permissions;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PermissionListCommand implements CommandExecutor {

	@Command(aliases = { "!!listPermissions", "!!listPermission" }, description = "Lists all Permissions <for a user>", usage = "!!listPermissions <@user>", async = true)
	public void onPermissionAddCommand(String[] args, Server server, User user, Channel channel) {
		
	}
}
