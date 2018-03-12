package space.funin.questBot.Quests;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import space.funin.questBot.QuestBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Quest {
    private String name;
    private String searchString;
    private String description;
    private String archive;

    private List<Long> authorIds = new ArrayList<>();
    private Long roleId;


    private Quest(QuestBuilder builder) {
        this.name = builder.name.trim();
        this.searchString = builder.searchString != null ? builder.searchString.trim() : builder.name.trim();
        this.description = builder.description != null ? builder.description.trim() : "No description provided";
        this.archive = builder.archive != null ? builder.archive.trim() : "No archive provided.";
        setAuthorIds(builder.authors);
        setRoleId(builder.role);
    }

    private void setAuthorIds(List<User> authors) {
        for (User user : authors) {
            authorIds.add(user.getId());
        }
    }

    private void setRoleId(Role role) {
        roleId = role.getId();
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
        return authorIds.stream().map(id -> QuestBot.getApi().getUserById(id).join()).collect(Collectors.toList());
    }

    public void setAuthors(List<User> authors) {
        setAuthorIds(authors);
    }

    public Role getRole() {
        return QuestBot.getApi().getRoleById(roleId).orElse(null);
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

        public Quest build() throws IllegalArgumentException {
            if (name == null || authors == null || role == null)
                throw new IllegalArgumentException("Missing at least one required argument. Please double check your input.");

            return new Quest(this);
        }
    }
}
