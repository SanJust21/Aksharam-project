package com.example.MuseumTicketing.Guide.UPDATE.MainTitle;

import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mpFileData.MediaTypeService;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4DataRepo;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MainUpdateService {
    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Mp3Repo mp3Repo;
    @Autowired
    private Mp3Data1Repo mp3Data1Repo;
    @Autowired
    private Mp3Data2Repo mp3Data2Repo;
    @Autowired
    private Mp4DataRepo mp4DataRepo;
    @Autowired
    private Mp4Data1Repo mp4Data1Repo;
    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private MediaTypeService mediaTypeService;
    @Autowired
    private S3Service s3Service;



    public ResponseEntity<?> updateDataMalyalam(String uId, MainDTO mainDTO) {
        try {
            Optional<MainTitleMal> mainTitleMal = mainTitleMalRepo.findBymMalUid(uId);
            if (mainTitleMal.isPresent()){
                MainTitleMal mainTitleMal1 = mainTitleMal.get();
                mainTitleMal1.setTitle(mainDTO.getTitle());
                mainTitleMal1.setDescription(mainDTO.getDescription());
                mainTitleMal1.setRef(mainDTO.getReferenceURL());
                mainTitleMalRepo.save(mainTitleMal1);
                //return new ResponseEntity<>(mainTitleMal1,HttpStatus.OK);
                // Fetch all records ordered by primary key
                List<MainTitleMal> orderedList = mainTitleMalRepo.findAll();
                orderedList.sort(Comparator.comparing(MainTitleMal::getMMalId));

                return new ResponseEntity<>(orderedList, HttpStatus.OK);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateDataEnglish(String uId, MainDTO mainDTO) {
        try {
            MainTitleEng mainTitleEng = mainTitleEngRepo.findBymEngUid(uId);
            mainTitleEng.setTitle(mainDTO.getTitle());
            mainTitleEng.setDescription(mainDTO.getDescription());
            mainTitleEng.setRef(mainDTO.getReferenceURL());
            mainTitleEngRepo.save(mainTitleEng);
            //return new ResponseEntity<>(mainTitleEng,HttpStatus.OK);
            // Fetch all records ordered by primary key
            List<MainTitleEng> orderedList = mainTitleEngRepo.findAll();
            orderedList.sort(Comparator.comparing(MainTitleEng::getMEngId));

            return new ResponseEntity<>(orderedList, HttpStatus.OK);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseEntity<?> updateAudioMain(MultipartFile file, String uId, Integer id) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp3Data>mp3DataOptional=mp3Repo.findByDtIdAndId(uId,id);
        if (mp3DataOptional.isPresent()){
            Mp3Data mp3Data =mp3DataOptional.get();
            mp3Data.setFName(fileName);
            mp3Data.setFUrl(fileUrl);
            mp3Repo.save(mp3Data);
            return new ResponseEntity<>(mp3Data+" File updated",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateVideoMain(MultipartFile file, String uId, Integer id) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp4Data>mp4DataOptional=mp4DataRepo.findByDtIdAndId(uId,id);
        if (mp4DataOptional.isPresent()){
            Mp4Data mp4Data=mp4DataOptional.get();
            mp4Data.setFName(fileName);
            mp4Data.setFUrl(fileUrl);
            mp4DataRepo.save(mp4Data);
            return new ResponseEntity<>(mp4Data,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateThumbnail(MultipartFile files, String uId, Integer id) throws IOException{
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<Mp4Data>mp4DataOptional=mp4DataRepo.findByDtIdAndId(uId,id);
        if (mp4DataOptional.isPresent()){
            Mp4Data mp4Data=mp4DataOptional.get();
            mp4Data.setThumbnailName(fileName);
            mp4Data.setThumbnailUrl(fileUrl);
            mp4DataRepo.save(mp4Data);
            return new ResponseEntity<>(mp4Data,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }
}
