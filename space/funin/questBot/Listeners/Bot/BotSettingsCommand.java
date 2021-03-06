package space.funin.questBot.Listeners.Bot;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author Ken Wieland
 * 
 *         Handles commands to do with quest updates
 */
public class BotSettingsCommand implements CommandExecutor {

	@Command(aliases = {
			"!!save" }, description = "Manually save settings to disk", usage = "!!save", async = true, showInHelpPage = false)
	public void onSaveCommand(String[] args, Channel channel) {
		boolean isEmbed = CommandUtils.isEmbed(args);
		Settings.save();
		CommandUtils.saveLoadResponse(true, channel, isEmbed);
	}

	@Command(aliases = { "!!load",
			"!!reload" }, description = "Manually reload settings from disk", usage = "!!load", async = true, showInHelpPage = false)
	public void onLoadCommand(String[] args, Channel channel) {
		boolean isEmbed = CommandUtils.isEmbed(args);
		Settings.reload();
		CommandUtils.saveLoadResponse(false, channel, isEmbed);
	}

}
