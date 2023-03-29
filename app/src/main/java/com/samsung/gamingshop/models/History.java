package com.samsung.gamingshop.models;

import java.io.Serializable;

public class History implements Serializable {
    private final long owner;
    private final float price;
    private final String address;
    private final String orders;

    public History(long owner, float price, String address, String orders) {
        this.owner = owner;
        this.price = price;
        this.address = address;
        this.orders = orders;
    }

    public long getOwner() {
        return owner;
    }

    public float getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getOrders() {
        return orders;
    }
}
