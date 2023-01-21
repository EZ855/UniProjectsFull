package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class auth_tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int user;
    private String token;

    public auth_tokens() {}

    public auth_tokens(int id, int user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public auth_tokens(int user, String token) {
        this.user = user;
        this.token = token;
    }

    public int getUser() {
        return this.user;
    }

    public void setUser(int user) {
        this.user = user;
    }
    
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
