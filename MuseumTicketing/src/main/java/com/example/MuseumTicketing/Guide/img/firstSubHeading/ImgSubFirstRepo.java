package com.example.MuseumTicketing.Guide.img.firstSubHeading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ImgSubFirstRepo extends JpaRepository<ImgSubFirst,Integer> {
    List<ImgSubFirst> findByengId(String fsUid);

    List<ImgSubFirst> findBymalId(String fsUid);

    List<ImgSubFirst> findByEngId(String fsUid);

    List<ImgSubFirst> findByCommonId(String commonId);

    void deleteByCommonId(String commonId);

    void deleteAllByengId(String fsEngId);

    Optional<ImgSubFirst> findByImgIDAndCommonId(Integer imgId, String commonId);

    Optional<ImgSubFirst> findByEngIdAndMalId(String engId, String malId);
}
