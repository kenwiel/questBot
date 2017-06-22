package space.funin.questBot;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.FutureCallback;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import javach.Board.specBoard;
import space.funin.questBot.Listeners.NonCommandListener;
import space.funin.questBot.Listeners.Bot.BotAddResponseCommand;
import space.funin.questBot.Listeners.Bot.BotInfoCommand;
import space.funin.questBot.Listeners.Bot.BotSettingsCommand;
import space.funin.questBot.Listeners.Commands.CommandsAliasCommand;
import space.funin.questBot.Listeners.Commands.CommandsHelpCommand;
import space.funin.questBot.Listeners.Message.MessagePurgeCommand;
import space.funin.questBot.Listeners.Quest.QuestCacheCommand;
import space.funin.questBot.Listeners.Quest.QuestCreateCommand;
import space.funin.questBot.Listeners.Quest.QuestDeleteCommand;
import space.funin.questBot.Listeners.Quest.QuestInfoCommand;
import space.funin.questBot.Listeners.Role.RoleCreateCommand;
import space.funin.questBot.Listeners.Role.RoleDeleteCommand;
import space.funin.questBot.Listeners.Role.RoleJoinCommand;
import space.funin.questBot.Listeners.Role.RoleLeaveCommand;
import space.funin.questBot.Listeners.Role.RoleListCommand;
import space.funin.questBot.Listeners.Server.ServerSetupMuteCommand;
import space.funin.questBot.Listeners.User.UserMuteCommand;
import space.funin.questBot.Listeners.User.UserUnmuteCommand;
import space.funin.questBot.runnables.RunnableCatalogFetcher;
import javach.Thread;

public class QuestBot {
	private static Logger logger = LoggerFactory.getLogger(QuestBot.class);
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
	public static specBoard qst;

	private static DiscordAPI api;

	public static void main(String[] args) {
		qst = new specBoard(false, "qst");
		connect(Settings.loadToken());
		Settings.reload();
		api.setWaitForServersOnStartup(false);
		api.setAutoReconnect(true);
		
		api.connect(new FutureCallback<DiscordAPI>() {
			@Override
			public void onSuccess(final DiscordAPI api) {
				api.setGame("RTFM | !!help");
				CommandHandler commandHandler = new JavacordHandler(api);


				// handles reactions to things that arent commands
				NonCommandListener nonCommandListener = new NonCommandListener();
				api.registerListener(nonCommandListener);

				// handles all other commands
				commandHandler.registerCommand(new BotAddResponseCommand());
				commandHandler.registerCommand(new BotInfoCommand(api));
				commandHandler.registerCommand(new BotSettingsCommand());
				
				commandHandler.registerCommand(new CommandsAliasCommand(commandHandler));
				commandHandler.registerCommand(new CommandsHelpCommand(commandHandler));
				
				commandHandler.registerCommand(new MessagePurgeCommand());
				
				commandHandler.registerCommand(new QuestCacheCommand());
				commandHandler.registerCommand(new QuestCreateCommand());
				commandHandler.registerCommand(new QuestDeleteCommand());
				commandHandler.registerCommand(new QuestInfoCommand());
				
				commandHandler.registerCommand(new RoleCreateCommand());
				commandHandler.registerCommand(new RoleDeleteCommand());
				commandHandler.registerCommand(new RoleJoinCommand());
				commandHandler.registerCommand(new RoleLeaveCommand());
				commandHandler.registerCommand(new RoleListCommand());
				
				commandHandler.registerCommand(new ServerSetupMuteCommand(api));

				commandHandler.registerCommand(new UserMuteCommand(executor, api));
				commandHandler.registerCommand(new UserUnmuteCommand(executor, api));

			}

			@Override
			public void onFailure(Throwable t) {
				// login failed
				t.printStackTrace();
			}
		});
		
		// update the qst cache every 2 minutes
		executor.scheduleAtFixedRate(new RunnableCatalogFetcher(), 0, 120, TimeUnit.SECONDS);

	}

	private static void connect(String token) {
		api = Javacord.getApi(token, true);
	}

	@SuppressWarnings("unused")
	private static void connect(String email, String password) {
		api = Javacord.getApi(email, password);
	}

	public static DiscordAPI getAPI() {
		return api;
	}

	public static User getUser(String userID) {
		logger.debug("userID: " + userID);
		try {
			return api.getUserById(userID).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Role getRole(String roleID, Server server) {
		logger.debug("roleID: " + roleID);
		return server.getRoleById(roleID);
	}

	public static specBoard getQST() {
		return qst;
	}

	public static void setQST(specBoard board) {
		qst = board;
	}

	public static ScheduledExecutorService getExecutor() {
		return executor;
	}

	public static Thread getLatestThread(String subject) {
		TreeMap<Long, Thread> threadIDMap = new TreeMap<Long, Thread>();
		for (Thread t : qst.getAllThreads()) {
			// if the thread title matches the search term
			if (t.getOP().subject().equalsIgnoreCase(subject)) {
				threadIDMap.put(t.getID(), t);
			}
		}
		// threads are sorted ascending by threadID, therefore highest/last
		// thread is the most recent one.
		return threadIDMap.lastEntry().getValue();
	}
}
