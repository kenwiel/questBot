package space.funin.questBot.settings;

import com.google.gson.Gson;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.QuestBot;
import space.funin.questBot.Quests.Quest;
import space.funin.questBot.Quests.QuestHandler;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class SettingLoader {
    QuestHandler questHandler;
    Gson gson;

    public SettingLoader(QuestHandler questHandler) {
        this.questHandler = questHandler;
        this.gson = new Gson();
        loadSettings();
    }

    public void loadSettings() {
        for (Server s : QuestBot.getServers()) {
            File serverDir = new File(s.getIdAsString());
            for (File directory : serverDir.listFiles()) {

                //two subfolders: Quest and User
                FilePathBuilder.PathType pathType = null;
                switch (directory.getName()) {
                    case "questSettings":
                        pathType = FilePathBuilder.PathType.QUEST;
                        break;
                    case "userSettings":
                        pathType = FilePathBuilder.PathType.USER;
                        break;
                    default:
                        pathType = null;
                        break;
                }

                for (File settings : directory.listFiles()) {
                    Optional<String> jsonContent = SettingsHelper.readFile(settings.toPath(), null);;
                    if(!jsonContent.isPresent())
                        continue;

                    switch (pathType) {
                        case QUEST:
                            Quest quest = gson.fromJson(jsonContent.get(), Quest.class);
                            questHandler.registerQuest(quest);
                            break;
                        case USER:
                            //TODO user settings maybe one day
                            //Persistent roles and shit like that
                            break;
                        default:
                            //nothing should be in here
                            break;
                    }
                }
            }
        }
    }

    public void saveQuests() {
        questHandler.getQuestList().forEach((key, value) -> {
            String serverId = key.toString();
            value.forEach(quest -> {
                Role questRole = quest.getRole();

                Path output = new FilePathBuilder(serverId, FilePathBuilder.PathType.QUEST).setQuest(questRole.getIdAsString()).toPath();
                SettingsHelper.writeFile(output, gson.toJson(quest, Quest.class), null);
            });
        });
    }

    public void saveQuest(Quest quest) {
        Role questRole = quest.getRole();
        String serverId = questRole.getServer().getIdAsString();

        Path output = new FilePathBuilder(serverId, FilePathBuilder.PathType.QUEST).setQuest(questRole.getIdAsString()).toPath();
        SettingsHelper.writeFile(output, gson.toJson(quest, Quest.class), null);
    }
}
