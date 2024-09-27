package com.example.MuseumTicketing.Guide.mpFileData;

import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
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
import com.example.MuseumTicketing.Guide.mpFileData.mp4.MediaTypeVideoDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.mainHeading.Mp3Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4Data;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.mainHeading.Mp4DataRepo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.util.S3Service;
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
import java.util.ArrayList;
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

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private S3Service s3Service;


    public MediaTypeDTO uploadMp3(MultipartFile file, String uId) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
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

    public ResponseEntity<?> upload_Mp4(MultipartFile[] video, String commonId) throws IOException{
        List<MediaTypeVideoDTO> response = new ArrayList<>();
        for (MultipartFile file : video){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4_main(file,commonId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private MediaTypeVideoDTO uploadMp4_main(MultipartFile file, String commonId)throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Mp4Data mp4Data= new Mp4Data(fileName,fileUrl,commonId);

        Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
        if (commonIdQRCodeOptional.isPresent()){
            CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
            if (commonIdQRCode.getCommonId().equals(commonId)){
                mp4Data.setDtId(commonId);
                mp4Data.setEngId(commonIdQRCode.getEngId());
                mp4Data.setMalId(commonIdQRCode.getMalId());
                mp4DataRepo.save(mp4Data);
                return new MediaTypeVideoDTO(fileName,fileUrl,commonId,commonIdQRCode.getEngId(),commonIdQRCode.getMalId());
            }
        }else {
            return new MediaTypeVideoDTO("FileName: NoData",fileUrl+"NoData","CommonId : Nodata","EnglishId:NoData","MalayalamId:NoData");
        }
        return new MediaTypeVideoDTO("FileName: NULL",fileUrl+"NULL","CommonId : NULL","EnglishId:NULL","MalayalamId:NULL");
    }

    public ResponseEntity<List<MediaTypeVideoDTO>> uploadMp4(MultipartFile[] files,MultipartFile[] thumbNailFile, String uId) throws IOException{
        List<MediaTypeVideoDTO> response = new ArrayList<>();
//        for (MultipartFile file : files){
//            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4Main(file,thumbNailFile,uId);
//            response.add(mediaTypeVideoDTO);
//        }
        for (int i=0;i<files.length;i++){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4Main(files[i],thumbNailFile[i],uId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    public MediaTypeVideoDTO uploadMp4Main(MultipartFile file,MultipartFile thumbnailFile, String uId) throws IOException{
        try {
            File fileObj = convertMultiPartFileToFile(file);
            String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
            //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
            // Use the S3Service's uploadLargeFile method to upload the file
            s3Service.uploadLargeFile(fileName, fileObj);
            fileObj.delete();
            //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
            // Retrieve the file URL from S3
            String fileUrl = s3Service.getFileUrl(fileName);

            String thumbnailFileName = System.currentTimeMillis() + "_thumbnail_" + thumbnailFile.getOriginalFilename();
            File thumbnailObj = convertMultiPartFileToFile(thumbnailFile);
            s3Service.uploadLargeFile(thumbnailFileName, thumbnailObj);
            thumbnailObj.delete();
            String thumbnailUrl = s3Service.getFileUrl(thumbnailFileName);

            Mp4Data mp4Data= new Mp4Data(fileName,fileUrl,uId);
            mp4Data.setThumbnailUrl(thumbnailUrl);
            mp4Data.setThumbnailName(thumbnailFileName);

//            Mp4Data mp4Data= new Mp4Data(fileName,fileUrl,uId);
            Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(uId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
                if (commonIdQRCode.getCommonId().equals(uId)){
                    mp4Data.setDtId(uId);
                    mp4Data.setEngId(commonIdQRCode.getEngId());
                    mp4Data.setMalId(commonIdQRCode.getMalId());
                    mp4DataRepo.save(mp4Data);
                    return new MediaTypeVideoDTO(fileName,fileUrl,uId,commonIdQRCode.getEngId(),commonIdQRCode.getMalId(),thumbnailUrl,thumbnailFileName);
                }
            }else {
                return new MediaTypeVideoDTO("FileName: NoData",fileUrl+"NoData","CommonId : Nodata","EnglishId:NoData","MalayalamId:NoData", "ThumbNail:NoData", "ThumbnailName:NoData");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new MediaTypeVideoDTO(null,null,null,null,null,null,null);
    }

    public MediaTypeDTO uploadPdf(MultipartFile file, String uId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Mp4Data mp4Data= new Mp4Data(fileName,fileUrl,uId);
        mp4DataRepo.save(mp4Data);
        //PdfData pdfData = new PdfData(fileName,fileUrl,uId);
        //pdfDataRepo.save(pdfData);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadMp3fs(MultipartFile file, String uId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
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

    public ResponseEntity<?> uploadFirst_Mp4(MultipartFile[] video, String commonId) throws IOException{
        List<MediaTypeVideoDTO> response = new ArrayList<>();
        for (MultipartFile file : video){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4_First(file,commonId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MediaTypeVideoDTO uploadMp4_First(MultipartFile file, String commonId)throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Mp4Data1 mp4Data1= new Mp4Data1(fileName,fileUrl,commonId);

        Optional<CommonIdFs>commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
        if (commonIdFsOptional.isPresent()){
            CommonIdFs commonIdFs = commonIdFsOptional.get();
            if (commonIdFs.getFsCommonId().equals(commonId)){
                mp4Data1.setEngId(commonIdFs.getFsEngId());
                mp4Data1.setMalId(commonIdFs.getFsMalId());
                Optional<FirstSubEnglish>firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(commonIdFs.getFsEngId());
                if (firstSubEnglishOptional.isPresent()){
                    FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
                    mp4Data1.setMainEngId(firstSubEnglish.getMainUid());
                }
                Optional<FirstSubMalayalam>firstSubMalayalamOptional = firstSubMalayalamRepo.findByFsUid(commonIdFs.getFsMalId());
                if (firstSubMalayalamOptional.isPresent()){
                    FirstSubMalayalam firstSubMalayalam= firstSubMalayalamOptional.get();
                    mp4Data1.setMainMalId(firstSubMalayalam.getMainUid());
                }
            }else {
                log.info("CommonId is not matching");
            }
        }
        mp4Data1Repo.save(mp4Data1);
        return new MediaTypeVideoDTO(fileName,fileUrl,commonId,mp4Data1.getEngId(),mp4Data1.getMalId());
    }
    public ResponseEntity<?> uploadMp4fs(MultipartFile[] video, MultipartFile[] thumbnailFile, String commonId) throws IOException{
        List<MediaTypeVideoDTO>response = new ArrayList<>();
        for (int i =0;i<video.length;i++){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4First(video[i],thumbnailFile[i],commonId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private MediaTypeVideoDTO uploadMp4First(MultipartFile file, MultipartFile thumbnailFile, String commonId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        String thumbnailFileName = System.currentTimeMillis() + "_thumbnail_" + thumbnailFile.getOriginalFilename();
        File thumbnailObj = convertMultiPartFileToFile(thumbnailFile);
        s3Service.uploadLargeFile(thumbnailFileName, thumbnailObj);
        thumbnailObj.delete();
        String thumbnailUrl = s3Service.getFileUrl(thumbnailFileName);

        Mp4Data1 mp4Data1 = new Mp4Data1(fileName,fileUrl,commonId);
        mp4Data1.setThumbnailUrl(thumbnailUrl);
        mp4Data1.setThumbnailName(thumbnailFileName);


        Optional<CommonIdFs>commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
        if (commonIdFsOptional.isPresent()){
            CommonIdFs commonIdFs = commonIdFsOptional.get();
            if (commonIdFs.getFsCommonId().equals(commonId)){
                mp4Data1.setEngId(commonIdFs.getFsEngId());
                mp4Data1.setMalId(commonIdFs.getFsMalId());
                Optional<FirstSubEnglish>firstSubEnglishOptional = firstSubEnglishRepo.findByfsUid(commonIdFs.getFsEngId());
                if (firstSubEnglishOptional.isPresent()){
                    FirstSubEnglish firstSubEnglish = firstSubEnglishOptional.get();
                    mp4Data1.setMainEngId(firstSubEnglish.getMainUid());
                }
                Optional<FirstSubMalayalam>firstSubMalayalamOptional = firstSubMalayalamRepo.findByFsUid(commonIdFs.getFsMalId());
                if (firstSubMalayalamOptional.isPresent()){
                    FirstSubMalayalam firstSubMalayalam= firstSubMalayalamOptional.get();
                    mp4Data1.setMainMalId(firstSubMalayalam.getMainUid());
                }
            }else {
                log.info("CommonId is not matching");
            }
        }
        mp4Data1Repo.save(mp4Data1);
        return new MediaTypeVideoDTO(fileName,fileUrl,commonId,mp4Data1.getEngId(),mp4Data1.getMalId(),thumbnailUrl, thumbnailFileName);
    }


    public MediaTypeDTO uploadMp3ss(MultipartFile file, String uId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Mp3Data2 mp3Data2 = new Mp3Data2(fileName,fileUrl,uId);
        mp3Data2Repo.save(mp3Data2);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public ResponseEntity<?> uploadSecond_Mp4(MultipartFile[] video, String commonId) throws IOException{
        List<MediaTypeVideoDTO> response = new ArrayList<>();
        for (MultipartFile file : video){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4_Second(file,commonId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MediaTypeVideoDTO uploadMp4_Second(MultipartFile file, String commonId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);

        Mp4Data2 mp4Data2 = new Mp4Data2(fileName,fileUrl,commonId);
        Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
        if (commonIdSsOptional.isPresent()){
            CommonIdSs commonIdSs = commonIdSsOptional.get();
            if (commonIdSs.getSsCommonId().equals(commonId)){
                mp4Data2.setEngId(commonIdSs.getSsEngId());
                mp4Data2.setMalId(commonIdSs.getSsMalId());
            }
        }
        mp4Data2Repo.save(mp4Data2);
        return new MediaTypeVideoDTO(fileName,fileUrl,commonId,mp4Data2.getEngId(),mp4Data2.getMalId());
    }

    public ResponseEntity<?> uploadMp4ss(MultipartFile[] video, MultipartFile[] thumbnailFile, String commonId) throws IOException{
        List<MediaTypeVideoDTO>response = new ArrayList<>();
        for (int i =0;i<video.length;i++){
            MediaTypeVideoDTO mediaTypeVideoDTO = uploadMp4Second(video[i],thumbnailFile[i],commonId);
            response.add(mediaTypeVideoDTO);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private MediaTypeVideoDTO uploadMp4Second(MultipartFile file, MultipartFile thumbnailFile, String commonId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);

        String thumbnailFileName = System.currentTimeMillis() + "_thumbnail_" + thumbnailFile.getOriginalFilename();
        File thumbnailObj = convertMultiPartFileToFile(thumbnailFile);
        s3Service.uploadLargeFile(thumbnailFileName, thumbnailObj);
        thumbnailObj.delete();
        String thumbnailUrl = s3Service.getFileUrl(thumbnailFileName);

        Mp4Data2 mp4Data2 = new Mp4Data2(fileName,fileUrl,commonId);
        mp4Data2.setThumbnailUrl(thumbnailUrl);
        mp4Data2.setThumbnailName(thumbnailFileName);


        Optional<CommonIdSs>commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
        if (commonIdSsOptional.isPresent()){
            CommonIdSs commonIdSs = commonIdSsOptional.get();
            if (commonIdSs.getSsCommonId().equals(commonId)){
                mp4Data2.setEngId(commonIdSs.getSsEngId());
                mp4Data2.setMalId(commonIdSs.getSsMalId());
            }
        }
        mp4Data2Repo.save(mp4Data2);
        return new MediaTypeVideoDTO(fileName,fileUrl,commonId,mp4Data2.getEngId(),mp4Data2.getMalId(), thumbnailUrl, thumbnailFileName);
    }

}
