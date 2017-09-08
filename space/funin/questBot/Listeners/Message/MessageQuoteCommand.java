package space.funin.questBot.Listeners.Message;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.utils.CommandUtils;

public class MessageQuoteCommand implements CommandExecutor {
	@Command(aliases = { "!!quote",
			"!!q" }, description = "Quotes a message", usage = "!!quote [messageID]", async = true)
	public void onQuoteCommand(String[] args, Server server, Channel channel) {
		if (args.length != 1) {
			channel.sendMessage("!! quote : " + CommandResponses.errorArgAmount);
			return;
		}
		System.out.println(args[0]);
		Message toQuote;
		try {
			toQuote = channel.getMessageById(args[0]).get();
		} catch (InterruptedException | ExecutionException e) {
			channel.sendMessage("!!quote : " + CommandResponses.errorOperationFailed);
			e.printStackTrace();
			return;
		}
		User author = toQuote.getAuthor();
		String authorName = (author.getNickname(server) == null) ? author.getName() : author.getNickname(server);
		URL authorAvatar = author.getAvatarUrl();
		String messageContent = toQuote.getContent();
		Calendar messageTimestampCalendar = toQuote.getCreationDate();

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'UTC'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String messageTimestamp = df.format(messageTimestampCalendar.getTime());

		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(authorName, null, authorAvatar.toString()).setDescription(messageContent)
				.setFooter(messageTimestamp);
		
		channel.sendMessage("", eb);

	}
}
