package space.funin.questBot.Listeners.Bot;

import java.util.Random;

import de.btobastian.javacord.entities.Channel;
import space.funin.questBot.Settings;

public class BotMentionCommand {
    public static void onBotMention(Channel channel) {
    	Random random = new Random();
    	int responseNo = random.nextInt(Settings.getMentionResponses().length);
    	channel.sendMessage(Settings.getMentionResponses()[responseNo]);
    }
}
