package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.comments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface commentsRepository extends JpaRepository<comments, Integer> {
    public List<comments> findById(int id);
    public List<comments> findByRecipe(int cid);
    public List<comments> findByCommenterAndRecipe(int uid, int rid);
    public Long deleteById(int cid);
    public Long deleteByRecipe(int rid);
}
