package com.example.MuseumTicketing.spotReg.category.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<CategoryData,Integer> {
    Optional<CategoryData> findByCategory(String name);
}
