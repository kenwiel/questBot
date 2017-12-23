package space.funin.questBot.Quests;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import javach.Thread;
import org.apache.commons.lang3.StringEscapeUtils;
import space.funin.questBot.Helper;
import space.funin.questBot.QuestBot;

import java.util.*;
import java.util.stream.Collectors;

public class QuestHelper {

    /**
     * sends a representation of a quest to a channel
     * @param quest the quest to represent
     * @param channel the channel to send to
     */
    public static void linkThread(Quest quest, ServerTextChannel channel) {
        EmbedBuilder eb = embedQuestThread(quest);
        if (eb == null)
            channel.sendMessage("Unable to find thread");
        channel.sendMessage(eb);
    }

    /**
     * Creates an EmbedBuilder to represent a quest
     * @param quest the quest to represent
     * @return an embed representation of the quest
     */
    public static EmbedBuilder embedQuestThread(Quest quest) {
        Optional<Thread> thread = getThreadBySubject(quest.getSearchString());
        EmbedBuilder eb = new EmbedBuilder();
        thread.ifPresent(realThread -> {

            String content = Helper.replaceHtmlWithMarkdown(realThread.getOP().getText());
            try {
                content = content.substring(0, content.indexOf('\n', 250)) + "\n\n...";
            } catch (StringIndexOutOfBoundsException ignored) {}


            eb.setAuthor(realThread.getOP().getPosterName())
                    .setThumbnail(realThread.getOP().getFile().url())
                    .setTitle(StringEscapeUtils.unescapeHtml3(realThread.getOP().subject()))
                    .setUrl(realThread.url())
                    .setDescription(content)
                    .setFooter(quest.getArchive());
        });
        if (thread.isPresent())
            return eb;

        return null;
    }

    public static EmbedBuilder embedQuest(Quest quest, Server server) {
        List<String> authors = quest.getAuthors().stream().map(user -> user.getDisplayName(server)).collect(Collectors.toList());
        String authorString = "";
        for (String s : authors) {
            authorString += s + " ";
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setAuthor(authorString)
                .setTitle(quest.getName())
                .setDescription(quest.getDescription())
                .setFooter(quest.getArchive());

        return eb;
    }

    /**
     * gets the newest thread on /qst/ for which the OP subject contains the given subject
     * @param subject the subject to look for
     * @return the newest thread with that subject
     */
    public static Optional<Thread> getThreadBySubject(String subject) {
        List<Thread> threadList = QuestBot.getQst().getCachedThreads();

        threadList = threadList.stream().filter(thread -> {
                    if(thread.getOP().subject() == null) {
                        return false;
                    }
                    return StringEscapeUtils.unescapeHtml3(thread.getOP().subject()).toLowerCase().contains(subject.toLowerCase());
                }).collect(Collectors.toList());
        Collections.sort(threadList);

        if (threadList.size() == 0)
            return Optional.empty();

        return Optional.of(threadList.get(0));
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
                        String value = partialInput.replaceFirst(property + " ", "");
                        mapping.put(questProperty, value);
                        break;
                    case Authors:
                        //user mentions MUST be authors, as no other property takes users as argument
                        mapping.put(questProperty, message.getMentionedUsers());
                        break;
                    case Role:
                        //role mentions MUST be the role belonging to the quest, as no other property takes a role as argument
                        //take only index 0, there shouldnt be more than one anyways
                        mapping.put(questProperty, message.getMentionedRoles().get(0));
                        break;
                }
            }
        }

        return mapping;
    }
}
