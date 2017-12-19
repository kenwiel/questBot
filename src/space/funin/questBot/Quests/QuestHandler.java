package space.funin.questBot.Quests;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.List;
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


        if (user.equals(api.getYourself()))
            return;

        // only check messages that contain a group mention and DONT start with
        // a bot command
        if (message.getContent().contains("<@&") && !message.getContent().startsWith(defaultPrefix)) {
            List<Long> mentionedRoles = message.getMentionedRoles().stream().map(Role::getId).collect(Collectors.toList());

            mentionedRoles.forEach(roleId -> {
                questList.stream().filter(quest -> quest.getRole().getId() == roleId).forEach(quest -> {

                });
            });

        }
    }



    public List<Quest> getQuestList() {
        return questList;
    }

    public void registerQuest(Quest quest) {
        questList.add(quest);
    }

    public void removeQuest(Quest quest) {
        questList.remove(quest);
    }
}
