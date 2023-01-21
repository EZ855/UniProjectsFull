package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.likes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface likesRepository extends JpaRepository<likes, Integer> {
    public List<likes> findByUser(int user);
    public List<likes> findByUserAndRecipe(int uid, int id);
    public List<likes> findByRecipe(int rid);
    public Long deleteByRecipe(int rid);
}
