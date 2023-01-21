package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.cookbook_recipes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cookbook_recipesRepository extends JpaRepository<cookbook_recipes, Integer> {
    public List<cookbook_recipes> findByCookbook(int cid);
    public Long deleteByCookbook(int cid);
    public Long deleteByRecipe(int rid);
}
