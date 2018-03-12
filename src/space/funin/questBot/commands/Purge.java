package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Helper;
import java.util.concurrent.CompletionException;

public class Purge implements CommandExecutor {

    private static final String DESCRIPTION = "Purges messages from the current channel.";
    private static final String USAGE =
            "**__!!purge__**\n\n"+
            "__Usage:__\n"+
            "!!purge `amount`\n"+
            "\n"+
            "Purges between 2 and 100 messages from the current channel.";

    @Command(aliases = {"purge"}, description = DESCRIPTION, usage = USAGE, privateMessages = false, async = true)
    public void onMute(User user, Server server, Message message, ServerTextChannel channel) {
        if (!Helper.isModerator(user, server))
            return;

        int amount = Integer.parseInt(message.getContent().replaceAll("[\\D]", ""));

        if(amount > 1) {
            if (amount > 100)
                amount = 100;
            try {
                channel.getMessages(amount).join().tailSet(message).deleteAll();
                channel.sendMessage("Deleted " + amount + " messages.");
            } catch (CompletionException e) {
                channel.sendMessage("Unable to bulkdelete messages older than 14 days.");
            }
        } else {
            channel.sendMessage("Not enough messages to bulkdelete.");
        }

    }
}
