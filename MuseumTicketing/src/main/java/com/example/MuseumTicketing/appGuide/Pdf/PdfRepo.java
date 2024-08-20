package com.example.MuseumTicketing.appGuide.Pdf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfRepo extends JpaRepository<PdfData, Integer> {
    List<PdfData> findByuId(String malId);



    Optional<PdfData> findByuIdAndId(String malId, Integer ids);
//    List<PdfData> findByUId(String malId);
}
