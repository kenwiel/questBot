package space.funin.questBot.runnables;

import java.util.ArrayList;
import java.util.List;

import javach.Thread;
import space.funin.questBot.QuestBot;

public class RunnableCatalogFetcher implements Runnable{

	public void run() {
		try {
			System.out.println("refreshing qst");
			QuestBot.qst.refreshCache();
			
			List<Thread> threadList = new ArrayList<Thread>( QuestBot.qst.cache.values() );
	
			
			//iterate through all threads
			for(Thread t : threadList) {
				String subject = t.OriginalPost.subject();
				System.out.println(subject);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
