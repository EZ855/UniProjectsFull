package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.cookbook_subscribers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface cookbook_subscribersRepository extends JpaRepository<cookbook_subscribers, Integer> {
    public List<cookbook_subscribers> findByCookbook(int cid);
    public List<cookbook_subscribers> findByFollower(int fid);
    public List<cookbook_subscribers> findByCookbookAndFollower(int cid, int fid);
    public Long deleteByCookbook(int cid);
    public Long deleteByCookbookAndFollower(int cid, int fid);
}
