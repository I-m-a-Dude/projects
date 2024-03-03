package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainUI extends JFrame {

    private final JTextField numberOfClientsField, numberOfServersField, timeLimitField;
    private final JTextField minArrivalTimeField, maxArrivalTimeField, minServiceTimeField, maxServiceTimeField;
    private final JButton startSimulationButton;

    public MainUI() {
        setTitle("Queues management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 0, 0));

        GridBagConstraints main = new GridBagConstraints();
        main.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        Color labelColor = new Color(255, 255, 255);

        JLabel numberOfClientsLabel = new JLabel("Numarul de clienti");
        numberOfClientsLabel.setFont(labelFont);
        numberOfClientsLabel.setForeground(labelColor);

        JLabel numberOfServersLabel = new JLabel("Numarul de cozi");
        numberOfServersLabel.setFont(labelFont);
        numberOfServersLabel.setForeground(labelColor);

        JLabel timeLimitLabel = new JLabel("Timpul maxim de simulare");
        timeLimitLabel.setFont(labelFont);
        timeLimitLabel.setForeground(labelColor);

        JLabel minArrivalTimeLabel = new JLabel("Timpul minim de sosire");
        minArrivalTimeLabel.setFont(labelFont);
        minArrivalTimeLabel.setForeground(labelColor);

        JLabel maxArrivalTimeLabel = new JLabel("Timpul maxim de sosire");
        maxArrivalTimeLabel.setFont(labelFont);
        maxArrivalTimeLabel.setForeground(labelColor);

        JLabel minServiceTimeLabel = new JLabel("Timpul minim de servire");
        minServiceTimeLabel.setFont(labelFont);
        minServiceTimeLabel.setForeground(labelColor);

        JLabel maxServiceTimeLabel = new JLabel("Timpul maxim de servire");
        maxServiceTimeLabel.setFont(labelFont);
        maxServiceTimeLabel.setForeground(labelColor);

        timeLimitField = new JTextField(10);
        numberOfServersField = new JTextField(10);
        minArrivalTimeField = new JTextField(10);
        numberOfClientsField = new JTextField(10);
        maxArrivalTimeField = new JTextField(10);
        minServiceTimeField = new JTextField(10);
        maxServiceTimeField = new JTextField(10);

        main.gridx = 0;
        main.gridy = 0;
        add(numberOfClientsLabel, main);

        main.gridx = 1;
        add(numberOfClientsField, main);

        main.gridx = 0;
        main.gridy++;
        add(numberOfServersLabel, main);

        main.gridx = 1;
        add(numberOfServersField, main);

        main.gridx = 0;
        main.gridy++;
        add(timeLimitLabel, main);

        main.gridx = 1;
        add(timeLimitField, main);

        main.gridx = 0;
        main.gridy++;
        add(minArrivalTimeLabel, main);

        main.gridx = 1;
        add(minArrivalTimeField, main);

        main.gridx = 0;
        main.gridy++;
        add(maxArrivalTimeLabel, main);

        main.gridx = 1;
        add(maxArrivalTimeField, main);

        main.gridx = 0;
        main.gridy++;
        add(minServiceTimeLabel, main);

        main.gridx = 1;
        add(minServiceTimeField, main);

        main.gridx = 0;
        main.gridy++;
        add(maxServiceTimeLabel, main);

        main.gridx = 1;
        add(maxServiceTimeField, main);

        main.gridx = 0;
        main.gridy++;
        main.gridwidth = 2;
        main.fill = GridBagConstraints.HORIZONTAL;

        startSimulationButton = new JButton("START");
        startSimulationButton.setBackground(new Color(43, 223, 114, 255));
        startSimulationButton.setForeground(new Color(255, 255, 255));
        startSimulationButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(startSimulationButton, main);
    }

    public void StartButtonActionListener(ActionListener listener) {
        startSimulationButton.addActionListener(listener);
    }

    public int getNumClients() {
        return Integer.parseInt(numberOfClientsField.getText());
    }

    public int getNumServers() {
        return Integer.parseInt(numberOfServersField.getText());
    }

    public int getSimTime() {
        return Integer.parseInt(timeLimitField.getText());
    }

    public int getMinArTime() {
        return Integer.parseInt(minArrivalTimeField.getText());
    }

    public int getMaxArTime() {
        return Integer.parseInt(maxArrivalTimeField.getText());
    }

    public int getMinServTime() {
        return Integer.parseInt(minServiceTimeField.getText());
    }

    public int getMaxServTime() {
        return Integer.parseInt(maxServiceTimeField.getText());
    }




}