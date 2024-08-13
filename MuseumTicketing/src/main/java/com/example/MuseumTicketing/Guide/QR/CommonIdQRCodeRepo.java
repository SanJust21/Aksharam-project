package com.example.MuseumTicketing.Guide.QR;

import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonIdQRCodeRepo extends JpaRepository<CommonIdQRCode, Long> {

    boolean existsByMalIdAndEngId(String malId, String engId);

    Optional<CommonIdQRCode> findByMalIdAndEngId(String malId, String engId);

    Optional<CommonIdQRCode> findByCommonId(String commonId);

    Optional<CommonIdQRCode> findByEngId(String mEngUid);

    Optional<CommonIdQRCode> findByMalId(String mMalUid);

    void deleteByCommonId(String commonId);


}
