package space.funin.questBot;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import space.funin.questBot.Runnables.CleanMap;
import space.funin.questBot.Runnables.Unmute;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimingHandler {
    private ScheduledThreadPoolExecutor executor;
    private Map<User, ScheduledFuture> unmuteMap = new HashMap<>();

    public TimingHandler() {
        executor = new ScheduledThreadPoolExecutor(5);
        executor.setRemoveOnCancelPolicy(true);

        //every minute: remove completed futures from the map
        executor.scheduleWithFixedDelay(new CleanMap(unmuteMap), 0, 60, TimeUnit.SECONDS);

    }

    public void scheduleUnmute(Server server, User user, LocalDateTime time) {
        long delay = Duration.between(LocalDateTime.now(), time).getSeconds();
        ScheduledFuture future = executor.schedule(new Unmute(server, user), delay, TimeUnit.SECONDS);

        if(unmuteMap.containsKey(user)) {
            unmuteMap.get(user).cancel(false);
            unmuteMap.replace(user, future);
            return;
        }
        unmuteMap.put(user, future);
    }
}
