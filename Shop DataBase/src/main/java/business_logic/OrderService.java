package business_logic;

import dao.LogDAO;
import dao.OrderDAO;
import model.Bill;
import model.Order;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final LogDAO logDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.logDAO = new LogDAO();
    }

    public void createOrder(Order order) {
        // check product stock before creating order
        // update product stock after creating order
        // create a new bill (Log record) after creating order
    }

    public void updateOrder(Order order) {
        orderDAO.update(order);
    }

    public void deleteOrder(int id) {
        orderDAO.delete(id);
    }

    public Order findOrderById(int id) {
        return orderDAO.findById(id);
    }

    public List<Order> findAllOrders() {
        return orderDAO.findAll();
    }

    public List<Bill> findAllBills() {
        return logDAO.findAll();
    }
}
