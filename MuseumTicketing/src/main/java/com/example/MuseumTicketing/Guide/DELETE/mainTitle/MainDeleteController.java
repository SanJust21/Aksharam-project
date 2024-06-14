package com.example.MuseumTicketing.Guide.DELETE.mainTitle;

import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.UPDATE.MainTitle.MainUpdateService;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirst;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirstRepo;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeService;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4DataRepo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.mpType.FileTypeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/deleteMain")
@CrossOrigin
public class MainDeleteController {
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
    private MainDeleteService mainDeleteService;

    @Autowired
    private Mp3Data1Repo mp3Data1Repo;

    @Autowired
    private Mp4Data1Repo mp4Data1Repo;

    @Autowired
    private Mp3Repo mp3Repo;

    @Autowired
    private Mp4DataRepo mp4DataRepo;

    @Autowired
    private Mp3Data2Repo mp3Data2Repo;

    @Autowired
    private Mp4Data2Repo mp4Data2Repo;

    @Autowired
    private ImgSubFirstRepo imgSubFirstRepo;

    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @DeleteMapping("/delete/{commonId}")
    public ResponseEntity<String> deleteByCommonId(@PathVariable String commonId) {
        if (commonId == null || "undefined".equalsIgnoreCase(commonId)||commonId.isEmpty()) {
            return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
        }
        int count = mainDeleteService.deleteByCommonId(commonId);
        if (count>0){
            return ResponseEntity.ok("Deleted all entities associated with commonId: " + commonId);
        }else {
            return new ResponseEntity<>("No Data associated with commonId: "+commonId,HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(path = "/stringDelete/{uId}")
    public ResponseEntity<?> deleteMainData(@PathVariable String uId) {
        return mainDeleteService.deleteMainString(uId);
    }

//    @DeleteMapping(path = "/deleteImages")
//    public ResponseEntity<?> deleteImages(
//            @RequestParam String commonId,
//            @RequestParam(required = false) List<Integer> imgIds) {
//
//        try {
//          //  if (commonId == null) {
//            Optional<CommonIdQRCode> commonIdQRCode = commonIdQRCodeRepo.findByCommonId(commonId);
//            if (commonIdQRCode.isEmpty()) {
//                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
//            } else {
//                if (imgIds == null || imgIds.isEmpty()) {
//                    // Delete all images associated with the commonId
//                    mainDeleteService.deleteImagesByCommonId(commonId);
//                } else {
//                    // Delete specific images associated with the commonId
//                    mainDeleteService.deleteImagesByCommonIdAndIds(commonId, imgIds);
//                }
//                return new ResponseEntity<>("Images deleted successfully", HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteImages")
    public ResponseEntity<?> deleteImages(
            @RequestParam String commonId,
            @RequestParam(required = false) List<Integer> imgIds) {

        try {
            if (commonId == null || "undefined".equalsIgnoreCase(commonId)|| commonId.isEmpty()) {
                return new ResponseEntity<>("CommonId is required",HttpStatus.BAD_REQUEST);
            }else {
                Optional<CommonIdQRCode> commonIdQRCode = commonIdQRCodeRepo.findByCommonId(commonId);
                if (commonIdQRCode.isPresent()){
                    CommonIdQRCode commonIdQRCode1 = commonIdQRCode.get();
                    if (commonIdQRCode1.getCommonId().equals(commonId)){
                        if (imgIds == null || imgIds.isEmpty()) {
                            // Delete all images associated with the commonId
                            mainDeleteService.deleteImagesByCommonId(commonId);
                            return new ResponseEntity<>("Images deleted by commonId",HttpStatus.OK);
                        } else {
                            // Delete specific images associated with the commonId
                            mainDeleteService.deleteImagesByCommonIdAndIds(commonId, imgIds);
                            return new ResponseEntity<>("Images deleted by commonId",HttpStatus.OK);
                        }
                    }else {
                        return new ResponseEntity<>("CommonId is not present in database",HttpStatus.BAD_REQUEST);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @DeleteMapping(path = "/deleteImagesFirst")
//    public ResponseEntity<?> deleteImagesFirst(
//            @RequestParam String commonId,
//            @RequestParam(required = false) List<Integer> imgIds) {
//
//        try {
//            //if (commonId == null) {
//            Optional<CommonIdQRCode> commonIdQRCode = commonIdQRCodeRepo.findByCommonId(commonId);
//            if (commonIdQRCode.isEmpty()) {
//                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
//            } else {
//                if (imgIds == null || imgIds.isEmpty()) {
//                    // Delete all images associated with the commonId
//                    mainDeleteService.deleteImagesFirstByCommonId(commonId);
//                } else {
//                    // Delete specific images associated with the commonId
//                    mainDeleteService.deleteImagesFirstByCommonIdAndIds(commonId, imgIds);
//                }
//                return new ResponseEntity<>("Images deleted successfully", HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteImagesFirst")
    public ResponseEntity<?> deleteImagesFirst(
            @RequestParam String commonId,
            @RequestParam(required = false) List<Integer> imgIds) {

        try {
            if (commonId == null || "undefined".equalsIgnoreCase(commonId) ||commonId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            } else {
                List<ImgSubFirst> imgSubFirst = imgSubFirstRepo.findByCommonId(commonId);
                if (!imgSubFirst.isEmpty()){
                    if (imgIds == null || imgIds.isEmpty()) {
                        // Delete all images associated with the commonId
                        mainDeleteService.deleteImagesFirstByCommonId(commonId);
                    } else {
                        // Delete specific images associated with the commonId
                        mainDeleteService.deleteImagesFirstByCommonIdAndIds(commonId, imgIds);
                    }
                    return new ResponseEntity<>("Images deleted successfully", HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("Images are not deleted",HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping(path = "/deleteImagesSecond")
//    public ResponseEntity<?> deleteImagesSecond(
//            @RequestParam String commonId,
//            @RequestParam(required = false) List<Integer> imgIds) {
//
//        try {
//            //if (commonId == null) {
//                Optional<CommonIdQRCode> commonIdQRCode = commonIdQRCodeRepo.findByCommonId(commonId);
//                if (commonIdQRCode.isEmpty()) {
//                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
//            } else {
//                if (imgIds == null || imgIds.isEmpty()) {
//                    // Delete all images associated with the commonId
//                    mainDeleteService.deleteImagesSecondByCommonId(commonId);
//                } else {
//                    // Delete specific images associated with the commonId
//                    mainDeleteService.deleteImagesSecondByCommonIdAndIds(commonId, imgIds);
//                }
//                return new ResponseEntity<>("Images deleted successfully", HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteImagesSecond")
    public ResponseEntity<?> deleteImagesSecond(
            @RequestParam String commonId,
            @RequestParam(required = false) List<Integer> imgIds) {

        try {
            if (commonId == null || "undefined".equalsIgnoreCase(commonId)||commonId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            } else {
                List<ImgSubSecond> imgSubSeconds = imgSubSecondRepo.findByCommonId(commonId);
                if (!imgSubSeconds.isEmpty()){
                    if (imgIds == null || imgIds.isEmpty()) {
                        // Delete all images associated with the commonId
                        mainDeleteService.deleteImagesSecondByCommonId(commonId);
                    } else {
                        // Delete specific images associated with the commonId
                        mainDeleteService.deleteImagesSecondByCommonIdAndIds(commonId, imgIds);
                    }
                    return new ResponseEntity<>("Images deleted successfully", HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @DeleteMapping(path = "/deleteMp3")
//    public ResponseEntity<?> deleteMp3(@RequestParam String dtId) {
//        try {
//            mainDeleteService.deleteMp3ByDtId(dtId);
//            return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteMp3")
    public ResponseEntity<?> deleteMp3(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId)||dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            List<Mp3Data> mp3Data = mp3Repo.findBydtId(dtId);
            if (!mp3Data.isEmpty()){
                mainDeleteService.deleteMp3ByDtId(dtId);
                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @DeleteMapping(path = "/deleteMp4")
//    public ResponseEntity<?> deleteMp4(@RequestParam String dtId) {
//        try {
//            mainDeleteService.deleteMp4ByDtId(dtId);
//            return new ResponseEntity<>("MP4 deleted successfully", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @DeleteMapping(path = "/deleteMp4")
    public ResponseEntity<?> deleteMp4(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId)||dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            List<Mp4Data> mp4Data = mp4DataRepo.findBydtId(dtId);
            if (!mp4Data.isEmpty()){
                mainDeleteService.deleteMp4ByDtId(dtId);
                return new ResponseEntity<>("MP4 deleted successfully", HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @DeleteMapping(path = "/deleteMp3First")
//    public ResponseEntity<?> deleteMp3First(@RequestParam String dtId) {
//        try {
//            Optional<Mp3Data1> mp3Data1Optional = mp3Data1Repo.findByDtId(dtId);
//            if (mp3Data1Optional.isPresent()) {
//                Mp3Data1 mp3Data1 = mp3Data1Optional.get();
//                String mainEngId = mp3Data1.getMainEngId();
//                String mainMalId = mp3Data1.getMainMalId();
//
//                if (mainEngId != null) {
//                    mainDeleteService.deleteMp3FirstByMainEngId(mainEngId);
//                } else if (mainMalId != null) {
//                    mainDeleteService.deleteMp3FirstByMainMalId(mainMalId);
//                }
//
//                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("MP3 data not found", HttpStatus.NOT_FOUND);
//            }
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @DeleteMapping(path = "/deleteMp3First")
    public ResponseEntity<?> deleteMp3First(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId)||dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            Optional<Mp3Data1> mp3Data1Optional = mp3Data1Repo.findByDtId(dtId);
            if (mp3Data1Optional.isPresent()) {
                Mp3Data1 mp3Data1 = mp3Data1Optional.get();
                String mainEngId = mp3Data1.getMainEngId();
                String mainMalId = mp3Data1.getMainMalId();

                if (mainEngId != null) {
                    mainDeleteService.deleteMp3FirstByMainEngId(mainEngId);
                } else if (mainMalId != null) {
                    mainDeleteService.deleteMp3FirstByMainMalId(mainMalId);
                }

                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("MP3 data not found", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @DeleteMapping(path = "/deleteMp4First")
//    public ResponseEntity<?> deleteMp4First(@RequestParam String dtId) {
//        try {
//            Optional<Mp4Data1> mp4Data1Optional = mp4Data1Repo.findByDtId(dtId);
//            if (mp4Data1Optional.isPresent()) {
//                Mp4Data1 mp4Data1 = mp4Data1Optional.get();
//                String mainEngId = mp4Data1.getMainEngId();
//                String mainMalId = mp4Data1.getMainMalId();
//
//                if (mainEngId != null) {
//                    mainDeleteService.deleteMp4FirstByMainEngId(mainEngId);
//                } else if (mainMalId != null) {
//                    mainDeleteService.deleteMp4FirstByMainMalId(mainMalId);
//                }
//
//                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("MP3 data not found", HttpStatus.NOT_FOUND);
//            }
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteMp4First")
    public ResponseEntity<?> deleteMp4First(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId) || dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            Optional<Mp4Data1> mp4Data1Optional = mp4Data1Repo.findByDtId(dtId);
            if (mp4Data1Optional.isPresent()) {
                Mp4Data1 mp4Data1 = mp4Data1Optional.get();
                String mainEngId = mp4Data1.getMainEngId();
                String mainMalId = mp4Data1.getMainMalId();

                if (mainEngId != null) {
                    mainDeleteService.deleteMp4FirstByMainEngId(mainEngId);
                } else if (mainMalId != null) {
                    mainDeleteService.deleteMp4FirstByMainMalId(mainMalId);
                }

                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("MP3 data not found", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping(path = "/deleteMp3Second")
//    public ResponseEntity<?> deleteMp3Second(@RequestParam String dtId) {
//        try {
//            mainDeleteService.deleteMp3SecondByDtId(dtId);
//            return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteMp3Second")
    public ResponseEntity<?> deleteMp3Second(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId)||dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            List<Mp3Data2> mp3Data2s = mp3Data2Repo.findBydtId(dtId);
            if (!mp3Data2s.isEmpty()){
                mainDeleteService.deleteMp3SecondByDtId(dtId);
                return new ResponseEntity<>("MP3 deleted successfully", HttpStatus.OK);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
//
//    @DeleteMapping(path = "/deleteMp4Second")
//    public ResponseEntity<?> deleteMp4Second(@RequestParam String dtId) {
//        try {
//            mainDeleteService.deleteMp4SecondByDtId(dtId);
//            return new ResponseEntity<>("MP4 deleted successfully", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/deleteMp4Second")
    public ResponseEntity<?> deleteMp4Second(@RequestParam String dtId) {
        try {
            if (dtId == null || "undefined".equalsIgnoreCase(dtId)||dtId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }
            List<Mp4Data2> mp4Data2s = mp4Data2Repo.findBydtId(dtId);
            if (!mp4Data2s.isEmpty()){
                mainDeleteService.deleteMp4SecondByDtId(dtId);
                return new ResponseEntity<>("MP4 deleted successfully", HttpStatus.OK);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
