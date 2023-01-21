package com.example.soupkitchen.soupkitchen.database.Repositories;

import com.example.soupkitchen.soupkitchen.database.Database.equipments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface equipmentsRepository extends JpaRepository<equipments, Integer> {
    public List<equipments> findByName(String name);
    public List<equipments> findById(int id);
    public List<equipments> findByNameContainingIgnoreCase(String name);
}
