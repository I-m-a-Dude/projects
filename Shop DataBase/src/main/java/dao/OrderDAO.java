package dao;

import model.Order;
import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The OrderDAO class provides data access methods for interacting with the "ordertable" table in the database,
 * which represents orders placed by clients.
 */
public class OrderDAO extends AbstractDAO<Order> {

    /**
     * Constructs a new OrderDAO object.
     */
    public OrderDAO() {
        super(Order.class);
    }

    /**
     * Creates a new order in the database.
     *
     * @param order The order object to create.
     */
    @Override
    public void create(Order order) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO ordertable (id, clientId, productId, quantity, totalprice) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, order.getId());
            statement.setInt(2, order.getClientId());
            statement.setInt(3, order.getProductId());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotalprice());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while creating order: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Updates an existing order in the database.
     *
     * @param order The order object to update.
     */
    @Override
    public void update(Order order) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE ordertable SET clientId = ?, productId = ?, quantity = ?, totalprice = ? WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, order.getClientId());
            statement.setInt(2, order.getProductId());
            statement.setInt(3, order.getQuantity());
            statement.setDouble(4, order.getTotalprice());
            statement.setInt(5, order.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating order: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Deletes an order from the database by its ID.
     *
     * @param id The ID of the order to delete.
     */
    @Override
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM ordertable WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while deleting order: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Finds an order in the database by its ID.
     *
     * @param id The ID of the order to find.
     * @return The order object with the specified ID, or null if not found.
     */
    @Override
    public Order findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM ordertable WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractOrderFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding order by id: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all order objects.
     */
    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM ordertable";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = extractOrderFromResultSet(resultSet);
                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding all orders: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return orders;
    }

    /**
     * Extracts an order object from the current row of the result set.
     *
     * @param resultSet The result set from which to extract the order.
     * @return The extracted order object.
     * @throws SQLException If an error occurs while extracting the order from the result set.
     */
    private Order extractOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        Integer clientId = resultSet.getInt("clientId");
        Integer productId = resultSet.getInt("productId");
        Integer quantity = resultSet.getInt("quantity");
        Double totalprice = resultSet.getDouble("totalprice");

        return new Order(id, clientId, productId, quantity, totalprice);
    }
}

