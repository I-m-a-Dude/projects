package Business_Logic;

import Model.Server;
import Model.Task;

import java.util.List;

public interface Strategy {

    Server addTask(List<Server> servers, Task t);
}
