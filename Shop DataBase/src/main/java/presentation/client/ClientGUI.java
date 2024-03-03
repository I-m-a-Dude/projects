package presentation.client;

import model.Client;
import presentation.extras.Custom;
import presentation.extras.Reflection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ClientGUI class handles the graphical user interface for managing clients.
 */
public class ClientGUI extends JFrame {
    private final JFrame frame;
    private final JTextField nameField, addressField, idField;
    private ClientController clientController;
    private final JList<Client> clientJList; // Declare the JList
    private List<Client> clients;
    private final JTable clientTable; // Replace JList with JTable
    private JScrollPane scrollPane; // Declare JScrollPane here to update it later


    public ClientGUI() {
        frame = new JFrame("Client Operations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.getContentPane().setBackground(Color.BLACK);


        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("Client ID");
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField();

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String idText = idField.getText();
                if (idText != null && !idText.trim().isEmpty()) {
                    int id = Integer.parseInt(idText);
                    Client client = clientController.findClientById(id);
                    if (client != null) {
                        nameField.setText(client.getName());
                        addressField.setText(client.getAddress());
                    } else {
                        nameField.setText("");
                        addressField.setText("");
                    }
                } else {
                    nameField.setText("");
                    addressField.setText("");
                }
            }
        });
        JLabel nameLabel = new JLabel("Client Name");
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();
        JLabel addressLabel = new JLabel("Client Address");
        addressLabel.setForeground(Color.WHITE);
        addressField = new JTextField();

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton createButton = new Custom("Create Client");
        JButton updateButton = new Custom("Update Client");
        JButton deleteButton = new Custom("Delete Client");
        JButton readButton = new Custom("MainGUI Clients");

        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.setForeground(Color.WHITE);

        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setForeground(Color.WHITE);

        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setForeground(Color.WHITE);


        readButton.setFont(new Font("Arial", Font.BOLD, 14));
        readButton.setForeground(Color.WHITE);

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(readButton);

        // Initialize the JList
        clientJList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(clientJList); // Scroll pane for JList
        clientTable = new JTable();
        scrollPane = new JScrollPane(clientTable); // Scroll pane for JTable


        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                clientController.createClient(name, address);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.valueOf(idField.getText());
                String name = nameField.getText();
                String address = addressField.getText();
                clientController.updateClient(id, name, address);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.valueOf(idField.getText());
                clientController.deleteClient(id);
            }
        });

        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Client> clients = clientController.readClients();
                showClientsTable(clients);
            }
        });

        frame.setLayout(new GridLayout(6, 1)); // Adjust grid layout
        frame.add(idLabel);
        frame.add(idField);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(addressLabel);
        frame.add(addressField);
        frame.add(createButton);
        frame.add(updateButton);
        frame.add(deleteButton);
        frame.add(readButton);
      //  frame.add(scrollPane); // Add the scroll pane (with the JList) to the frame
    }


    public void show() {
        frame.setVisible(true);
    }
    /**
     * Sets the client controller for this view.
     *
     * @param clientController The client controller.
     */
    public void setController(ClientController clientController) {
        this.clientController = clientController;
    }
    /**
     * Displays the clients in a table.
     *
     * @param clients The list of clients to display.
     */
    public void showClientsTable(List<Client> clients) {
        if (clients == null) {
            JOptionPane.showMessageDialog(null, "No clients to display.");
            return;
        }
        JFrame tableFrame = new JFrame("Clients Table");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(400, 300);

        // Assume all clients have the same fields
        List<Map.Entry<String, Object>> fieldEntries = Reflection.retrieveProperties(clients.get(0));
        String[] columnNames = new String[fieldEntries.size()];
        for (int i = 0; i < fieldEntries.size(); i++) {
            columnNames[i] = fieldEntries.get(i).getKey();
        }

        Object[][] data = new Object[clients.size()][];
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            List<Object> values = new ArrayList<>();
            for (Map.Entry<String, Object> entry : Reflection.retrieveProperties(client)) {
                values.add(entry.getValue());
            }
            data[i] = values.toArray();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    /**
     * Updates the client table with the given list of clients.
     *
     * @param clients The updated list of clients.
     */
    public void updateClients(List<Client> clients) {
        DefaultTableModel tableModel = (DefaultTableModel) clientTable.getModel();

        // Clear the existing data
        tableModel.setRowCount(0);

        // Add the new data
        for (Client client : clients) {
            tableModel.addRow(new Object[]{client.getId(), client.getName(), client.getAddress()});
        }
    }
}
