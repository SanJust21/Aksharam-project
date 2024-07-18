package com.example.MuseumTicketing.Guide.img.secondSubHeading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ImgSubSecondRepo extends JpaRepository<ImgSubSecond,Integer> {
    List<ImgSubSecond> findByengId(String fsUid);

    List<ImgSubSecond> findByFsEngUid(String ssUid);

    List<ImgSubSecond> findByFsMalUid(String ssUid);

    List<ImgSubSecond> findBymalId(String ssUid);

    //List<ImgSubSecond> findByEngIdAndMalId(String englishUId, String malUid);

    List<ImgSubSecond> findByCommonId(String commonId);

    void deleteByCommonId(String commonId);
    List<ImgSubSecond> findByfsMalUid(String malId);

    List<ImgSubSecond> findByfsEngUid(String fsId);

    void deleteAllByfsEngUid(String fsEngId);


    Optional<ImgSubSecond> findByImgIDAndCommonId(Integer imgId, String commonId);

    void deleteAllByengId(String englishId);



    Optional<ImgSubSecond> findByImgIDAndEngId(Integer imgId, String englishUId);

    Optional<ImgSubSecond> findByEngIdAndMalId(String engId, String malId);
}
