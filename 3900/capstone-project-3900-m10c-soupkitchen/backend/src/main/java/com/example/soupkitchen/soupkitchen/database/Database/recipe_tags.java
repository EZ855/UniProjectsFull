package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class recipe_tags {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int recipe;
    private int tag;

    public recipe_tags(){}
    
    public recipe_tags(int id, int recipe, int tag){
        this.id = id;
        this.recipe = recipe;
        this.tag = tag;

    }
    public recipe_tags( int recipe, int tag){
        this.recipe = recipe;
        this.tag = tag;

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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Recipe tags{" +
                "id=" + id +
                ", recipe_id="+ this.recipe +
                ", tag_id=" + this.tag + '}';
    }
}
