package com.samsung.finalprojectsamsungtt;

public class Order {
    private long id;
    private String name;
    private String dateAndTime;
    private int place;

    public Order(long id, String name, String dateAndTime, int place) {
        this.id = id;
        this.name = name;
        this.dateAndTime = dateAndTime;
        this.place = place;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public int getPlace() {
        return place;
    }
}
