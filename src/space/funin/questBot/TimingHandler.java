package space.funin.questBot;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import space.funin.questBot.Runnables.CleanMap;
import space.funin.questBot.Runnables.UnMute;
import space.funin.questBot.Runnables.UpdateCache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimingHandler {
    private ScheduledThreadPoolExecutor executor;
    private Map<Server, Map<User, ScheduledFuture>> unMuteMap = new HashMap<>();

    public TimingHandler() {
        executor = new ScheduledThreadPoolExecutor(5);
        executor.setRemoveOnCancelPolicy(true);

        //every minute: remove completed futures from the map
        executor.scheduleWithFixedDelay(new CleanMap(unMuteMap), 0, 60, TimeUnit.SECONDS);
    }

    public void scheduleCacheUpdate() {
        executor.scheduleAtFixedRate(() -> System.out.println(QuestBot.getQst().cache.size()), 0, 5, TimeUnit.SECONDS);
        QuestBot.getQst().refreshCache();
        executor.scheduleAtFixedRate(new UpdateCache(), 0, 30, TimeUnit.SECONDS);
    }

    public void scheduleUnmute(Server server, User user, Duration duration) {
        scheduleUnmute(server, user, duration.getSeconds());
    }

    public void scheduleUnmute(Server server, User user, long duration) {
        ScheduledFuture future = executor.schedule(new UnMute(server, user), duration, TimeUnit.SECONDS);

        //if it doesnt exist yet, create it
        if(!unMuteMap.containsKey(server)) {
            unMuteMap.put(server, new HashMap<>());
        }

        Map<User, ScheduledFuture> innerMap = unMuteMap.get(server);
        if(innerMap.containsKey(user)) {
            innerMap.get(user).cancel(false);
            innerMap.replace(user, future);
            return;
        }
        innerMap.put(user, future);
    }
}
