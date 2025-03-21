package com.example.thrift_app_store;

import android.net.Uri;

import java.util.ArrayList;

public class Item {
    private String title;
    private String description;
    private double price;
    private String condition;
    private String category;


    //store multiple image
    private ArrayList<Uri> imageUrls;


    // Default constructor required for Firebase
    public Item() {
    }

    public Item(String title, String description, double price, String condition, String category, ArrayList<Uri> imageUrls) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.imageUrls = imageUrls;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
//
//    public ArrayList<String> getImageUrls() {
//        return imageUrls;
//    }

    public void setImageUrls(ArrayList<Uri> imageUrls) {
        this.imageUrls = imageUrls;
    }
}