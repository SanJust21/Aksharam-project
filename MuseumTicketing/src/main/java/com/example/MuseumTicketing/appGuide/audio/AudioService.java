package com.example.MuseumTicketing.appGuide.audio;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirstRepo;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import com.example.MuseumTicketing.appGuide.video.main.VideoMainRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeDTO;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirstRepo;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMainRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirstRepo;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import com.example.MuseumTicketing.appGuide.video.main.VideoMainRepo;
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
public class AudioService {
    @Autowired
    private AudioMainRepo audionMainRepo;
    @Autowired
    private AudioFirstRepo audioFirstRepo;
    @Autowired
    private VideoMainRepo videoMainRepo;
    @Autowired
    private VideoFirstRepo videoFirstRepo;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;


    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

    public MediaTypeDTO uploadAudioMain(String uId, MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        AudioMain audioMain = new AudioMain(fileName,fileUrl,uId);
        audionMainRepo.save(audioMain);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadAudioFirst(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        AudioFirst audioFirst = new AudioFirst(fileName,fileUrl,uId);
        Optional<FirstTopicEng> firstTopicEnglishOptional =firstTopicEngRepo.findByfsUid(uId);
        Optional<FirstTopicMal> firstTopicMalayalamOptional =firstTopicMalRepo.findByFsUid(uId);
        if (firstTopicEnglishOptional.isPresent()){
            FirstTopicEng firstTopicEng = firstTopicEnglishOptional.get();
            String mUid = firstTopicEng.getMainUid();
            if (mUid!=null){
                audioFirst.setMainEngId(uId);
                String id = "No Data";
                audioFirst.setMainMalId(id);
            }
        }else {
            if (firstTopicMalayalamOptional.isPresent()){
                FirstTopicMal firstTopicMal = firstTopicMalayalamOptional.get();
                String mUid = firstTopicMal.getMainUid();
                if (mUid!=null){
                    audioFirst.setMainMalId(mUid);
                    String id = "No Data";
                    audioFirst.setMainEngId(id);
                }
            }
        }
        audioFirstRepo.save(audioFirst);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadVideoMain(String uId, MultipartFile file, String engId, String malId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        VideoMain videoMain = new VideoMain(fileName,fileUrl,uId);
        if (uId!=null){
            videoMain.setEngId(engId);
            videoMain.setMalId(malId);
        }
        videoMainRepo.save(videoMain);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public ResponseEntity<?> uploadVideoFirst(String uId, MultipartFile file, String engId, String malId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        VideoFirst videoFirst  = new VideoFirst(fileName,fileUrl,uId);
        Optional<FirstTopicEng> firstTopicEngOptional =firstTopicEngRepo.findByfsUid(engId);
        Optional<FirstTopicMal> firstTopicMalOptional =firstTopicMalRepo.findByFsUid(malId);
        if (firstTopicEngOptional.isPresent()){
            FirstTopicEng firstTopicEng = firstTopicEngOptional.get();
            if (firstTopicEng.getFsUid().equals(engId)){
                videoFirst.setMainEngId(firstTopicEng.getMainUid());
                videoFirst.setFsEngId(engId);
            }
        }
        if (firstTopicMalOptional.isPresent()){
            FirstTopicMal firstTopicMal = firstTopicMalOptional.get();
            if (firstTopicMal.getFsUid().equals(malId)){
                videoFirst.setMainMalId(firstTopicMal.getMainUid());
                videoFirst.setFsMalId(malId);
            }

        }
        videoFirstRepo.save(videoFirst);
        return new ResponseEntity<>(videoFirst, HttpStatus.OK);
    }

}
