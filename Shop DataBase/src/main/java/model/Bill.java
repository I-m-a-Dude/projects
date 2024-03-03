package model;

/**
 * The Bill class represents a bill record in the system.
 */
public record Bill(Integer id, Integer orderId, String clientName, String productName, Integer quantity,
                   Double totalPrice) {

}
