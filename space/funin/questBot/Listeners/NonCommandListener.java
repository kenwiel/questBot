package space.funin.questBot.Listeners;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import space.funin.questBot.Listeners.Bot.BotMentionCommand;
import space.funin.questBot.Listeners.Quest.QuestMentionCommand;

public class NonCommandListener implements MessageCreateListener {

	public void onMessageCreate(final DiscordAPI api, final Message message) {
		final Channel channel = message.getChannelReceiver();
		final User user = message.getAuthor();
		final String[] args = getArgs(message);

		//if they use brackets when using the command, call them retarded
		if (message.getContent().matches("!!.+\\s[<\\[].+")) {
			message.reply("RTFM: !!help");
		}
		
		if (user.equals(api.getYourself()))
			return;

		// only check messages that contain a group mention and DONT start with
		// a bot command
		if (message.getContent().contains("<@&") && !message.getContent().startsWith("!!")) {
			QuestMentionCommand.onGroupMention(api, message, channel);
		}

		// if bot is mentioned
		if (message.getMentions().contains(api.getYourself()) && !message.getContent().startsWith("!!")) {
			BotMentionCommand.onBotMention(args, channel);
		}
	}

	private String[] getArgs(Message message) {
		String[] splitContent = message.getContent().split(" ");
		
		if (splitContent.length <= 1)
			return null;
		
		String[] temporary = new String[splitContent.length - 1];
		//remove the first entry
		for (int i = 0; i < temporary.length; i++) {
			temporary[i] = splitContent[i + 1];
		}
		return temporary;
	}
}
