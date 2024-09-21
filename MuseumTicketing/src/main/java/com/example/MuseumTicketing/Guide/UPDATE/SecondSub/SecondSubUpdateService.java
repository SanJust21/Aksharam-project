package com.example.MuseumTicketing.Guide.UPDATE.SecondSub;

import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.Guide.util.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SecondSubUpdateService {
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;
    @Autowired
    private Mp3Data2Repo mp3Data2Repo;
    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private S3Service s3Service;


    public ResponseEntity<?> updateSecondSubDataMalayalam(String uId, MainDTO mainDTO) {
        try {
            Optional<SecondSubMalayalam> secondSubMalayalam = secondSubMalayalamRepo.findByssUid(uId);
            if (secondSubMalayalam.isPresent()){
                SecondSubMalayalam secondSubMalayalam1 = secondSubMalayalam.get();
                if (secondSubMalayalam1.getSsUid().equals(uId)){
                    secondSubMalayalam1.setTitle(mainDTO.getTitle());
                    secondSubMalayalam1.setDescription(mainDTO.getDescription());
                    secondSubMalayalam1.setRef(mainDTO.getReferenceURL());
                    secondSubMalayalamRepo.save(secondSubMalayalam1);
                    //return new ResponseEntity<>(secondSubMalayalam1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<SecondSubMalayalam> orderedList = secondSubMalayalamRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateSecondSubDataEnglish(String uId, MainDTO mainDTO) {
        try {
            Optional<SecondSubEnglish> secondSubEnglish = secondSubEnglishRepo.findByssUid(uId);
            if (secondSubEnglish.isPresent()){
                SecondSubEnglish secondSubEnglish1 = secondSubEnglish.get();
                if (secondSubEnglish1.getSsUid().equals(uId)){
                    secondSubEnglish1.setTitle(mainDTO.getTitle());
                    secondSubEnglish1.setDescription(mainDTO.getDescription());
                    secondSubEnglish1.setRef(mainDTO.getReferenceURL());
                    secondSubEnglishRepo.save(secondSubEnglish1);
                    //return new ResponseEntity<>(secondSubEnglish1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<SecondSubEnglish> orderedList = secondSubEnglishRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

    public ResponseEntity<?> updateSecondSubAudio(MultipartFile files, String uId, Integer id) throws IOException {
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp3Data2>mp3Data2Optional=mp3Data2Repo.findByDtIdAndId(uId,id);
        if (mp3Data2Optional.isPresent()){
            Mp3Data2 mp3Data2 = mp3Data2Optional.get();
            mp3Data2.setFName(fileName);
            mp3Data2.setFUrl(fileUrl);
            mp3Data2Repo.save(mp3Data2);
            return new ResponseEntity<>(mp3Data2,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSecondSubVideo(MultipartFile files, String uId, Integer id) throws IOException{
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp4Data2>mp4Data2Optional=mp4Data2Repo.findByDtIdAndId(uId,id);
        if (mp4Data2Optional.isPresent()){
            Mp4Data2 mp4Data2 = mp4Data2Optional.get();
            mp4Data2.setFName(fileName);
            mp4Data2.setFUrl(fileUrl);
            mp4Data2Repo.save(mp4Data2);
            return new ResponseEntity<>(mp4Data2+" Video is updated",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateThumbnail(MultipartFile files, String uId, Integer id)throws IOException {
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp4Data2>mp4Data2Optional=mp4Data2Repo.findByDtIdAndId(uId,id);
        if (mp4Data2Optional.isPresent()){
            Mp4Data2 mp4Data2 = mp4Data2Optional.get();
            mp4Data2.setThumbnailName(fileName);
            mp4Data2.setThumbnailUrl(fileUrl);
            mp4Data2Repo.save(mp4Data2);
            return new ResponseEntity<>(mp4Data2+" ThumbnailVideo is updated",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }
}
