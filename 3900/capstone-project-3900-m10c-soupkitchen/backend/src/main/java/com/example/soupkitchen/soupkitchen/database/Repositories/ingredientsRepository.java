package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.ingredients;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ingredientsRepository extends JpaRepository<ingredients, Integer> {
    public List<ingredients> findByName(String name);
    public List<ingredients> findByNameContainingIgnoreCase(String name);
    public List<ingredients> findById(int id);
}
