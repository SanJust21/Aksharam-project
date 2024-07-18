package com.example.MuseumTicketing.appGuide.img.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgDataMainRepo extends JpaRepository<ImgDataMain,Integer> {
    List<ImgDataMain> findByEngId(String mEngUid);

    List<ImgDataMain> findByMalId(String mMalUid);
}
