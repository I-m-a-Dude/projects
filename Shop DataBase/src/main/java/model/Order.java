package model;
/**
 * The Order class represents an order in the system.
 */
public class Order {
    private Integer id;
    private Integer clientId;
    private Integer productId;
    private Integer quantity;
    private Double totalprice;


    public Order(Integer id, Integer clientId, Integer productId, Integer quantity, Double totalprice) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalprice = totalprice;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalprice=" + totalprice +

                '}';
    }
}
