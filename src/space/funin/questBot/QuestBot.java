package space.funin.questBot;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.DiscordApiBuilder;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import javach.Board;
import org.slf4j.Logger;
import space.funin.questBot.Quests.QuestHandler;
import space.funin.questBot.commands.*;
import space.funin.questBot.settings.SettingLoader;
import space.funin.questBot.settings.SettingsHelper;

import java.util.Collection;

public class QuestBot {
    private static final Logger logger = LoggerUtil.getLogger(QuestBot.class);

    private static TimingHandler timingHandler = new TimingHandler();
    private static Collection<Server> servers;
    private static Board.specBoard qst;
    private static DiscordApi api;
    private static QuestHandler questHandler;
    private static SettingLoader settingLoader;
    private static CommandHandler commandHandler;

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }

        new QuestBot();

    }

    public QuestBot() {
        connect(SettingsHelper.loadToken());
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

                    commandHandler = new JavacordHandler(api);
                    commandHandler.setDefaultPrefix("!!");

                    questHandler = new QuestHandler(api, commandHandler.getDefaultPrefix());
                    logger.debug("QuestHandler registered");

                    commandHandler.registerCommand(new QuestAdd());
                    commandHandler.registerCommand(new Time());
                    commandHandler.registerCommand(new Mute());
                    commandHandler.registerCommand(new BrowseQst());
                    commandHandler.registerCommand(new Conga());
                    commandHandler.registerCommand(new OutputQuest(questHandler));
                    commandHandler.registerCommand(new Help(commandHandler));
                    logger.debug("Commands registered");

                    settingLoader = new SettingLoader(questHandler);
                    logger.debug("Settings loaded");
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

    public static SettingLoader getSettingLoader() {
        return settingLoader;
    }

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
