package dao;

import model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static connection.ConnectionFactory.getConnection;

/**
 * The LogDAO class provides data access methods for interacting with the "bills" table in the database,
 * which represents the logs of completed orders and bills.
 */
public class LogDAO extends AbstractDAO<Bill> {

    /**
     * Constructs a new LogDAO object.
     */
    public LogDAO() {
        super(Bill.class);
    }

    /**
     * Creates a new bill log in the database.
     *
     * @param bill The bill object to create.
     */
    @Override
    public void create(Bill bill) {
        String sql = "INSERT INTO bills (id, orderId, clientName, productName, quantity, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bill.id());
            statement.setInt(2, bill.orderId());
            statement.setString(3, bill.clientName());
            statement.setString(4, bill.productName());
            statement.setInt(5, bill.quantity());
            statement.setDouble(6, bill.totalPrice());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " rows into bills.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a bill log in the database by its ID.
     *
     * @param id The ID of the bill to find.
     * @return The bill object with the specified ID, or null if not found.
     */
    @Override
    public Bill findById(int id) {
        String sql = "SELECT * FROM bills WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    String clientName = resultSet.getString("clientName");
                    String productName = resultSet.getString("productName");
                    int quantity = resultSet.getInt("quantity");
                    double totalPrice = resultSet.getDouble("totalPrice");

                    return new Bill(id, orderId, clientName, productName, quantity, totalPrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves all bill logs from the database.
     *
     * @return A list of all bill objects.
     */
    @Override
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int orderId = resultSet.getInt("orderId");
                String clientName = resultSet.getString("clientName");
                String productName = resultSet.getString("productName");
                int quantity = resultSet.getInt("quantity");
                double totalPrice = resultSet.getDouble("totalPrice");

                Bill bill = new Bill(id, orderId, clientName, productName, quantity, totalPrice);
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }
    /**
     * Updates a bill log in the database.
     * This operation is not supported for the LogDAO since bills should be immutable.
     *
     * @param bill The bill object to update.
     * @throws UnsupportedOperationException if the update operation is called.
     */
    @Override
    public void update(Bill bill) {
        // No update method required for LogDAO since bills should be immutable
        throw new UnsupportedOperationException("Bills cannot be updated.");
    }

    /**
     * Deletes a bill log from the database.
     * This operation is not supported for the LogDAO since bills should be immutable.
     *
     * @param id The ID of the bill to delete.
     * @throws UnsupportedOperationException if the delete operation is called.
     */
    @Override
    public void delete(int id) {
        // No delete method required for LogDAO since bills should be immutable
        throw new UnsupportedOperationException("Bills cannot be deleted.");
    }

}

