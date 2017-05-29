package space.funin.questBot.Listeners;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

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
    public void onLeaveCommand(Message message, User user, Channel channel, Server server) throws InterruptedException, ExecutionException {
        List<Role> mentions = message.getMentionedRoles();
        
        for(Role r : mentions) {
        	if(Settings.getMap().containsKey(r.getId()) && user.getRoles(server).contains(r)) {
        		r.removeUser(user).get();
                channel.sendMessage("!!leave: Removed role " + r.getMentionTag());
            } else {
                channel.sendMessage("!!leave: " + r.getMentionTag() + " : " + CommandResponses.errorInvalidRole);
            }
        }
    }
    
    @Command(aliases = { "!!removeRole", "!!deleteRole", "!!delRole" }, description = "Removes role(s). Moderator Only.",
            usage = "!!removeRole [@role]*", async = true)
    public void onRemoveCommand(String[] args, User user, Channel channel, Server server) {
        // command only available to moderators
		if (!CommandUtils.isMod(user, server)) {
			channel.sendMessage("!!mute : " + CommandResponses.errorPermissions);
			return;
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
                        channel.sendMessage("!!removeRole: " + "removed Role");
                        return;
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        channel.sendMessage("!!removeRole" + CommandResponses.errorArgMention);
    }
}
