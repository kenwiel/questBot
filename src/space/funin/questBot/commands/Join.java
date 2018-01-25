package space.funin.questBot.commands;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.List;

public class Join implements CommandExecutor {

    private static final String DESCRIPTION = "Joins one or more roles";
    private static final String USAGE =
            "**__!!join__**\n\n"+
                    "__Usage:__\n"+
                    "!!join `@role`\n"+
                    "!!join `@role` `@role` `@role`...\n\n"+
                    "Joins one or more roles";

    @Command(aliases = {"join"},  description = DESCRIPTION, usage = USAGE ,async = true)
    public void onCall(Server server, User user, ServerTextChannel channel, Message message) {
        List<Role> rolesToJoin = message.getMentionedRoles();
        List<Role> userRoles = user.getRoles(server);

        int old = userRoles.size();

        rolesToJoin.stream().filter(role -> !userRoles.contains(role)).forEach(userRoles::add);
        final int joined = userRoles.size() - old;

        server.updateRoles(user, userRoles).thenAccept((ignored) ->{
            channel.sendMessage("Joined " + joined + " roles.");
            }).exceptionally((ignored) -> {
            channel.sendMessage("Unable to join all roles: Missing Permissions");
            return null;
        });

    }
}
