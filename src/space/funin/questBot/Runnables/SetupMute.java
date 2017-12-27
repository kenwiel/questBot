package space.funin.questBot.Runnables;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.permissions.PermissionsBuilder;
import de.btobastian.javacord.entities.permissions.Role;

import java.util.Collection;
import java.util.List;

public class SetupMute implements Runnable {
    private Server server;

    public SetupMute(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Collection<Role> roleCollection = server.getRoles();

        if(roleCollection.parallelStream().map(Role::getName).noneMatch(role -> role.equalsIgnoreCase("muted"))) {
            Role muted = server.getRoleBuilder().setName("Muted").setPermissions(new PermissionsBuilder().build()).create().join();
            List<ServerTextChannel> channels = server.getTextChannels();
            //TODO: once its possible to set channel permission overrides, iterate over all channels and deny writing messages
        }
    }
}
