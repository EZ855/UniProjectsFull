package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usersRepository extends JpaRepository<users, Integer> {
    public List<users> findByEmail(String email);
    public List<users> findById(int id);
    public List<users> findByUsername(String username);
    public List<users> findByUsernameContainingIgnoreCase(String word);

}
