package es.codeurjc.webapp14.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "order" es palabra reservada en SQL
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    public enum State {
        CART, PAYED
    }

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Order() {

    }

    public Order(User user, State state) {
        this.user = user;
        this.state = state;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void calculateTotalPrice() {
        this.totalPrice = orderProducts.stream()
                .map(op -> BigDecimal.valueOf(op.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
