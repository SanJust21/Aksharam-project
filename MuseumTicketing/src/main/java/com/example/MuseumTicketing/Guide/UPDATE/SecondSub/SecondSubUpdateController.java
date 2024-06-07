package com.example.MuseumTicketing.Guide.UPDATE.SecondSub;

import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.ImgService;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeDTO;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeService;
import com.example.MuseumTicketing.Guide.mpType.FileType;
import com.example.MuseumTicketing.Guide.mpType.FileTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/updateSecondSub")
@CrossOrigin
public class SecondSubUpdateController {
    @Autowired
    private SecondSubUpdateService secondSubUpdateService;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;
    @Autowired
    private ImgService imgService;
    @Autowired
    private FileTypeRepo fileTypeRepo;
    @Autowired
    private MediaTypeService mediaTypeService;


    @PutMapping(path = "/updateSecondData/{uId}")
    public ResponseEntity<?> secondDataUpdate(@PathVariable String uId,
                                             @RequestBody MainDTO mainDTO){
        try {
            if (uId==null || uId.isEmpty()){
                return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
            }else {
                Optional<SecondSubMalayalam> secondSubMalayalam =secondSubMalayalamRepo.findByssUid(uId);
                if (secondSubMalayalam.isPresent()){
                    SecondSubMalayalam secondSubMalayalam1 = secondSubMalayalam.get();
                    if (secondSubMalayalam1.getSsUid().equals(uId)){
                        return secondSubUpdateService.updateSecondSubDataMalayalam(uId,mainDTO);
                    }else {
                        return new ResponseEntity<>("No Data",HttpStatus.BAD_REQUEST);
                    }
                }else {
                    Optional<SecondSubEnglish> secondSubEnglish = secondSubEnglishRepo.findByssUid(uId);
                    if (secondSubEnglish.isPresent()){
                        SecondSubEnglish secondSubEnglish1 = secondSubEnglish.get();
                        if (secondSubEnglish1.getSsUid().equals(uId)){
                            return secondSubUpdateService.updateSecondSubDataEnglish(uId,mainDTO);
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @PutMapping(path = "/updateUploadImg2/{englishUId}")
//    public ResponseEntity<?> updateJpgSecond(@RequestParam(value = "file") MultipartFile[] files,
//                                           @PathVariable String englishUId,
//                                           @RequestParam String malUid){
//
//        try {
//            if (englishUId == null || malUid == null) {
//                return new ResponseEntity<>("English UID and Malayalam UID are required", HttpStatus.BAD_REQUEST);
//            }else {
//                List<ImgSubSecond> existingImgData = imgSubSecondRepo.findByengId(englishUId);
//                if (!existingImgData.isEmpty()){
//                    List<ImgSubSecond> responses = new ArrayList<>();
//                    for (MultipartFile file : files){
//                        responses.add(imgService.updateSecondSubJPG(file,englishUId,malUid));
//                    }
//                    return new ResponseEntity<>(responses,HttpStatus.OK);
//                }else {
//                    return new ResponseEntity<>("imgRepo is empty",HttpStatus.BAD_REQUEST);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping(path = "/updateUploadImg2")
    public ResponseEntity<?> updateJpgSecond(
            @RequestParam(value = "files") MultipartFile[] files,
            @RequestParam List<Integer> imgIds,
            @RequestParam String commonId) {

        try {
            if (commonId == null || imgIds.isEmpty() || files.length != imgIds.size()) {
                return new ResponseEntity<>("Common ID, image IDs, and files are required, and the number of files must match the number of image IDs", HttpStatus.BAD_REQUEST);
            } else {
                List<ImgSubSecond> existingImgDataList = imgSubSecondRepo.findByCommonId(commonId);
                if (!existingImgDataList.isEmpty()) {
                    List<ImgSubSecond> responses = new ArrayList<>();
                    for (int i = 0; i < files.length; i++) {
                        responses.add(imgService.updateSecondSubJPG(files[i], imgIds.get(i), commonId));
                    }
                    return new ResponseEntity<>(responses, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No image data found for the provided Common ID", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(path = "/updateMpData2/{uId}")
    public ResponseEntity<?> addMp3DataSecond(@PathVariable String uId,
                                        @RequestParam Integer mtId,
                                        @RequestParam MultipartFile[] files){
        try {

            if (uId == null || mtId == null ) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo .findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    List<MediaTypeDTO> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(mediaTypeService.updateSecondSubUploadMp3(file,uId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                    //return mediaTypeService.addMp3(files,dtId);
                } else if (fData != null && "Video".equalsIgnoreCase(fData)) {
                    List<MediaTypeDTO> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(mediaTypeService.updateSecondSubUploadMp4(file,uId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
