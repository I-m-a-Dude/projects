package presentation.product;

import model.Product;
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

public class ProductGUI {
    private final JFrame frame;
    private final JTextField idField, nameField, priceField, stockField;
    private final JButton createButton, updateButton, deleteButton, readButton;
    private ProductController productController;
    private final JTable productTable;
    private JScrollPane scrollPane;

    public ProductGUI() {
        frame = new JFrame("Product Operations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel idLabel = new JLabel("Product ID");
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField();

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String idText = idField.getText();
                if (idText != null && !idText.trim().isEmpty()) {
                    Integer id = Integer.valueOf(idText);
                    Product product = productController.getProductById(id);
                    if (product != null) {
                        nameField.setText(product.getName());
                        priceField.setText(String.valueOf(product.getPrice()));
                        stockField.setText(String.valueOf(product.getStock()));
                    }
                }
            }
        });

        JLabel nameLabel = new JLabel("Product Name");
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();

        JLabel priceLabel = new JLabel("Product Price");
        priceLabel.setForeground(Color.WHITE);
        priceField = new JTextField();

        JLabel stockLabel = new JLabel("Product Stock");
        stockLabel.setForeground(Color.WHITE);
        stockField = new JTextField();

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(stockLabel);
        inputPanel.add(stockField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        createButton = new Custom("Create Product");
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.setForeground(Color.WHITE);

        updateButton = new Custom("Update Product");
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));

        deleteButton = new Custom("Delete Product");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setForeground(Color.WHITE);

        readButton = new Custom("MainGUI Products");
        readButton.setFont(new Font("Arial", Font.BOLD, 14));
        readButton.setForeground(Color.WHITE);

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(readButton);

        // Initialize the JList
        JList<Object> productJList = new JList<>();
        scrollPane = new JScrollPane(productJList); // Scroll pane for JList
        productTable = new JTable();
        scrollPane = new JScrollPane(productTable); // Scroll pane for JTable

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                Double price = Double.valueOf(priceField.getText());
                Integer stock = Integer.valueOf(stockField.getText());
                productController.createProduct(name, price, stock);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override

                public void actionPerformed (ActionEvent e){
                    Integer id = Integer.valueOf(idField.getText());
                    String name = nameField.getText();
                    Double price = Double.valueOf(priceField.getText());
                    Integer stock = Integer.valueOf(stockField.getText());
                    productController.updateProduct(id, name, price, stock);
                //client.updateClient(id, name, address);
                }

        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.valueOf(idField.getText());
                productController.deleteProduct(id);
            }
        });

        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> products = productController.readProduct();
                showProductsTable(products);
            }
        });

        frame.setLayout(new GridLayout(8, 1)); // Adjust grid layout
        frame.add(idLabel);
        frame.add(idField);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(stockLabel);
        frame.add(stockField);
        frame.add(createButton);
        frame.add(updateButton);
        frame.add(deleteButton);
        frame.add(readButton);
       // frame.add(scrollPane); // Add the scroll pane (with the JList) to the frame

    }

    public void show() {
        frame.setVisible(true);
    }

    public void setController(ProductController productController) {
        this.productController = productController;
    }

    public void showProductsTable(List<Product> products) {
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No products to display.");
            return;
        }

        JFrame tableFrame = new JFrame("Products Table");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(400, 300);

        // Assume all products have the same fields
        List<Map.Entry<String, Object>> fieldEntries = Reflection.retrieveProperties(products.get(0));
        String[] columnNames = new String[fieldEntries.size()];
        for (int i = 0; i < fieldEntries.size(); i++) {
            columnNames[i] = fieldEntries.get(i).getKey();
        }

        Object[][] data = new Object[products.size()][];
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            List<Object> values = new ArrayList<>();
            for (Map.Entry<String, Object> entry : Reflection.retrieveProperties(product)) {
                values.add(entry.getValue());
            }
            data[i] = values.toArray();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    public void updateProduct(List<Product> products) {
        DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();
        tableModel.setRowCount(0);

        for (Product product : products) {
            tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getStock()});
        }
    }
}
