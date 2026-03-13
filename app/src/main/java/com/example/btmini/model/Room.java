package com.example.btmini.model;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private boolean isRented;
    private String tenantName;
    private String phoneNumber;
    private String imageUri;

    public Room(String id, String name, double price, boolean isRented, String tenantName, String phoneNumber, String imageUri) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isRented = isRented;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isRented() { return isRented; }
    public void setRented(boolean rented) { isRented = rented; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
