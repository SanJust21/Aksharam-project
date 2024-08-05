package com.example.MuseumTicketing.Guide.img;

import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirst;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/imgData")
@CrossOrigin
@Slf4j
public class ImgController {
    @Autowired
    private ImgService imgService;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;

    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private ErrorService errorService;

    // *upload multiple files and background Image *
    @PostMapping(path = "/uploadImg")
    public ResponseEntity<?> uploadData(@RequestParam(value = "file") MultipartFile[] files,

                                                    @RequestParam String englishUId,
                                                    @RequestParam String malUid) throws IOException, WriterException {
        try {
            if (englishUId == null || malUid == null) {
                return new ResponseEntity<>("English UID and Malayalam UID are required", HttpStatus.BAD_REQUEST);
            }
            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByMalIdAndEngId(malUid, englishUId);
            if (commonIdQRCodeOptional.isEmpty()) {
                return new ResponseEntity<>("Common ID not found for given UIDs", HttpStatus.NOT_FOUND);
            }

            CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
            String commonId = commonIdQRCode.getCommonId();

            List<ImgData> responses = new ArrayList<>();
            for (MultipartFile file : files){
                responses.add(imgService.uploadJPG(file,englishUId,malUid,commonId));
            }
            return new ResponseEntity<>(responses,HttpStatus.OK);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/uploadImg1")
    public ResponseEntity<?> uploadData1(@RequestParam MultipartFile[] files,
                                                        @RequestParam String englishUId,
                                                        @RequestParam String malUid){
        try {

            if (englishUId == null || malUid == null) {
                return new ResponseEntity<>("English UID and Malayalam UID are required", HttpStatus.BAD_REQUEST);
            }
            Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(englishUId);
            Optional<FirstSubMalayalam> firstSubMalayalamOptional =firstSubMalayalamRepo.findByfsUid(malUid);

            String enguId = null;
            String maluId = null;

            if (firstSubEnglishOptional.isPresent()) {
                FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
                enguId = firstSubEnglish.getMainUid();
            } else {
                log.info("Eng id is null for fsUid: " + englishUId);
            }

            if (firstSubMalayalamOptional.isPresent()) {
                FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
                maluId = firstSubMalayalam.getMainUid();
            } else {
                log.info("Mal id is null for fsUid: " + malUid);
            }

            if (enguId == null || maluId == null) {
                log.info("Eng id or Mal id is null. EngUId: " + enguId + ", MalUId: " + maluId);
                return new ResponseEntity<>("Eng id or Mal id is null", HttpStatus.NOT_FOUND);
            }

            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByMalIdAndEngId(maluId, enguId);
            if (commonIdQRCodeOptional.isEmpty()) {
                return new ResponseEntity<>("Common ID not found for given UIDs", HttpStatus.NOT_FOUND);
            }

            CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
            String commonId = commonIdQRCode.getCommonId();
            List<ImgSubFirst> imgSubFirsts = new ArrayList<>();
            for (MultipartFile file : files){
                imgSubFirsts.add(imgService.uploadData1(file,englishUId,malUid, commonId));
            }
            return new ResponseEntity<>(imgSubFirsts,HttpStatus.OK);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/uploadImg2")
    public ResponseEntity<?>uploadData2(@RequestParam MultipartFile[] files,
                                                         @RequestParam String englishUId,
                                                         @RequestParam String malUid){
        try {

            if (englishUId == null || malUid == null) {
                return new ResponseEntity<>("English UID and Malayalam UID are required", HttpStatus.BAD_REQUEST);
            }

            // Fetch fsUid using ssUid
            Optional<SecondSubEnglish> secondSubEnglishOptional = secondSubEnglishRepo.findByssUid(englishUId);
            Optional<SecondSubMalayalam> secondSubMalayalamOptional = secondSubMalayalamRepo.findByssUid(malUid);

            String englishFsUid = null;
            String malayalamFsUid = null;

            if (secondSubEnglishOptional.isPresent()) {
                SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
                englishFsUid = secondSubEnglish.getFsUid();
            } else {
                log.info("English fsUid is null for ssUid: " + englishUId);
            }

            if (secondSubMalayalamOptional.isPresent()) {
                SecondSubMalayalam secondSubMalayalam = secondSubMalayalamOptional.get();
                malayalamFsUid = secondSubMalayalam.getFsUid();
            } else {
                log.info("Malayalam fsUid is null for ssUid: " + malUid);
            }

            if (englishFsUid == null || malayalamFsUid == null) {
                log.info("English fsUid or Malayalam fsUid is null. English fsUid: " + englishFsUid + ", Malayalam fsUid: " + malayalamFsUid);
                return new ResponseEntity<>("English fsUid or Malayalam fsUid is null", HttpStatus.NOT_FOUND);
            }

            // Fetch mainUid using fsUid
            Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(englishFsUid);
            Optional<FirstSubMalayalam> firstSubMalayalamOptional = firstSubMalayalamRepo.findByfsUid(malayalamFsUid);

            String mainEngUid = null;
            String mainMalUid = null;

            if (firstSubEnglishOptional.isPresent()) {
                FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
                mainEngUid = firstSubEnglish.getMainUid();
            } else {
                log.info("Main English id is null for fsUid: " + englishFsUid);
            }

            if (firstSubMalayalamOptional.isPresent()) {
                FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
                mainMalUid = firstSubMalayalam.getMainUid();
            } else {
                log.info("Main Malayalam id is null for fsUid: " + malayalamFsUid);
            }

            if (mainEngUid == null || mainMalUid == null) {
                log.info("Main English id or Main Malayalam id is null. Main English id: " + mainEngUid + ", Main Malayalam id: " + mainMalUid);
                return new ResponseEntity<>("Main English id or Main Malayalam id is null", HttpStatus.NOT_FOUND);
            }

            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByMalIdAndEngId(mainMalUid, mainEngUid);
            if (commonIdQRCodeOptional.isEmpty()) {
                return new ResponseEntity<>("Common ID not found for given UIDs", HttpStatus.NOT_FOUND);
            }

            CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
            String commonId = commonIdQRCode.getCommonId();

            List<ImgSubSecond> imgSubSeconds = new ArrayList<>();
            for (MultipartFile file : files){
                imgSubSeconds.add(imgService.uploadData2(file,englishUId,malUid,commonId));
            }
            return new ResponseEntity<>(imgSubSeconds,HttpStatus.OK);
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
