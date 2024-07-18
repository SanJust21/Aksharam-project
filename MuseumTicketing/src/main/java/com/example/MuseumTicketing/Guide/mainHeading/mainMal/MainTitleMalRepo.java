package com.example.MuseumTicketing.Guide.mainHeading.mainMal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainTitleMalRepo extends JpaRepository<MainTitleMal,Integer> {
    Optional<MainTitleMal> findBymMalUid(String uId);

    void deleteBymMalUid(String mMalUid);

    Optional<MainTitleMal> findBytitle(String title);


}
