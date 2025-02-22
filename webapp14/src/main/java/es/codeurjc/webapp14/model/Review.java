package es.codeurjc.webapp14.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int rating;
    private String reviewText;
    private boolean reported;
    private boolean own;

    @Transient
    private List<Boolean> ratingStars;

    @Transient
    private List<Boolean> emptyStars;


    public Review(){
        
    }

    public Review(int rating, String reviewText, boolean reported, Product product, User user){
        this.rating = rating;
        this.reviewText = reviewText;
        this.reported = reported;
        this.product = product;
        this.user = user;
        if (reported){
            user.setReports(1);
        }
        this.ratingStars = generateStars(rating);
        this.emptyStars = generateEmptyStars(rating);
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
        this.user.setReports(1);
    }
    
    public String getUsername(){
        return user.getName();
    }

    private List<Boolean> generateStars(int rating) {
        List<Boolean> stars = new ArrayList<>();
        for (int i = 0; i < rating; i++) {
            stars.add(true);
        }
        return stars;
    }

    private List<Boolean> generateEmptyStars(int rating) {
        List<Boolean> stars = new ArrayList<>();
        for (int i = rating; i < 5; i++) {
            stars.add(true);
        }
        return stars;
    }

    public void updateStars() {
        this.ratingStars = generateStars(this.rating);
        this.emptyStars = generateStars(5 - this.rating);
    }

    public List<Boolean> getRatingStars() {
        if (this.ratingStars == null || this.ratingStars.isEmpty()) {
            updateStars();
        }
        return ratingStars;
    }

    public List<Boolean> getEmptyStars() {
        if (this.emptyStars == null || this.emptyStars.isEmpty()) {
            updateStars();
        }
        return emptyStars;
    }

    public boolean getOwn() {
        return this.own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }
    

}