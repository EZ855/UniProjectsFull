package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class passwords {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int user;
    private String password_hashed;

    public passwords() {}

    public passwords(int id, int user, String password_hashed){
        this.id = id;
        this.user = user;
        this.password_hashed = password_hashed;
    }

    public passwords(int user, String password_hashed){
        this.user = user;
        this.password_hashed = password_hashed;
    }


    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getPassword_hashed() {
        return password_hashed;
    }

    public void setPassword_hashed(String password_hashed) {
        this.password_hashed = password_hashed;
    }
}