package space.funin.questBot.Listeners.Bot;

import java.util.concurrent.ExecutionException;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class BotInfoCommand implements CommandExecutor {
	DiscordAPI api;
	public BotInfoCommand(DiscordAPI api) {
		this.api = api;
	}
	@Command(aliases = { "!!botinfo",
			"!!bi" }, description = "Shows some information about the bot.", usage = "!!botinfo")
	public String onInfoCommand(String[] args) throws InterruptedException, ExecutionException {
		return "**INFO**\n- **Author:** " + api.getUserById("id").get().getMentionTag() + "\n"
				+ "- **Language:** Java\n"
				+ "- **Library:** Javacord\n"
				+ "- **Command-Lib:** sdcf4j\n"
				+ "- **Github:** https://github.com/kenwiel/questBot/";
	}
}
