package space.funin.questBot.commands;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Quests.Quest;
import space.funin.questBot.Quests.QuestHandler;
import space.funin.questBot.Quests.QuestHelper;
import space.funin.questBot.Quests.QuestProperty;

import java.util.List;
import java.util.Map;

public class QuestAdd implements CommandExecutor {

    @Command(aliases = {"addQuest"}, async = true)
    @SuppressWarnings("unchecked")
    public void onCall(Message message, ServerTextChannel channel) {
        //remove command from input
        String input = message.getContent().replaceFirst("../addquest/i", "");
        Map<QuestProperty, Object> mapping = QuestHelper.parseQuestString(input, message);

        //unchecked casts dont matter, QuestHelper#parseQuestString makes sure the correct values are in th map
        Quest quest = new Quest.QuestBuilder(
                (String) mapping.get(QuestProperty.Name),
                (List<User>) mapping.get(QuestProperty.Authors),
                (Role) mapping.get(QuestProperty.Role))
                .setArchive((String) mapping.get(QuestProperty.Archive))
                .setDescription((String) mapping.get(QuestProperty.Description))
                .setSearchString((String) mapping.get(QuestProperty.SearchString))
                .build();

        QuestHelper.linkThread(quest, channel);
        QuestBot.getQuestHandler().registerQuest(quest);
    }
}
