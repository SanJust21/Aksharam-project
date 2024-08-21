package com.example.MuseumTicketing.appGuide.img;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.Guide.util.S3Service;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirstRepo;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMainRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImgMobDataService {
    @Autowired
    private ImgDataMainRepo imgDataMainRepo;
    @Autowired
    private ImgDataFirstRepo imgDataFirstRepo;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private MainTopicEngRepo mainTopicEngRepo;
    @Autowired
    private MainTopicMalRepo mainTopicMalRepo;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;
    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private ErrorService errorService;

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

    public ImgDataMain uploadJPGMain(MultipartFile file, String engId, String malId, String commonId, String imageName, String imageUrl) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        ImgDataMain imgDataMain = new ImgDataMain(fileName,fileUrl,commonId,imageName,imageUrl);
        Optional<MainTopicEng> mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(engId);
        if (mainTopicEngOptional.isPresent()){
            imgDataMain.setEngId(engId);
        }else {
            imgDataMain.setEngId("NoData");
        }
        Optional<MainTopicMal> mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(malId);
        if (mainTopicMalOptional.isPresent()){
            MainTopicMal mainTitleMal = mainTopicMalOptional.get();
            if (mainTitleMal.getMMalUid().equals(malId)){
                imgDataMain.setMalId(malId);
            }else {
                imgDataMain.setMalId("No Data");
            }
        }
        imgDataMainRepo.save(imgDataMain);
        return imgDataMain;
    }

    public ImgDataFirst uploadJPGFirst(MultipartFile file, String engId, String malId, String commonId, String imageName, String imageUrl) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        ImgDataFirst imgDataFirst = new ImgDataFirst(fileName,fileUrl,commonId,imageName,imageUrl);
        Optional<FirstTopicEng> firstTopicEngOptional = firstTopicEngRepo.findByfsUid(engId);
        if (firstTopicEngOptional.isPresent()){
            FirstTopicEng firstTopicEng = firstTopicEngOptional.get();
            if (firstTopicEng.getFsUid().equals(engId)){
                imgDataFirst.setEngId(engId);
                imgDataFirst.setMainEngUid(firstTopicEng.getMainUid());
            }else {
                log.info("checkId");
            }
        }else {
            imgDataFirst.setMainEngUid("No Data");
            imgDataFirst.setEngId("No Data");
        }
        Optional<FirstTopicMal>firstTopicMalOptional = firstTopicMalRepo.findByFsUid(malId);
        if (firstTopicMalOptional.isPresent()){
            FirstTopicMal firstSubMalayalam = firstTopicMalOptional.get();
            imgDataFirst.setMalId(malId);
            imgDataFirst.setMainMalUid(firstSubMalayalam.getMainUid());
        }else {
            imgDataFirst.setMalId("No Data");
            imgDataFirst.setMainMalUid("No Data");
        }
        imgDataFirstRepo.save(imgDataFirst);
        return imgDataFirst;
    }

    public ImgDataMain updateMainJPG(MultipartFile file, Integer imgId, String commonId, String imageName, String imageUrl) throws IOException{
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
        Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId =commonQRParaIdOptional.get();
            if (commonQRParaId.getCommonId().equals(commonId)){
                Optional<ImgDataMain> existingImgDataOptional = imgDataMainRepo.findById(imgId);
                if (existingImgDataOptional.isPresent()&& !file.isEmpty()){
                    ImgDataMain imgDataMain = existingImgDataOptional.get();
                    imgDataMain.setFName(fileName);
                    imgDataMain.setFUrl(fileUrl);
                    imgDataMain.setImageName(imageName);
                    imgDataMain.setImageRefUrl(imageUrl);
                    imgDataMainRepo.save(imgDataMain);  // Save the updated entity
                    return imgDataMain;
                }
            }
        }
        return new ImgDataMain();
    }

    public ImgDataFirst updateFirstJPG(MultipartFile file, Integer imgId, String commonId, String imageName, String imageUrl) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
        if (subComIdOptional.isPresent()){
            SubComId subComId = subComIdOptional.get();
            if (subComId.getFsCommonId().equals(commonId)){
                Optional<ImgDataFirst> existingImgDataOptional = imgDataFirstRepo.findById(imgId);
                if (existingImgDataOptional.isPresent()&& !file.isEmpty()){
                    ImgDataFirst imgDataFirst = existingImgDataOptional.get();
                    imgDataFirst.setFName(fileName);
                    imgDataFirst.setFUrl(fileUrl);
                    imgDataFirst.setImageName(imageName);
                    imgDataFirst.setImageRefUrl(imageUrl);
                    imgDataFirstRepo.save(imgDataFirst);
                    return imgDataFirst;
                }
            }
        }
        return new ImgDataFirst();
    }

    public int deleteImagesByCommonId(String commonId) {
        Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
        Optional<SubComId>subComIdOptional =subComIdRepo.findByFsCommonId(commonId);
        if (commonQRParaIdOptional.isPresent()){
            List<ImgDataMain> existingImgDataList = imgDataMainRepo.findByCommonId(commonId);
            for (ImgDataMain imgDataMain : existingImgDataList) {
                String fileName = imgDataMain.getFName();
                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                imgDataMainRepo.delete(imgDataMain);
            }
            return 1;
        } else if (subComIdOptional.isPresent()) {
            List<ImgDataFirst> existingImgDataList = imgDataFirstRepo.findByCommonId(commonId);
            for (ImgDataFirst imgDataFirst : existingImgDataList) {
                String fileName = imgDataFirst.getFName();
                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                imgDataFirstRepo.delete(imgDataFirst);
            }
            return 1;
        }else {
            return 0;
        }
    }

    public int deleteImagesByCommonIdAndIds(String commonId, List<Integer> imgIds) {
        Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
        Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
            if (commonQRParaId.getCommonId().equals(commonId)){
                for (Integer imgId : imgIds) {
                    Optional<ImgDataMain> imgDataMainOptional = imgDataMainRepo.findByIdAndCommonId(imgId, commonId);
                    if (imgDataMainOptional.isPresent()) {
                        ImgDataMain imgDataMain = imgDataMainOptional.get();
                        String fileName = imgDataMain.getFName();;
                        s3Client.deleteObject(bucketName,fileName);
                        imgDataMainRepo.delete(imgDataMain);
                        return 1;
                    }
                }
            }
        } else if (subComIdOptional.isPresent()) {
            SubComId subComId = subComIdOptional.get();
            if (subComId.getFsCommonId().equals(commonId)){
                for (Integer imgId : imgIds) {
                    Optional<ImgDataFirst> imgDataFirstOptional = imgDataFirstRepo.findByIdAndCommonId(imgId, commonId);
                    if (imgDataFirstOptional.isPresent()) {
                        ImgDataFirst imgDataFirst = imgDataFirstOptional.get();
                        String fileName = imgDataFirst.getFName();;
                        s3Client.deleteObject(bucketName,fileName);
                        imgDataFirstRepo.delete(imgDataFirst);
                        return 1;
                    }
                }
            }
        }return 0;
    }

    public ResponseEntity<?> updateNameAndUrl(String commonId, Integer id, String imgName, String imgUrl) {
        Optional<ImgDataMain>imgDataMainOptional=imgDataMainRepo.findByIdAndCommonId(id,commonId);
        Optional<ImgDataFirst>imgDataFirstOptional=imgDataFirstRepo.findByIdAndCommonId(id,commonId);
        if (imgDataMainOptional.isPresent()){
            ImgDataMain imgDataMain = imgDataMainOptional.get();
            imgDataMain.setImageName(imgName);
            imgDataMain.setImageRefUrl(imgUrl);
            imgDataMainRepo.save(imgDataMain);
            return new ResponseEntity<>(imgDataMain,HttpStatus.OK);
        } else if (imgDataFirstOptional.isPresent()) {
            ImgDataFirst imgDataFirst = imgDataFirstOptional.get();
            imgDataFirst.setImageName(imgName);
            imgDataFirst.setImageRefUrl(imgUrl);
            imgDataFirstRepo.save(imgDataFirst);
            return new ResponseEntity<>(imgDataFirst,HttpStatus.OK);
        }return new ResponseEntity<>("CommonId : "+commonId+" and tableId : "+id+" is not matching", HttpStatus.NOT_FOUND);
    }
}
