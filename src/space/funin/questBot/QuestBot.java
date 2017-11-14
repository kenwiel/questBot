package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import space.funin.questBot.settings.SettingsHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestBot {
    private static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]);
        new SettingsHelper();
    }

    public QuestBot(String token) {
        new DiscordApiBuilder().setToken(token).setAccountType(AccountType.BOT).login().whenComplete(
                (api, throwable) -> {
                    if (throwable != null) { //Login failed
                        throwable.printStackTrace();
                        return;
                    }
                    //Login successful
                    CommandHandler commandHandler = new JavacordHandler(api);
                });
    }
}
