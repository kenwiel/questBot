package space.funin.questBot.Runnables;

import de.btobastian.javacord.utils.logging.LoggerUtil;
import javach.Board;
import org.slf4j.Logger;
import space.funin.questBot.QuestBot;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UpdateCache implements Runnable {
    private static final Logger logger = LoggerUtil.getLogger(UpdateCache.class);

    @Override
    public void run() {
        try { //prevent the cache update "service" from stopping if any exceptions get thrown, for example if 4chan is down
            Board.specBoard qstUpdate = new Board.specBoard("qst");
            qstUpdate.refreshCache();
            QuestBot.setQst(qstUpdate);
            logger.info("/qst/ cache updated. " + qstUpdate.cache.size() + " threads in cache.");

            //logging in owner-only server so I dont have to ssh in to see how the bot is doing
            QuestBot.getApi().getTextChannelById(422883413410840596L).ifPresent(u -> u.sendMessage("/qst/ cache updated. `" + qstUpdate.cache.size() + "` threads in cache."));
        } catch (Exception e) {
            logger.debug("/qst/ update failed.", e);
            QuestBot.getApi().getTextChannelById(422883413410840596L).ifPresent(u -> u.sendMessage("Exception Thrown while updating cache:`" + e.getCause() +"`"));
        }
    }
}
