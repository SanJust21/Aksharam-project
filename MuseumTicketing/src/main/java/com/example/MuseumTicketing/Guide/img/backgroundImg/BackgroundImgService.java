package com.example.MuseumTicketing.Guide.img.backgroundImg;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

@Service
@Slf4j
public class BackgroundImgService {
    @Autowired
    private BackgroundImgRepo backgroundImgRepo;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;
    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());

        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

    public ResponseEntity<?> uploadBgImageMain(String engId, String malId, MultipartFile bgFile,String commonId) {
        try {
            File fileObj2 = convertMultiPartFileToFile(bgFile);
            String bgFileName = System.currentTimeMillis()+"_"+bgFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
            fileObj2.delete();
            String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();
            BackgroundImg backgroundImg = new BackgroundImg(bgFileName,bgFileUrl,commonId);
            MainTitleEng mainTitleEngOptional=mainTitleEngRepo.findBymEngUid(engId);
            if (!mainTitleEngOptional.getMEngId().equals(engId)){
                backgroundImg.setEngId(mainTitleEngOptional.getMEngUid());
            }else {
                return new ResponseEntity<>("EnglishUid isn't valid. Generate EnglishUId",HttpStatus.BAD_REQUEST);
            }
            Optional<MainTitleMal>mainTitleMalOptional = mainTitleMalRepo.findBymMalUid(malId);
            if (mainTitleMalOptional.isPresent()){
                backgroundImg.setMalId(malId);
            }else {
                return new ResponseEntity<>("MalayalamUid isn't valid. Generate MalayalamUId",HttpStatus.BAD_REQUEST);
            }
            backgroundImgRepo.save(backgroundImg);
            return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> uploadBgImageFirst(String engId, String malId, String commonId, MultipartFile bgFile) {
        try {
            File fileObj2 = convertMultiPartFileToFile(bgFile);
            String bgFileName = System.currentTimeMillis()+"_"+bgFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
            fileObj2.delete();
            String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();
            BackgroundImg backgroundImg = new BackgroundImg(bgFileName,bgFileUrl,commonId);
            Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(engId);
            if (firstSubEnglishOptional.isPresent()){
                backgroundImg.setEngId(engId);
            }else {
                return new ResponseEntity<>("EnglishUid isn't valid. Generate EnglishUId",HttpStatus.BAD_REQUEST);
            }
            Optional<FirstSubMalayalam>firstSubMalayalamOptional = firstSubMalayalamRepo.findByFsUid(malId);
            if (firstSubMalayalamOptional.isPresent()){
                backgroundImg.setMalId(malId);
            }else {
                return new ResponseEntity<>("MalayalamUid isn't valid. Generate MalayalamUId",HttpStatus.BAD_REQUEST);
            }
            backgroundImgRepo.save(backgroundImg);
            return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> uploadBgImageSecond(String engId, String malId, MultipartFile bgFile, String commonId) {
        try {
            File fileObj2 = convertMultiPartFileToFile(bgFile);
            String bgFileName = System.currentTimeMillis()+"_"+bgFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
            fileObj2.delete();
            String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();
            BackgroundImg backgroundImg = new BackgroundImg(bgFileName,bgFileUrl,commonId);
            Optional<SecondSubEnglish>secondSubEnglishOptional= secondSubEnglishRepo.findByssUid(engId);
            if (secondSubEnglishOptional.isPresent()){
                SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
                backgroundImg.setEngId(secondSubEnglish.getSsUid());
            }else {
                return new ResponseEntity<>("EnglishUid isn't valid. Generate EnglishUId",HttpStatus.BAD_REQUEST);
            }
            Optional<SecondSubMalayalam>secondSubMalayalamOptional = secondSubMalayalamRepo.findByssUid(malId);
            if (secondSubMalayalamOptional.isPresent()){

                backgroundImg.setMalId(malId);
            }else {
                return new ResponseEntity<>("MalayalamUid isn't valid. Generate MalayalamUId",HttpStatus.BAD_REQUEST);
            }
            backgroundImgRepo.save(backgroundImg);
            return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateBgImageMain(String engId, String malId, MultipartFile bgFile, String commonId) {
        try {
            Optional<BackgroundImg> existingData = backgroundImgRepo.findByEngIdAndMalId(engId,malId);
            if (existingData.isPresent()){
                BackgroundImg backgroundImg = existingData.get();
                if (backgroundImg.getCommonId().equals(commonId)){
                    File fileObj = convertMultiPartFileToFile(bgFile);
                    String fileName = System.currentTimeMillis() + "_" + bgFile.getOriginalFilename();
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                    fileObj.delete();
                    String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                    backgroundImg.setBgName(fileName);
                    backgroundImg.setBgUrl(fileUrl);
                    backgroundImgRepo.save(backgroundImg);
                    return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("No matching Data is found",HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateBgImageFirst(String engId, String malId, String commonId, MultipartFile bgFile) {
        try {
            Optional<BackgroundImg> existingData = backgroundImgRepo.findByEngIdAndMalId(engId,malId);
            if (existingData.isPresent()){
                BackgroundImg backgroundImg = existingData.get();
                if (backgroundImg.getCommonId().equals(commonId)){
                    File fileObj = convertMultiPartFileToFile(bgFile);
                    String fileName = System.currentTimeMillis() + "_" + bgFile.getOriginalFilename();
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                    fileObj.delete();
                    String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                    backgroundImg.setBgName(fileName);
                    backgroundImg.setBgUrl(fileUrl);
                    backgroundImgRepo.save(backgroundImg);
                    return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("No matching Data is found",HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateBgImageSecond(String engId, String malId, MultipartFile bgFile, String commonId) {
        try {
            Optional<BackgroundImg> existingData = backgroundImgRepo.findByEngIdAndMalId(engId,malId);
            if (existingData.isPresent()){
                BackgroundImg backgroundImg = existingData.get();
                if (backgroundImg.getCommonId().equals(commonId)){
                    File fileObj = convertMultiPartFileToFile(bgFile);
                    String fileName = System.currentTimeMillis() + "_" + bgFile.getOriginalFilename();
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                    fileObj.delete();
                    String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                    backgroundImg.setBgName(fileName);
                    backgroundImg.setBgUrl(fileUrl);
                    backgroundImgRepo.save(backgroundImg);
                    return new ResponseEntity<>(backgroundImg,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("No matching Data is found",HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void deleteImageFromS3(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }


    public int deleteBgImageMain(String engId, String malId, Integer imgId, String commonId) {
        Optional<BackgroundImg>backgroundImgOptional = backgroundImgRepo.findByCommonIdAndId(commonId,imgId);
        if (backgroundImgOptional.isPresent()){
            BackgroundImg backgroundImg = backgroundImgOptional.get();
            if (backgroundImg.getMalId().equals(malId)&&backgroundImg.getEngId().equals(engId)){
                deleteImageFromS3(backgroundImg.getBgName());
                backgroundImgRepo.delete(backgroundImg);
                return 1;
            }return -1;
        }
        return 0;
    }

    public int deleteBgImageFirst(String engId, String malId, Integer imgId, String commonId) {
        Optional<BackgroundImg>backgroundImgOptional = backgroundImgRepo.findByCommonIdAndId(commonId, imgId);
        if (backgroundImgOptional.isPresent()){
            BackgroundImg backgroundImg = backgroundImgOptional.get();
            if (backgroundImg.getEngId().equals(engId)&& backgroundImg.getMalId().equals(malId)){
                deleteImageFromS3(backgroundImg.getBgName());
                backgroundImgRepo.delete(backgroundImg);
                return 1;
            }return -1;
        }return 0;
    }

    public int deleteBgImageSecond(String engId, String malId, Integer imgId, String commonId) {
        Optional<BackgroundImg>backgroundImgOptional =backgroundImgRepo.findByCommonIdAndId(commonId,imgId);
        if (backgroundImgOptional.isPresent()){
            BackgroundImg backgroundImg = backgroundImgOptional.get();
            if (backgroundImg.getMalId().equals(malId)&&backgroundImg.getEngId().equals(engId)){
                deleteImageFromS3(backgroundImg.getBgName());
                backgroundImgRepo.delete(backgroundImg);
                return 1;
            }return -1;
        }return 0;
    }


//    public int deleteBgImageSecond(String engId, String malId, Integer imgId) {
//        Optional<BackgroundImg> backgroundImgOptional = backgroundImgRepo.findByEngIdAndMalId(engId,malId);
//        if (backgroundImgOptional.isPresent()) {
//            BackgroundImg backgroundImg = backgroundImgOptional.get();
//            if (backgroundImg.getId().equals(imgId)){
//                deleteImageFromS3(backgroundImg.getBgName());
//                backgroundImgRepo.delete(backgroundImg);
//                return 1;
//            }
//           return -1;
//        }
//        return 0;
//    }


}
