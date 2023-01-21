package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int user;
    private int recipe;
    private Boolean liked;

    public likes(){}

    public likes(int id, int user, int recipe, Boolean liked) {
        this.id = id;
        this.user = user;
        this.recipe = recipe;
        this.liked = liked;
    }

    public likes(int user, int recipe, Boolean liked) {
        this.user = user;
        this.recipe = recipe;
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
