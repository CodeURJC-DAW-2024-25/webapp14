package es.codeurjc.webapp14.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private boolean outOfStock;
    private int sold;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private String category;

    public enum CategoryType {
        PANTALONES, CAMISETAS, ABRIGOS, JERSEYS
    }

    @JsonIgnore
    @Lob
    // Conflicts
    // private byte[] image;
    private Blob image;
    private boolean imageBool;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Size> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;


    public Product() {

    }

    public Product(String name, String description, double price, Blob image, int stock, CategoryType category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.imageBool = true;
        this.stock = stock;
        this.outOfStock = stock == 0;
        this.category = category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase();
        this.sold = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        BigDecimal bd = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.outOfStock = (stock == 0);
    }

    public int getStock() {
        return this.stock;
    }

    public boolean isOutOfStock() {
        return this.outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public void setSold(int sold) {
        this.sold += sold;
    }

    public int getSold() {
        return this.sold;
    }

    public void incrementSold(int quantity) {
        this.sold += quantity;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getTwoReviews(int from, int to) {
        if (reviews.isEmpty()) {
            return List.of();
        }

        int validFrom = Math.max(0, from);
        int validTo = Math.min(reviews.size(), to);

        if (validFrom >= validTo) {
            return List.of();
        }

        return reviews.subList(validFrom, validTo);
    }

    public boolean getImageBool() {
        return this.imageBool;
    }

    public void setImageBool(boolean imageBool) {
        this.imageBool = imageBool;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }
}
