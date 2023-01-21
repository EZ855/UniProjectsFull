package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class recipe_ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int recipe;
    private int ingredient;
    private int quantity;
    private String units;

    public recipe_ingredients(){}

    public recipe_ingredients(int id, int recipe, int ingredient, int quantity, String units){
        this.id = id;
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.units = units;
    }

    public recipe_ingredients(int recipe, int ingredient, int quantity, String units){
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.units = units;
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

    public int getIngredient() {
        return ingredient;
    }

    public void setIngredient(int ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + id +
                ",\nrecipe_id="+ this.recipe +
                ",\ningredient_id='" + this.ingredient +
                ",\nquantity='" + this.quantity +
                ",\nunits='" + this.units +

                '}';
    }

}
