package space.funin.questBot.commands;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import space.funin.questBot.QuestBot;

public class RestartCache implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        //Bot owner only
        if (messageCreateEvent.getMessage().getAuthor().isBotOwner()) {
            Message message = messageCreateEvent.getMessage();
            String[] commands = message.getContent().split(" ");
            String command = commands[0];
            if (command.equals("cache")) {
                QuestBot.getTimingHandler().scheduleCacheUpdate();
            }
        }
    }
}
