package space.funin.questBot.Listeners;

import java.awt.Color;
import java.util.Arrays;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author Ken Wieland
 *
 *         Handles commands to do with help and information
 */
public class HelpListener implements CommandExecutor {
	CommandHandler commandHandler;

	public HelpListener(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Command(aliases = { "!!help", "!!h", "!!info",
			"!!i" }, description = "Shows all available commands", usage = "!!help", async = true)
	public void onHelpCommand(String[] args, User user, Channel channel) {
		System.out.println("in");
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.CYAN);
		for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {

			String usage = simpleCommand.getCommandAnnotation().usage();
			if (usage.isEmpty()) { // no usage provided, using the first
									// alias
				usage = simpleCommand.getCommandAnnotation().aliases()[0];
			}

			String description = simpleCommand.getCommandAnnotation().description();
			if (description.isEmpty()) {
				description = "No description provided";
			}
			System.out.println(usage);
			System.out.println(description);
			eb.addField(usage, description, false);
		}
		eb.addField("*", "Multiple arguments of this kind are possible", false);
		channel.sendMessage("", eb);

	}

	@Command(aliases = { "!!botinfo",
			"!!bi" }, description = "Shows some information about the bot.", usage = "!!botinfo")
	public String onInfoCommand(String[] args) {
		return "**INFO**\n- **Author:** @kenwiel\n" + "- **Language:** Java\n" + "- **Library:** Javacord\n"
				+ "- **Command-Lib:** sdcf4j\n" + "- **Github:** https://github.com/kenwiel/questBot/";
	}

	@Command(aliases = {
			"!!alias" }, description = "Lists the aliases for a command", usage = "!!alias [command]", async = true)
	public void onAliasCommand(String[] args, Channel channel) {
		boolean isEmbed = CommandUtils.isEmbed(args);
		if (args.length == 0)
			return;

		if (isEmbed) {
			EmbedBuilder eb = new EmbedBuilder().setColor(Color.CYAN);
			for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {
				for (String s : args) {
					String[] aliases = simpleCommand.getCommandAnnotation().aliases();
					String combinedAliases = Arrays.toString(aliases);
					String combinedAliases2 = combinedAliases.toLowerCase();
					if (combinedAliases2.contains(s.toLowerCase())) {
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
