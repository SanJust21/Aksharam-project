package com.example.MuseumTicketing.Guide.firstSubHeading.english;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FirstSubEnglishRepo extends JpaRepository<FirstSubEnglish,Integer> {
    //FirstSubEnglish findBymEngUid(String uId);

    FirstSubEnglish findByFsUid(String uId);

    List<FirstSubEnglish> findByMainUid(String mainId);

    Optional<FirstSubEnglish> findByfsUid(String englishUId);

    void deleteByMainUid(String mEngUid);

    List<FirstSubEnglish> findAllByMainUid(String mEngUid);

    List<FirstSubEnglish> findAllByOrderByIdAsc();
    void deleteAllByfsUid(String fsEngId);

    Optional<FirstSubEnglish> findBytitleIgnoreCase(String title);
}
