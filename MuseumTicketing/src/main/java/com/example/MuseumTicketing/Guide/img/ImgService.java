package com.example.MuseumTicketing.Guide.img;

import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
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
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
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
import com.example.MuseumTicketing.Guide.util.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @Autowired
    private S3Service s3Service;



    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());

        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }
    public ImgData uploadJPG(MultipartFile file, String englishUId, String malUid, String commonId) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        //Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        ImgData imgData = new ImgData(fileName,fileUrl,englishUId,malUid,commonId);
        imgRepo.save(imgData);
        return imgData;
    }



    public ImgSubFirst uploadData1(MultipartFile file, String englishUId, String malUid, String commonId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        ImgSubFirst imgSubFirst1 = new ImgSubFirst(fileName,fileUrl,commonId);
        Optional<FirstSubEnglish> firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(englishUId);
        if (firstSubEnglishOptional.isPresent()){
            FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
            imgSubFirst1.setMainEngUid(firstSubEnglish.getMainUid());
            imgSubFirst1.setEngId(englishUId);
        }
        Optional<FirstSubMalayalam> firstSubMalayalamOptional = firstSubMalayalamRepo.findByfsUid(malUid);
        if (firstSubMalayalamOptional.isPresent()){
            FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
            imgSubFirst1.setMainMalUid(firstSubMalayalam.getMainUid());
            imgSubFirst1.setMalId(malUid);
        }
        imgSubFirstRepo.save(imgSubFirst1);
        return imgSubFirst1;
    }

    public ImgSubSecond uploadData2(MultipartFile file, String englishUId, String malUid, String commonId) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =  System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        ImgSubSecond imgSubSecond1 = new ImgSubSecond(fileName,fileUrl,commonId);
        Optional<SecondSubEnglish> secondSubEnglishOptional = secondSubEnglishRepo.findByssUid(englishUId);
        if (secondSubEnglishOptional.isPresent()){
            SecondSubEnglish secondSubEnglish = secondSubEnglishOptional.get();
            imgSubSecond1.setFsEngUid(secondSubEnglish.getFsUid());
            imgSubSecond1.setEngId(englishUId);
        }
        Optional<SecondSubMalayalam> secondSubMalayalamOptional = secondSubMalayalamRepo.findByssUid(malUid);
        if (secondSubMalayalamOptional.isPresent()){
            SecondSubMalayalam secondSubMalayalam = secondSubMalayalamOptional.get();
            imgSubSecond1.setFsMalUid(secondSubMalayalam.getFsUid());
            imgSubSecond1.setMalId(malUid);
        }
        imgSubSecondRepo.save(imgSubSecond1);
        return imgSubSecond1;
    }




    public ImgData updateMainJPG(MultipartFile file, Integer imgId, String commonId)throws IOException {
        try {
            Optional<ImgData> existingImgDataOptional = imgRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgData imgData = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                // Use the S3Service's uploadLargeFile method to upload the file
                s3Service.uploadLargeFile(fileName, fileObj);
                fileObj.delete();
                //String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                // Retrieve the file URL from S3
                String fileUrl = s3Service.getFileUrl(fileName);
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
        return new ImgData( "No Data", "No Data", "No Data","No Data","No Data");
    }



    public ImgSubFirst updateFirstSubJPG(MultipartFile file, Integer imgId, String commonId) throws IOException{
        try {
            Optional<ImgSubFirst> existingImgDataOptional = imgSubFirstRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgSubFirst imgSubFirst = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                // Use the S3Service's uploadLargeFile method to upload the file
                s3Service.uploadLargeFile(fileName, fileObj);
                fileObj.delete();
                //String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                // Retrieve the file URL from S3
                String fileUrl = s3Service.getFileUrl(fileName);
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
        return new ImgSubFirst("No Data", "No Data", "No Data");
    }

    public ImgSubSecond updateSecondSubJPG(MultipartFile file, Integer imgId, String commonId) throws IOException{
        try {
            Optional<ImgSubSecond> existingImgDataOptional = imgSubSecondRepo.findById(imgId);
            if (existingImgDataOptional.isPresent() && !file.isEmpty()) {
                ImgSubSecond imgSubSecond = existingImgDataOptional.get();

                // Convert file and upload to S3
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                // Use the S3Service's uploadLargeFile method to upload the file
                s3Service.uploadLargeFile(fileName, fileObj);
                fileObj.delete();
                //String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
                // Retrieve the file URL from S3
                String fileUrl = s3Service.getFileUrl(fileName);
                // Update existing ImgSubSecond with new file info
                imgSubSecond.setFName(fileName);
                imgSubSecond.setFUrl(fileUrl);

                Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
                if (commonIdSsOptional.isPresent()){
                    CommonIdSs commonIdSs = commonIdSsOptional.get();
                    imgSubSecond.setCommonId(commonId);
                }


                imgSubSecondRepo.save(imgSubSecond);  // Save the updated entity
                return imgSubSecond;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImgSubSecond( "No Data", "No Data", "No Data");
    }



}
