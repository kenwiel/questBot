package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.List;

public class Leave implements CommandExecutor {

    private static final String DESCRIPTION = "Leaves one or more roles";
    private static final String USAGE =
            "**__!!leave__**\n\n"+
                    "__Usage:__\n"+
                    "!!leave `@role`\n"+
                    "!!leave `@role` `@role` `@role`...\n\n"+
                    "Leaves one or more roles";

    @Command(aliases = {"leave"},  description = DESCRIPTION, usage = USAGE ,async = true)
    public void onCall(Server server, User user, ServerTextChannel channel, Message message) {
        List<Role> rolesToLeave = message.getMentionedRoles();
        List<Role> userRoles = user.getRoles(server);

        int old = userRoles.size();

        rolesToLeave.stream().filter(userRoles::contains).forEach(userRoles::remove);
        server.updateRoles(user, userRoles);
        final int left = old - userRoles.size();

        server.updateRoles(user, userRoles).thenAccept((ignored) ->{
            channel.sendMessage("Left " + left + " role" + (left>1 || left == 0 ? "s." : "."));
        }).exceptionally((ignored) -> {
            channel.sendMessage("Unable to leave all roles: Missing Permissions");
            return null;
        });

    }
}
