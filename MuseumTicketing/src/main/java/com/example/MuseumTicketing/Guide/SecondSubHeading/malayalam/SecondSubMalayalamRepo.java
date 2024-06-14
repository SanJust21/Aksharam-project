package com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondSubMalayalamRepo extends JpaRepository<SecondSubMalayalam,Integer> {
    Optional<SecondSubMalayalam> findByssUid(String malUid);

    List<SecondSubMalayalam> findByFsUidIn(List<String> collect);

    Optional<SecondSubMalayalam> findByFsUid(String malUid);

    void deleteByFsUid(String fsUid);

    List<SecondSubMalayalam> findAllByFsUid(String fsUid);

    List<SecondSubMalayalam> findAllByOrderByIdAsc();

    List<SecondSubMalayalam> findByfsUid(String fsUid);

    void deleteAllByssUid(String malId);

    List<SecondSubMalayalam> findBySsUid(String ssMalId);

    Optional<SecondSubMalayalam> findBytitleIgnoreCase(String title);
}
