package space.funin.questBot.Runnables;

import javach.Board;
import space.funin.questBot.QuestBot;

public class UpdateCache implements Runnable {

    @Override
    public void run() {
        Board.specBoard qstUpdate = QuestBot.getQst();
        qstUpdate.refreshCache();
        QuestBot.setQst(qstUpdate);

    }
}
