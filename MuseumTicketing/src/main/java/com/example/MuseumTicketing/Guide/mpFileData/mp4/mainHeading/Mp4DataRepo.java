package com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Mp4DataRepo extends JpaRepository<Mp4Data,Integer> {
    List<Mp4Data> findBydtId(String s);

    void deleteByDtId(String mEngUid);

    List<Mp4Data> findByengId(String mEngUid);

    List<Mp4Data> findBymalId(String mMalUid);


    Optional<Mp4Data> findByDtIdAndId(String dtId, Integer id);
}
