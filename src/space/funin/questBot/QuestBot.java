package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import javach.Board;
import space.funin.questBot.Quests.QuestHandler;
import space.funin.questBot.Quests.QuestHelper;
import space.funin.questBot.commands.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class QuestBot {
    private static TimingHandler timingHandler = new TimingHandler();
    private static Collection<Server> servers;
    private static Board.specBoard qst;

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]);

    }

    public QuestBot(String token) {
        setupQst();
        connect(token);
    }

    private void setupQst() {
        qst = new Board.specBoard("qst");
        timingHandler.scheduleCacheUpdate();
    }

    private void connect(String token) {

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
                    commandHandler.registerCommand(new Time());
                    commandHandler.registerCommand(new Mute());
                    commandHandler.registerCommand(new BrowseQst());

                    QuestHandler questHandler = new QuestHandler(api, commandHandler.getDefaultPrefix());
                });
    }

    public static TimingHandler getTimingHandler() {
        return timingHandler;
    }

    public static Collection<Server> getServers() {
        return servers;
    }

    public static Board.specBoard getQst() {
        return qst;
    }

    public static void setQst(Board.specBoard updatedQst) {
        qst = updatedQst;
    }
}
