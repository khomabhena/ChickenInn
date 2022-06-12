package com.kmab.chickeninn;

public class SetterOrders {

    public SetterOrders() {
    }

    private String type;
    private int id;
    private String name;
    private double price;
    private long timestamp;
    private String message, orderNumber;

    public SetterOrders(String type, int id, String name, double price, long timestamp, String message, String orderNumber) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.price = price;
        this.timestamp = timestamp;
        this.message = message;
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
