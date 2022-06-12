package com.kmab.chickeninn;

public class SetterMeals {

    public SetterMeals() {
    }

    private String type;
    private int id;
    private String name;
    private int imageResource;
    private double price;

    public SetterMeals(String type, int id, String name, int imageResource, double price) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
