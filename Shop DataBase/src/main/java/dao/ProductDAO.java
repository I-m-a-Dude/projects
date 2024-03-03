package dao;

import connection.ConnectionFactory;
import model.Product;

import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The ProductDAO class provides data access methods for interacting with the "product" table in the database,
 * which represents products available for sale.
 */
public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Constructs a new ProductDAO object.
     */
    public ProductDAO() {
        super(Product.class);
    }

    /**
     * Creates a new product in the database.
     *
     * @param product The product object to create.
     */
    @Override
    public void create(Product product) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO product (name, price, stock) VALUES (?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while creating product: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Finds a product in the database by its ID.
     *
     * @param id The ID of the product to find.
     * @return The product object with the specified ID, or null if not found.
     */
    @Override
    public Product findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM product WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding product by id: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Extracts a product object from the current row of the result set.
     *
     * @param resultSet The result set from which to extract the product.
     * @return The extracted product object.
     * @throws SQLException If an error occurs while extracting the product from the result set.
     */
    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        Double price = resultSet.getDouble("price");
        Integer stock = resultSet.getInt("stock");

        return new Product(id, name, price, stock);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The product object to update.
     */
    @Override
    public void update(Product product) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE product SET name = ?, price = ?, stock = ? WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getStock());
            statement.setInt(4, product.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating product: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM product WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while deleting product: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all product objects.
     */
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM product";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = extractProductFromResultSet(resultSet);
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding all products: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return products;
    }

    /**
     * Retrieves the names of all products from the database.
     *
     * @return A list of all product names.
     */
    public List<String> findAllProductNames() {
        List<String> productNames = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT name FROM product";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                productNames.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Error while finding all product names: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return productNames;
    }

    /**
     * Finds a product in the database by its name.
     *
     * @param name The name of the product to find.
     * @return The product object with the specified name, or null if not found.
     */
    public Product findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM product WHERE name = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, name);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error while finding product by name: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

/**
 * Finds a product in the database by its ID.
 *
 * @param id The ID of the product to find.
 * @return The product object
*/
public Product findProductById(int id) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
        connection = ConnectionFactory.getConnection();
        statement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock")
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
