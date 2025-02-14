package es.codeurjc.webapp14.model;


public class Review {
    private String username;
    private String reviewText;
    private int rating;
    private boolean reported; // Nuevo atributo
    private int productId;

    public Review(String username, String reviewText, int rating, boolean reported, int productId) {
        this.username = username;
        this.reviewText = reviewText;
        this.rating = rating;
        this.reported = reported;
        this.productId = productId;

    }

    public String getUsername() {
        return username;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getRating() {
        return rating;
    }

    public boolean isReported() {
        return reported;
    }
}
