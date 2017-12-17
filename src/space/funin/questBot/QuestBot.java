package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import javach.Board;
import space.funin.questBot.commands.BrowseQst;
import space.funin.questBot.commands.Mute;
import space.funin.questBot.commands.PingPong;

import java.util.Collection;

public class QuestBot {
    public static TimingHandler timingHandler = new TimingHandler();
    public static Collection<Server> servers;
    public static Board.specBoard qst = new Board.specBoard("qst");

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]);
    }

    public QuestBot(String token) {
        new DiscordApiBuilder().setToken(token).setAccountType(AccountType.BOT).login().whenComplete(
                (api, throwable) -> {
                    if (throwable != null) { //Login failed
                        throwable.printStackTrace();
                        return;
                    }
                    servers = api.getServers();

                    //Login successful
                    CommandHandler commandHandler = new JavacordHandler(api);
                    commandHandler.setDefaultPrefix("!!");

                    commandHandler.registerCommand(new PingPong());
                    commandHandler.registerCommand(new Mute());
                    commandHandler.registerCommand(new BrowseQst());
                });
    }
}
