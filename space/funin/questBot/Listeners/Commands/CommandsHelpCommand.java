package space.funin.questBot.Listeners.Commands;

import java.awt.Color;
import java.util.Arrays;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;

/**
 * @author Ken Wieland
 *
 *         Handles commands to do with help and information
 */
public class CommandsHelpCommand implements CommandExecutor {
	CommandHandler commandHandler;

	public CommandsHelpCommand(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Command(aliases = { "!!help", "!!h", "!!info",
			"!!i" }, description = "Shows all available commands, or detailed help for a single command", usage = "!!help <!!command>", async = true)
	public void onHelpCommand(String[] args, User user, Channel channel) {
		boolean isSpecific = false;

		// do the arguments contain an alias of any command
		for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {
			for (String s : args) {
				String[] aliases = simpleCommand.getCommandAnnotation().aliases();
				String combinedAliases = Arrays.toString(aliases).toLowerCase();
				if (combinedAliases.contains(s.toLowerCase())) {
					isSpecific = true;
					onHelpCommand(channel, simpleCommand);
				}
			}
		}
		//if no specific command was requested
		if (!isSpecific) {
			onHelpCommand(channel);
		}
	}

	public void onHelpCommand(Channel channel, CommandHandler.SimpleCommand simpleCommand) {
		String aliases = Arrays.toString(simpleCommand.getCommandAnnotation().aliases()).toLowerCase();
		
		EmbedBuilder eb = new EmbedBuilder().setColor(Color.CYAN)
				.setTitle(simpleCommand.getCommandAnnotation().aliases()[0])
				.addField("**Alias(es): **", aliases, false)
				.addField("**Description: **", simpleCommand.getCommandAnnotation().description(), false)
				.addField("**Usage: **", simpleCommand.getCommandAnnotation().usage(), false);
		
		channel.sendMessage("", eb);
	}
	
	public void onHelpCommand(Channel channel) {
		StringBuilder builder = new StringBuilder();
		builder.append("```xml"); // a xml code block looks fancy
		for (CommandHandler.SimpleCommand simpleCommand : commandHandler.getCommands()) {
			if (!simpleCommand.getCommandAnnotation().showInHelpPage()) {
				continue; // skip command
			}
			builder.append("\n");
			if (!simpleCommand.getCommandAnnotation().requiresMention()) {
				// the default prefix only works if the command does not
				// require
				// a mention
				builder.append(commandHandler.getDefaultPrefix());
			}
			String usage = simpleCommand.getCommandAnnotation().usage();
			if (usage.isEmpty()) { // no usage provided, using the first
									// alias
				usage = simpleCommand.getCommandAnnotation().aliases()[0];
			}
			builder.append(usage);
			String description = simpleCommand.getCommandAnnotation().description();
			if (!description.equals("none")) {
				builder.append(" | ").append(description);
			}
		}
		builder.append("\n\n[Brackets] : Argument is required")
		.append("\n<Brackets> : Argument is optional")
		.append("\n * after an argument : Multiple arguments of this type are accepted");
		
		builder.append("\n```"); // end of xml code block
		channel.sendMessage(builder.toString());

	}
}
