package com.example.MuseumTicketing.Guide.img.backgroundImg;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/bgData")
@CrossOrigin
public class BackgroundImgController {
    @Autowired
    private BackgroundImgService backgroundImgService;
    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;
    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @PostMapping(path = "/uploadBackgroundImg")
    public ResponseEntity<?> uploadBackgroundImg(@RequestParam String commonId,
                                                 @RequestParam MultipartFile bgFile){
        try {
            if (commonId.isEmpty() || commonId.isBlank() || commonId==null||"undefined".equalsIgnoreCase(commonId)){
                return new ResponseEntity<>("commonId is not generated. Please generate it",HttpStatus.NOT_FOUND);
            }
            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
            Optional<CommonIdFs> commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
            Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                String engId = commonIdQRCode.getEngId();
                String malId = commonIdQRCode.getMalId();
                return backgroundImgService.uploadBgImageMain(engId,malId,bgFile,commonId);
            } else if (commonIdFsOptional.isPresent()) {
                CommonIdFs commonIdFs = commonIdFsOptional.get();
                String engId = commonIdFs.getFsEngId();
                String malId = commonIdFs.getFsMalId();
                return backgroundImgService.uploadBgImageFirst(engId,malId,commonId,bgFile);
            } else if (commonIdSsOptional.isPresent()) {
                CommonIdSs commonIdSs = commonIdSsOptional.get();
                String engId = commonIdSs.getSsEngId();
                String malId = commonIdSs.getSsMalId();
                return backgroundImgService.uploadBgImageSecond(engId,malId,bgFile,commonId);

            }else {
                return new ResponseEntity<>("CommonId is not matchig",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin
    @PutMapping(path = "/updateBackgroundImg/{commonId}/{imgId}")
    public ResponseEntity<?>updateBackgroundImage(@PathVariable String commonId,
                                                  @PathVariable Integer imgId,
                                                  @RequestParam MultipartFile bgFile){
        try {
            if (commonId.isEmpty() || commonId.isBlank() || commonId==null||"undefined".equalsIgnoreCase(commonId) ||
                    imgId==null ){
                return new ResponseEntity<>("commonId is not generated. Please generate it",HttpStatus.NOT_FOUND);
            }
            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
            Optional<CommonIdFs> commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
            Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                String engId = commonIdQRCode.getEngId();
                String malId = commonIdQRCode.getMalId();
                return backgroundImgService.updateBgImageMain(engId,malId,bgFile,commonId);
            } else if (commonIdFsOptional.isPresent()) {
                CommonIdFs commonIdFs = commonIdFsOptional.get();
                String engId = commonIdFs.getFsEngId();
                String malId = commonIdFs.getFsMalId();
                return backgroundImgService.updateBgImageFirst(engId,malId,commonId,bgFile);
            } else if (commonIdSsOptional.isPresent()) {
                CommonIdSs commonIdSs = commonIdSsOptional.get();
                String engId = commonIdSs.getSsEngId();
                String malId = commonIdSs.getSsMalId();
                return backgroundImgService.updateBgImageSecond(engId,malId,bgFile,commonId);

            }else {
                return new ResponseEntity<>("CommonId is not matchig",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteBackgroundImg/{commonId}/{imgId}")
    public ResponseEntity<?>deleteBackgroundImage(@PathVariable String commonId,
                                                  @PathVariable Integer imgId){
        try {
            if (commonId.isEmpty() || commonId.isBlank() || commonId==null||"undefined".equalsIgnoreCase(commonId) ||
                    imgId==null ){
                return new ResponseEntity<>("commonId is not generated. Please generate it",HttpStatus.NOT_FOUND);
            }
            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
            Optional<CommonIdFs> commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
            Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                String engId = commonIdQRCode.getEngId();
                String malId = commonIdQRCode.getMalId();
                int count = backgroundImgService.deleteBgImageMain(engId,malId,imgId,commonId);
                if ( count>0){
                    return new ResponseEntity<>("BackgroundImage :"+commonId+" is deleted successfully",HttpStatus.OK);
                } else if (count<0) {
                    return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
                }else {
                    return new ResponseEntity<>("BackgroundImage is not deleted "+commonId,HttpStatus.BAD_REQUEST);
                }
            } else if (commonIdFsOptional.isPresent()) {
                CommonIdFs commonIdFs = commonIdFsOptional.get();
                String engId = commonIdFs.getFsEngId();
                String malId = commonIdFs.getFsMalId();
                int count = backgroundImgService.deleteBgImageFirst(engId,malId,imgId,commonId);
                if ( count>0){
                    return new ResponseEntity<>("BackgroundImage :"+commonId+" is deleted successfully",HttpStatus.OK);
                } else if (count<0) {
                    return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
                }else {
                    return new ResponseEntity<>("BackgroundImage is not deleted "+commonId,HttpStatus.BAD_REQUEST);
                }
            } else if (commonIdSsOptional.isPresent()) {
                CommonIdSs commonIdSs = commonIdSsOptional.get();
                String engId = commonIdSs.getSsEngId();
                String malId = commonIdSs.getSsMalId();
                int count = backgroundImgService.deleteBgImageSecond(engId,malId,imgId,commonId);
                if ( count>0){
                    return new ResponseEntity<>("BackgroundImage :"+commonId+" is deleted successfully",HttpStatus.OK);
                } else if (count<0) {
                    return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
                }else {
                    return new ResponseEntity<>("BackgroundImage is not deleted "+commonId,HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity<>("CommonId is not matchig",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong!!!",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> handlerException(Exception e) {
        if (e instanceof NullPointerException){
            return new ResponseEntity<>(new ErrorResponse("Null pointer exception occurred",400),HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            return new ResponseEntity<>(new ErrorResponse("Invalid argument exception occurred",400),HttpStatus.BAD_REQUEST);
        } else if (e instanceof AmazonS3Exception) {
            return new ResponseEntity<>(new ErrorResponse("error deleting image from s3 "+e.getMessage(),500),HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred : "+e.getMessage(),500),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
