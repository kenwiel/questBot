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
    public static TimingHandler timingHandler = new TimingHandler();
    public static Collection<Server> servers;
    public static Board.specBoard qst;

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]).testUpdateTime();

    }

    public QuestBot(String token) {
/*
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

*/
    }

    private void testUpdateTime() {
        qst = new Board.specBoard("qst");
        LocalDateTime time = LocalDateTime.now();
        qst.getAllThreads();
        Duration duration = Duration.between(time, LocalDateTime.now());
        System.out.println(duration.getSeconds());

        for (int i = 0; i < 30; i++) {
            time = LocalDateTime.now();
            qst.getAllThreads();
            duration = Duration.between(time, LocalDateTime.now());
            System.out.println(duration.getSeconds());
        }

        //60s
    }
}
