package space.funin.questBot.Listeners.Bot;

import java.util.Random;

import de.btobastian.javacord.entities.Channel;
import space.funin.questBot.Settings;

public class BotMentionCommand {
	public static void onBotMention(Channel channel, String[] args) {
		Integer responseNo = null;
		Random random = new Random();

		if (!(args == null)) {
			for (String s : args) {
				try {
					responseNo = Integer.parseInt(s);
				} catch (NumberFormatException e) {

				}
			}
		}
		if (responseNo == null || responseNo < 0 || responseNo > Settings.getMentionResponses().size())
			responseNo = random.nextInt(Settings.getMentionResponses().size());

		channel.sendMessage((String) Settings.getMentionResponses().get(responseNo));
	}
}
