package space.funin.questBot.Runnables;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public class CleanMap implements Runnable {
    private Map<?, ScheduledFuture> toClean;

    public CleanMap(Map<?, ScheduledFuture> toClean) {
        this.toClean = toClean;
    }

    @Override
    public void run() {
        List<?> toRemove = toClean.entrySet().stream()
                .filter(userScheduledFutureEntry -> userScheduledFutureEntry.getValue().isDone()) //only entries that return true for isDone
                .map(Map.Entry::getKey).collect(Collectors.toList()); //make a list of the keys of those entries

        toRemove.forEach(toClean::remove);
    }
}
