package presentation.order;

import dao.ClientDAO;
import dao.LogDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Bill;
import model.Client;
import model.Order;
import model.Product;

import java.util.List;
import java.util.Random;


public class OrderController {
    Random random = new Random();
    private final OrderGUI orderView;
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final ClientDAO clientDAO;
    private final LogDAO logDAO;

    /**
     * Constructs an OrderController with the specified dependencies.
     *
     * @param orderGUI  The OrderGUI instance for displaying and interacting with orders.
     * @param orderDAO   The OrderDAO instance for accessing order data.
     * @param productDAO The ProductDAO instance for accessing product data.
     */

    public OrderController(OrderGUI orderGUI, OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderView = orderGUI;
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.clientDAO = new ClientDAO();
        this.logDAO = new LogDAO();
    }
    /**
     * Creates a new order with the specified client ID, product ID, quantity, and total price.
     *
     * @param clientId   The ID of the client placing the order.
     * @param productId  The ID of the product being ordered.
     * @param quantity   The quantity of the product being ordered.
     * @param totalprice The total price of the order.
     */
    public void createOrder(Integer clientId, Integer productId, Integer quantity, Double totalprice) {
        try {
            Integer orderId = random.nextInt(1000000000);

            Product product = productDAO.findById(productId);
            Client client = clientDAO.findById(clientId);
            String clientName = client.getName();
            String productName = product.getName();
            if (product.getStock() < quantity) {
                orderView.showUnderStockMessage();
            } else {
                Order order = new Order(orderId, clientId, productId, quantity, totalprice);
                orderDAO.create(order);

                // Generate a random bill ID with max 10 digits
                Integer billId = random.nextInt(1000000000);

                // Create the Bill using the same order ID
                Bill bill = new Bill(billId, orderId, clientName, productName, quantity, totalprice);
                logDAO.create(bill);

                product.setStock(product.getStock() - quantity);
                productDAO.update(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    /**
     * Retrieves a list of all orders.
     *
     * @return The list of all orders.
     */

    public List<Order> findAllOrders() {
        return orderDAO.findAll();
    }
    // Rest of the methods as before
}
