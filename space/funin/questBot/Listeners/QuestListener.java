package space.funin.questBot.Listeners;

import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.CommandResponses;
import space.funin.questBot.Quest;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author Ken Wieland
 *
 *         Handles commands to do with quest
 */
public class QuestListener implements CommandExecutor {
    private static Logger logger = LoggerFactory.getLogger(QuestListener.class);
    
    @Command(aliases = { "!!newQuest", "!!new", "!!addQuest", "!!add" }, description = "Creates a new Quest.",
            usage = "!!newQuest [@role] [@qm] [\"quest name\"] [\"quest title\"] <\"quest description\">",
            async = true)
    public String onCreateQuestCommand(String[] args, Channel channel, Message message, Server server, DiscordAPI api)
            throws InterruptedException, ExecutionException {
        boolean isEmbed = CommandUtils.isEmbed(args);
        
        String messageContent = message.getContent();
        messageContent = CommandUtils.removeCommand(messageContent);
        messageContent = CommandUtils.removeQuiet(messageContent);
        
        // at least 4 args required
        if (args.length < 4) {
            return "!!newQuest : " + CommandResponses.errorArgAmount;
        }
        
        logger.info(CommandUtils.removeCommand(message.getContent()));
        String[] combinedArgs = CommandUtils.combineCommands(messageContent);
        combinedArgs = CommandUtils.splitFirstCommand(combinedArgs);
        
        // at least 4 args required after commands are combined
        // checking again because args got modified
        if (combinedArgs.length < 4) {
            return "!!newQuest : " + CommandResponses.errorArgAmount;
        }
        
        if (combinedArgs[0].startsWith("<@&") && combinedArgs[0].endsWith(">")) {
            // arg1 should be a qm(user)
            if (combinedArgs[1].startsWith("<@") && combinedArgs[1].endsWith(">")) {
                String roleID = combinedArgs[0].substring(3, combinedArgs[0].length() - 1);
                
                // if the user has a nickname, the mention starts with "<@!"
                // instead of "<@"
                int substringstart = combinedArgs[1].contains("<@!") ? 3 : 2;
                String userID = combinedArgs[1].substring(substringstart, combinedArgs[1].length() - 1);
                
                String questName = combinedArgs[2];
                String questTitle = combinedArgs[3];
                String questDescription = "None";
                if (combinedArgs.length > 4)
                    questDescription = combinedArgs[4];
                
                Quest quest = new Quest(roleID, userID, questName, questTitle, questDescription);
                Map<String, Quest> questMap = Settings.getMap();
                questMap.put(roleID, quest);
                
                CommandUtils.questInfoCreator(Color.CYAN, quest, channel, server, isEmbed);
                Settings.saveMap(questMap);
                return "";
            }
        }
        return "!!newQuest : " + CommandResponses.errorArgMention;
    }
    
    @Command(aliases = { "!!ranks", "!!roles", "!!getPairs", "!!getPair", "!!get", "!!pair", "!!pairs", "!!quests", "!!q" },
            description = "Outputs a list of ranks and their corresponding quests", usage = "!!ranks", async = true)
    public void onGetPairCommand(String[] args, User user, Channel channel, Server server) {
        boolean isEmbed = CommandUtils.isEmbed(args);
        if (isEmbed) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.CYAN);
            
            for (Quest q : Settings.getMap().values()) {
                eb.addField(q.getName(), QuestBot.getRole(q.getRole(), server).getName(), true);
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
    
    @Command(aliases = { "!!questInfo", "!!qInfo", "!!quest", "!!qi" },
            description = "Gives additional info about a quest", usage = "!!QuestInfo [@quest]*", async = true)
    public void onQuestInfoCommand(String[] args, Channel channel, Server server) {
        logger.info("entering info command");
        Map<String, Quest> questListComplete = Settings.getMap();
        Quest quest;
        String questID;
        
        for (String s : args) {
            logger.debug(s);
            if (s.startsWith("<@&") && s.endsWith(">")) {
                logger.debug("" + (s.startsWith("<@&") && s.endsWith(">")));
                // if the mentioned role is a quest
                questID = s.substring(3, s.length() - 1);
                if (questListComplete.containsKey(questID)) {
                    quest = questListComplete.get(questID);
                    logger.debug("quest is null? " + (questListComplete.get(s.substring(3, s.length() - 1) == null)));
                    CommandUtils.questInfoCreator(Color.CYAN, quest, channel, server, CommandUtils.isEmbed(args));
                }
            }
        }
    }
    
    @Command(aliases = { "!!delQuest", "!!del", "!!deleteQuest", "!!rem", "!!removeQuest" },
            description = "Removes a Quest. Moderator only.", usage = "!!delQuest [@quest]*", async = true)
    public void onDeleteCommand(String[] args, User user, Channel channel, Server server) {
        // command only available to moderators
        if (!CommandUtils.isMod(user, server)) {
            channel.sendMessage("!!delQuest : " + CommandResponses.errorPermissions);
            return;
        }
        
        Map<String, Quest> questListComplete = Settings.getMap();
        Quest quest;
        String questID;
        
        for (String s : args) {
            logger.debug(s);
            if (s.startsWith("<@&") && s.endsWith(">")) {
                // if the mentioned role is a quest
                questID = s.substring(3, s.length() - 1);
                if (questListComplete.containsKey(questID)) {
                    quest = questListComplete.get(questID);
                    channel.sendMessage("**Removing quest:**");
                    CommandUtils.questInfoCreator(Color.RED, quest, channel, server, CommandUtils.isEmbed(args));
                    questListComplete.remove(questID);
                    Settings.deleteQuest(questID);
                }
            }
        }
        
        Settings.saveMap(questListComplete);
    }
    
}
