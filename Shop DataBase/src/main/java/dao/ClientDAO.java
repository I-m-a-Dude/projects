package dao;

import connection.ConnectionFactory;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The ClientDAO class provides data access methods for interacting with the "client" table in the database.
 */
public class ClientDAO extends AbstractDAO<Client> {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * Constructs a new ClientDAO object.
     */
    public ClientDAO() {
        super(Client.class);
    }

    /**
     * Generates a unique ID for a new client.
     *
     * @return The generated unique ID.
     */
    private int generateUniqueId() {
        int highestId = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT MAX(id) as max_id FROM client";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                highestId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            System.err.println("Error while generating unique ID: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        if (highestId >= atomicInteger.get()) {
            atomicInteger.set(highestId);
        }

        return atomicInteger.incrementAndGet();
    }

    /**
     * Creates a new client in the database.
     *
     * @param client The client object to create.
     */
    @Override
    public void create(Client client) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO client (id, name, address) VALUES (?, ?, ?)";

        int id = generateUniqueId();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            statement.setString(2, client.getName());
            statement.setString(3, client.getAddress());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while creating client: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client The client object to update.
     */
    @Override
    public void update(Client client) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE client SET name = ?, address = ? WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setInt(3, client.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating client: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Deletes a client from the database by its ID.
     *
     * @param id The ID of the client to delete.
     */
    @Override
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM client WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while deleting client: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Finds a client in the database by its ID.
     *
     * @param id The ID of the client to find.
     * @return The client object with the specified ID, or null if not found.
     */
    @Override
    public Client findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM client WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractClientFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding client by id: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Finds all clients in the database.
     *
     * @return A list of all client objects.
     */
    @Override
    public List<Client> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM client";
        List<Client> clients = new ArrayList<>();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Client client = extractClientFromResultSet(resultSet);
                clients.add(client);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding all clients: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return clients;
    }

    /**
     * Extracts a client object from the current row of the result set.
     *
     * @param resultSet The result set from which to extract the client.
     * @return The extracted client object.
     * @throws SQLException If an error occurs while extracting the client from the result set.
     */
    private Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");

        return new Client(id, name, address);
    }

    /**
     * Finds a client in the database by its ID.
     *
     * @param id The ID of the client to find.
     * @return The client object with the specified ID, or null if not found.
     */
    public Client findClientById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}

