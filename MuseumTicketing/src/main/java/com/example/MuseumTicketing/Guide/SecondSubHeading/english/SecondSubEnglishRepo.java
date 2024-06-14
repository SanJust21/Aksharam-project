package com.example.MuseumTicketing.Guide.SecondSubHeading.english;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondSubEnglishRepo extends JpaRepository<SecondSubEnglish,Integer> {
    Optional<SecondSubEnglish> findByssUid(String englishUId);


    List<SecondSubEnglish> findByFsUidIn(List<String> collect);

    Optional<SecondSubEnglish> findByFsUid(String englishUId);
    List<SecondSubEnglish> findByfsUid(String englishUId);
    void deleteAllByfsUid(String fsEngId);

    void deleteByFsUid(String fsUid);

    List<SecondSubEnglish> findAllByFsUid(String fsUid);

    List<SecondSubEnglish> findAllByOrderByIdAsc();

    //Optional<SecondSubEnglish> findBytitle(String title);

    void deleteAllByssUid(String englishId);

    List<SecondSubEnglish> findBySsUid(String ssEngId);

    Optional<SecondSubEnglish> findBytitleIgnoreCase(String title);
}
