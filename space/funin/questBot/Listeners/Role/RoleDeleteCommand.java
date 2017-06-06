package space.funin.questBot.Listeners.Role;

import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

public class RoleDeleteCommand implements CommandExecutor {
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
