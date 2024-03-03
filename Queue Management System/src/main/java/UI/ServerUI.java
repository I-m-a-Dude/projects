package UI;

import Model.Server;
import Model.ServerListener;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ServerUI extends JPanel implements ServerListener {
    private final Server server;
    private final StringBuilder tasksString = new StringBuilder();

    public ServerUI(Server server) {
        this.server = server;
        server.setListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel queueNameLabel = new JLabel("Queue " + (server.getID()));
        queueNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        queueNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        queueNameLabel.setForeground(Color.WHITE);
        add(queueNameLabel);
    }

    public void refresh() {
        removeAll();

        JLabel queueNameL = new JLabel("Queue " + (server.getID()));
        queueNameL.setFont(new Font("Arial", Font.PLAIN, 16));
        queueNameL.setAlignmentX(Component.CENTER_ALIGNMENT);
        queueNameL.setForeground(Color.LIGHT_GRAY);
        add(queueNameL);

        tasksString.setLength(0);

        Task[] tasks = server.getTasks().toArray(new Task[0]);
        for (Task t : tasks) {
            JLabel taskl = new JLabel("Customer " + t.getID() +" ~"+ t.getArrivalTime() + " ~ " + t.getServiceTime());
            taskl.setFont(new Font("Plain", Font.PLAIN, 14));
            taskl.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskl.setForeground(Color.WHITE);

            add(taskl);
            tasksString.append(taskl.getText()).append("\n");

            System.out.println("Customer " + t.getID() +" ~"+ t.getArrivalTime() + " ~ " + t.getServiceTime());
        }

        revalidate();

        repaint();
        writeToFile("output.txt");
    }


    public void writeToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName, true); // set the second argument to true to append to the file
            JLabel queueNameL = new JLabel("Queue " + (server.getID())+" at the time "+ (server.getWaitingTime()));

            writer.write("\n\n"); // add a separator between different queues
            writer.write(queueNameL.getText() + "\n");
            writer.write(tasksString.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void Update(Server server) {
        Runnable refreshRunnable = this::refresh;
        SwingUtilities.invokeLater(refreshRunnable);
    }
}
