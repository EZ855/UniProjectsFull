package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class cookbooks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int creator;

    public cookbooks() {}

    public cookbooks(int id, String name, int creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }

    public cookbooks(String name, int creator){
        this.name = name;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreator() {
        return creator;
    }

    public void setcreator(int creator) {
        this.creator = creator;
    }
}
