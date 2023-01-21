package com.example.soupkitchen.soupkitchen.database.Database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int author;
    private String mealType;
    // private int is_published;
    private String photo;
    private String name;

    private LocalDateTime time;

    public recipes(){}
    
    public recipes(int id, String name, int is_published, int author, String meal_type, String photo){
        this.id = id;
        //this.is_published = is_published;
        this.time = LocalDateTime.now();
        this.name = name;
        this.author = author;
        this.mealType = meal_type;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    // int is_published
    public recipes(int author, String name, String meal_type, String photo){
        // this.is_published = is_published;
        this.time = LocalDateTime.now();
        this.author = author;
        this.name = name;
        this.mealType = meal_type;
        this.photo = photo;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String meal_type) {
        this.mealType = meal_type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public int isIs_published() {
        return is_published;
    }

    public void setIs_published(int is_published) {
        this.is_published = is_published;
    }
    */

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String toString(){
        return "{ id:" + this.id +
                ",\n\tname: " + this.name +
                ",\n\tauthor_id: " + this.author +
                ",\n\tMeal Type+ " + this.mealType +
                ",\n\tphoto_url : + " + this.photo + "\n}";

    }
}
