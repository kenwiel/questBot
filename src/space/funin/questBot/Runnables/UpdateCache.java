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
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();

            //notify the bot owner that its dead
            QuestBot.getApi().getUserById(156879547214725120L).ifPresent(u -> u.sendMessage("Exception Thrown while updating cache:```" + stackTrace + "```"));
        }
    }
}
