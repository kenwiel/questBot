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
import space.funin.questBot.Listeners.HelpListener;
import space.funin.questBot.Listeners.MentionListener;
import space.funin.questBot.Listeners.ModListener;
import space.funin.questBot.Listeners.QuestListener;
import space.funin.questBot.Listeners.RoleListener;
import space.funin.questBot.Listeners.UpdateListener;
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
		
		
		
		// update the qst cache every 2 minutes
		executor.scheduleAtFixedRate(new RunnableCatalogFetcher(), 0, 120, TimeUnit.SECONDS);

		api.connect(new FutureCallback<DiscordAPI>() {
			@Override
			public void onSuccess(final DiscordAPI api) {
				api.setGame("RTFM | !!help");
				CommandHandler commandHandler = new JavacordHandler(api);

				QuestListener questListener = new QuestListener();
				RoleListener roleListener = new RoleListener();
				UpdateListener updateListener = new UpdateListener();
				HelpListener helpListener = new HelpListener(commandHandler);
				ModListener modListener = new ModListener(executor, api);

				// reacts to the @mention, not to a command
				MentionListener mentionListener = new MentionListener();

				commandHandler.registerCommand(questListener);
				commandHandler.registerCommand(roleListener);
				commandHandler.registerCommand(updateListener);
				commandHandler.registerCommand(helpListener);
				commandHandler.registerCommand(modListener);

				api.registerListener(mentionListener);
			}

			@Override
			public void onFailure(Throwable t) {
				// login failed
				t.printStackTrace();
			}
		});
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
