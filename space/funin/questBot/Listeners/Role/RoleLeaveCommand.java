package space.funin.questBot.Listeners.Role;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.Settings;

public class RoleLeaveCommand implements CommandExecutor {
	@Command(aliases = { "!!leave",
			"!!l" }, description = "Leave the mentioned role(s)", usage = "!!leave [@role]*", async = true)
	public void onLeaveCommand(Message message, User user, Channel channel, Server server)
			throws InterruptedException, ExecutionException {
		List<Role> mentions = message.getMentionedRoles();

		for (Role r : mentions) {
			if (Settings.getMap().containsKey(r.getId()) && user.getRoles(server).contains(r)) {
				r.removeUser(user).get();
				channel.sendMessage("!!leave: Removed role " + r.getMentionTag());
			} else {
				channel.sendMessage("!!leave: " + r.getMentionTag() + " : " + CommandResponses.errorInvalidRoleMissing);
			}
		}
	}
}
