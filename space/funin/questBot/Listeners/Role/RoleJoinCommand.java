package space.funin.questBot.Listeners.Role;

import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;

public class RoleJoinCommand implements CommandExecutor {
	@Command(aliases = { "!!join", "!!rank",
			"!!role", }, description = "Join the mentioned role(s)", usage = "!!join [@mention]*", async = true)
	public String onJoinCommand(String[] args, User user, Channel channel, Server server) {
		String roleID;
		for (String s : args) {
			if (s.startsWith("<@&") && s.endsWith(">")) {
				// if the mentioned role is a quest
				roleID = s.substring(3, s.length() - 1);
				Role role = QuestBot.getRole(roleID, server);

				if (Settings.getMap().containsKey(roleID) && !user.getRoles(server).contains(role)) {
					try {
						role.addUser(user).get();
						channel.sendMessage("!!join: Joined role " + role.getMentionTag());
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					channel.sendMessage("!!join: " + role.getMentionTag() + " : " + CommandResponses.errorInvalidRole);
				}
			}
		}
		return "";
	}
}
