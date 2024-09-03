package com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Mp3Repo extends JpaRepository<Mp3Data,Integer> {
    List<Mp3Data> findBydtId(String s);

    void deleteByDtId(String mEngUid);

    Optional<Mp3Data> findByDtIdAndId(String dtId, Integer id);
}
