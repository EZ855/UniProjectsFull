package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.tags;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface tagsRepository extends JpaRepository<tags, Integer> {
    public boolean existsByName(String name);
    public List<tags> findByName(String name);
    public List<tags> findById(int id);
    public List<tags> findByNameContainingIgnoreCase(String name);
}
