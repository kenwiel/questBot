package space.funin.questBot.commands;

import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingPong implements CommandExecutor {

    @Command(aliases = { "ping10" })
    public void onCall(MessageCreateEvent event) {
        System.out.println("in");
    }
}
