package com.samsung.gamingshop.models;

public class Order {
    private final long id;
    private final long owner;
    private final long product;
    private final int isWishlist;
    private int quantity;

    public Order(long id, long owner, long product, int isWishlist, int quantity) {
        this.id = id;
        this.owner = owner;
        this.product = product;
        this.isWishlist = isWishlist;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getOwner() {
        return owner;
    }

    public long getProduct() {
        return product;
    }

    public int getIsWishlist() {
        return isWishlist;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
