package com.example.streepjesblad;

import android.service.autofill.AutofillService;

public class User {

    private String username, email, isAdmin;
    private long amount;

    private User(){

    }

    private User(String username, long amount){
        this.username = username;
        this.amount = amount;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
