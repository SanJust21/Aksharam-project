package com.example.MuseumTicketing.Guide.QR;

import com.example.MuseumTicketing.Guide.mainHeading.CombinedData;
import com.example.MuseumTicketing.Guide.mainHeading.MainTitleService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> generateQRCode(@RequestParam String mMalUid, @RequestParam String mEngUid) {
        try {

            if (commonIdQRCodeRepo.existsByMalIdAndEngId(mMalUid, mEngUid)) {

                CommonIdQRCode existingQRCode = commonIdQRCodeRepo.findByMalIdAndEngId(mMalUid, mEngUid).orElse(null);
                if (existingQRCode != null) {

                    String url = existingQRCode.getQrCodeUrl();
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "QR code already exists");
                    response.put("url", url);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(response);
                }
            }

            byte[] qrCode = qrCodeGenerateService.generateQRCodeAndSave(mMalUid, mEngUid);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(qrCode);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "An error occurred while generating or saving the QR code. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(path = "/getScanDetails")
    public ResponseEntity<List<CombinedData>> getAllMainTitleData(@RequestParam Integer dtId, @RequestParam String commonId) {
        return mainTitleService.getCombinedDataByCommonId(dtId, commonId);
    }

}







