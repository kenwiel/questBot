package space.funin.questBot.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;

/**
 * @author Ken Wieland
 *
 *         Handles commands to do with creating/removing and assigning roles
 */
public class RoleListener implements CommandExecutor {
    
    @Command(aliases = { "!!join", "!!rank", "!!role", }, description = "Join the mentioned role(s)",
            usage = "!!join [@mention]*", async = true)
    public String onJoinCommand(String[] args, User user, Channel channel, Server server) {
        String roleID;
        for (String s : args) {
            if (s.startsWith("<@&") && s.endsWith(">")) {
                // if the mentioned role is a quest
                roleID = s.substring(3, s.length() - 1);
                Role role = QuestBot.getRole(roleID, server);
                
                if (Settings.getMap().containsKey(roleID) && !user.getRoles(server).contains(role)) {
                    try {
                        role.addUser(user).get();
                        channel.sendMessage("!!join: Joined role " + role.getMentionTag());
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    channel.sendMessage("!!join: " + role.getMentionTag() + " : " + CommandResponses.errorInvalidRole);
                }
            }
        }
        return "";
    }
    
    @Command(aliases = { "!!leave", "!!l" }, description = "Leave the mentioned role(s)", usage = "!!leave [@mention]*",
            async = true)
    public String onLeaveCommand(String[] args, User user, Channel channel, Server server) {
        String roleID;
        for (String s : args) {
            if (s.startsWith("<@&") && s.endsWith(">")) {
                // if the mentioned role is a quest
                roleID = s.substring(3, s.length() - 1);
                Role role = QuestBot.getRole(roleID, server);
                
                if (Settings.getMap().containsKey(roleID) && user.getRoles(server).contains(role)) {
                    role.removeUser(user);
                    channel.sendMessage("!!leave: Removed role " + role.getMentionTag());
                } else {
                    channel.sendMessage("!!leave: " + role.getMentionTag() + " : " + CommandResponses.errorInvalidRole);
                }
            }
        }
        return "";
    }
    
    @Command(aliases = { "!!addRank", "!!createRole", "!!create", "!!addRole", }, description = "Creates a quest Role",
            usage = "!!addRank [roleName]", async = true)
    public String onCreateCommand(String[] args, User user, Channel channel, Server server, DiscordAPI api) {
        // command only available to moderators
        List<Role> userRoles = new ArrayList<Role>(user.getRoles(server));
        Role moderator = QuestBot.getRole("242403619108814850", server);
        if (!userRoles.contains(moderator)) {
            return "!!createRole: " + CommandResponses.errorPermissions;
        }
        
        Role newRole = null;
        try {
            newRole = server.createRole().get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (newRole == null)
            return "!!createRole: " + CommandResponses.errorNull;
                
        try {
            newRole.updateName(args[0]).get();
            return "Created role " + newRole.getMentionTag();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "!!createRole: " + CommandResponses.errorTimeout;
    }
    
    @Command(aliases = { "!!removeRole", "!!deleteRole", "!!delRole" }, description = "Removes role(s)",
            usage = "!!removeRole [@role]*", async = true)
    public String onRemoveCommand(String[] args, User user, Server server) {
        // command only available to moderators
        List<Role> userRoles = new ArrayList<Role>(user.getRoles(server));
        Role moderator = QuestBot.getRole("242403619108814850", server);
        if (!userRoles.contains(moderator)) {
            return "!!removeRole: " + CommandResponses.errorPermissions;
        }
        
        String questID;
        for (String s : args) {
            if (s.startsWith("<@&") && s.endsWith(">")) {
                // if the mentioned role is a quest
                questID = s.substring(3, s.length() - 1);
                Role role = QuestBot.getRole(questID, server);
                
                if (Settings.getMap().containsKey(questID)) {
                    
                    try {
                        role.delete().get();
                        return "!!removeRole: " + "removed Role";
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return "!!removeRole" + CommandResponses.errorArgMention;
    }
}
