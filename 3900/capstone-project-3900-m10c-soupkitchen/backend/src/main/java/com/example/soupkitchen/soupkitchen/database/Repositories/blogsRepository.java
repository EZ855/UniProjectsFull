package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.Blog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface blogsRepository extends JpaRepository<Blog, Integer> {
    public List<Blog> findByTitleContainingOrContentContaining(String searchTerm, String searchTerm1);
}
