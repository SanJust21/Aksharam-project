package com.example.MuseumTicketing.Guide.QR;

import com.example.MuseumTicketing.Guide.mainHeading.CombinedData;
import com.example.MuseumTicketing.Guide.mainHeading.MainTitleService;
import com.example.MuseumTicketing.Guide.mainHeading.CombinedData;
import com.example.MuseumTicketing.Guide.mainHeading.MainTitleService;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin
public class QRCodeController {

    @Autowired
    private QRCodeGenerateService qrCodeGenerateService;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private MainTitleService mainTitleService;


    @GetMapping("/generate")
    public ResponseEntity<QRCodeResponse> generateQRCode(@RequestParam String mMalUid, @RequestParam String mEngUid) {
        try {
            if (commonIdQRCodeRepo.existsByMalIdAndEngId(mMalUid, mEngUid)){
                CommonIdQRCode existingQRCode = commonIdQRCodeRepo.findByMalIdAndEngId(mMalUid, mEngUid).orElse(null);
                if (existingQRCode != null) {

                    String commonId = existingQRCode.getCommonId();
                    QRCodeResponse response = new QRCodeResponse(commonId, " QRCode already exists");
                    return ResponseEntity.ok(response);
                }
            }
            byte[] qrCodeImg = qrCodeGenerateService.generateQRCodeAndSave(mMalUid,mEngUid);
            String commonId = qrCodeGenerateService.getCommonId(mMalUid,mEngUid);

            QRCodeResponse response = new QRCodeResponse(commonId,"QrCode generated successfully");
            return ResponseEntity.ok(response);

        }catch (IllegalArgumentException e){
            QRCodeResponse response = new QRCodeResponse(null,e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (WriterException | IOException exception){
            exception.printStackTrace();
            QRCodeResponse qrCodeResponse = new QRCodeResponse(null,"An error occurred while generating or saving the QrCode .Please try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(qrCodeResponse);
        }
    }

    @GetMapping(path = "/getScanDetails")
    public ResponseEntity<List<CombinedData>> getAllMainTitleData(@RequestParam Integer dtId, @RequestParam String commonId) {
        return mainTitleService.getCombinedDataByCommonId(dtId, commonId);
    }

}







