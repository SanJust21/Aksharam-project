package com.example.MuseumTicketing.spotReg.category.discount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCountRepo extends JpaRepository<DiscountCount,Integer> {
    Optional<DiscountCount> findByCategoryIdAndTypeId(Integer category, Integer studentTypeId);
}
