package com.example.MuseumTicketing.Guide.UPDATE.MainTitle;

import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeDTO;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeService;
import com.example.MuseumTicketing.Guide.img.ImgService;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeDTO;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeService;
import com.example.MuseumTicketing.Guide.mpType.FileType;
import com.example.MuseumTicketing.Guide.mpType.FileTypeRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/updateMain")
@CrossOrigin
public class MainUpdateController {
    @Autowired
    private MainUpdateService mainUpdateService;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;
    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private ImgRepo imgRepo;
    @Autowired
    private FileTypeRepo fileTypeRepo;
    @Autowired
    private MediaTypeService mediaTypeService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;
    @Autowired
    private ErrorService errorService;

    @PutMapping(path = "/stringUpdate/{uId}")
    public ResponseEntity<?> mainDataUpdate(@PathVariable String uId,
                                            @RequestBody MainDTO mainDTO){
        try {
            if (uId==null || uId.isEmpty()|| "undefined".equalsIgnoreCase(uId)){
                return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
            }else {
                Optional<MainTitleMal> mainTitleMal =mainTitleMalRepo.findBymMalUid(uId);
                if (mainTitleMal.isPresent()){
                    MainTitleMal mainTitleMal1 = mainTitleMal.get();
                    if (mainTitleMal1.getMMalUid().equals(uId)){
                        return mainUpdateService.updateDataMalyalam(uId,mainDTO);
                    }else {
                        return new ResponseEntity<>("No Data",HttpStatus.BAD_REQUEST);
                    }
                }else {
                    MainTitleEng mainTitleEng = mainTitleEngRepo.findBymEngUid(uId);
                    if (mainTitleEng!=null && mainTitleEng.getMEngUid().equals(uId)){
                        return mainUpdateService.updateDataEnglish(uId,mainDTO);
                    }

                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateUploadImg")
    public ResponseEntity<?> updateJpgMain(
            @RequestParam(value = "files") MultipartFile[] files,
            @RequestParam List<Integer> imgIds,
            @RequestParam String commonId) {

        try {
            if (commonId == null || imgIds.isEmpty() || files.length != imgIds.size()||"undefined".equalsIgnoreCase(commonId)) {
                return new ResponseEntity<>("Common ID, image IDs, and files are required, and the number of files must match the number of image IDs", HttpStatus.BAD_REQUEST);
            } else {
                List<ImgData> existingImgDataList = imgRepo.findByCommonId(commonId);
                if (!existingImgDataList.isEmpty()) {
                    List<ImgData> responses = new ArrayList<>();
                    for (int i = 0; i < files.length; i++) {
                        responses.add(imgService.updateMainJPG(files[i], imgIds.get(i), commonId));
                    }
                    return new ResponseEntity<>(responses, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No image data found for the provided Common ID", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
            return errorService.handlerException(e);
        }
    }


    @PutMapping(path = "/updateMpData")
    public ResponseEntity<?> addMp3Data(@RequestParam String uId,
                                        @RequestParam Integer mtId,
                                        @RequestParam Integer id,
                                        @RequestParam MultipartFile files){
        try {

            if (uId == null || mtId == null ||uId.isEmpty()||"undefined".equalsIgnoreCase(uId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo.findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
//                    List<MediaTypeDTO> responses = new ArrayList<>();
//                    for (MultipartFile file : files){
//                        responses.add(mediaTypeService.updateUploadMp3(file,uId));
//                    }
//                    return new ResponseEntity<>(responses,HttpStatus.OK);
                    //return mediaTypeService.addMp3(files,dtId);
                    return mainUpdateService.updateAudioMain(files,uId,id);
                } else if (fData != null && "Video".equalsIgnoreCase(fData)) {
                    Optional<CommonIdQRCode>commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(uId);
                    if (commonIdQRCodeOptional.isPresent()){
                        CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                        if (commonIdQRCode.getCommonId().equals(uId)){
//                            List<MediaTypeDTO> responses = new ArrayList<>();
//                            for (MultipartFile file : files){
//                                responses.add(mediaTypeService.updateUploadMp4(file,uId));
//                            }
//                            return new ResponseEntity<>(responses,HttpStatus.OK);
                            return mainUpdateService.updateVideoMain(files,uId,id);
                        }else {
                            return new ResponseEntity<>("CommonId : "+uId+"   is not matching",HttpStatus.BAD_REQUEST);
                        }
                    }else {
                        return new ResponseEntity<>("CommonId : "+uId+ "  is not correct.check CommonId",HttpStatus.BAD_REQUEST);
                    }

                } else {
                    return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateThumbnail")
    public ResponseEntity<?>updateThumbNail(@RequestParam String uId,
                                            @RequestParam Integer id,
                                            @RequestParam MultipartFile files){
        try {
            Optional<CommonIdQRCode>commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(uId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                if (commonIdQRCode.getCommonId().equals(uId)){
                    return mainUpdateService.updateThumbnail(files,uId,id);
                }
            }else {
                return new ResponseEntity<>("CommonId : "+uId+ "  is not correct.check CommonId",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
