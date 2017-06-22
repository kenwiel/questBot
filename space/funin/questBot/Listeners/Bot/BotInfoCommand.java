package space.funin.questBot.Listeners.Bot;

import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class BotInfoCommand implements CommandExecutor {
	DiscordAPI api;
	String authorID = "156879547214725120";
	
	public BotInfoCommand(DiscordAPI api) {
		this.api = api;
	}
	@Command(aliases = { "!!botinfo",
			"!!bi" }, description = "Shows some information about the bot.", usage = "!!botinfo")
	public void onInfoCommand(String[] args, Channel channel) throws InterruptedException, ExecutionException {
		String botInfo =  "**INFO**\n- **Author:** " + api.getUserById(authorID).get().getMentionTag() + "\n"
				+ "- **Language:** Java\n"
				+ "- **Library:** Javacord\n"
				+ "- **Command-Lib:** sdcf4j\n"
				+ "- **Github:** https://github.com/kenwiel/questBot/";
		channel.sendMessage(botInfo);
	}
}
