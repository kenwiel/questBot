package space.funin.questBot.settings;

import com.google.gson.Gson;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.Quests.Quest;
import space.funin.questBot.Quests.QuestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingLoader {
    QuestHandler questHandler;
    Gson gson;

    public SettingLoader(QuestHandler questHandler) {
        this.questHandler = questHandler;
        this.gson = new Gson();

        try {
            loadSettings();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public void loadSettings() throws IOException {
        Files.walk(Paths.get(FilePathBuilder.getBaseDir())).filter(Files::isRegularFile)
                .forEach(path -> {
            File file = path.toFile();
            System.out.println(path);
            if (file.getParentFile().getName().contains("questSettings")) {
                System.out.println("contains");
                SettingsHelper.readFile(path, null).ifPresent(json -> {
                    System.out.println("reading");
                    Quest quest = gson.fromJson(json, Quest.class);
                    System.out.println("reading");
                    questHandler.registerQuestInit(quest);
                    System.out.println("reading");
                    System.out.println("registered as quest: " + quest.getName());
                });
            }
        });
        System.out.println("done");
    }

    public void saveQuest(Quest quest) {
        Role questRole = quest.getRole();
        String serverId = questRole.getServer().getIdAsString();

        Path output = new FilePathBuilder(serverId, FilePathBuilder.PathType.QUEST).setQuest(questRole.getIdAsString()).toPath();
        SettingsHelper.writeFile(output, gson.toJson(quest, Quest.class), null);
    }
}