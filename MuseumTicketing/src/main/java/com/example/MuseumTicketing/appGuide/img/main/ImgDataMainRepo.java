package com.example.MuseumTicketing.appGuide.img.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImgDataMainRepo extends JpaRepository<ImgDataMain,Integer> {


    List<ImgDataMain> findByEngId(String mEngUid);

    List<ImgDataMain> findByMalId(String mMalUid);

    

    Optional<ImgDataMain> findByIdAndCommonId(Integer imgId, String commonId);


    List<ImgDataMain> findByCommonId(String commonId);


    Optional<ImgDataMain> findByCommonIdAndId(String commonId, Integer id);
}
