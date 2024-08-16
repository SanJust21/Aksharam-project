package com.example.MuseumTicketing.appGuide.Pdf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfRepo extends JpaRepository<PdfData, Integer> {
}
