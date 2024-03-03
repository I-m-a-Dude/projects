package Model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private static final AtomicInteger nextID = new AtomicInteger(1);
    private final AtomicInteger waitingTime;
    private final int ID;
    private volatile boolean running = true;

    private ServerListener listener;

    public List<Task> getTasks() {
        return new LinkedList<>(tasks);
    }

    public int getWaitingTime() {
        return waitingTime.get();
    }

    public int getID() {
        return ID;
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    public Server() {
        //initialize queue and waitingPeriod
        tasks = new LinkedBlockingQueue<>();
        waitingTime = new AtomicInteger();
        ID = nextID.getAndIncrement();
        running = true;
    }

    public void addTask(Task newTask) {
        //adding task to queue
        tasks.add(newTask);
        //incrementing the waitingPeriod
        waitingTime.addAndGet(newTask.getServiceTime());
    }

    public void run() {
        while (running) {
            try {
                Task task = tasks.poll(100, TimeUnit.MILLISECONDS);
                if (task != null) {
                    Thread.sleep(task.getServiceTime() * 1000L);
                    waitingTime.addAndGet(-task.getServiceTime());
                    removeTask(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    public void removeTask(Task task) {
        tasks.remove(task);
        if (listener != null) {
            listener.Update(this);
        }
    }
}
