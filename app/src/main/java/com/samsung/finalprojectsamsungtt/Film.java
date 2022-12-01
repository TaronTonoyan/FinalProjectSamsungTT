package com.samsung.finalprojectsamsungtt;

import java.util.ArrayList;

public class Film {
    private long id;
    private String name;
    private int size;
    private double price;
    private String category;
    private String genre;
    private String description;
    private String dateAndTime;

    public Film(long id, String name, int size, double price, String category, String genre, String description, String dateAndTime) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
        this.genre = genre;
        this.description = description;
        this.dateAndTime = dateAndTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}
