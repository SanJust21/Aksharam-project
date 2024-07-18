package com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FsCommonIdRepo extends JpaRepository<CommonIdFs,Integer> {
    CommonIdFs findByfsEngId(String englishUId);

    CommonIdFs findByfsMalId(String malUid);

//    CommonIdFs findByfsCommonId(String commonId);


    int deleteAllByfsCommonId(String commonId);

    Optional<CommonIdFs> findByFsEngIdAndFsMalId(String englishUId, String malUId);


    Optional<CommonIdFs> findByFsCommonId(String commonId);

   // Optional<CommonIdFs> findByCommonId(String commonId);

    Optional<CommonIdFs> findByfsCommonId(String commonId);
}