package space.funin.questBot.Listeners.Quest;

import java.util.concurrent.TimeUnit;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import space.funin.questBot.QuestBot;
import space.funin.questBot.runnables.RunnableCatalogFetcher;
import space.funin.questBot.utils.CommandUtils;

public class QuestCacheCommand implements CommandExecutor {

	@Command(aliases = {
			"!!cache" }, description = "Manually update the /qst/ cache", usage = "!!cache", async = true)
	public void onCacheCommand(String[] args, Channel channel) {
		boolean isEmbed = CommandUtils.isEmbed(args);
		QuestBot.getExecutor().schedule(new RunnableCatalogFetcher(), 0, TimeUnit.SECONDS);
		CommandUtils.updateCache(channel, isEmbed);
	}
}
