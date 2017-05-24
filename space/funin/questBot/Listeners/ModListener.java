package space.funin.questBot.Listeners;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import space.funin.questBot.runnables.RunnableUnmute;
import space.funin.questBot.utils.CommandUtils;

public class ModListener implements CommandExecutor {
	ScheduledExecutorService executor;
	DiscordAPI api;

	public ModListener(ScheduledExecutorService executor, DiscordAPI api) {
		this.executor = executor;
		this.api = api;
		unmuteAll();
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

	@Command(aliases = { "!!purge", "!!p",
			"!!clean" }, description = "Deletes the last x messages from the channel. Moderator only.", usage = "!!purge [amount]", async = true)
	public void onPurgeCommand(String[] args, User user, Server server, Channel channel, Message message) {
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!purge : " + CommandResponses.errorPermissions);
			return;
		}
		Integer amount = null;
		try {
			amount = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(amount <= 0 || amount == null)
			return;
		if(amount > 100)
			amount = 100;
		
		List<Message> lastMessages = null;
		try {
			lastMessages = new ArrayList<Message>(channel.getMessageHistory(amount+1).get().getMessages());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		if(lastMessages == null) {
			channel.sendMessage("!!purge : " + CommandResponses.errorNull);
			return;
		}
		for(Message m : lastMessages) {
			if(!m.equals(message))
				m.delete();
		}
	}
	
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
		
		for(String s : args) {
			Role r = server.createRole().get();
			Permissions permissions = api.getPermissionsBuilder().build();
			r.update(s, new Color(0), false, permissions, false, true).get();
			
			channel.sendMessage("Created Role: " + r.getMentionTag());
		}
	}
}
