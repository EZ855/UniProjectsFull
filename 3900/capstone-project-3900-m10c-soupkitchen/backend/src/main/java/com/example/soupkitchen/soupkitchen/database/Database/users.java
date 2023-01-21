package com.example.soupkitchen.soupkitchen.database.Database;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String email;
    private String profile_base64;
    private Date registration_date;

    public users() {}

    public users(int id, String username, String email, Date registration_date){
        this.id = id;
        this.username = username;
        this.email = email;
        this.registration_date = registration_date;
    }

    public users(String username, String email, Date registration_date){
        this.username = username;
        this.email = email;
        this.registration_date = registration_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProfile_base64() {
        return profile_base64;
    }

    public void setProfile_base64(String profile_base64) {
        this.profile_base64 = profile_base64;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }
}


