package space.funin.questBot.Quests;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import space.funin.questBot.settings.SettingLoader;

import java.util.*;
import java.util.stream.Collectors;

public class QuestHandler {
    private Map<Long, List<Quest>> questList = new HashMap<>();

    private String defaultPrefix;
    private SettingLoader settingLoader;


    public QuestHandler(DiscordApi api, String defaultPrefix) {
            this.defaultPrefix = defaultPrefix;
            api.addMessageCreateListener(this::handleMessageCreate);
            settingLoader = new SettingLoader(this);
    }

    private  void handleMessageCreate(MessageCreateEvent event) {
        User user = event.getMessage().getAuthor().asUser().get();
        Message message = event.getMessage();
        ServerTextChannel channel = event.getChannel().asServerTextChannel().get();
        Server server = event.getServer().get();

        if (user.isBot())
            return;

        // only check messages that contain a group mention and DONT start with
        // a bot command
        if (message.getContent().contains("<@&") && !message.getContent().startsWith(defaultPrefix)) {
            List<Long> mentionedRoles = message.getMentionedRoles().stream().map(Role::getId).collect(Collectors.toList());

            mentionedRoles.forEach(roleId -> {
                questList.get(server.getId()).stream().filter(quest -> quest.getRole().getId() == roleId).forEach(quest -> {
                    QuestHelper.linkThread(quest, channel);
                });
            });

        }
    }



    public List<Quest> getQuestList(Server server) {
        return questList.get(server.getId());
    }

    public Map<Long, List<Quest>> getQuestList() {
        return questList;
    }

    /**
     * Adds a new quest or replaces an existing quest with the same role
     * @param quest the quest to add/replace
     */
    public void registerQuest(Quest quest) {
        //make sure there's a list to put stuff into
        if(!questList.containsKey(quest.getRole().getServer().getId()))
            questList.put(quest.getRole().getServer().getId(), new ArrayList<>());

        //remove potential duplicate quest
        removeQuest(quest);

        questList.get(quest.getRole().getServer().getId()).add(quest);

        settingLoader.saveQuest(quest);
    }

    public void removeQuest(Quest quest) {
        //removes a quest if it has the same role as quest, as role Ids are unique, this removes the wanted quest
        Optional<Quest> exists = questList.get(quest.getRole().getServer().getId()).stream()
                .filter(quest1 -> quest.getRole().getId() == quest1.getRole().getId()).findAny();

        exists.ifPresent(quest1 -> questList.get(quest.getRole().getServer().getId()).remove(quest1));
    }

    public Optional<Quest> getQuestByRole(Role role) {
        List<Quest> allQuests = new ArrayList<>();
        questList.values().forEach(allQuests::addAll);

        return allQuests.stream().filter(quest -> quest.getRole().getId() == role.getId()).findAny();
    }
}
