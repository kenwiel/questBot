package space.funin.questBot.Quests;

import de.btobastian.javacord.entities.channels.ServerTextChannel;
import javach.Thread;
import space.funin.questBot.QuestBot;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestHelper {

    public static void linkThread(Quest quest, ServerTextChannel channel) {

    }

    public static Thread getThreadBySubject(String subject) {
        List<Thread> threadList = QuestBot.qst.getCachedThreads();

        threadList = threadList.stream().filter(thread -> thread.getOP().subject().contains(subject)).collect(Collectors.toList());
        threadList.forEach(thread -> System.out.println(thread.getID()));

        Collections.sort(threadList);

        threadList.forEach(thread -> System.out.println(thread.getID()));

        return null;
    }
}
