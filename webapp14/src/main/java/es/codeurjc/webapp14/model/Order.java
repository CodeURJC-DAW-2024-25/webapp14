package es.codeurjc.webapp14.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // Because "order" is a reserved word in SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isPaid;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public enum State {
        Pagado, No_pagado, Enviado, Procesado
    }

    @Enumerated(EnumType.STRING)
    private State state;

    @Transient // Esto indica que este campo no se guarda en la base de datos
    private String createdAtFormatted;

    public Order() {

    }

    public Order(User user, State state, Boolean isPaid) {
        this.user = user;
        this.state = state;
        this.isPaid = isPaid;
    }

    // Getters and Setters
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

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
        calculateTotalPrice();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = BigDecimal.valueOf(totalPrice);
    }

    public void calculateTotalPrice() {
        this.totalPrice = orderProducts.stream()
                .map(op -> BigDecimal.valueOf(op.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }

    public void setCreatedAtFormatted(String createdAtFormatted) {
        this.createdAtFormatted = createdAtFormatted;
    }
}
