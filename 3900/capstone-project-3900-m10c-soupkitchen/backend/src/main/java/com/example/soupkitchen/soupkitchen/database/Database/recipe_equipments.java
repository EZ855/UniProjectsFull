package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class recipe_equipments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int recipe;
    private int equipment;

    public recipe_equipments(){}

    public recipe_equipments(int id, int recipe, int equipment){
        this.id = id;
        this.recipe = recipe;
        this.equipment = equipment;
    }

    public recipe_equipments(int recipe, int equipment){
        this.recipe = recipe;
        this.equipment = equipment;
    }
    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public int getEquipment() {
        return equipment;
    }

    public void setEquipment(int equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ",\nrecipe_id="+ this.recipe +
                ",\nequipment_id='" + this.equipment + '}';
    }
}
