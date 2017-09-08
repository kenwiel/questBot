package space.funin.questBot.Listeners.Bot;

import java.util.List;
import java.util.Random;

import de.btobastian.javacord.entities.Channel;
import space.funin.questBot.Settings;

public class BotMentionCommand {
	public static void onBotMention(String[] args, Channel channel) {
		Integer responseNo = null;
		boolean responseOutOfBounds = false;
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
		if (responseNo == null) {
			responseNo = random.nextInt(responseList.size());
		} else {
			if (responseNo < 0 || responseNo >= Settings.loadMentionResponses().size()) {
				responseNo = random.nextInt(responseList.size());
				responseOutOfBounds = true;
			}
		}
		if (responseOutOfBounds)
			channel.sendMessage("Only " + (Settings.loadMentionResponses().size() - 1) + " responses available.");
		channel.sendMessage(responseList.get(responseNo));
	}
}
