package space.funin.questBot.Runnables;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

public class Unmute implements Runnable {
    private Server server;
    private User user;

    public Unmute(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public void run() {

    }
}
