package start;

import dao.ClientDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import presentation.MainGUI;
import presentation.client.ClientController;
import presentation.client.ClientGUI;
import presentation.order.OrderController;
import presentation.order.OrderGUI;
import presentation.product.ProductController;
import presentation.product.ProductGUI;

public class AppConstructor {
    private final ClientController clientController;
    private final ProductController productController;
    private final OrderController orderController;
    private final MainGUI mainMainGUI;

    public AppConstructor() {
        // Initialize your DAOs
        ClientDAO clientDAO = new ClientDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        // Initialize your Views
        ClientGUI clientGUI = new ClientGUI();
        ProductGUI productGUI = new ProductGUI();

        // Initialize your Controllers with Views and DAOs
        clientController = new ClientController(clientGUI, clientDAO);
        productController = new ProductController(productGUI, productDAO);
        OrderGUI orderGUI = new OrderGUI(productController);
        orderController = new OrderController(orderGUI, orderDAO, productDAO);

        // Initialize your main MainGUI with the controllers
        mainMainGUI = new MainGUI(clientController, productController, orderController);
    }

    public void startApp() {
        // Display the main view
        mainMainGUI.show();
    }

    public static void main(String[] args) {
        AppConstructor constructor = new AppConstructor();
        constructor.startApp();
    }
}
