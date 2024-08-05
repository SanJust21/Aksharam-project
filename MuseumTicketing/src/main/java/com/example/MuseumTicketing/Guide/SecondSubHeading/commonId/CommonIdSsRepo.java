package com.example.MuseumTicketing.Guide.SecondSubHeading.commonId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CommonIdSsRepo extends JpaRepository<CommonIdSs,Integer> {
    CommonIdSs findByssCommonId(String id);

    Optional<CommonIdSs> findByssEngId(String ssUid);

    void deleteByssCommonId(String id);

    Optional<CommonIdSs> findByssMalId(String ssUid);

    CommonIdSs findBySsEngId(String ssUid);

    CommonIdSs findBySsMalId(String ssUid);

    Optional<CommonIdSs> findBySsCommonId(String commonId);

    Optional<CommonIdSs> findBySsMalIdAndSsEngId(String ssMalId, String ssEngId);
}