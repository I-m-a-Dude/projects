package presentation;

import dao.ClientDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import presentation.client.ClientController;
import presentation.client.ClientGUI;
import presentation.extras.Custom;
import presentation.order.OrderController;
import presentation.order.OrderGUI;
import presentation.product.ProductController;
import presentation.product.ProductGUI;

import javax.swing.*;
import java.awt.*;

public class MainGUI {
    private final JFrame frame;
    private final JPanel panel;
    private final JButton clientButton, productButton, orderButton;
    private ClientGUI clientGUI;
    private ProductGUI productGUI;

    public MainGUI(ClientController clientController, ProductController productController, OrderController orderController) {
        frame = new JFrame("Orders Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create the panel
        panel = new JPanel(null);
        panel.setBackground(Color.BLACK);
        panel.setBounds(0, 0, 400, 300);

        // Setup Buttons
        clientButton = new Custom("Client Operations");
        productButton = new Custom("Product Operations");
        orderButton = new Custom("Order Operations");

        // Customize Buttons
        clientButton.setFont(new Font("Arial", Font.BOLD, 14));
        clientButton.setBounds(50, 50, 300, 50);
        clientButton.setForeground(Color.WHITE);

        productButton.setFont(new Font("Arial", Font.BOLD, 14));
        productButton.setForeground(Color.WHITE);
        productButton.setBounds(50, 120, 300, 50);

        orderButton.setForeground(Color.WHITE);
        orderButton.setFont(new Font("Arial", Font.BOLD, 14));
        orderButton.setBounds(50, 190, 300, 50);

        // Setup Action Listeners
        clientButton.addActionListener(e -> {
            ClientGUI clientGUI = new ClientGUI();
            ClientController clientController1 = new ClientController(clientGUI, new ClientDAO());
            clientGUI.setController(clientController1);
            clientGUI.show();
        });

        productButton.addActionListener(e -> {
            ProductGUI productGUI = new ProductGUI();
            ProductController productController1 = new ProductController(productGUI, new ProductDAO());
            productGUI.setController(productController1);
            productGUI.show();
        });

        orderButton.addActionListener(e -> {
            ProductController productController1 = new ProductController(productGUI, new ProductDAO());
            OrderGUI orderGUI = new OrderGUI(productController1);
            OrderController orderController1 = new OrderController(orderGUI, new OrderDAO(), new ProductDAO());
            orderGUI.setController(orderController1);
            orderGUI.show();
        });

        // Add buttons to the panel
        panel.add(clientButton);
        panel.add(productButton);
        panel.add(orderButton);

        // Add the panel to the frame
        frame.add(panel);

    }

    public void show() {
        frame.setVisible(true);
    }
}
