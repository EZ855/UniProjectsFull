package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.recipe_ingredients;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recipe_ingredientsRepository extends JpaRepository<recipe_ingredients, Integer> {
    public List<recipe_ingredients> findByRecipe(int rid);
    public List<recipe_ingredients> findByIngredient(int ingredient);
    public Long deleteByRecipe(int rid);
}
