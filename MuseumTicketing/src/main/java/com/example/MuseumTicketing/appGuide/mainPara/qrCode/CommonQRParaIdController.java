package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.example.MuseumTicketing.appGuide.mainPara.CombinedPara;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/guideAppQR")
public class CommonQRParaIdController {
    @Autowired
    private CommonQRParaIdService commonQRParaIdService;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;

    @GetMapping("/generate")
    public ResponseEntity<?> generateQRCode(@RequestParam String mMalUid, @RequestParam String mEngUid) {
        try {

            if (commonQRParaIdRepo.existsByMalIdAndEngId(mMalUid, mEngUid)) {

                CommonQRParaId existingQRCode = commonQRParaIdRepo.findByMalIdAndEngId(mMalUid, mEngUid).orElse(null);
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

            byte[] qrCode = commonQRParaIdService.generateQRCodeAndSave(mMalUid, mEngUid);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(qrCode);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }     catch (WriterException | IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "An error occurred while generating or saving the QR code. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping(path = "/getScanDetails")
    public ResponseEntity<List<CombinedPara>> getAllMainTopicData(@RequestParam Integer dtId, @RequestParam String commonId) {
        return commonQRParaIdService.getCombinedDataByCommonId(dtId, commonId);
    }



}
