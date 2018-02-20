package space.funin.questBot.Runnables;

import de.btobastian.javacord.utils.logging.LoggerUtil;
import javach.Board;
import org.slf4j.Logger;
import space.funin.questBot.Helper;
import space.funin.questBot.QuestBot;

public class UpdateCache implements Runnable {
    private static final Logger logger = LoggerUtil.getLogger(UpdateCache.class);

    @Override
    public void run() {
        try {
            Board.specBoard qstUpdate = new Board.specBoard("qst");
            qstUpdate.refreshCache();
            if (qstUpdate.cache.size() == 151) {
                QuestBot.setQst(qstUpdate);
                logger.info("/qst/ cache updated. " + qstUpdate.cache.size() + " threads in cache.");
            } else {
                throw new IllegalArgumentException("/qst/ cache update, 151 Threads were expected, " + qstUpdate.cache.size() + " received.");
            }
        } catch (Throwable throwable) {
            logger.trace("/qst/ cache update failed: ", throwable);
            Helper.notifyDeveloper(throwable);
        }
    }
}
