package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.auth_tokens;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface auth_tokensRepository extends JpaRepository<auth_tokens, Integer> {
    public boolean existsByUser(int id);
    public boolean existsByToken(String token);
    public List<auth_tokens> findByUser(int uid);
    public List<auth_tokens> findByToken(String token);
    public Long deleteByToken(String token);
    public void deleteByUser(int uid);
}
