package com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Mp3Repo extends JpaRepository<Mp3Data,Integer> {
    List<Mp3Data> findBydtId(String s);

    void deleteByDtId(String mEngUid);
}
