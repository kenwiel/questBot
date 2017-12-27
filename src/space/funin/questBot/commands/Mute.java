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

    private static final String DESCRIPTION = "Mutes any amount of users.";
    private static final String USAGE =
            "**__!!mute__**\n\n"+
            "__Usage:__\n"+
            "!!mute `@user`\n"+
            "!!mute `@user` `duration`\n"+
            "!!mute `@user` `@user` `@user`\n"+
            "!!mute `@user` `@user` `@user` `duration`\n"+
            "!!mute `@user` 1d4h30m5s\n"+
            "\n"+
            "Mutes any amount of users for the given duration";

    @Command(aliases = {"mute"}, description = DESCRIPTION, usage = USAGE, privateMessages = false, async = true)
    public void onMute(User user, Server server, Message message, ServerTextChannel channel) {
        if (!Helper.isModerator(user, server))
            return;

        List<User> toMute = message.getMentionedUsers();

        Optional<Role> muted = server.getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("muted")).findAny();

        Optional<Duration> duration = Helper.getDurationFromString(message.getContent().split(" "));
        Duration muteDuration = duration.orElseGet(() -> Duration.parse("PT10M"));

        if (muted.isPresent()) {
            StringBuilder sb = new StringBuilder().append("**").append("Muted until ").append(muteDuration.addTo(LocalDateTime.now())).append(":**").append('\n');

            toMute.forEach(userToMute -> {
                try {
                    Helper.addRole(server, userToMute, muted.get());
                } catch (RoleAlreadyPresentException ignored) {
                    //doesnt matter
                }
                sb.append(userToMute.getDisplayName(server)).append(", ");
                QuestBot.getTimingHandler().scheduleUnmute(server, userToMute, muteDuration);
            });
            sb.replace(sb.lastIndexOf(", "), sb.length(), "");
            channel.sendMessage(sb.toString());
        } else {
            channel.sendMessage("No \"Muted\" Role exists, please create one first. [Role creation not supported yet.]");
        }

    }
}
