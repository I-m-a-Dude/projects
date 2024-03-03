package presentation.product;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The ProductController class handles product-related operations and interacts with the ProductGUI and ProductDAO.
 */
public class ProductController {
    private final ProductGUI view;
    private final ProductDAO dao;

    /**
     * Constructs a ProductController object.
     *
     * @param view The product view.
     * @param dao  The product data access object.
     */
    public ProductController(ProductGUI view, ProductDAO dao) {
        this.view = view;
        this.dao = dao;
    }

    /**
     * Retrieves the names of all products.
     *
     * @return The list of product names.
     */
    public List<String> getAllProductNames() {
        List<Product> products = dao.findAll();
        return products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the price of a product based on its name.
     *
     * @param productName The name of the product.
     * @return The price of the product.
     */
    public double getProductPrice(String productName) {
        Product product = dao.findByName(productName);
        return product.getPrice();
    }


    /**
     * Retrieves a product by its name.
     *
     * @param name The name of the product.
     * @return The product object.
     */
    public Product getProductByName(String name) {
        return dao.findByName(name);
    }

    /**
     * Creates a new product with the specified name, price, and stock.
     *
     * @param name  The name of the product.
     * @param price The price of the product.
     * @param stock The stock of the product.
     */
    public void createProduct(String name, Double price, Integer stock) {
        Product product = new Product(null, name, price, stock);
        dao.create(product);
        view.updateProduct(dao.findAll()); // update view with new product list
    }

    /**
     * Updates the product with the specified ID with the new name, price, and stock.
     *
     * @param id    The ID of the product to update.
     * @param name  The new name of the product.
     * @param price The new price of the product.
     * @param stock The new stock of the product.
     */
    public void updateProduct(Integer id, String name, Double price, Integer stock) {
        Product product = new Product(id, name, price, stock);
        dao.update(product);
        view.updateProduct(dao.findAll()); // update view with updated product list
    }

    /**
     * Deletes the product with the specified ID.
     *
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(Integer id) {
        dao.delete(id);
        view.updateProduct(dao.findAll()); // update view with updated product list
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product object if found, null otherwise.
     */
    public Product getProductById(int id) {
        return dao.findProductById(id);
    }

    /**
     * Retrieves the list of all products.
     *
     * @return The list of all products.
     */
    public List<Product> readProduct() {
        return dao.findAll(); // return current client list
    }
}
