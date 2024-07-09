package com.example.MuseumTicketing.Guide.img;

import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
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
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

@Service
@Slf4j
public class ImgService {
    @Autowired
    private ImgRepo imgRepo;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private ImgSubFirstRepo imgSubFirstRepo;

    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;

    @Autowired
    private ImgSubFirst imgSubFirst;

    @Autowired
    private ImgSubSecond imgSubSecond;

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
    public ImgData uploadJPG(MultipartFile file, String englishUId, String malUid, String commonId, MultipartFile bg) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();

        File fileObj2 = convertMultiPartFileToFile(bg);
        String bgFileName = System.currentTimeMillis()+"_"+bg.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
        fileObj2.delete();
        String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();

        ImgData imgData = new ImgData(fileName,fileUrl,bgFileName,bgFileUrl,englishUId,malUid,commonId);
        imgRepo.save(imgData);
        return imgData;
    }


//    public ImgSubFirst uploadData1(MultipartFile file, String englishUId, String malUid, String commonId) {
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        fileObj.delete();
//        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//
//        ImgSubFirst imgSubFirst = new ImgSubFirst(fileName,fileUrl,englishUId,malUid, commonId);
//        Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(englishUId);
//        if (firstSubEnglishOptional.isPresent()){
//            FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
//            String uId = firstSubEnglish.getMainUid();
//            imgSubFirst.setMainEngUid(uId);
//
//        }else {
//            log.info(" Eng id is null");
//        }
//        Optional<FirstSubMalayalam> firstSubMalayalamOptional =firstSubMalayalamRepo.findByfsUid(malUid);
//        if (firstSubMalayalamOptional.isPresent()){
//            FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
//            String uId = firstSubMalayalam.getMainUid();
//            imgSubFirst.setMainMalUid(uId);
//
//        }else {
//            log.info("Mal id is null");
//        }
//
//
//        imgSubFirstRepo.save(imgSubFirst);
//        return imgSubFirst;
//    }

    public ImgSubFirst uploadData1(MultipartFile file, String englishUId, String malUid, String commonId, MultipartFile bg) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();

        File fileObj2 = convertMultiPartFileToFile(bg);
        String bgFileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
        fileObj2.delete();
        String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();
        ImgSubFirst imgSubFirst1 = new ImgSubFirst(fileName,fileUrl,bgFileName,bgFileUrl,englishUId,malUid,commonId);
        imgSubFirstRepo.save(imgSubFirst1);
        return imgSubFirst1;
    }

    public ImgSubSecond uploadData2(MultipartFile file, String englishUId, String malUid, String commonId, MultipartFile bg) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =  System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();

        File fileObj2 = convertMultiPartFileToFile(bg);
        String bgFileName = System.currentTimeMillis()+"_"+bg.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,bgFileName,fileObj2));
        fileObj2.delete();
        String bgFileUrl = s3Client.getUrl(bucketName,bgFileName).toString();
        ImgSubSecond imgSubSecond1 = new ImgSubSecond(fileName,fileUrl,bgFileName,bgFileUrl,englishUId,malUid,commonId);
        imgSubSecondRepo.save(imgSubSecond1);
        return imgSubSecond1;
    }

//    public ImgSubSecond uploadData2(MultipartFile file, String englishUId, String malUid, String commonId) {
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        fileObj.delete();
//        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//        ImgSubSecond imgSubSecond = new ImgSubSecond(fileName,fileUrl,englishUId,malUid,commonId);
//
//        Optional<SecondSubEnglish> secondSubEnglishOptional = secondSubEnglishRepo.findByssUid(englishUId);
//        if (secondSubEnglishOptional.isPresent()){
//            SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
//            String uId = secondSubEnglish.getFsUid();
//            imgSubSecond.setFsEngUid(uId);
//
//        }else {
//            log.info(" Eng id is null");
//        }
//        Optional<SecondSubMalayalam> secondSubMalayalamOptional = secondSubMalayalamRepo.findByssUid(malUid);
//        if (secondSubMalayalamOptional.isPresent()){
//            SecondSubMalayalam secondSubMalayalam = secondSubMalayalamOptional.get();
//            String uId = secondSubMalayalam.getFsUid();
//            imgSubSecond.setFsMalUid(uId);
//        }else {
//            log.info("Mal id is null");
//        }
//        imgSubSecondRepo.save(imgSubSecond);
//        return imgSubSecond;
//    }

//
//    public ImgData updateMainJPG(MultipartFile file, String englishUId, String malUid) {
//        try {
//            List<ImgData> existingImgData = imgRepo.findByengId(englishUId);
//            if (!file.isEmpty()){
//                File fileObj = convertMultiPartFileToFile(file);
//                String fileName = System.currentTimeMillis() +"_"+file.getOriginalFilename();
//                s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//                fileObj.delete();
//                String fileUrl = s3Client.getUrl(bucketName,fileName).toString() ;
////                for (ImgData imgData : existingImgData){
////                    imgData.setFUrl(fileUrl);
////                    imgData.setFName(fileName);
////                }
////                imgRepo.saveAll(existingImgData);
////                return new ImgData(fileName,fileUrl,englishUId,malUid);
//
//                ImgData imgData = new ImgData(fileName,fileUrl,englishUId,malUid);
//                imgRepo.save(imgData);
//                return imgData;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ImgData("No Data","No Data","No Data","No Data");
//    }


    public ImgData updateMainJPG(MultipartFile file, Integer imgId, String commonId) {
        try {
            Optional<ImgData> existingImgDataOptional = imgRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgData imgData = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing ImgData with new file info
                imgData.setFName(fileName);
                imgData.setFUrl(fileUrl);
                imgData.setCommonId(commonId);

                imgRepo.save(imgData);  // Save the updated entity
                return imgData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImgData("No Data", "No Data", "No Data", "No Data", "No Data","No Data","No Data");
    }


//    public ImgSubFirst updateFirstSubJPG(MultipartFile file, String englishUId, String malUid) {
//        try {
//            List<ImgSubFirst> existingImgData = imgSubFirstRepo.findByengId(englishUId);
//            if (!file.isEmpty()){
//                File fileObj = convertMultiPartFileToFile(file);
//                String fileName = System.currentTimeMillis() +"_"+file.getOriginalFilename();
//                s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//                fileObj.delete();
//                String fileUrl = s3Client.getUrl(bucketName,fileName).toString() ;
//
//                ImgSubFirst imgSubFirst = new ImgSubFirst(fileName,fileUrl,englishUId,malUid);
//                Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(englishUId);
//                if (firstSubEnglishOptional.isPresent()){
//                    FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
//                    String uId = firstSubEnglish.getMainUid();
//                    imgSubFirst.setMainEngUid(uId);
//                }else {
//                    log.info(" Eng id is null");
//                }
//                Optional<FirstSubMalayalam> firstSubMalayalamOptional =firstSubMalayalamRepo.findByfsUid(malUid);
//                if (firstSubMalayalamOptional.isPresent()){
//                    FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
//                    String uId = firstSubMalayalam.getMainUid();
//                    imgSubFirst.setMainMalUid(uId);
//                }else {
//                    log.info("Mal id is null");
//                }
//                imgSubFirstRepo.save(imgSubFirst);
//                return imgSubFirst;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ImgSubFirst("No Data","No Data","No Data","No Data");
//    }


    public ImgSubFirst updateFirstSubJPG(MultipartFile file, Integer imgId, String commonId) {
        try {
            Optional<ImgSubFirst> existingImgDataOptional = imgSubFirstRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgSubFirst imgSubFirst = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing ImgSubFirst with new file info
                imgSubFirst.setFName(fileName);
                imgSubFirst.setFUrl(fileUrl);
                imgSubFirst.setCommonId(commonId);

                imgSubFirstRepo.save(imgSubFirst);  // Save the updated entity
                return imgSubFirst;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImgSubFirst("No Data", "No Data", "No Data", "No Data", "No Data","No Data","No Data");
    }


////    public ImgSubSecond updateSecondSubJPG(MultipartFile file, String englishUId, String malUid) {
////        try {
////            List<ImgSubSecond> existingImgData = imgSubSecondRepo.findByengId(englishUId);
////            if (!file.isEmpty()){
////                File fileObj = convertMultiPartFileToFile(file);
////                String fileName = System.currentTimeMillis() +"_"+file.getOriginalFilename();
////                s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
////                fileObj.delete();
////                String fileUrl = s3Client.getUrl(bucketName,fileName).toString() ;
////
////                ImgSubSecond imgSubSecond1 = new ImgSubSecond(fileName,fileUrl,englishUId,malUid);
////                Optional<SecondSubEnglish> secondSubEnglishOptional = secondSubEnglishRepo.findByssUid(englishUId);
////                if (secondSubEnglishOptional.isPresent()){
////                    SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
////                    String uId = secondSubEnglish.getFsUid();
////                    imgSubSecond1.setFsEngUid(uId);
////                }else {
////                    log.info(" Eng id is null");
////                }
////
////                Optional<SecondSubMalayalam> secondSubMalayalamOptional =secondSubMalayalamRepo.findByssUid(malUid);
////                if (secondSubMalayalamOptional.isPresent()){
////                    SecondSubMalayalam secondSubMalayalam = secondSubMalayalamOptional.get();
////                    String uId = secondSubMalayalam.getFsUid();
////                    imgSubSecond.setFsMalUid(uId);
////                }else {
////                    log.info("Mal id is null");
////                }
////                imgSubSecondRepo.save(imgSubSecond);
////                return imgSubSecond;
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////        return new ImgSubSecond("No Data","No Data","No Data","No Data");
////    }
//
//    public ImgSubSecond updateSecondSubJPG(MultipartFile file, String englishUId, String malUid) {
//        try {
//            if (!file.isEmpty()) {
//                File fileObj = convertMultiPartFileToFile(file);
//                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
//                fileObj.delete();
//                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
//
//                // Retrieve all existing ImgSubSecond objects
//                List<ImgSubSecond> existingImgSubSecondList = imgSubSecondRepo.findByEngIdAndMalId(englishUId, malUid);
//
//                for (ImgSubSecond imgSubSecond : existingImgSubSecondList) {
//                    // Update each existing object
//                    imgSubSecond.setFName(fileName);
//                    imgSubSecond.setFUrl(fileUrl);
//
//                    Optional<SecondSubEnglish> secondSubEnglishOptional = secondSubEnglishRepo.findByssUid(englishUId);
//                    if (secondSubEnglishOptional.isPresent()) {
//                        SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
//                        String uId = secondSubEnglish.getFsUid();
//                        imgSubSecond.setFsEngUid(uId);
//                    } else {
//                        log.info("Eng id is null");
//                    }
//
//                    Optional<SecondSubMalayalam> secondSubMalayalamOptional = secondSubMalayalamRepo.findByssUid(malUid);
//                    if (secondSubMalayalamOptional.isPresent()) {
//                        SecondSubMalayalam secondSubMalayalam = secondSubMalayalamOptional.get();
//                        String uId = secondSubMalayalam.getFsUid();
//                        imgSubSecond.setFsMalUid(uId);
//                    } else {
//                        log.info("Mal id is null");
//                    }
//
//                    imgSubSecondRepo.save(imgSubSecond);
//                }
//
//                // Return the first updated object for simplicity
//                if (!existingImgSubSecondList.isEmpty()) {
//                    return existingImgSubSecondList.get(0);
//                } else {
//                    // Create a new object if none exist
//                    ImgSubSecond newImgSubSecond = new ImgSubSecond(fileName, fileUrl, englishUId, malUid);
//                    imgSubSecondRepo.save(newImgSubSecond);
//                    return newImgSubSecond;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ImgSubSecond("No Data", "No Data", "No Data", "No Data");
//    }

    public ImgSubSecond updateSecondSubJPG(MultipartFile file, Integer imgId, String commonId) {
        try {
            Optional<ImgSubSecond> existingImgDataOptional = imgSubSecondRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgSubSecond imgSubSecond = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing ImgSubSecond with new file info
                imgSubSecond.setFName(fileName);
                imgSubSecond.setFUrl(fileUrl);
                imgSubSecond.setCommonId(commonId);

                imgSubSecondRepo.save(imgSubSecond);  // Save the updated entity
                return imgSubSecond;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImgSubSecond("No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data");
    }

}
