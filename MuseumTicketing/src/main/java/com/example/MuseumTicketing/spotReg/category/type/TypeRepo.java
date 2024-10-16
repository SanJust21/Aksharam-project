package com.example.MuseumTicketing.spotReg.category.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TypeRepo extends JpaRepository<TypeData,Integer> {

    List<TypeData> findByCategoryId(Integer id);
}
