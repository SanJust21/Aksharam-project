package com.example.MuseumTicketing.Guide.mpFileData;

import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4DataRepo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MediaTypeService {
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
    private FirstSubEnglishRepo firstSubEnglishRepo;

    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    public MediaTypeDTO uploadMp3(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp3Data mp3Data = new Mp3Data(fileName,fileUrl,uId);
        mp3Repo.save(mp3Data);
        return new MediaTypeDTO(fileName,fileUrl,uId);
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

    public MediaTypeDTO uploadMp4(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp4Data mp4Data= new Mp4Data(fileName,fileUrl,uId);
        mp4DataRepo.save(mp4Data);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadMp3fs(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp3Data1 mp3Data1 = new Mp3Data1(fileName,fileUrl,uId);

        Optional<FirstSubEnglish> firstSubEnglishOptional =firstSubEnglishRepo.findByfsUid(uId);
        if (firstSubEnglishOptional.isPresent()){
            FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
            String mUid = firstSubEnglish.getMainUid();
            if (mUid!=null){
                mp3Data1.setMainEngId(mUid);
                String id = "No Data";
                mp3Data1.setMainMalId(id);
            }else {
                String id = "No Data";
                mp3Data1.setMainEngId(id);
            }
        }

        Optional<FirstSubMalayalam> firstSubMalayalamOptional =firstSubMalayalamRepo.findByfsUid(uId);
        if (firstSubMalayalamOptional.isPresent()){
            FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
            String mUid = firstSubMalayalam.getMainUid();
            if (mUid!=null){
                mp3Data1.setMainMalId(mUid);
                String id = "No Data";
                mp3Data1.setMainEngId(id);
            }else {
                String id = "No Data";
                mp3Data1.setMainMalId(id);
            }

        }

        mp3Data1Repo.save(mp3Data1);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadMp4fs(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp4Data1 mp4Data1 = new Mp4Data1(fileName,fileUrl,uId);

        Optional<FirstSubEnglish> firstSubEnglishOptional =firstSubEnglishRepo.findByfsUid(uId);
        if (firstSubEnglishOptional.isPresent()){
            FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
            String mUid = firstSubEnglish.getMainUid();
            if (mUid!=null){
                mp4Data1.setMainEngId(mUid);
                String id = "No Data";
                mp4Data1.setMainMalId(id);
            }else {
                String id = "No Data";
                mp4Data1.setMainEngId(id);
            }
        }

        Optional<FirstSubMalayalam> firstSubMalayalamOptional =firstSubMalayalamRepo.findByfsUid(uId);
        if (firstSubMalayalamOptional.isPresent()){
            FirstSubMalayalam firstSubMalayalam = firstSubMalayalamOptional.get();
            String mUid = firstSubMalayalam.getMainUid();
            if (mUid!=null){
                mp4Data1.setMainMalId(mUid);
                String id = "No Data";
                mp4Data1.setMainEngId(id);
            }else {
                String id = "No Data";
                mp4Data1.setMainMalId(id);
            }

        }

        mp4Data1Repo.save(mp4Data1);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }
    public MediaTypeDTO uploadMp3ss(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp3Data2 mp3Data2 = new Mp3Data2(fileName,fileUrl,uId);
        mp3Data2Repo.save(mp3Data2);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadMp4ss(MultipartFile file, String uId) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        Mp4Data2 mp4Data2 = new Mp4Data2(fileName,fileUrl,uId);
        mp4Data2Repo.save(mp4Data2);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO updateUploadMp3(MultipartFile file, String uId) {
        try {
            List<Mp3Data> mp3Data1 = mp3Repo.findBydtId(uId);
            if (!mp3Data1.isEmpty()){
                File fileObj = convertMultiPartFileToFile(file);
                String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName,fileName).toString();

                for (Mp3Data mp3Data : mp3Data1) {
                    mp3Data.setFName(fileName);
                    mp3Data.setFUrl(fileUrl);
                    mp3Repo.save(mp3Data);
                }

                return new MediaTypeDTO(fileName,fileUrl,uId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data","No Data","No Data");
    }

    public MediaTypeDTO updateUploadMp4(MultipartFile file, String uId) {
        try {
            List<Mp4Data> mp4DataList = mp4DataRepo.findBydtId(uId);
            if (!mp4DataList.isEmpty() && !file.isEmpty()) {
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing Mp4Data entries
                for (Mp4Data mp4Data : mp4DataList) {
                    mp4Data.setFName(fileName);
                    mp4Data.setFUrl(fileUrl);
                    mp4DataRepo.save(mp4Data);
                }
                return new MediaTypeDTO(fileName, fileUrl, uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data", "No Data", "No Data");
    }


    public MediaTypeDTO updateFirstSubUploadMp3(MultipartFile file, String uId) {
        try {
            List<Mp3Data1> mp3DataList = mp3Data1Repo.findBydtId(uId);
            if (!mp3DataList.isEmpty() && !file.isEmpty()) {
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing Mp3Data1 entries
                for (Mp3Data1 mp3Data : mp3DataList) {
                    mp3Data.setFName(fileName);
                    mp3Data.setFUrl(fileUrl);
                    mp3Data1Repo.save(mp3Data);
                }
                return new MediaTypeDTO(fileName, fileUrl, uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data", "No Data", "No Data");
    }


    public MediaTypeDTO updateFirstSubUploadMp4(MultipartFile file, String uId) {
        try {
            List<Mp4Data1> mp4DataList = mp4Data1Repo.findBydtId(uId);
            if (!mp4DataList.isEmpty() && !file.isEmpty()) {
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing Mp4Data1 entries
                for (Mp4Data1 mp4Data : mp4DataList) {
                    mp4Data.setFName(fileName);
                    mp4Data.setFUrl(fileUrl);
                    mp4Data1Repo.save(mp4Data);
                }
                return new MediaTypeDTO(fileName, fileUrl, uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data", "No Data", "No Data");
    }


//    public MediaTypeDTO updateSecondSubUploadMp3(MultipartFile file, String uId) {
//        try {
//            File fileObj = convertMultiPartFileToFile(file);
//            String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//            s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//            fileObj.delete();
//            String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//            Mp3Data2 mp3Data2= new Mp3Data2(fileName,fileUrl,uId);
//            mp3Data2Repo.save(mp3Data2);
//            return new MediaTypeDTO(fileName,fileUrl,uId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new MediaTypeDTO("No Data","No Data","No Data");
//    }
//
//    public MediaTypeDTO updateSecondSubUploadMp4(MultipartFile file, String uId) {
//        try {
//            File fileObj = convertMultiPartFileToFile(file);
//            String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//            s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//            fileObj.delete();
//            String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//            Mp4Data2 mp4Data2= new Mp4Data2(fileName,fileUrl,uId);
//            mp4Data2Repo.save(mp4Data2);
//            return new MediaTypeDTO(fileName,fileUrl,uId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new MediaTypeDTO("No Data","No Data","No Data");
//    }

    public MediaTypeDTO updateSecondSubUploadMp3(MultipartFile file, String uId) {
        try {
            List<Mp3Data2> mp3DataList = mp3Data2Repo.findBydtId(uId);
            if (!mp3DataList.isEmpty() && !file.isEmpty()) {
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing Mp3Data2 entries
                for (Mp3Data2 mp3Data : mp3DataList) {
                    mp3Data.setFName(fileName);
                    mp3Data.setFUrl(fileUrl);
                    mp3Data2Repo.save(mp3Data);
                }
                return new MediaTypeDTO(fileName, fileUrl, uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data", "No Data", "No Data");
    }

    public MediaTypeDTO updateSecondSubUploadMp4(MultipartFile file, String uId) {
        try {
            List<Mp4Data2> mp4DataList = mp4Data2Repo.findBydtId(uId);
            if (!mp4DataList.isEmpty() && !file.isEmpty()) {
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

                // Update existing Mp4Data2 entries
                for (Mp4Data2 mp4Data : mp4DataList) {
                    mp4Data.setFName(fileName);
                    mp4Data.setFUrl(fileUrl);
                    mp4Data2Repo.save(mp4Data);
                }
                return new MediaTypeDTO(fileName, fileUrl, uId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MediaTypeDTO("No Data", "No Data", "No Data");
    }


}
