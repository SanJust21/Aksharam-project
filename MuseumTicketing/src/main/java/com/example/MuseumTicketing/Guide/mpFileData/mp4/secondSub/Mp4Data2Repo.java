package com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Mp4Data2Repo extends JpaRepository<Mp4Data2,Integer> {
    List<Mp4Data2> findBydtId(String ssUid);

    void deleteByDtId(String ssUid);

    void deleteAllBydtId(String sMalId);
}
