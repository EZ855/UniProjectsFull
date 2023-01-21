package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class cookbook_recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private  int cookbook_index;
    private int recipe;
    private int cookbook;

    public cookbook_recipes() {}

    public cookbook_recipes(int id, int cookbook_index, int recipe, int cookbook){
        this.id = id;
        this.cookbook_index = cookbook_index;
        this.recipe = recipe;
        this.cookbook = cookbook;
    }

    public cookbook_recipes(int cookbook_index, int recipe, int cookbook){
        this.cookbook_index = cookbook_index;
        this.recipe = recipe;
        this.cookbook = cookbook;
    }

    public int getCookbook_index() {
        return cookbook_index;
    }

    public void setCookbook_index(int cookbook_index) {
        this.cookbook_index = cookbook_index;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public int getCookbook() {
        return cookbook;
    }

    public void setCookbook(int cookbook) {
        this.cookbook = cookbook;
    }
}
