package Business_Logic;

import Model.Server;
import Model.Task;

import java.util.List;

public class QueueStrategy implements Strategy {

    @Override
    public Server addTask(List<Server> servers, Task t) {
        Server minServer = null;
        int minQueueSize = Integer.MAX_VALUE;

        for (Server s : servers) {
            int queueSize = s.getTasks().size();
            if (queueSize < minQueueSize) {
                minServer = s;
                minQueueSize = queueSize;
            }
        }

        if (minServer == null) {
            throw new IllegalMonitorStateException("No available queue!");
        }

        return minServer;
    }
}
