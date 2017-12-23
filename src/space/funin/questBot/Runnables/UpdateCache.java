package space.funin.questBot.Runnables;

import de.btobastian.javacord.utils.logging.LoggerUtil;
import javach.Board;
import org.slf4j.Logger;
import space.funin.questBot.QuestBot;

public class UpdateCache implements Runnable {
    private static final Logger logger = LoggerUtil.getLogger(UpdateCache.class);

    @Override
    public void run() {
        Board.specBoard qstUpdate = new Board.specBoard("qst");
        qstUpdate.refreshCache();
        QuestBot.setQst(qstUpdate);
        logger.info("/qst/ cache updated. " + qstUpdate.cache.size() + " threads in cache.");
    }
}
