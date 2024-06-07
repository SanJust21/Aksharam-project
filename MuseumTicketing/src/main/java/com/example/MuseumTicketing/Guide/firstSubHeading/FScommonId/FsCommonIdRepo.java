package com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FsCommonIdRepo extends JpaRepository<CommonIdFs,Integer> {
    CommonIdFs findByfsEngId(String englishUId);

    CommonIdFs findByfsMalId(String malUid);

    CommonIdFs findByfsCommonId(String commonId);


    int deleteAllByfsCommonId(String commonId);
}