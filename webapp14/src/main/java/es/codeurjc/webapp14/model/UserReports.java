package es.codeurjc.webapp14.model;

import java.util.ArrayList;
import java.util.List;

public class UserReports {

    private String username;
    private List<Review> reviews;
    private int reviewCount;

    // Constructor
    public UserReports(String username) {
        this.username = username;
        this.reviews = new ArrayList<>();
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
