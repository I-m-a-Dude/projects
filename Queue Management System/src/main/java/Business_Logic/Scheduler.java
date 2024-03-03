package Business_Logic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;


public class Scheduler {

    private final List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTaskPerServer) {


        servers = new ArrayList<>();


        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
        }
    }

    public void changeStrategy(Selection policy) {

        if (policy == Selection.SHORTEST_QUEUE) {
            strategy = new QueueStrategy();
        }else
        if (policy == Selection.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task t) {

        Server selectedServer = strategy.addTask(servers, t);
        selectedServer.addTask(t);
    }

    public List<Server> getServers() {
        return servers;
    }

}
