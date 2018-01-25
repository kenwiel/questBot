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

    private static final String DESCRIPTION = "Registers a new quest in the bot.";
    private static final String USAGE =
            "**__!!addQuest__**\n\n"+
            "__Usage:__\n"+
            "!!addQuest --name `name` --author `@user` --role `@role`\n"+
            "!!addQuest --name `name` --author `@user` --role `@role` --archive `archive` --description `description` --searchstring `searchstring`\n"+
            "__Options:__\n"+
            "*Required arguments:*\n```"+
            "--name <name can have spaces>  The name of the quest\n"+
            "--author <@user> [@user...]    The author(s) of the quest\n"+
            "--role <@role>                 The role of the quest\n"+
            "```*Optional arguments:*\n```"+
            "--archive <archive link>       The archive link of the quest\n"+
            "--description <description>    The description of the quest\n"+
            "--searchstring <search string> The string to search for; defaults to <name> if not provided\n"+
            "```\n"+
            "Creates or modifies the quest with the given `@role`";

    @Command(aliases = {"addQuest"},description = DESCRIPTION, usage = USAGE, async = true)
    @SuppressWarnings("unchecked")
    public void onCall(Message message, ServerTextChannel channel) {
        System.out.println("in");

        //remove command from input
        String input = message.getContent().replaceFirst("../addquest/i", "");
        Map<QuestProperty, Object> mapping = QuestHelper.parseQuestString(input, message);

        try {
            //unchecked casts dont matter, QuestHelper#parseQuestString makes sure the correct values are in the map
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
        } catch (IllegalArgumentException e) { //in case of missing arguments
            channel.sendMessage(e.getMessage());
        }

    }
}
