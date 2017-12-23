package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import javach.Board;
import space.funin.questBot.Quests.QuestHandler;
import space.funin.questBot.commands.*;

import java.util.Collection;

public class QuestBot {
    private static TimingHandler timingHandler = new TimingHandler();
    private static Collection<Server> servers;
    private static Board.specBoard qst;
    private static DiscordApi api;
    private static QuestHandler questHandler;

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]);

    }

    public QuestBot(String token) {
        connect(token);
        setupQst();
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
                    QuestBot.api = api;
                    servers = api.getServers();

                    //Login successful

                    CommandHandler commandHandler = new JavacordHandler(api);
                    commandHandler.setDefaultPrefix("!!");
                    questHandler = new QuestHandler(api, commandHandler.getDefaultPrefix());

                    commandHandler.registerCommand(new PingPong());
                    commandHandler.registerCommand(new Time());
                    commandHandler.registerCommand(new Mute());
                    commandHandler.registerCommand(new BrowseQst());
                    commandHandler.registerCommand(new QuestAdd());
                    commandHandler.registerCommand(new Conga());
                    commandHandler.registerCommand(new OutputQuest(questHandler));


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

    public static DiscordApi getApi() {
        return api;
    }

    public static QuestHandler getQuestHandler() {
        return questHandler;
    }
}
