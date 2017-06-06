package space.funin.questBot.Listeners.User;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.utils.CommandUtils;

public class UserUnmuteCommand implements CommandExecutor {
	ScheduledExecutorService executor;
	DiscordAPI api;

	public UserUnmuteCommand(ScheduledExecutorService executor, DiscordAPI api) {
		this.executor = executor;
		this.api = api;
	}

	@Command(aliases = { "!!unmute",
			"!!um" }, description = "Unmutes a user. Moderator only.", usage = "!!unmute [@user]*", async = true)
	public void onMuteCommand(String[] args, Message message, User user, Server server, Channel channel) throws InterruptedException, ExecutionException {
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!mute : " + CommandResponses.errorPermissions);
			return;
		}

		List<User> unmuteList = message.getMentions();
		Role muted = CommandUtils.getMutedRole(server);

		if(muted == null) {
			System.out.println("muted is null");
		}
		
		for (User u : unmuteList) {
			muted.removeUser(u).get();
			message.reply("**Unmuted: **" + u.getName());
		}

	}
}
