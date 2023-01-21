package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.recipes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recipesRepository extends JpaRepository<recipes, Integer> {
    public List<recipes> findById(int rid);
    public List<recipes> findByAuthor(int uid);
    public List<recipes> findByNameContainingIgnoreCase(String name);
    public List<recipes> findByMealType(String word);
    // public List<recipes> findByMealTypeAndTag(String mealType, int tid);
    // public List<recipes> findByIdAndMealType(int id, String mealType);
    public Long deleteById(int rid);
}
