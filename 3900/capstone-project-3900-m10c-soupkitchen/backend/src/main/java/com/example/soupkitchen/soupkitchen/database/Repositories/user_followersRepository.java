package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.user_followers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface user_followersRepository extends JpaRepository<user_followers, Integer> {
    public Long deleteByFollowerAndUser(int fid, int uid);
    public List<user_followers> findByFollower(int fid);
    public List<user_followers> findByUser(int uid);
}
