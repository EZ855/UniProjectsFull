package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.recipe_methods;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recipe_methodsRepository extends JpaRepository<recipe_methods, Integer> {
    public List<recipe_methods> findByRecipe(int rid);
    public List<recipe_methods> findByMethodContainingIgnoreCase(String method);
    public Long deleteByRecipe(int rid);
}
