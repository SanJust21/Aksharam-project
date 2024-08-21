package com.example.MuseumTicketing.appGuide.img.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImgDataFirstRepo extends JpaRepository<ImgDataFirst,Integer> {
    List<ImgDataFirst> findByEngId(String fsUid);

    List<ImgDataFirst> findByMalId(String fsUid);

    List<ImgDataFirst> findByCommonId(String commonId);

    Optional<ImgDataFirst> findByIdAndCommonId(Integer imgId, String commonId);


    Optional<ImgDataFirst> findByCommonIdAndId(String commonId, Integer id);
}
