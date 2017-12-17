package space.funin.questBot.commands;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Helper;

import java.time.Duration;
import java.util.Optional;

public class PingPong implements CommandExecutor {

    @Command(aliases = { "time" })
    public void onCall(Message message) {
        Optional<Duration> duration = Helper.getDurationFromString(message.getContent().split(" "));

        if (duration.isPresent()) {
            message.getServerTextChannel().ifPresent(serverTextChannel ->
                        serverTextChannel.sendMessage(duration.get().getSeconds() + "s"));
        } else {
            message.getServerTextChannel().ifPresent(serverTextChannel ->
                    serverTextChannel.sendMessage("unable to parse time, use ISO-8601 format"));
        }
    }
}
