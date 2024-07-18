package com.example.MuseumTicketing.appGuide.img.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgDataFirstRepo extends JpaRepository<ImgDataFirst,Integer> {
    List<ImgDataFirst> findByEngId(String fsUid);

    List<ImgDataFirst> findByMalId(String fsUid);
}
