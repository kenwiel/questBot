package space.funin.questBot.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javach.Board.specBoard;
import space.funin.questBot.QuestBot;
import javach.Thread;

public class BoardUtils {
    public static Thread findThread(String subject) {
        specBoard qst = QuestBot.qst;
        List<Thread> matchingThreads = new ArrayList<Thread>();
        
        for(Thread t : qst.getThreads()) {
            if(t.getOP().subject().equalsIgnoreCase(subject)) {
                matchingThreads.add(t);
                System.out.println(t.getOP().getPostID());
            }
        }
        
        Collections.sort(matchingThreads, Collections.reverseOrder());
        
        System.out.println(matchingThreads.get(0).getID());
        
        return null;
    }
}
