package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.recipe_tags;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recipe_tagsRepository extends JpaRepository<recipe_tags, Integer> {
    public List<recipe_tags> findByRecipe(int rid);
    public List<recipe_tags> findByTag(int tid);
    public Long deleteByRecipe(int rid);
}
