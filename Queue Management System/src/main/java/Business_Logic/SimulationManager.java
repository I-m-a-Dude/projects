package Business_Logic;


import Model.Task;
import UI.MainUI;
import UI.SimUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class SimulationManager implements Runnable {

    private final Scheduler scheduler;
    private final SimUI SimFrame;
    private final List<Task> generatedTasks;
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    public Selection selection = Selection.SHORTEST_TIME;




    public SimulationManager(MainUI setupFrame) {
        timeLimit = setupFrame.getSimTime();
        minArrivalTime = setupFrame.getMinArTime();
        maxArrivalTime = setupFrame.getMaxArTime();
        minServiceTime = setupFrame.getMinServTime();
        maxServiceTime = setupFrame.getMaxServTime();
        numberOfServers = setupFrame.getNumServers();
        numberOfClients = setupFrame.getNumClients();

        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selection);

        SimFrame = new SimUI(scheduler.getServers());
        SimFrame.setVisible(true);
        generatedTasks = generateNRandomTasks(numberOfClients);


    }

    public static void main(String[] args) {
        MainUI setupFrame = new MainUI();
        setupFrame.setVisible(true);
        setupFrame.StartButtonActionListener(e -> {
            SimulationManager simulationManager = new SimulationManager(setupFrame);
            Thread t = new Thread(simulationManager);
            t.start();
            setupFrame.setVisible(true);
        });
    }

    public void update() {
        SimFrame.reload();
    }

    private List<Task> generateNRandomTasks(int n) {
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int arrivalTime = randomArrivalTime();
            int processingTime = randomProcessingTime();
            tasks.add(new Task(i, arrivalTime, processingTime));
        }

        tasks.sort(Comparator.comparing(Task::getArrivalTime));

        return tasks;
    }

    private int randomArrivalTime() {
        Random random = new Random();
        return minArrivalTime + random.nextInt(maxArrivalTime - minArrivalTime + 1);
    }

    private int randomProcessingTime() {
        Random random = new Random();
        return minServiceTime + random.nextInt(maxServiceTime - minServiceTime + 1);
    }


    @Override
    public void run() {
        int currentTime = 0;


        while (currentTime < timeLimit) {
            while (!generatedTasks.isEmpty() && generatedTasks.get(0).getArrivalTime() == currentTime) {
                Task currentTask = generatedTasks.remove(0);
                scheduler.dispatchTask(currentTask);
            }
            update();

            currentTime++;

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted: " + e.getMessage());
            }

        }

    }
}
