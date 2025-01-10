package com.example.MuseumTicketing.spotReg.category.gst;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GSTRepo extends JpaRepository<GSTData,Integer> {
    Optional<GSTData> findByItem(String name);
}
