package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class user_followers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int user;
    private int follower;

    public user_followers() {}
    
    public user_followers(int id, int user, int follower){
        this.id = id;
        this.user = user;
        this.follower = follower;
    }

    public user_followers(int user, int follower){
        this.user = user;
        this.follower = follower;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
