package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class cookbook_subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int cookbook;
    private int follower;

    public cookbook_subscribers() {}

    public cookbook_subscribers(int cookbook, int follower) {
        this.cookbook = cookbook;
        this.follower = follower;
    }

    public void setCookbook(int cookbook) {
        this.cookbook = cookbook;
    }

    public int getCookbook() {
        return cookbook;
    }

    public void setFollower(int follower) {
        this.cookbook = follower;
    }

    public int getFollower() {
        return follower;
    }
}
