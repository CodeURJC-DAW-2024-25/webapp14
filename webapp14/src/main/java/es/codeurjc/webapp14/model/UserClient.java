package es.codeurjc.webapp14.model;

public class UserClient {
    private int id;
    private String name;
    private String email;
    private String address;
    private int orders;
    private String profileImage;

    // Constructor
    public UserClient(int id, String name, String email, String address, int orders, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.orders = orders;
        this.profileImage = profileImage;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getOrders() { return orders; }
    public void setOrders(int orders) { this.orders = orders; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
