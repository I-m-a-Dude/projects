package Business_Logic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {

    @Override
    public Server addTask(List<Server> servers, Task t) {
        Server minServer = null;
        int minWaitingPeriod = Integer.MAX_VALUE;

        for (Server s : servers) {
            int waitingPeriod = s.getWaitingTime();
            if (waitingPeriod < minWaitingPeriod) {
                minServer = s;
                minWaitingPeriod = waitingPeriod;
            }
        }

        if (minServer == null) {
            throw new IllegalStateException("No available queue");
        }

        return minServer;
    }
}
