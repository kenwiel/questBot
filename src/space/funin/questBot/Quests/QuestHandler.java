package space.funin.questBot.Quests;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestHandler {
    private List<Quest> questList = new ArrayList<>();

    private String defaultPrefix;
    private DiscordApi api;

    public QuestHandler(DiscordApi api, String defaultPrefix) {
            this.api = api;
            this.defaultPrefix = defaultPrefix;
            api.addMessageCreateListener(this::handleMessageCreate);
    }

    private  void handleMessageCreate(MessageCreateEvent event) {
        User user = event.getMessage().getAuthor().asUser().get();
        Message message = event.getMessage();
        ServerTextChannel channel = event.getChannel().asServerTextChannel().get();

        if (user.isBot())
            return;

        // only check messages that contain a group mention and DONT start with
        // a bot command
        if (message.getContent().contains("<@&") && !message.getContent().startsWith(defaultPrefix)) {
            List<Long> mentionedRoles = message.getMentionedRoles().stream().map(Role::getId).collect(Collectors.toList());

            mentionedRoles.forEach(roleId -> {
                questList.stream().filter(quest -> quest.getRole().getId() == roleId).forEach(quest -> {
                    QuestHelper.linkThread(quest, channel);
                });
            });

        }
    }



    public List<Quest> getQuestList() {
        return questList;
    }

    /**
     * Adds a new quest or replaces an existing quest with the same role
     * @param quest the quest to add/replace
     */
    public void registerQuest(Quest quest) {
        //remove potential duplicate quest with the same  role --> same quest, as role is unique
        Optional<Quest> exists = questList.stream()
                .filter(quest1 -> quest.getRole().getId() == quest1.getRole().getId()).findAny();
        exists.ifPresent(questList::remove);

        questList.add(quest);
    }

    public void removeQuest(Quest quest) {
        questList.remove(quest);
    }

    public Optional<Quest> getQuestByRole(Role role) {
        return questList.stream().filter(quest -> quest.getRole().equals(role)).findAny();
    }
}
