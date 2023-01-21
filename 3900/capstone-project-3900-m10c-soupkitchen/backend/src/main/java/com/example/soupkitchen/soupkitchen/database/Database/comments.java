package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int commenter;
    private int recipe;
    private String comment;

    public comments(){}

    public comments(int id, int commenter, int recipe, String comment) {
        this.id = id;
        this.commenter = commenter;
        this.recipe = recipe;
        this.comment = comment;
    }

    public comments(int commenter, int recipe, String comment) {
        this.commenter = commenter;
        this.recipe = recipe;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommenter() {
        return commenter;
    }

    public void setCommenter(int commenter) {
        this.commenter = commenter;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
