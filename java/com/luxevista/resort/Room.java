package com.luxevista.resort;

public class Room {
    private String name;
    private String description;
    private double price;
    private int imageResource;
    private String type;

    public Room(String name, String description, double price, int imageResource, String type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.type = type;
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
