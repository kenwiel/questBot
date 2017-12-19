package space.funin.questBot.Quests;

import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import javach.Thread;
import space.funin.questBot.QuestBot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestHelper {

    /**
     * sends a representation of a quest to a channel
     * @param quest the quest to represent
     * @param channel the channel to send to
     */
    public static void linkThread(Quest quest, ServerTextChannel channel) {
        channel.sendMessage(embedQuest(quest));
    }

    /**
     * Creates an EmbedBuilder to represent a quest
     * @param quest the quest to represent
     * @return an embed representation of the quest
     */
    public static EmbedBuilder embedQuest(Quest quest) {
        Thread thread = getThreadBySubject(quest.getSearchString());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(thread.getOP().getPosterName())
                .setThumbnail(thread.getOP().getFile().url())
                .setTitle(thread.getOP().subject())
                .setUrl(thread.url());

        return eb;
    }

    /**
     * gets the newest thread on /qst/ for which the OP subject contains the given subject
     * @param subject the subject to look for
     * @return the newest thread with that subject
     */
    public static Thread getThreadBySubject(String subject) {
        List<Thread> threadList = QuestBot.getQst().getCachedThreads();

        threadList = threadList.stream().filter(thread -> thread.getOP().subject().contains(subject)).collect(Collectors.toList());
        Collections.sort(threadList);

        return threadList.get(0);
    }

    /**
     * takes a string and maps its contents to the various quest properties
     *
     * The input should have the following format:
     * --property value1 value2 --property2 value3 --property3 value4 ...
     *
     * @param input the string to parse
     * @return a mapping of questProperties to the describing entries
     */
    public static Map<QuestProperty, Object> parseQuestString(String input, Message message) {
        Map<QuestProperty, Object> mapping = new HashMap<>();

        //split at the properties
        String[] splitInput = input.split("--");

        for (String partialInput : splitInput) {
            //get the property from the input
            String property = partialInput.split(" ")[0].toLowerCase();
            QuestProperty questProperty = null;

            switch(property) {
                case "name":
                    questProperty = QuestProperty.Name;
                    break;
                case "searchstring":
                    questProperty = QuestProperty.SearchString;
                    break;
                case "description":
                    questProperty = QuestProperty.Description;
                    break;
                case "archive":
                    questProperty = QuestProperty.Archive;
                    break;
                case "authors":

                    questProperty = QuestProperty.Authors;
                    break;
                case "role":
                    questProperty = QuestProperty.Role;
                    break;
                default:
                    break;
            }
            if(questProperty != null) {
                //assign each property the right type of value
                switch (questProperty) {
                    //fallthrough for all string values
                    case Name:
                    case SearchString:
                    case Description:
                    case Archive:
                        String value = partialInput.replace(property + " ", "");
                        mapping.put(questProperty, value);
                        break;
                    case Authors:
                        //user mentions MUST be authors, as no other property takes users as argument
                        mapping.put(questProperty, message.getMentionedUsers());
                        break;
                    case Role:
                        //role mentions MUST be the role belonging to the quest, as no other property takes a role as argument
                        mapping.put(questProperty, message.getMentionedRoles());
                        break;
                }
            }
        }

        return mapping;
    }
}
