package com.example.MuseumTicketing.Guide.img.backgroundImg;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BackgroundImgRepo extends JpaRepository<BackgroundImg,Integer> {

    List<BackgroundImg> findBymalId(String mMalUid);

    List<BackgroundImg> findByengId(String mEngUid);

    Optional<BackgroundImg> findByEngIdAndMalId(String engId, String malId);

    Optional<BackgroundImg> findByCommonIdAndId(String commonId, Integer imgId);
}
