package com.example.MuseumTicketing.Guide.UPDATE.FirstSub;

import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
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
public class FirstSubUpdateService {
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private Mp3Data1Repo mp3Data1Repo;
    @Autowired
    private Mp4Data1Repo mp4Data1Repo;

    public ResponseEntity<?> updateFirstSubDataMalyalam(String uId, MainDTO mainDTO) {
        try {
            Optional<FirstSubMalayalam> firstSubMalayalam = firstSubMalayalamRepo.findByfsUid(uId);
            if (firstSubMalayalam.isPresent()){
                FirstSubMalayalam firstSubMalayalam1 = firstSubMalayalam.get();
                if (firstSubMalayalam1.getFsUid().equals(uId)){
                    firstSubMalayalam1.setTitle(mainDTO.getTitle());
                    firstSubMalayalam1.setDescription(mainDTO.getDescription());
                    firstSubMalayalam1.setRef(mainDTO.getReferenceURL());
                    firstSubMalayalamRepo.save(firstSubMalayalam1);
                    //return new ResponseEntity<>(firstSubMalayalam1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<FirstSubMalayalam> orderedList = firstSubMalayalamRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> updateFirstSubDataEnglish(String uId, MainDTO mainDTO) {
        try {
            Optional<FirstSubEnglish> firstSubEnglish = firstSubEnglishRepo.findByfsUid(uId);
            if (firstSubEnglish.isPresent()){
                FirstSubEnglish firstSubEnglish1 = firstSubEnglish.get();
                if (firstSubEnglish1.getFsUid().equals(uId)){
                    firstSubEnglish1.setTitle(mainDTO.getTitle());
                    firstSubEnglish1.setDescription(mainDTO.getDescription());
                    firstSubEnglish1.setRef(mainDTO.getReferenceURL());
                    firstSubEnglishRepo.save(firstSubEnglish1);
                   // return new ResponseEntity<>(firstSubEnglish1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<FirstSubEnglish> orderedList = firstSubEnglishRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseEntity<?> updateAudioFirstSub(MultipartFile files, String uId, Integer id) throws IOException {
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp3Data1>mp3Data1Optional=mp3Data1Repo.findByDtIdAndId(uId,id);
        if (mp3Data1Optional.isPresent()){
            Mp3Data1 mp3Data1 = mp3Data1Optional.get();
            mp3Data1.setFName(fileName);
            mp3Data1.setFUrl(fileUrl);
            mp3Data1Repo.save(mp3Data1);
            return new ResponseEntity<>(mp3Data1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateVideoFirstSub(MultipartFile files, String uId, Integer id) throws IOException{
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp4Data1>mp4Data1Optional=mp4Data1Repo.findByDtIdAndId(uId,id);
        if (mp4Data1Optional.isPresent()){
            Mp4Data1 mp4Data1=mp4Data1Optional.get();
            mp4Data1.setFName(fileName);
            mp4Data1.setFUrl(fileUrl);
            mp4Data1Repo.save(mp4Data1);
            return new ResponseEntity<>(mp4Data1+" video is updated.",HttpStatus.OK);
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
        Optional<Mp4Data1>mp4Data1Optional=mp4Data1Repo.findByDtIdAndId(uId,id);
        if (mp4Data1Optional.isPresent()){
            Mp4Data1 mp4Data1=mp4Data1Optional.get();
            mp4Data1.setThumbnailName(fileName);
            mp4Data1.setThumbnailUrl(fileUrl);
            mp4Data1Repo.save(mp4Data1);
            return new ResponseEntity<>(mp4Data1+" thumbnail is updated.",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }
}
