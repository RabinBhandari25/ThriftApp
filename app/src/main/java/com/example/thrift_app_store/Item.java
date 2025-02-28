package com.example.thrift_app_store;

/**
 * This class is for the items sold in the app
 * */
public class Item {
    private String item_name;
    private Double price;
    private String Description;

    public Item(String item_name, Double price, String description) {
        this.item_name = item_name;
        this.price = price;
        Description = description;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
