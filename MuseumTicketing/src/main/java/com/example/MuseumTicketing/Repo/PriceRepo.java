package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.Price;
import com.example.MuseumTicketing.Model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepo extends JpaRepository<Price, Integer>{

    Price findAllByType(String type);

    List<Price> findAll();

    void deleteByTypeAndCategory(String type, String category);

    Optional<Price> findByTypeAndCategory(String type, String category);

    Optional<Price> findByTypeIgnoreCaseAndCategoryIgnoreCase(String type, String category);
}

