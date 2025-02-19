package es.codeurjc.webapp14.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.*;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;


    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    private Double subtotalPrice;

    public OrderProduct() {
    }

    public OrderProduct(Order order, Product product, Size size, int quantity) {
        this.order = order;
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Double getSubtotalPrice() {
        if (product != null) {
            BigDecimal bd = new BigDecimal(product.getPrice() * quantity).setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } else {
            return 0.0;
        }
    }

    public void setSubtotalPrice(Double subtotalPrice) {
        if (product != null) {
            this.subtotalPrice = product.getPrice() * quantity;
        } else {
            this.subtotalPrice = 0.0;
        }
    }
}
