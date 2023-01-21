package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.recipe_equipments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recipe_equipmentsRepository extends JpaRepository<recipe_equipments, Integer> {
    public List<recipe_equipments> findByRecipe(int recipe_id);
    public List<recipe_equipments> findByEquipment(int recipe_id);
    public Long deleteByRecipe(int rid);
}
