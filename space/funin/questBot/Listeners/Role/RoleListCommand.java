package space.funin.questBot.Listeners.Role;

import java.awt.Color;
import java.util.Map.Entry;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Quest;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

public class RoleListCommand implements CommandExecutor {
	@Command(aliases = { "!!listRoles","!!ranks", "!!roles", "!!getPairs", "!!getPair", "!!get", "!!pair", "!!pairs", "!!quests", "!!q" },
            description = "Outputs a list of ranks and their corresponding quests", usage = "!!ranks", async = true)
    public void onGetPairCommand(String[] args, User user, Channel channel, Server server) {
        boolean isEmbed = CommandUtils.isEmbed(args);
        if (isEmbed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.CYAN);
            
            for (Quest q : Settings.getMap().values()) {
            	if(q.isValid(server, channel)) {
                	eb.addField(q.getName(), QuestBot.getRole(q.getRole(), server).getName(), true);
            	}
            }
            channel.sendMessage("", eb);
            
        } else {
            Role questRole;
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Quest> e : Settings.getMap().entrySet()) {
                questRole = QuestBot.getRole(e.getKey(), server);
                sb.append("\n" + questRole.getName() + " : " + e.getValue().toString());
            }
            channel.sendMessage(sb.toString());
        }
    }
}
