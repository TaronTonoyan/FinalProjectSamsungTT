package com.samsung.finalprojectsamsungtt;

import java.io.Serializable;

public class Account implements Serializable {
    private long id;
    private String email;
    private String password;
    private int isAdmin;

    public Account(long id, String email, String password, int isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
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

    public void setIsAdmin(boolean isAdmin) {
        if (isAdmin) {
            this.isAdmin = 1;
        } else {
            this.isAdmin = 0;
        }
    }

}
