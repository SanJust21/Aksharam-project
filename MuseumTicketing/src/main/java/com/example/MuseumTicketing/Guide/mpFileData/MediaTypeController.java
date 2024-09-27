package com.example.MuseumTicketing.Guide.mpFileData;

import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.audio.AudioService;
import com.example.MuseumTicketing.Guide.mpType.FileType;
import com.example.MuseumTicketing.Guide.mpType.FileTypeRepo;
import com.example.MuseumTicketing.appGuide.audio.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/mediaData")
@CrossOrigin
public class MediaTypeController {
    @Autowired
    private AudioService audioService;
    @Autowired
    private MediaTypeService mediaTypeService;
    @Autowired
    private FileTypeRepo fileTypeRepo;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @Autowired
    private ErrorService errorService;

    @PostMapping(path = "/mpData")
    public ResponseEntity<?> addMp3Data(@RequestParam String uId,
                                        @RequestParam Integer mtId,
                                        @RequestParam MultipartFile[] files){
        try {

            if (uId == null || mtId == null ||uId.isEmpty()||"undefined".equalsIgnoreCase(uId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo .findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {

                    List<MediaTypeDTO> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(mediaTypeService.uploadMp3(file,uId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
//                else if (fData != null && "Video".equalsIgnoreCase(fData)) {
////                    return mediaTypeService.uploadMp4(files,thumbnailFile,uId);
//                }
                else {
                    return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/mpData1")
    public ResponseEntity<?> addMp3Data1(@RequestParam String uId,
                                                         @RequestParam Integer mtId,
                                                         @RequestParam MultipartFile[] files){
        try {

            if (uId == null || mtId == null ||uId.isEmpty()||"undefined".equalsIgnoreCase(uId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo .findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    List<MediaTypeDTO> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(mediaTypeService.uploadMp3fs(file,uId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                    //return mediaTypeService.addMp3(files,dtId);
                }
//                else if (fData != null && "Video".equalsIgnoreCase(fData)) {
//                    return mediaTypeService.uploadMp4fs(files,uId);
//                }
                else {
                    return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/mpData2")
    public ResponseEntity<?> addMp3Data2(@RequestParam String uId,
                                                          @RequestParam Integer mtId,
                                                          @RequestParam MultipartFile[] files){
        try {

            if (uId == null || mtId == null ||uId.isEmpty()||"undefined".equalsIgnoreCase(uId)) {
                return new ResponseEntity<>("Topic ID, Media Type ID required", HttpStatus.BAD_REQUEST);
            }

            Optional<FileType> fileTypeOptional =fileTypeRepo .findById(mtId);
            if (fileTypeOptional.isPresent()) {
                FileType fileType = fileTypeOptional.get();
                String fData = fileType.getFileType();
                if (fData != null && "Audio".equalsIgnoreCase(fData)) {
                    List<MediaTypeDTO> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(mediaTypeService.uploadMp3ss(file,uId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
//                else if (fData != null && "Video".equalsIgnoreCase(fData)) {
//                    return mediaTypeService.uploadMp4ss(files,uId);
//                }
                else {
                    return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("File not present. Resend the file.", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/videoUpload")
    public ResponseEntity<?>uploadVideo(@RequestParam String commonId,@RequestParam MultipartFile[] video,
                                        @RequestParam(required = false) MultipartFile[] thumbnailFile){
        try {
            Optional<CommonIdQRCode>commonIdQRCodeOptional=commonIdQRCodeRepo.findByCommonId(commonId);
            Optional<CommonIdFs>commonIdFsOptional=fsCommonIdRepo.findByFsCommonId(commonId);
            Optional<CommonIdSs>commonIdSsOptional=commonIdSsRepo.findBySsCommonId(commonId);
            if (commonIdQRCodeOptional.isPresent()){
                if (thumbnailFile ==null){
                    return mediaTypeService.upload_Mp4(video,commonId);
                }else {
                    return mediaTypeService.uploadMp4(video,thumbnailFile,commonId);
                }

            } else if (commonIdFsOptional.isPresent()) {
                if (thumbnailFile==null){
                    return mediaTypeService.uploadFirst_Mp4(video,commonId);
                }else {
                    return mediaTypeService.uploadMp4fs(video,thumbnailFile,commonId);
                }
            } else if (commonIdSsOptional.isPresent()) {
                if (thumbnailFile==null){
                    return mediaTypeService.uploadSecond_Mp4(video,commonId);
                }else {
                    return mediaTypeService.uploadMp4ss(video,thumbnailFile,commonId);
                }
            }else {
                return new ResponseEntity<>("CommonId : "+commonId+" isn't present ",HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
}
