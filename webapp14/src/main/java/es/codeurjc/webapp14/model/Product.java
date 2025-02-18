package es.codeurjc.webapp14.model;



import java.util.ArrayList;
import java.util.List;

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
    //private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    private String category;

    public enum CategoryType {
        PANTALONES, CAMISETAS, ABRIGOS, JERSEYS
    }

    @Lob // Guarda la imagen como BLOB
    private byte[] image;

    public Product() {

    }

    public Product(String name, String description, double price, byte[] image, int stock, CategoryType category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.outOfStock = stock == 0;
        this.category = category.name();
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
        return price;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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

    /*public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }*/
}
