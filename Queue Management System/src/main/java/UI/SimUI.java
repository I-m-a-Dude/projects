package UI;

import Model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimUI extends JFrame {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;
    private static final int PADDING = 60;


    public SimUI(List<Server> servers) {

        setTitle("Demo");
        setSize(WIDTH * servers.size() + PADDING * (servers.size() + 1), HEIGHT + PADDING * 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(Color.BLACK);

        for (int i = 0; i < servers.size(); i++) {
            ServerUI serverUI = new ServerUI(servers.get(i));
            serverUI.setBounds(PADDING + i * (WIDTH + PADDING), PADDING, WIDTH, HEIGHT);
            serverUI.setBackground(Color.BLACK);

            add(serverUI);
        }

        Timer timer = new Timer(1000, e -> reload());
        timer.start();
    }

    public void reload() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof ServerUI) {
                ((ServerUI) component).refresh();
                ((ServerUI)component).writeToFile("output.txt");
            }
        }
    }
}
