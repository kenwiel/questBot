package space.funin.questBot.Listeners.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.utils.CommandUtils;

public class MessagePurgeCommand implements CommandExecutor {
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
}
