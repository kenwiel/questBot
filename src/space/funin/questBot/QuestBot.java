package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import space.funin.questBot.commands.PingPong;

public class QuestBot {

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        new QuestBot(args[0]);
    }

    public QuestBot(String token) {

        TimingHandler t = new TimingHandler();

        new DiscordApiBuilder().setToken(token).setAccountType(AccountType.BOT).login().whenComplete(
                (api, throwable) -> {
                    if (throwable != null) { //Login failed
                        throwable.printStackTrace();
                        return;
                    }
                    //Login successful
                    CommandHandler commandHandler = new JavacordHandler(api);
                    commandHandler.setDefaultPrefix("!!");

                    commandHandler.registerCommand(new PingPong());
                });
    }
}
