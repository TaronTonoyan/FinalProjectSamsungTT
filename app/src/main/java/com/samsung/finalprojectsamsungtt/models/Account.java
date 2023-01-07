package com.samsung.finalprojectsamsungtt.models;

import java.io.Serializable;

public class Account implements Serializable {
    private final long id;
    private final String email;
    private String password;
    private int isAdmin;
    private String address;

    public Account(long id, String email, String password, int isAdmin, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public String getAddress() {
        return address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIsAdmin(boolean isAdmin) {
        if (isAdmin) {
            this.isAdmin = 1;
        } else {
            this.isAdmin = 0;
        }
    }

}
