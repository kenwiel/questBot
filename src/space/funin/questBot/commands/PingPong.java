package space.funin.questBot.commands;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingPong implements CommandExecutor {

    @Command(aliases = { "ping" })
    public void onCall(Message message) {
        message.getServerTextChannel().ifPresent(
                serverTextChannel -> serverTextChannel.sendMessage(
                        message.getContent().replaceFirst("..ping ", "")));
    }
}
