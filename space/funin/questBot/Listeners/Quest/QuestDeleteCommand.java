package space.funin.questBot.Listeners.Quest;

import java.awt.Color;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.Quest;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author Ken Wieland
 *
 *         Handles commands to do with quest
 */
public class QuestDeleteCommand implements CommandExecutor {
	private static Logger logger = LoggerFactory.getLogger(QuestDeleteCommand.class);

	@Command(aliases = { "!!delQuest", "!!del", "!!deleteQuest", "!!rem",
			"!!removeQuest" }, description = "Removes a Quest. Moderator only.", usage = "!!delQuest [@quest]*", async = true)
	public void onDeleteCommand(String[] args, User user, Channel channel, Server server) {
		// command only available to moderators
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!delQuest : " + CommandResponses.errorPermissions);
			return;
		}

		Map<String, Quest> questListComplete = Settings.getMap();
		Quest quest;
		String questID;

		for (String s : args) {
			logger.debug(s);
			if (s.startsWith("<@&") && s.endsWith(">")) {
				// if the mentioned role is a quest
				questID = s.substring(3, s.length() - 1);
				if (questListComplete.containsKey(questID)) {
					quest = questListComplete.get(questID);
					channel.sendMessage("**Removing quest:**");
					CommandUtils.questInfoCreator(Color.RED, quest, channel, server, CommandUtils.isEmbed(args));
					questListComplete.remove(questID);
					Settings.deleteQuest(questID);
				}
			}
		}

		Settings.saveMap(questListComplete);
	}

}
