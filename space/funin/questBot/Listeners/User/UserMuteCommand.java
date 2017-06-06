package space.funin.questBot.Listeners.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.runnables.RunnableUnmute;
import space.funin.questBot.utils.CommandUtils;

public class UserMuteCommand implements CommandExecutor {
	ScheduledExecutorService executor;
	DiscordAPI api;

	public UserMuteCommand(ScheduledExecutorService executor, DiscordAPI api) {
		this.executor = executor;
		this.api = api;
		unmuteAll();
	}

	private void unmuteAll() {
		List<Server> serverList = new ArrayList<Server>(api.getServers());
		// for all servers
		for (Server s : serverList) {
			Role muted = CommandUtils.getMutedRole(s);
			List<User> mutedUsers = muted.getUsers();
			// unmute all users
			for (User u : mutedUsers) {
				muted.removeUser(u);
			}
		}
	}

	@Command(aliases = { "!!mute",
			"!!m" }, description = "Mutes a user (default 10 min). Moderator only.", usage = "!!mute [@user]* <Time>", async = true)
	public void onMuteCommand(String[] args, Message message, User user, Server server, Channel channel) {
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!mute : " + CommandResponses.errorPermissions);
			return;
		}

		Integer duration = null;
		for (String s : args) {
			try {
				duration = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (duration == null)
			duration = 10;

		List<User> muteList = message.getMentions();
		Role muted = CommandUtils.getMutedRole(server);

		for (User u : muteList) {
			muted.addUser(u);
			executor.schedule(new RunnableUnmute(u, muted), duration, TimeUnit.MINUTES);
			message.reply("**Muted: **" + u.getName() + " for " + duration + " minutes.");
		}

	}
}
