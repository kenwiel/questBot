package space.funin.questBot.Listeners.Bot;

import java.util.List;
import java.util.Random;

import de.btobastian.javacord.entities.Channel;
import space.funin.questBot.Settings;

public class BotMentionCommand {
	public static void onBotMention(Channel channel, String[] args) {
		Integer responseNo = null;
		Random random = new Random();
		List<String> responseList = Settings.loadMentionResponses();

		if (!(args == null)) {
			for (String s : args) {
				try {
					responseNo = Integer.parseInt(s);
				} catch (NumberFormatException e) {

				}
			}
		}
		if (responseNo == null || responseNo < 0 || responseNo >= Settings.loadMentionResponses().size())
			responseNo = random.nextInt(responseList.size());

		
		
		channel.sendMessage(responseList.get(responseNo));
	}
}
