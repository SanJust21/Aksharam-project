package com.example.MuseumTicketing.Guide.firstSubHeading.malayalam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FirstSubMalayalamRepo extends JpaRepository<FirstSubMalayalam,Integer> {
    //Optional<FirstSubMalayalam> findBymMalUid(String uId);

    Optional<FirstSubMalayalam> findByFsUid(String uId);

    Optional<FirstSubMalayalam> findByfsUid(String malUid);

    List<FirstSubMalayalam> findByMainUid(String mainId);

    void deleteByMainUid(String mMalUid);

    List<FirstSubMalayalam> findAllByMainUid(String mMalUid);

    List<FirstSubMalayalam> findAllByOrderByIdAsc();

    void deleteAllByfsUid(String fsMalId);

    Optional<FirstSubMalayalam> findBytitleIgnoreCase(String title);
}
