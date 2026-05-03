package com.luxevista.resort;

public class Service {
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private String category;

    public Service(String name, String description, double price, int imageResource, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
