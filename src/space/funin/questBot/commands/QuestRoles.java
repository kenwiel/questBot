package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Helper;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Quests.Quest;

import java.util.List;

public class QuestRoles implements CommandExecutor {

    private static final String DESCRIPTION = "Prints the list of quests registered on this server";
    private static final String USAGE =
            "**__!!quests__**\n\n"+
                    "__Usage:__\n"+
                    "!!quests`\n"+
                    "Prints the list of quests registered on this server";

    @Command(aliases = {"quests"},  description = DESCRIPTION, usage = USAGE ,async = true)
    public void onCall(Server server, ServerTextChannel channel, Message message) {
        List<Quest> questList = QuestBot.getQuestHandler().getQuestList(server);
        if (questList.size() == 0) {
            channel.sendMessage("No Quests have been registered on this server.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("**__!!quests__**\n\n```");

        //find longest quest title
        int longest = 10; //length of "Quest Name"
        for (Quest q : questList) {
            longest = longest < q.getName().length() ? q.getName().length() : longest;
        }
        longest += 1;

        //add quests to output
        sb.append(Helper.padLeft("Quest Name", longest))
                .append(" | ")
                .append("Quest Role")
                .append("\n");

        for (Quest q : questList) {
            sb.append(Helper.padLeft(q.getName(), longest))
                    .append(" | ")
                    .append("@")
                    .append(q.getRole().getName())
                    .append("\n");
        }
        sb.append("```");

        channel.sendMessage(sb.toString());
    }
}
