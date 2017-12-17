package space.funin.questBot.Runnables;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class CleanMap implements Runnable {
    private Map<Server, Map<User, ScheduledFuture>> toClean;

    public CleanMap(Map<Server, Map<User, ScheduledFuture>> toClean) {
        this.toClean = toClean;
    }

    @Override
    public void run() {
        //go through each server
        for (Map<User, ScheduledFuture> entry : toClean.values()) {
            //for each ScheduledFuture, remove it from the inner map if it is done.
            entry.entrySet().removeIf(userScheduledFutureEntry -> userScheduledFutureEntry.getValue().isDone());
        }
    }
}
