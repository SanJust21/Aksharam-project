package com.example.MuseumTicketing.Guide.mainHeading.mainEng;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainTitleEngRepo extends JpaRepository<MainTitleEng,Integer> {

    MainTitleEng findBymEngUid(String engUId);


    void deleteBymEngUid(String mEngUid);

    Optional<MainTitleEng> findBytitle(String title);



    // Optional<MainTitleEng> findByMEngUid(String engId);

//    Optional<MainTitleEng> findBymEngUid(String mainId);
}
