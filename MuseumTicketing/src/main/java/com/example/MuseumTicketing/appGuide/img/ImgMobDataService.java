package com.example.MuseumTicketing.appGuide.img;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());

        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

    public ImgDataMain uploadJPGMain(MultipartFile file, String engId, String malId, String commonId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        ImgDataMain imgDataMain = new ImgDataMain(fileName,fileUrl,commonId);
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

    public ImgDataFirst uploadJPGFirst(MultipartFile file, String engId, String malId, String commonId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        ImgDataFirst imgDataFirst = new ImgDataFirst(fileName,fileUrl,commonId);
        Optional<FirstTopicEng> firstTopicEngOptional = firstTopicEngRepo.findByfsUid(engId);
        if (firstTopicEngOptional.isPresent()){
            FirstTopicEng firstSubEnglish = firstTopicEngOptional.get();
            imgDataFirst.setEngId(engId);
            imgDataFirst.setMainEngUid(firstSubEnglish.getMainUid());
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
}
