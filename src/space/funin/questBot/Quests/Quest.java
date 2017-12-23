package space.funin.questBot.Quests;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.List;

public class Quest {
    private String name;
    private String searchString;
    private String description;
    private String archive;
    private List<User> authors;
    private Role role;


    private Quest(QuestBuilder builder) {
        this.name = builder.name.trim();
        this.searchString = builder.searchString != null ? builder.searchString.trim() : builder.name.trim();
        this.description = builder.description != null ? builder.description.trim() : "No description provided";
        this.archive = builder.archive != null ? builder.archive.trim() : "No archive provided.";
        this.authors = builder.authors;
        this.role = builder.role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchString() {
        return searchString != null ? searchString : name;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static class QuestBuilder {
        private String name;
        private String searchString;
        private String description;
        private String archive;
        private List<User> authors;
        private Role role;

        public QuestBuilder(String name, List<User> authors, Role role) {
            this.name = name;
            this.authors = authors;
            this.role = role;
        }

        public QuestBuilder setSearchString(String searchString) {
            this.searchString = searchString;
            return this;
        }

        public QuestBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public QuestBuilder setArchive(String archive) {
            this.archive = archive;
            return this;
        }

        public Quest build() {
            return new Quest(this);
        }
    }
}
