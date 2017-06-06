package space.funin.questBot.Listeners.Quest;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Quest;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

public class QuestInfoCommand implements CommandExecutor {
	private static Logger logger = LoggerFactory.getLogger(QuestInfoCommand.class);

	@Command(aliases = { "!!questInfo", "!!qInfo", "!!quest",
			"!!qi" }, description = "Gives additional info about a quest", usage = "!!QuestInfo [@quest]*", async = true)
	public void onQuestInfoCommand(String[] args, Channel channel, Server server, Message message) {
		logger.info("entering info command");
		Map<String, Quest> questListComplete = Settings.getMap();
		Quest quest;
		String questID;

		List<Role> mentionedRoles = message.getMentionedRoles();
		
		for (Role r : mentionedRoles) {
				// if the mentioned role is a quest
				questID = r.getId();
				if (questListComplete.containsKey(questID)) {
					quest = questListComplete.get(questID);
					CommandUtils.questInfoCreator(Color.CYAN, quest, channel, server, CommandUtils.isEmbed(args));
				}
		}
	}
}
