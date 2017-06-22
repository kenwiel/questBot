package space.funin.questBot.Listeners.Quest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringEscapeUtils;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.sdcf4j.CommandExecutor;
import javach.Thread;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Settings;
import space.funin.questBot.utils.CommandUtils;

/**
 * @author ayylmao
 *
 */
public class QuestMentionCommand {
    
    public static void onGroupMention(DiscordAPI api, Message message, Channel channel) {
    	List<String> mentions  = CommandUtils.getRoleIDs(message.getMentionedRoles());
        
        for(String s : mentions) {
            if(Settings.getMap().containsKey(s)) {
                String search = Settings.getMap().get(s).getSearch();
                foundThread(getThreads(search), channel);
                System.out.println(getThreads(search));
            }
        }
    }
    
    private static void foundThread(Thread thread, Channel channel) {
    	System.out.println(thread == null);
    	if(thread == null) 
    		return;
    	    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(Color.GREEN)
    	.addField(StringEscapeUtils.unescapeHtml3(thread.OriginalPost.subject()), thread.url(), false);
    	
    	channel.sendMessage("", eb);
    }
    
    
    
    /**
     * @param questName stuff to find in the subject field
     * @return the most recent thread with questName in the subject field
     */
    private static Thread getThreads(String questName) {
		//get a list of all threads on /qst/
		
		List<Thread> threadList = new ArrayList<Thread>( QuestBot.qst.cache.values() );
		
		TreeMap<Long, Thread> fittingThreads = new TreeMap<>(Collections.reverseOrder());
		
		
		//iterate through all threads
		for(Thread t : threadList) {
			String subject = t.OriginalPost.subject();
			//System.out.print(subject + " : " + subject.toLowerCase().contains(questName.toLowerCase())+"\n");
			if (subject.toLowerCase().contains(questName.toLowerCase())) {
				fittingThreads.put(t.getID(), t);
				System.out.println(t.getID() + " : " + subject);
			}
		}
		Thread thread;
		try {
			thread = fittingThreads.firstEntry().getValue();
			System.out.println(thread.getID() + " : " + thread.OriginalPost.subject());
		} catch (Exception e) {
			return null;
		}
		return thread;
	}

}
