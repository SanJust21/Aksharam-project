package com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Mp3Data2Repo extends JpaRepository<Mp3Data2,Integer> {
    List<Mp3Data2> findBydtId(String ssUid);

    void deleteByDtId(String ssUid);

    void deleteAllBydtId(String sEngId);

    Optional<Mp3Data2> findByDtIdAndId(String dtId, Integer id);
}
