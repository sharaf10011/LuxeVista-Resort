package com.luxevista.resort;

public class Booking {
    private int id;
    private String type;
    private String title;
    private String date;
    private String status;
    private double price;

    // Constructors
    public Booking() {}

    public Booking(String type, String title, String date, String status, double price) {
        this.type = type;
        this.title = title;
        this.date = date;
        this.status = status;
        this.price = price;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}