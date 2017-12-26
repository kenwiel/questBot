package space.funin.questBot.settings;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A File Path Builder.
 *
 * File hierarchy:
 * Settings                      directory
 *  ┗Server                      directory
 *     ┣Quest                    directory
 *     ┃  ┗questID               settings file
 *     ┗User                     directory
 *        ┗userID                settings file
 */
public class FilePathBuilder {
    private static String baseDir = "settings/";

    //required
    private File serverDir;
    private PathType pathType;
    //only one required
    private String userId;
    private String questId;

    public FilePathBuilder(String serverID, PathType pathType) {
        this.serverDir = new File(baseDir+serverID);
        this.pathType = pathType;
    }

    /**
     * Turns the builder into a File object.
     * Returns an empty Optional if not all values have been set.
     *
     * @return The path object.
     */
    public File toFile() {
        File filePath = null;
        switch (pathType) {
            case QUEST:
                if(! (serverDir == null || questId == null) ) {
                    filePath = new File(serverDir, (pathType.getFolder() + questId) );
                }
                break;
            case USER:
                if(! (serverDir == null || userId == null) ) {
                    filePath = new File(serverDir, (pathType.getFolder() + userId) );
                }
                break;
        }
        return filePath;
    }

    public Path toPath() {
        return toFile().toPath();
    }

    /**
     * Sets the User id
     *
     * @param userId the user id.
     * @return the FilePathBuilder.
     */
    public FilePathBuilder setUser(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Sets the Quest Id
     *
     * @param questId the quest ID.
     * @return the FilePathBuilder.
     */
    public FilePathBuilder setQuest(String questId) {
        this.questId = questId;
        return this;
    }

    /**
     * This is a list of subfolders in settings
     */
    public enum PathType {
        QUEST("questSettings/"),
        USER("userSettings/");

        private String folder;

        PathType(String folder) {
            this.folder = folder;
        }

        public String getFolder() {
            return folder;
        }
    }

    public static String getBaseDir() {
        return baseDir;
    }
}
