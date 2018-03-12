package space.funin.questBot.commands;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import space.funin.questBot.QuestBot;

public class Say implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        //Bot owner only
        if (messageCreateEvent.getMessage().getAuthor().isBotOwner()) {
            System.out.println("by owner");
            Message message = messageCreateEvent.getMessage();
            String[] commands = message.getContent().split(" ");
            String channelId = commands[0];

            String content = message.getContent().replace("" + channelId, "").trim();
            QuestBot.getApi().getTextChannelById(channelId).ifPresent(textChannel -> {
                textChannel.asServerTextChannel().ifPresent(serverTextChannel -> {
                    serverTextChannel.sendMessage(content);
                });
            });
        }
    }
}
