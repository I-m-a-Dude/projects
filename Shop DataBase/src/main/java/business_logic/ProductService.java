package business_logic;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public void createProduct(Product product) {
        productDAO.create(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    public Product findProductById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }
}
