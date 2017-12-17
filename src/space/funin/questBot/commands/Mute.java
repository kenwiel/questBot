package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Exceptions.RoleAlreadyPresentException;
import space.funin.questBot.Helper;
import space.funin.questBot.QuestBot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Mute implements CommandExecutor {

    @Command(aliases = {"mute"}, privateMessages = false, async = true)
    public void onMute(Server server, Message message, ServerTextChannel channel) {
        List<User> toMute = message.getMentionedUsers();

        Optional<Role> muted = server.getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("muted")).findAny();

        Optional<Duration> duration = Helper.getDurationFromString(message.getContent().split(" "));
        Duration muteDuration = duration.orElseGet(() -> Duration.parse("PT10M"));

        if (muted.isPresent()) {
            StringBuilder sb = new StringBuilder().append("**").append("Muted until ").append(muteDuration.addTo(LocalDateTime.now())).append(":**").append('\n');

            toMute.forEach(user -> {
                try {
                    Helper.addRole(server, user, muted.get());
                } catch (RoleAlreadyPresentException ignored) {
                    //doesnt matter
                }
                sb.append(user.getDisplayName(server)).append(", ");
                QuestBot.timingHandler.scheduleUnmute(server, user, muteDuration);
            });
            sb.replace(sb.lastIndexOf(", "), sb.length(), "");
            channel.sendMessage(sb.toString());
        } else {
            channel.sendMessage("No \"Muted\" Role exists, please create one first. [Role creation not supported yet.]");
        }

    }
}
