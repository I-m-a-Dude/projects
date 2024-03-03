package presentation.order;

import dao.LogDAO;
import model.Bill;
import model.Order;
import presentation.extras.Custom;
import presentation.extras.Reflection;
import presentation.product.ProductController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OrderGUI {

    private final LogDAO logDAO;
    private final JFrame frame;
    private final JTextField idField, clientIdField, quantityField, totalPriceField;
    private final ProductController productController;
    private OrderController orderController;
    private final JTable orderTable;
    private final JComboBox<String> productNamesBox;
    private final JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private final JButton readButton;
   // private final JButton showBillsButton;

    public OrderGUI(ProductController productController) {
        this.productController = productController;
        this.logDAO = new LogDAO();
        frame = new JFrame("Order Operations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel idLabel = new JLabel("Order ID");
        idField = new JTextField();
        JLabel clientIdLabel = new JLabel("Client ID");
        clientIdField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity");
        quantityField = new JTextField();
        JLabel totalPriceLabel = new JLabel("Total Price");
        totalPriceField = new JTextField();

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(clientIdLabel);
        inputPanel.add(clientIdField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(totalPriceLabel);
        inputPanel.add(totalPriceField);

        JLabel productLabel = new JLabel("Product");
        productNamesBox = new JComboBox<>();
        // populate the JComboBox
        List<String> productNames = productController.getAllProductNames();
        for (String productName : productNames) {
            productNamesBox.addItem(productName);
        }

        inputPanel.add(productLabel);
        inputPanel.add(productNamesBox);

        quantityField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            public void removeUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            public void changedUpdate(DocumentEvent e) {
                updateTotalPrice();
            }
            public void updateTotalPrice() {
                String selectedProduct = (String) productNamesBox.getSelectedItem();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException e) {
                    // quantity is not a number, can't calculate total price
                    return;
                }

                double productPrice = productController.getProductPrice(selectedProduct);
                double totalPrice = productPrice * quantity;
                totalPriceField.setText(String.valueOf(totalPrice));
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));

        createButton = new Custom("Create Order");
        readButton = new Custom("MainGUI Orders");
        //showBillsButton = new Custom("Show Bills");

      //  showBillsButton.setForeground(Color.WHITE);
        readButton.setForeground(Color.WHITE);
        createButton.setForeground(Color.WHITE);

       // buttonPanel.add(showBillsButton);
        buttonPanel.add(createButton);
        buttonPanel.add(readButton);


        // Initialize the JTable
        orderTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(orderTable); // Scroll pane for JTable

        frame.setLayout(new GridLayout(3, 1));
        frame.add(inputPanel);
        frame.add(buttonPanel);
        frame.add(scrollPane);
    }

    public void setController(OrderController orderController) {
        this.orderController = orderController;
        AtomicInteger clientId = new AtomicInteger();
        AtomicInteger productId = new AtomicInteger();
        AtomicInteger quantity = new AtomicInteger();
        AtomicReference<Double> totalPrice = new AtomicReference<>(0.0); // Initialize with a default value of 0.0

        createButton.addActionListener(e -> {
            clientId.set(Integer.parseInt(clientIdField.getText()));
            productId.set(productController.getProductByName((String) productNamesBox.getSelectedItem()).getId());
            quantity.set(Integer.parseInt(quantityField.getText()));
            totalPrice.set(Double.parseDouble(totalPriceField.getText()));
            orderController.createOrder(clientId.get(), productId.get(), quantity.get(), totalPrice.get());
        });
       // showBillsButton.addActionListener(e -> showBillsTable(logDAO.findAll()));
        readButton.addActionListener(e -> showOrdersTable(orderController.findAllOrders()));
    }
    private void showBillsTable(List<Bill> bills) {
        if (bills == null || bills.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No bills to display.");
            return;
        }

        JFrame billsFrame = new JFrame("Bills");
        billsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        billsFrame.setSize(500, 400);

        // Assume all bills have the same fields
        List<Map.Entry<String, Object>> fieldEntries = Reflection.retrieveProperties(bills.get(0));
        String[] columnNames = new String[fieldEntries.size()];
        for (int i = 0; i < fieldEntries.size(); i++) {
            columnNames[i] = fieldEntries.get(i).getKey();
        }

        Object[][] data = new Object[bills.size()][];
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            List<Object> values = new ArrayList<>();
            for (Map.Entry<String, Object> entry : Reflection.retrieveProperties(bill)) {
                values.add(entry.getValue());
            }
            data[i] = values.toArray();
        }

        JTable billsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(billsTable);
        billsFrame.add(scrollPane);
        billsFrame.setVisible(true);
    }

    private void showOrdersTable(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No orders to display.");
            return;
        }

        // Assume all orders have the same fields
        List<Map.Entry<String, Object>> fieldEntries = Reflection.retrieveProperties(orders.get(0));
        String[] columnNames = new String[fieldEntries.size()];
        for (int i = 0; i < fieldEntries.size(); i++) {
            columnNames[i] = fieldEntries.get(i).getKey();
        }

        Object[][] data = new Object[orders.size()][];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            List<Object> values = new ArrayList<>();
            for (Map.Entry<String, Object> entry : Reflection.retrieveProperties(order)) {
                values.add(entry.getValue());
            }
            data[i] = values.toArray();
        }

        orderTable.setModel(new DefaultTableModel(data, columnNames));
    }


    public void showUnderStockMessage() {
        JOptionPane.showMessageDialog(frame, "There is not enough stock for this product!", "Under Stock", JOptionPane.WARNING_MESSAGE);
    }
    public void show() {
        frame.setVisible(true);
    }
}
