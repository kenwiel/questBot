package space.funin.questBot.Listeners.Commands;

import java.awt.Color;
import java.util.Arrays;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import space.funin.questBot.utils.CommandUtils;

public class CommandsAliasCommand implements CommandExecutor {

	CommandHandler commandHandler;

	public CommandsAliasCommand(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Command(aliases = {
			"!!alias" }, description = "Lists the aliases for a command", usage = "!!alias [!!command]", async = true)
	public void onAliasCommand(String[] args, Channel channel) {
		boolean isEmbed = CommandUtils.isEmbed(args);
		if (args.length == 0)
			return;

		if (isEmbed) {
			EmbedBuilder eb = new EmbedBuilder().setColor(Color.CYAN);
			for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {
				for (String s : args) {
					String[] aliases = simpleCommand.getCommandAnnotation().aliases();
					String combinedAliases = Arrays.toString(aliases).toLowerCase();
					if (combinedAliases.contains(s.toLowerCase())) {
						eb.addField("**" + aliases[0] + ": **", combinedAliases, false);
					}
				}
			}
			channel.sendMessage("", eb);
		} else {
			StringBuilder sb = new StringBuilder();
			for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {
				for (String s : args) {
					String[] aliases = simpleCommand.getCommandAnnotation().aliases();
					String combinedAliases = Arrays.toString(aliases);
					String combinedAliases2 = combinedAliases.toLowerCase();
					if (combinedAliases2.contains(s.toLowerCase())) {
						sb.append("**" + aliases[0] + ": **\n").append(combinedAliases);
					}
				}
			}
			channel.sendMessage(sb.toString());
		}
	}
}
