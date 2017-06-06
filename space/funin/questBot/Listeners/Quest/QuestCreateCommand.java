package space.funin.questBot.Listeners.Quest;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.Quest;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

public class QuestCreateCommand implements CommandExecutor {
    private static Logger logger = LoggerFactory.getLogger(QuestCreateCommand.class);

	@Command(aliases = { "!!newQuest", "!!new", "!!addQuest",
			"!!add" }, description = "Creates a new Quest.", usage = "!!newQuest [@role] [@qm] [\"quest name\"] [\"search string\"] <\"quest description\">", async = true)
	public String onCreateQuestCommand(String[] args, Channel channel, Message message, Server server, DiscordAPI api)
			throws InterruptedException, ExecutionException {
		boolean isEmbed = CommandUtils.isEmbed(args);

		String messageContent = message.getContent();
		messageContent = CommandUtils.removeCommand(messageContent);
		messageContent = CommandUtils.removeQuiet(messageContent);

		// at least 4 args required
		if (args.length < 4) {
			return "!!newQuest : " + CommandResponses.errorArgAmount;
		}

		logger.info(CommandUtils.removeCommand(message.getContent()));
		String[] combinedArgs = CommandUtils.combineCommands(messageContent);
		combinedArgs = CommandUtils.splitFirstCommand(combinedArgs);

		// at least 4 args required after commands are combined
		// checking again because args got modified
		if (combinedArgs.length < 4) {
			return "!!newQuest : " + CommandResponses.errorArgAmount;
		}

		if (combinedArgs[0].startsWith("<@&") && combinedArgs[0].endsWith(">")) {
			// arg1 should be a qm(user)
			if (combinedArgs[1].startsWith("<@") && combinedArgs[1].endsWith(">")) {
				String roleID = combinedArgs[0].substring(3, combinedArgs[0].length() - 1);

				// if the user has a nickname, the mention starts with "<@!"
				// instead of "<@"
				int substringstart = combinedArgs[1].contains("<@!") ? 3 : 2;
				String userID = combinedArgs[1].substring(substringstart, combinedArgs[1].length() - 1);

				String questName = combinedArgs[2];
				String questTitle = combinedArgs[3];
				String questDescription = "None";
				if (combinedArgs.length > 4)
					questDescription = combinedArgs[4];

				Quest quest = new Quest(roleID, userID, questName, questTitle, questDescription);
				Map<String, Quest> questMap = Settings.getMap();
				questMap.put(roleID, quest);

				CommandUtils.questInfoCreator(Color.CYAN, quest, channel, server, isEmbed);
				Settings.saveMap(questMap);
				return "";
			}
		}
		return "!!newQuest : " + CommandResponses.errorArgMention;
	}
}
