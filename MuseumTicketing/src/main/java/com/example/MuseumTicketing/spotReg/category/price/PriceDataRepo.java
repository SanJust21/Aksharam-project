package com.example.MuseumTicketing.spotReg.category.price;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceDataRepo extends JpaRepository<PriceData,Integer> {
    Optional<PriceData> findByCategoryIdAndTypeId(Integer category, Integer adultTypeId);

    List<PriceData> findByCategoryId(Integer categoryId);

    Optional<PriceData> findByIdAndTypeId(Integer id, Integer typeId);
}
