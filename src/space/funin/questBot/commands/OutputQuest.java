package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Quests.Quest;
import space.funin.questBot.Quests.QuestHandler;
import space.funin.questBot.Quests.QuestHelper;

import java.util.Optional;

public class OutputQuest implements CommandExecutor {
    private QuestHandler questHandler;

    public OutputQuest(QuestHandler questHandler) {
        this.questHandler = questHandler;
    }

    @Command(aliases = {"info", "qi", "questinfo", "quest"})
    public void onCall(Message message, ServerTextChannel channel, Server server) {
        for (Role r : message.getMentionedRoles()) {
            Optional<Quest> quest = questHandler.getQuestByRole(r);

            quest.ifPresent(value -> channel.sendMessage(QuestHelper.embedQuest(value, server)));
        }
    }
}
