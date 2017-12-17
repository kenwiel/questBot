package space.funin.questBot.Runnables;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.Collection;

public class UnMute implements Runnable {
    private Server server;
    private User user;

    public UnMute(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public void run() {
        //get the roles the user has
        Collection<Role> roleCollection = user.getRoles(server);
        //remove the "muted" role from them
        roleCollection.removeIf(role -> role.getName().equalsIgnoreCase("muted"));
        //set that as the users new roles
        server.updateRoles(user, roleCollection);
    }
}
