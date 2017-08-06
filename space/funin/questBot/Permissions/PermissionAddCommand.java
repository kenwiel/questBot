package space.funin.questBot.Permissions;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PermissionAddCommand implements CommandExecutor {

	@Command(aliases = { "!!addPermission" }, description = "Gives a user the stated permission", usage = "!!addPermission [@user] [Permission]", async = true)
	public void onPermissionAddCommand(String[] args, Server server, User user, Channel channel) {
		
	}
}
