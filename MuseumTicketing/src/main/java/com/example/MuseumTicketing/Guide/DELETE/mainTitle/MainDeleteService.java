package com.example.MuseumTicketing.Guide.DELETE.mainTitle;

import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImg;
import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImgRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
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
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirst;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirstRepo;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
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
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class MainDeleteService {

    @Autowired
    private ImgRepo imgRepo;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;

    @Autowired
    private ImgSubFirstRepo imgSubFirstRepo;

    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;

    @Autowired
    private Mp3Repo mp3Repo;

    @Autowired
    private Mp4DataRepo mp4DataRepo;

    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;

    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;

    @Autowired
    private Mp3Data1Repo mp3Data1Repo;

    @Autowired
    private Mp4Data1Repo mp4Data1Repo;

    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;

    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;

    @Autowired
    private Mp3Data2Repo mp3Data2Repo;

    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;
    @Autowired
    private CommonIdSsRepo commonIdSsRepo;
    @Autowired
    private BackgroundImgRepo backgroundImgRepo;
    @Autowired
    private ErrorService errorService;



    private void deleteIfPresent(Runnable deleteFunction) {
        try {
            deleteFunction.run();
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found during deletion: {}", ex.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteMainString(String uId) {
        try {
            if (uId == null || uId.isEmpty()) {
                return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
            } else {
                Optional<MainTitleMal> mainTitleMal = mainTitleMalRepo.findBymMalUid(uId);
                if (mainTitleMal.isPresent()) {
                    MainTitleMal mainTitleMal1 = mainTitleMal.get();
                    mainTitleMalRepo.delete(mainTitleMal1);
                    return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
                } else {
                    MainTitleEng mainTitleEng1 = mainTitleEngRepo.findBymEngUid(uId);

                        if (mainTitleEng1 != null && mainTitleEng1.getMEngUid().equals(uId)) {
                            mainTitleEngRepo.delete(mainTitleEng1);
                            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
                        } else {
                            return new ResponseEntity<>("No data found for ID: " + uId, HttpStatus.NOT_FOUND);
                        }


                }
            }
        } catch (Exception e) {
           return errorService.handlerException(e);

        }
    }




    public void deleteImagesByCommonId(String commonId) {
        List<ImgData> existingImgDataList = imgRepo.findByCommonId(commonId);
        for (ImgData imgData : existingImgDataList) {
            deleteImageFromS3(imgData.getFName());
            imgRepo.delete(imgData);
        }
    }

    public void deleteImagesFirstByCommonId(String commonId) {
        // Delete images from ImgSubFirst
        List<ImgSubFirst> existingImgSubFirstList = imgSubFirstRepo.findByCommonId(commonId);
        for (ImgSubFirst imgSubFirst : existingImgSubFirstList) {
            deleteImageFromS3(imgSubFirst.getFName());
            imgSubFirstRepo.delete(imgSubFirst);
        }
    }

    public void deleteImagesSecondByCommonId(String commonId) {
        // Delete images from ImgSubSecond
        List<ImgSubSecond> existingImgSubSecondList = imgSubSecondRepo.findByCommonId(commonId);
        for (ImgSubSecond imgSubSecond : existingImgSubSecondList) {
            deleteImageFromS3(imgSubSecond.getFName());
            imgSubSecondRepo.delete(imgSubSecond);
        }
    }


    public void deleteImagesByCommonIdAndIds(String commonId, List<Integer> imgIds) {
        for (Integer imgId : imgIds) {
            Optional<ImgData> imgDataOptional = imgRepo.findByImgIDAndCommonId(imgId, commonId);
            if (imgDataOptional.isPresent()) {
                ImgData imgData = imgDataOptional.get();
                deleteImageFromS3(imgData.getFName());
                imgRepo.delete(imgData);
            }
        }
    }

    public void deleteImagesFirstByCommonIdAndIds(String commonId, List<Integer> imgIds) {
        for (Integer imgId : imgIds) {
            Optional<ImgSubFirst> imgDataOptional = imgSubFirstRepo.findByImgIDAndCommonId(imgId, commonId);
            if (imgDataOptional.isPresent()) {
                ImgSubFirst imgData = imgDataOptional.get();
                deleteImageFromS3(imgData.getFName());
                imgSubFirstRepo.delete(imgData);
            }
        }
    }

    public void deleteImagesSecondByCommonIdAndIds(String commonId, List<Integer> imgIds) {
        for (Integer imgId : imgIds) {
            Optional<ImgSubSecond> imgDataOptional = imgSubSecondRepo.findByImgIDAndCommonId(imgId, commonId);
            if (imgDataOptional.isPresent()) {
                ImgSubSecond imgData = imgDataOptional.get();
                deleteImageFromS3(imgData.getFName());
                imgSubSecondRepo.delete(imgData);
            }
        }
    }

    private void deleteImageFromS3(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    public void deleteMp3ByDtId(String dtId) {
        // Delete MP3 details from the database and S3 bucket
        List<Mp3Data> existingMp3List = mp3Repo.findBydtId(dtId);
        if (!existingMp3List.isEmpty()) {
            for (Mp3Data mp3 : existingMp3List) {
                deleteFileFromS3(mp3.getFName());
                mp3Repo.delete(mp3);
            }
        } else {
            throw new EntityNotFoundException("MP3 file not found for dtId: " + dtId);
        }
    }

    public void deleteMp3ByDtIdMain(String dtId, Integer id) {
        Optional<Mp3Data>mp3DataOptional=mp3Repo.findByDtIdAndId(dtId,id);
        if (mp3DataOptional.isPresent()){
            Mp3Data mp3Data = mp3DataOptional.get();
            String fileName = mp3Data.getFName();
            deleteFileFromS3(fileName);
            mp3Repo.delete(mp3Data);
        }
    }

    public void deleteMp4MainByDtId(String dtId, Integer id) {
        Optional<Mp4Data>mp4DataOptional=mp4DataRepo.findByDtIdAndId(dtId,id);
        if (mp4DataOptional.isPresent()){
            Mp4Data mp4Data = mp4DataOptional.get();
            String fileName = mp4Data.getFName();
            String thumbnailName =mp4Data.getThumbnailName();
            deleteFileFromS3(fileName);
            deleteFileFromS3(thumbnailName);
            mp4DataRepo.delete(mp4Data);
        }
    }

    public void deleteMp3FirstSubByMainEngId(String mainEngId, Integer id) {
        Optional<Mp3Data1>mp3Data1Optional=mp3Data1Repo.findByMainEngIdAndId(mainEngId,id);
        Optional<Mp3Data1>mp3Data1Optional1=mp3Data1Repo.findByMainMalIdAndId(mainEngId,id);
        if (mp3Data1Optional.isPresent()){
            Mp3Data1 mp3Data1=mp3Data1Optional.get();
            String fileName = mp3Data1.getFName();
            deleteFileFromS3(fileName);
            mp3Data1Repo.delete(mp3Data1);
        } else if (mp3Data1Optional1.isPresent()) {
            Mp3Data1 mp3Data1=mp3Data1Optional.get();
            String fileName = mp3Data1.getFName();
            deleteFileFromS3(fileName);
            mp3Data1Repo.delete(mp3Data1);
        }
    }

    public void deleteMp4FirstSubByMain(String mainEngId, Integer id) {
        Optional<Mp4Data1>mp4Data1Optional=mp4Data1Repo.findByMainEngIdAndId(mainEngId,id);
        Optional<Mp4Data1>mp4Data1Optional1=mp4Data1Repo.findByMainMalIdAndId(mainEngId,id);
        if (mp4Data1Optional.isPresent()){
            Mp4Data1 mp4Data1 = mp4Data1Optional.get();
            String fileName = mp4Data1.getFName();
            String thumbnailName = mp4Data1.getThumbnailName();
            deleteFileFromS3(fileName);
            deleteFileFromS3(thumbnailName);
            mp4Data1Repo.delete(mp4Data1);
        } else if (mp4Data1Optional1.isPresent()) {
            Mp4Data1 mp4Data1 = mp4Data1Optional.get();
            String fileName = mp4Data1.getFName();
            String thumbnailName = mp4Data1.getThumbnailName();
            deleteFileFromS3(fileName);
            deleteFileFromS3(thumbnailName);
            mp4Data1Repo.delete(mp4Data1);
        }
    }

    public void deleteMp3SecondSubByDtId(String dtId, Integer id) {
        Optional<Mp3Data2>mp3Data2Optional=mp3Data2Repo.findByDtIdAndId(dtId, id);
        if (mp3Data2Optional.isPresent()){
            Mp3Data2 mp3Data2=mp3Data2Optional.get();
            String fileName = mp3Data2.getFName();
            deleteFileFromS3(fileName);
            mp3Data2Repo.delete(mp3Data2);
        }
    }

    public void deleteMp4SecondSubByDtId(String dtId, Integer id) {
        Optional<Mp4Data2>mp4Data2Optional=mp4Data2Repo.findByDtIdAndId(dtId,id);
        if (mp4Data2Optional.isPresent()){
            Mp4Data2 mp4Data2 = mp4Data2Optional.get();
            String fileName= mp4Data2.getFName();
            String thumbnailName =mp4Data2.getThumbnailName();
            deleteFileFromS3(fileName);
            deleteFileFromS3(thumbnailName);
            mp4Data2Repo.delete(mp4Data2);
        }
    }



    public void deleteMp4ByDtId(String dtId) {
        // Delete MP4 details from the database and S3 bucket
        List<Mp4Data> existingMp4List = mp4DataRepo.findBydtId(dtId);
        if (!existingMp4List.isEmpty()) {
            for (Mp4Data mp4 : existingMp4List) {
                deleteFileFromS3(mp4.getFName());
                deleteFileFromS3(mp4.getThumbnailName());
                mp4DataRepo.delete(mp4);
            }
        } else {
            throw new EntityNotFoundException("MP4 file not found for dtId: " + dtId);
        }
    }


    public void deleteMp3FirstByMainEngId(String mainEngId) {
        List<Mp3Data1> existingMp3List = mp3Data1Repo.findByMainEngId(mainEngId);
        for (Mp3Data1 mp3 : existingMp3List) {
            deleteFileFromS3(mp3.getFName());
            mp3Data1Repo.delete(mp3);
        }
    }

    public void deleteMp3FirstByMainMalId(String mainMalId) {
        List<Mp3Data1> existingMp3List = mp3Data1Repo.findByMainMalId(mainMalId);
        for (Mp3Data1 mp3 : existingMp3List) {
            deleteFileFromS3(mp3.getFName());
            mp3Data1Repo.delete(mp3);
        }
    }

    public void deleteMp4FirstByMainEngId(String mainEngId) {
        // Find and delete MP4 details from the database and S3 bucket
        List<Mp4Data1> existingMp4List = mp4Data1Repo.findByMainEngId(mainEngId);
        for (Mp4Data1 mp4 : existingMp4List) {
            deleteFileFromS3(mp4.getFName());
            deleteFileFromS3(mp4.getThumbnailName());
            mp4Data1Repo.delete(mp4);
        }
    }

    public void deleteMp4FirstByMainMalId(String mainMalId) {
        // Find and delete MP4 details from the database and S3 bucket
        List<Mp4Data1> existingMp4List = mp4Data1Repo.findByMainMalId(mainMalId);
        for (Mp4Data1 mp4 : existingMp4List) {
            deleteFileFromS3(mp4.getFName());
            deleteFileFromS3(mp4.getThumbnailName());
            mp4Data1Repo.delete(mp4);
        }
    }

    public void deleteMp3SecondByDtId(String dtId) {
        // Delete MP3 details from the database and S3 bucket
        List<Mp3Data2> existingMp3List = mp3Data2Repo.findBydtId(dtId);
        for (Mp3Data2 mp3 : existingMp3List) {
            deleteFileFromS3(mp3.getFName());
            mp3Data2Repo.delete(mp3);
        }
    }

    public void deleteMp4SecondByDtId(String dtId) {
        // Delete MP4 details from the database and S3 bucket
        List<Mp4Data2> existingMp4List = mp4Data2Repo.findBydtId(dtId);
        for (Mp4Data2 mp4 : existingMp4List) {
            deleteFileFromS3(mp4.getFName());
            deleteFileFromS3(mp4.getThumbnailName());
            mp4Data2Repo.delete(mp4);
        }
    }

    public void deleteFileFromS3(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            // Handle Amazon Service Exception
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Handle SDK Client Exception
            e.printStackTrace();
        }
    }



    public ResponseEntity<?> deleteMalMainTitleByUid(String uId) {
        Optional<MainTitleMal>mainTitleMalOptional = mainTitleMalRepo.findBymMalUid(uId);
        if (mainTitleMalOptional.isPresent()){
            MainTitleMal mainTitleMal = mainTitleMalOptional.get();
            List<Mp3Data>mp3DataList = mp3Repo.findBydtId(uId);
            if (!mp3DataList.isEmpty()){
                for (Mp3Data mp3Data : mp3DataList){
                    mp3Repo.delete(mp3Data);
                }
            }
            List<FirstSubMalayalam>firstSubMalayalamList = firstSubMalayalamRepo.findByMainUid(uId);
            if (!firstSubMalayalamList.isEmpty()){
                for (FirstSubMalayalam firstSubMalayalam : firstSubMalayalamList){
                    String fsUid = firstSubMalayalam.getFsUid();
                    List<Mp3Data1>mp3Data1List = mp3Data1Repo.findBydtId(fsUid);
                    if (!mp3Data1List.isEmpty()){
                        for (Mp3Data1 mp3Data1 : mp3Data1List){
                            mp3Data1Repo.delete(mp3Data1);
                        }
                    }
                    List<SecondSubMalayalam>secondSubMalayalam = secondSubMalayalamRepo.findByfsUid(fsUid);
                    if (!secondSubMalayalam.isEmpty()){
                        for (SecondSubMalayalam secondSubMalayalam1 : secondSubMalayalam){
                            String ssUid = secondSubMalayalam1.getSsUid();
                            List<Mp3Data2>mp3Data2List = mp3Data2Repo.findBydtId(ssUid);
                            if (!mp3Data2List.isEmpty()){
                                for (Mp3Data2 mp3Data2 :mp3Data2List){
                                    mp3Data2Repo.delete(mp3Data2);
                                }

                            }
                            secondSubMalayalamRepo.delete(secondSubMalayalam1);
                        }
                    }
                    firstSubMalayalamRepo.delete(firstSubMalayalam);
                }
            }
            mainTitleMalRepo.delete(mainTitleMal);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("check uId",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteEngMainTitleByUid(String uId) {
        MainTitleEng mainTitleEngOptional = mainTitleEngRepo.findBymEngUid(uId);
        if (mainTitleEngOptional!=null){
            List<Mp3Data>mp3DataList = mp3Repo.findBydtId(uId);
            if (!mp3DataList.isEmpty()){
                for (Mp3Data mp3Data : mp3DataList){
                    mp3Repo.delete(mp3Data);
                }
            }
            List<FirstSubEnglish>firstSubEnglishList = firstSubEnglishRepo.findByMainUid(uId);
            if (!firstSubEnglishList.isEmpty()){
                for (FirstSubEnglish firstSubEnglish : firstSubEnglishList){
                    String fsUid = firstSubEnglish.getFsUid();
                    List<Mp3Data1>mp3Data1List = mp3Data1Repo.findBydtId(fsUid);
                    if (!mp3Data1List.isEmpty()){
                        for (Mp3Data1 mp3Data1 : mp3Data1List){
                            mp3Data1Repo.delete(mp3Data1);
                        }
                    }
                    List<SecondSubEnglish>secondSubEnglishList =secondSubEnglishRepo.findByfsUid(fsUid);
                    if (!secondSubEnglishList.isEmpty()){
                        for (SecondSubEnglish secondSubEnglish :secondSubEnglishList){
                            String ssUid = secondSubEnglish.getSsUid();
                            List<Mp3Data2>mp3Data2List = mp3Data2Repo.findBydtId(ssUid);
                            if (!mp3Data2List.isEmpty()){
                                for (Mp3Data2 mp3Data2 :mp3Data2List){
                                    mp3Data2Repo.delete(mp3Data2);
                                }
                            }
                            secondSubEnglishRepo.delete(secondSubEnglish);
                        }
                    }
                    firstSubEnglishRepo.delete(firstSubEnglish);
                }
            }
            mainTitleEngRepo.delete(mainTitleEngOptional);
            return new ResponseEntity<>("Main title is deleted by uId",HttpStatus.OK);
        }
        return new ResponseEntity<>("uniqueId : "+uId+"  is not valid. ",HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> deleteByCommonId(String commonId) {
        try {
            // Fetch CommonIdQRCode entity
            Optional<CommonIdQRCode> commonIdQRCode = commonIdQRCodeRepo.findByCommonId(commonId);
            if (!commonIdQRCode.isPresent()) {
                throw new EntityNotFoundException("CommonIdQRCode not found for commonId: " + commonId);
            }

            CommonIdQRCode commonIdQRCode1 = commonIdQRCode.get();
            String mEngUid = commonIdQRCode1.getEngId();
            String mMalUid = commonIdQRCode1.getMalId();

            log.info("Fetching fsUids for mEngUid: {}", mEngUid);
            List<FirstSubEnglish> englishFirstSubList = firstSubEnglishRepo.findByMainUid(mEngUid);
            List<String> englishFsUids = englishFirstSubList.stream()
                    .map(FirstSubEnglish::getFsUid)
                    .collect(Collectors.toList());
            log.info("Fetched English fsUids: {}", englishFsUids);

            log.info("Fetching fsUids for mMalUid: {}", mMalUid);
            List<FirstSubMalayalam> malayalamFirstSubList = firstSubMalayalamRepo.findByMainUid(mMalUid);
            List<String> malayalamFsUids = malayalamFirstSubList.stream()
                    .map(FirstSubMalayalam::getFsUid)
                    .collect(Collectors.toList());
            log.info("Fetched Malayalam fsUids: {}", malayalamFsUids);

            // Find all ssUids from SecondSubEnglish and SecondSubMalayalam entities
            List<String> englishSsUids = new ArrayList<>();
            for (String fsUid : englishFsUids) {
                englishSsUids.addAll(secondSubEnglishRepo.findAllByFsUid(fsUid).stream()
                        .map(SecondSubEnglish::getSsUid)
                        .collect(Collectors.toList()));
            }
            log.info("English ssUids: {}", englishSsUids);

            List<String> malayalamSsUids = new ArrayList<>();
            for (String fsUid : malayalamFsUids) {
                malayalamSsUids.addAll(secondSubMalayalamRepo.findAllByFsUid(fsUid).stream()
                        .map(SecondSubMalayalam::getSsUid)
                        .collect(Collectors.toList()));
            }
            log.info("Malayalam ssUids: {}", malayalamSsUids);

            // Delete main title entities related to mEngUid and mMalUid
            mainTitleEngRepo.deleteBymEngUid(mEngUid);
            mainTitleMalRepo.deleteBymMalUid(mMalUid);

            //imgRepo.deleteByCommonId(commonId);
            deleteIfPresent(() ->deleteImagesByCommonId(commonId));

            Optional<BackgroundImg>backgroundImgOptional = backgroundImgRepo.findByEngIdAndMalId(mEngUid,mMalUid);
            if (backgroundImgOptional.isPresent()){
                BackgroundImg backgroundImg = backgroundImgOptional.get();
                if (backgroundImg.getCommonId().equals(commonId)){
                    deleteFileFromS3(backgroundImg.getBgName());
                    backgroundImgRepo.delete(backgroundImg);
                }
            }


            //imgSubFirstRepo.deleteByCommonId(commonId);
            deleteIfPresent(() ->deleteImagesFirstByCommonId(commonId));

            //imgSubSecondRepo.deleteByCommonId(commonId);
            deleteIfPresent(() ->deleteImagesSecondByCommonId(commonId));

            // Delete MP3 entities related to mEngUid and mMalUid
            //mp3Repo.deleteByDtId(mEngUid);
            deleteIfPresent(() ->deleteMp3ByDtId(mEngUid));
            //mp3Repo.deleteByDtId(mMalUid);
            deleteIfPresent(() ->deleteMp3ByDtId(mMalUid));

            // Delete MP4 entities related to mEngUid and mMalUid
            //mp4DataRepo.deleteByDtId(mEngUid);
            deleteIfPresent(() ->deleteMp4ByDtId(commonId));
            //mp4DataRepo.deleteByDtId(mMalUid);
            //deleteIfPresent(() ->deleteMp4ByDtId(mMalUid));

            // Delete first subheading entities related to mEngUid and mMalUid
            deleteIfPresent(() ->firstSubEnglishRepo.deleteByMainUid(mEngUid));
            deleteIfPresent(() ->firstSubMalayalamRepo.deleteByMainUid(mMalUid));
            List<FirstSubEnglish> firstSubEnglishList = firstSubEnglishRepo.findByMainUid(mEngUid);
            if (!firstSubEnglishList.isEmpty()){
                for (FirstSubEnglish firstSubEnglish : firstSubEnglishList){
                    String fsEngId = firstSubEnglish.getFsUid();
                    List<FirstSubMalayalam> firstSubMalayalamList = firstSubMalayalamRepo.findByMainUid(mMalUid);
                    if (!firstSubMalayalamList.isEmpty()){
                        for (FirstSubMalayalam firstSubMalayalam : firstSubMalayalamList){
                            String fsMalId = firstSubMalayalam.getFsUid();

                            Optional<CommonIdFs>commonIdFsOptional =fsCommonIdRepo.findByFsEngIdAndFsMalId(fsEngId,fsMalId);
                            if (commonIdFsOptional.isPresent()){
                                CommonIdFs commonIdFs =commonIdFsOptional.get();
                                String fsCommonId = commonIdFs.getFsCommonId();
                                deleteIfPresent(() ->deleteMp4FirstByMainMalId(fsCommonId));

                                Optional<BackgroundImg>optionalBackgroundImg = backgroundImgRepo.findByEngIdAndMalId(fsEngId,fsMalId);
                                if (optionalBackgroundImg.isPresent()){
                                    BackgroundImg backgroundImg = optionalBackgroundImg.get();
                                    if (backgroundImg.getCommonId().equals(fsCommonId)){
                                        deleteFileFromS3(backgroundImg.getBgName());
                                        backgroundImgRepo.delete(backgroundImg);
                                    }
                                }
                            }

                            List<SecondSubEnglish>secondSubEnglishList = secondSubEnglishRepo.findByfsUid(fsEngId);
                            if (!secondSubEnglishList.isEmpty()){
                                for (SecondSubEnglish secondSubEnglish :secondSubEnglishList){
                                    String ssEngId = secondSubEnglish.getSsUid();

                                    List<SecondSubMalayalam>secondSubMalayalams = secondSubMalayalamRepo.findByfsUid(fsMalId);
                                    if (!secondSubMalayalams.isEmpty()){
                                        for (SecondSubMalayalam secondSubMalayalam :secondSubMalayalams){
                                            String ssMalId = secondSubMalayalam.getSsUid();

                                            Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsMalIdAndSsEngId(ssMalId,ssEngId);
                                            if (commonIdSsOptional.isPresent()){
                                                CommonIdSs commonIdSs = commonIdSsOptional.get();
                                                String ssCommonId = commonIdSs.getSsCommonId();

                                                deleteIfPresent(() -> {
                                                    deleteMp4SecondByDtId(ssCommonId);
                                                });

                                                Optional<BackgroundImg>backgroundImgOptional1 =backgroundImgRepo.findByEngIdAndMalId(ssEngId,ssMalId);
                                                if (backgroundImgOptional1.isPresent()){
                                                    BackgroundImg backgroundImg = backgroundImgOptional1.get();
                                                    if (backgroundImg.getCommonId().equals(ssCommonId)){
                                                        deleteFileFromS3(backgroundImg.getBgName());
                                                        backgroundImgRepo.delete(backgroundImg);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //mp3Data1Repo.deleteByMainEngId(mEngUid);
            //mp3Data1Repo.deleteByMainMalId(mMalUid);

            deleteIfPresent(() ->deleteMp3FirstByMainEngId(mEngUid));
            deleteIfPresent(() ->deleteMp3FirstByMainMalId(mMalUid));


            // Delete MP4 entities related to mEngUid and mMalUid
//        mp4Data1Repo.deleteByMainEngId(mEngUid);
//        mp4Data1Repo.deleteByMainMalId(mMalUid);
            //deleteIfPresent(() ->deleteMp4FirstByMainEngId(mEngUid));



            // Delete second subheading entities related to each fsUid
            for (String fsUid : englishFsUids) {
                deleteIfPresent(() ->secondSubEnglishRepo.deleteByFsUid(fsUid));
            }

            for (String fsUid : malayalamFsUids) {
                deleteIfPresent(() ->secondSubMalayalamRepo.deleteByFsUid(fsUid));
            }

            // Delete mp3Data2 and mp4Data2 entities related to each ssUid
            for (String ssUid : englishSsUids) {
                //mp3Data2Repo.deleteByDtId(ssUid);
                deleteIfPresent(() ->deleteMp3SecondByDtId(ssUid));
                //mp4Data2Repo.deleteByDtId(ssUid);
//                deleteIfPresent(() -> {
//                    deleteMp4SecondByDtId(ssUid);
//                });
            }

            for (String ssUid : malayalamSsUids) {
                //mp3Data2Repo.deleteByDtId(ssUid);
                deleteIfPresent(() ->deleteMp3SecondByDtId(ssUid));
                //mp4Data2Repo.deleteByDtId(ssUid);
                //deleteIfPresent(() ->deleteMp4SecondByDtId(ssUid));
            }

            // Delete QR code from S3
            String qrCodeName = commonIdQRCode1.getFName();
            if (qrCodeName != null && !qrCodeName.isEmpty()) {
                deleteImageFromS3(qrCodeName);
            }
            // Finally, delete the CommonIdQRCode entity
            commonIdQRCodeRepo.deleteByCommonId(commonId);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    public ResponseEntity<?> deleteThumbnailMain(String commonId, Integer tId) {
        Optional<Mp4Data>mp4DataOptional=mp4DataRepo.findByDtIdAndId(commonId,tId);
        if (mp4DataOptional.isPresent()){
            Mp4Data mp4Data = mp4DataOptional.get();
            String thumbnailName = mp4Data.getThumbnailName();
            deleteFileFromS3(thumbnailName);

            mp4Data.setThumbnailName(null);
            mp4Data.setThumbnailUrl(null);

            mp4DataRepo.save(mp4Data);
            return new ResponseEntity<>("Thumbnail is deleted successfully  "+thumbnailName,HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteThumbnailFirst(String commonId, Integer tId) {
        Optional<Mp4Data1>mp4Data1Optional=mp4Data1Repo.findByDtIdAndId(commonId,tId);
        if (mp4Data1Optional.isPresent()){
            Mp4Data1 mp4Data1 = mp4Data1Optional.get();
            String thumbnailName = mp4Data1.getThumbnailName();
            deleteFileFromS3(thumbnailName);

            mp4Data1.setThumbnailName(null);
            mp4Data1.setThumbnailUrl(null);

            mp4Data1Repo.save(mp4Data1);
            return new ResponseEntity<>("Thumbnail is deleted successfully  "+thumbnailName,HttpStatus.OK);
        }return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteThumbnailSecond(String commonId, Integer tId) {
        Optional<Mp4Data2>mp4Data2Optional=mp4Data2Repo.findByDtIdAndId(commonId,tId);
        if (mp4Data2Optional.isPresent()){
            Mp4Data2 mp4Data2 = mp4Data2Optional.get();
            String thumbnailName = mp4Data2.getThumbnailName();
            deleteFileFromS3(thumbnailName);

            mp4Data2.setThumbnailUrl(null);
            mp4Data2.setThumbnailName(null);

            mp4Data2Repo.save(mp4Data2);
            return new ResponseEntity<>("Thumbnail is deleted successfully  "+thumbnailName,HttpStatus.OK);
        }return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
    }
}
