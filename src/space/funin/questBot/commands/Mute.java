package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.Optional;

public class Mute implements CommandExecutor {

    @Command(aliases = {"mute"}, privateMessages = false, async = true)
    public void onMute(MessageCreateEvent e) {
        Optional<Server> server = e.getServer();
        Optional<User> author = e.getMessage().getUserAuthor();
        //@TODO do things once Message#getMentions is a thing again
    }
}
