package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.QRCodeResponse;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.mainPara.CombinedPara;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg.MobileReg;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/guideAppQR")
@CrossOrigin
public class CommonQRParaIdController {
    @Autowired
    private CommonQRParaIdService commonQRParaIdService;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;
    @Autowired
    private ErrorService errorService;

    @GetMapping("/generate")
    public ResponseEntity<QRCodeResponse>generateQRCode(@RequestParam String mMalUid, @RequestParam String mEngUid){
        try {
            if (commonQRParaIdRepo.existsByMalIdAndEngId(mMalUid, mEngUid)){
                Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByMalIdAndEngId(mMalUid,mEngUid);
                if (commonQRParaIdOptional.isPresent()){
                    CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                    String commonId = commonQRParaId.getCommonId();
                    QRCodeResponse response = new QRCodeResponse(commonId, " QRCode already exists");
                    return ResponseEntity.ok(response);
                }
            }
            Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByEngId(mEngUid);
            if (commonQRParaIdOptional.isEmpty()){
                Optional<CommonQRParaId>optionalCommonQRParaId=commonQRParaIdRepo.findByMalId(mMalUid);
                if (optionalCommonQRParaId.isEmpty()){
                    byte[] qrCodeImg = commonQRParaIdService.generateQRCodeAndSave(mMalUid,mEngUid);
                    String commonId = commonQRParaIdService.getCommonId(mMalUid,mEngUid);

                    QRCodeResponse response = new QRCodeResponse(commonId,"QrCode generated successfully");
                    return ResponseEntity.ok(response);
                }else {
                    String commonId =commonQRParaIdService.getCommonId(mMalUid,mEngUid);
                    QRCodeResponse response = new QRCodeResponse(commonId, "Malayalam uniqueId is present. commonId : "+commonId);
                    return ResponseEntity.ok(response);
                }
            }else {
                String commonId =commonQRParaIdService.getCommonId(mMalUid,mEngUid);
                QRCodeResponse response = new QRCodeResponse(commonId, "English uniqueId is present. commonId : "+commonId);
                return ResponseEntity.ok(response);
            }

        }catch (IllegalArgumentException e){
            QRCodeResponse response = new QRCodeResponse(null,e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (WriterException | IOException exception){
            exception.printStackTrace();
        }
        QRCodeResponse qrCodeResponse = new QRCodeResponse(null,"An error occurred while generating or saving the QrCode .Please try again later");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(qrCodeResponse);
    }



    @GetMapping(path = "/getScanDetails")
    public ResponseEntity<List<CombinedPara>> getAllMainTopicData(@RequestParam Integer dtId, @RequestParam String commonId) {
        return commonQRParaIdService.getCombinedDataByCommonId(dtId, commonId);
    }

    @GetMapping(path = "/getAllDetailsByDataType")
    public ResponseEntity<List<CombinedGetAllPara>>getAllDetailsByDataType(@RequestParam Integer dtId){
        try {
            return commonQRParaIdService.getAllDetailsByDataType(dtId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path="/mobReg")
    public ResponseEntity<?>userMobileReg(@RequestBody MobileReg mobileReg){
        try {
            return commonQRParaIdService.userMobileReg(mobileReg);
        }catch (Exception e){
            return errorService.handlerException(e);
        }


    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<List<MobileReg>>getAllMobileUser(){
        try {
            return commonQRParaIdService.getAllUsersData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
