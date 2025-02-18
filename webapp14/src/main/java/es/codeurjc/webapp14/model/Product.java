package es.codeurjc.webapp14.model;


import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer stock;
    private boolean outOfStock;
    //private List<Review> reviews;


    @Lob // Guarda la imagen como BLOB
    private byte[] image;

    public Product() {

    }

    public Product(String name, String description, double price, byte[] image, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.outOfStock = stock > 0;
        //this.reviews = reviews;
    }

    // Getters y Setters
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

    /*public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }*/
}
