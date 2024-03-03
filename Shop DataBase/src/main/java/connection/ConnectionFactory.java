package connection;

import java.sql.*;

/**
 * The ConnectionFactory class provides methods to establish and close database connections.
 */
public class ConnectionFactory {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/tp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private ConnectionFactory() {
        // Prevent instantiation
    }

    /**
     * Returns a database connection.
     *
     * @return The Connection object representing the database connection, or null if an error occurs.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the provided PreparedStatement.
     *
     * @param statement The PreparedStatement to close.
     */
    public static void close(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the provided ResultSet.
     *
     * @param resultSet The ResultSet to close.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the provided Connection.
     *
     * @param connection The Connection to close.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
