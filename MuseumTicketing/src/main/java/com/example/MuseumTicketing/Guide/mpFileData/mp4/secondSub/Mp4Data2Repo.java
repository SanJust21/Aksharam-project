package com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Mp4Data2Repo extends JpaRepository<Mp4Data2,Integer> {
    List<Mp4Data2> findBydtId(String ssUid);

    void deleteByDtId(String ssUid);

    void deleteAllBydtId(String sMalId);

    List<Mp4Data2> findByengId(String ssUid);

    List<Mp4Data2> findBymalId(String ssUid);

    Optional<Mp4Data2> findByDtIdAndId(String dtId, Integer id);
}
