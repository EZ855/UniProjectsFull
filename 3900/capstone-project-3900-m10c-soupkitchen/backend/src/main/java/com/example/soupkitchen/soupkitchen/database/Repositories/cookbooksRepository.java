package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.cookbooks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cookbooksRepository extends JpaRepository<cookbooks, Integer> {
    public List<cookbooks> findById(int cid);
    public List<cookbooks> findByCreator(int uid);
    public Long deleteById(int cid);
}
