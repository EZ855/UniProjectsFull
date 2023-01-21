package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class recipe_methods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int recipe;
    private String method;

    public recipe_methods(){}

    public recipe_methods(int id, int recipe, String method){
        this.id = id;
        this.recipe = recipe;
        this.method = method;
    }

    public recipe_methods(int recipe, String method){
        this.recipe = recipe;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ",\nrecipe_id="+ this.recipe +
                ",\nmethods=" + this.method + '}';
    }
}
