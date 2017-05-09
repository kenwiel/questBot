package space.funin.questBot.Listeners;

import java.util.concurrent.TimeUnit;


import de.btobastian.javacord.entities.Channel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.runnables.RunnableCatalogFetcher;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author Ken Wieland
 * 
 * Handles commands to do with quest updates
 */
public class UpdateListener implements CommandExecutor {

    @Command(aliases = { "!!save" }, description = "Manually save quests to disk", usage = "!!save", async = true)
    public void onSaveCommand(String[] args, Channel channel) {
        boolean isEmbed = CommandUtils.isEmbed(args);
        Settings.save();
        CommandUtils.saveLoadResponse(true, channel, isEmbed);
    }

    @Command(aliases = { "!!load", "!!reload" }, description = "Manually reload quests from disk", usage = "!!load", async = true)
    public void onLoadCommand(String[] args, Channel channel) {
        boolean isEmbed = CommandUtils.isEmbed(args);
        Settings.reload();
        CommandUtils.saveLoadResponse(false, channel, isEmbed);
    }
    
    @Command(aliases = { "!!cache" }, description = "Manually update the qst cache", usage = "!!cache", async = true)
    public void onCacheCommand(String[] args, Channel channel) {
        boolean isEmbed = CommandUtils.isEmbed(args);
        QuestBot.getExecutor().schedule(new RunnableCatalogFetcher(), 0, TimeUnit.SECONDS);
        CommandUtils.updateCache(channel, isEmbed);
    }
    
    
    
    
}
