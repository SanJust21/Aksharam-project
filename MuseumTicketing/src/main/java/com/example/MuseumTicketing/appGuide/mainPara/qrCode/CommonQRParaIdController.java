package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.QRCodeResponse;
import com.example.MuseumTicketing.Guide.util.ErrorResponse;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.mainPara.CombinedPara;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg.MobileReg;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg.MobileRegRepo;
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
    @Autowired
    private MobileRegRepo mobileRegRepo;

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

    @GetMapping(path="/mobReg")
    public ResponseEntity<?>userMobileReg(@RequestParam(required = true) String phNumber,
                                          @RequestParam(required = false) String email,
                                          @RequestParam(required = false)String fullName){
        try {
//            if (phNumber!=null){
//                Optional<MobileReg>mobileRegOptional=mobileRegRepo.findByPhNumber(phNumber);
//                if (mobileRegOptional.isPresent()){
//                    MobileReg mobileReg = mobileRegOptional.get();
//                    if (phNumber.equals(mobileReg.getPhNumber())){
//                        return new ResponseEntity<>("Mobile Number is already present. phoneNumber : "+mobileReg.getPhNumber()+
//                                ". emailId : "+mobileReg.getEmail()+". Name : "+mobileReg.getFullName(),HttpStatus.ACCEPTED);
//                    }
//                } else if (email==null&&fullName==null) {
//                    return new ResponseEntity<>("mobile Number is not present.New User",HttpStatus.OK);
//                }else {
//                    return commonQRParaIdService.userMobileReg(phNumber, email, fullName);
//                }
//            }
//            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            Optional<MobileReg> existingMobileReg = mobileRegRepo.findByPhNumber(phNumber);
            if (existingMobileReg.isPresent()) {
                // If the mobile number exists, return true with an error message
                return new ResponseEntity<>(new ErrorResponse("Mobile number already registered",1), HttpStatus.OK);
            } else if (email == null && fullName == null) {
                // If the mobile number does not exist and email/fullName are not provided
                return new ResponseEntity<>(new ErrorResponse("Mobile number not registered", 0), HttpStatus.OK);
            }

            // Step 2: Register the mobile number with email and full name
            if (email != null && fullName != null) {
                if (phNumber.length() >= 10) {
                    MobileReg mobileReg = new MobileReg();
                    mobileReg.setPhNumber(phNumber);
                    mobileReg.setEmail(email);
                    mobileReg.setFullName(fullName);
                    mobileRegRepo.save(mobileReg);
                    return new ResponseEntity<>(new ErrorResponse(fullName, 0), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Enter a 10-digit mobile number", HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>("Missing required parameters", HttpStatus.BAD_REQUEST);

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

    @DeleteMapping(path = "/deleteMobileReg")
    public ResponseEntity<?>deleteMobileReg(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String phNumber){
        if (id != null) {
            Optional<MobileReg> existingMobileReg = mobileRegRepo.findById(id);
            if (existingMobileReg.isPresent()) {
                mobileRegRepo.delete(existingMobileReg.get());
                return new ResponseEntity<>("Mobile registration deleted successfully by ID", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Mobile registration not found by ID", HttpStatus.NOT_FOUND);
            }
        } else if (phNumber != null) {
            Optional<MobileReg> existingMobileReg = mobileRegRepo.findByPhNumber(phNumber);
            if (existingMobileReg.isPresent()) {
                mobileRegRepo.delete(existingMobileReg.get());
                return new ResponseEntity<>("Mobile registration deleted successfully by phone number", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Mobile registration not found by phone number", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Please provide either an ID or phone number", HttpStatus.BAD_REQUEST);
        }
    }
}
