package es.codeurjc.webapp14.model;

import java.util.List;

import jakarta.validation.constraints.*;

public class Product {

    private int id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double price;

    @NotBlank(message = "Debe seleccionar una categoría")
    private String category;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 1, message = "El stock debe ser al menos 1")
    private Integer stock;

    private boolean outOfStock;

    private List<Review> reviews; // Nueva lista de reseñas


    // Constructor
    public Product(int id, String name, String description, double price, String category, int stock, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.outOfStock = (stock == 0);
        this.reviews = reviews;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.outOfStock = (stock == 0);
    }

    public int getStock() {
        return stock;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getCategory() {
        return category;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
