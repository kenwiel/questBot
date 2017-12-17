package space.funin.questBot.Runnables;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.Collection;

public class SetupMute implements Runnable {
    private Server server;

    public SetupMute(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Collection<Role> roleCollection = server.getRoles();

        if(roleCollection.parallelStream().map(Role::getName).noneMatch(role -> role.equalsIgnoreCase("muted"))) {

        }
    }
}
