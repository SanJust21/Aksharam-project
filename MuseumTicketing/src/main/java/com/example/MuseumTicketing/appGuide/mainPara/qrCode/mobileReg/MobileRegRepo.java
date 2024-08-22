package com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MobileRegRepo extends JpaRepository<MobileReg,Long> {
    Optional<MobileReg> findByPhNumber(String phNumber);
}
