package com.example.sulochanasalon;

public class Service
{
    private int id;
    private String name;
    private double price;
    private String description;
    private int parentId;

    public Service(int id, String name, double price, String description, int parentId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.parentId = parentId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getParentId() {
        return parentId;
    }

    // Setters (optional)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}

