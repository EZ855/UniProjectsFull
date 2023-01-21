package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.passwords;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface passwordsRepository extends JpaRepository<passwords, Integer> {
    public List<passwords> findByUser(int user);
}
