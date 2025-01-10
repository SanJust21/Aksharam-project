package com.example.MuseumTicketing.spotReg.category.additionCharge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionChargeRepo extends JpaRepository<AdditionCharge,Integer> {
    Optional<AdditionCharge> findByAddChargeName(String name);
}
