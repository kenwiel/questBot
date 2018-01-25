package space.funin.questBot.commands;

import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import space.funin.questBot.Helper;

import java.util.Arrays;

public class Help  implements CommandExecutor{
    private CommandHandler commandHandler;

    public Help(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public static final String DESCRIPTION = "Outputs either the list of commands or a specific helpfile for a given command.";
    public static final String USAGE =
            "**__!!help__**\n\n"+
            "__Usage:__\n"+
            "!!help\n"+
            "!!help `command`\n"+
            "\n"+
            "Outputs either a list of commands or the usage for the given command(s)";

    @Command(aliases = {"help"},description = DESCRIPTION, usage = USAGE,  async = true)
    public void onCall(String[] args, ServerTextChannel channel) {
        boolean specificCommand = false;

        //do the arguments contain an alias of any command
        for (CommandHandler.SimpleCommand command : commandHandler.getCommands()) {
            for (String s : args) {
                String combinedAliases = Arrays.toString(command.getCommandAnnotation().aliases()).toLowerCase();
                if (combinedAliases.contains(s)) {
                    specificCommand = true;
                    commandHelp(command, channel);
                }
            }
        }
        if (!specificCommand)
            generalHelp(channel);
    }

    private void commandHelp(CommandHandler.SimpleCommand command, ServerTextChannel channel) {
        channel.sendMessage(command.getCommandAnnotation().usage());
    }

    private void generalHelp(ServerTextChannel channel) {
        StringBuilder sb = new StringBuilder();
        sb.append("**__!!help__**\n\n").append("__Commands:__```\n");

        for (CommandHandler.SimpleCommand command : commandHandler.getCommands()) {
            if (command.getCommandAnnotation().usage().equals(""))
                continue;

            sb.append(Helper.padLeft(command.getCommandAnnotation().aliases()[0], 15)).append(" | ").append(command.getCommandAnnotation().description()).append("\n");
        }
        sb.append("```");

        channel.sendMessage(sb.toString());
    }
}
