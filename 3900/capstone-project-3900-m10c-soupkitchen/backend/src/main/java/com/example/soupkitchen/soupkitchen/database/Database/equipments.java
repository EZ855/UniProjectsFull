package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class equipments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    public equipments(){}

    public equipments(int id, String name){
        this.id = id;
        this.name = name;
    }

    public equipments(String name){
        this.name = name;

    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", equipment_name='" + this.name + '}';
    }
}
