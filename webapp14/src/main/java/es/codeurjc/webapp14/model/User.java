package es.codeurjc.webapp14.model;

import jakarta.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String encodedPassword;

    private String address;

    private boolean banned;

    private int reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Lob
    private Blob profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

    public User() {
    }

    public User(String name, String surname, String email, String encodedPassword, String... roles) {
        super();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.banned = false;
        this.reports = 0;
    }

    public User(String name, String surname, Blob profileImage, String address, String email, String encodedPassword, String... roles) {
        super();
        this.name = name;
        this.surname = surname;
        this.profileImage = profileImage;
        this.address = address;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
        this.banned = false;
        this.reports = 0;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Blob getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Blob profileImage) {
        this.profileImage = profileImage;
    }

    public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

    public boolean getBanned() {
        return this.banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getReports() {
        return this.reports;
    }

    public void setReports(int reports) {
        this.reports += reports;
    }

    public String getEncodedPassword(){
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword){
        this.encodedPassword = encodedPassword;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
