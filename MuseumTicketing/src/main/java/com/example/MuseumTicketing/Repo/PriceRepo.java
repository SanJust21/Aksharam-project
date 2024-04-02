package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepo extends JpaRepository<Price, Integer>{

    Price findAllByType(String type);

    List<Price> findAll();
}

