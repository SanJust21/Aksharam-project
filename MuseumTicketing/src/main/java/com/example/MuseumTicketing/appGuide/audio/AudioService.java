package com.example.MuseumTicketing.appGuide.audio;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.util.S3Service;
import com.example.MuseumTicketing.appGuide.Pdf.PdfData;
import com.example.MuseumTicketing.appGuide.Pdf.PdfRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AudioService {
    @Autowired
    private AudioMainRepo audionMainRepo;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo ;
    @Autowired
    private SubComIdRepo subComIdRepo;
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
    @Autowired
    private S3Service s3Service;
    @Autowired
    private PdfRepo pdfRepo;

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }catch (Exception e){
            log.error("Error converting multipartFile to file",e);
        }
        return convertedFile;
    }

    public MediaTypeDTO uploadAudioMain(String uId, MultipartFile file) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        AudioMain audioMain = new AudioMain(fileName,fileUrl,uId);
        Optional<CommonQRParaId>commonQRParaIdOptional =  commonQRParaIdRepo.findByEngId(uId);
        Optional<CommonQRParaId>commonQRParaIdOptional1 = commonQRParaIdRepo.findByMalId(uId);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
            if (commonQRParaId.getEngId().equals(uId)){
                audioMain.setCommonId(commonQRParaId.getCommonId());
            }
        } else if (commonQRParaIdOptional1.isPresent()) {
            CommonQRParaId commonQRParaId = commonQRParaIdOptional1.get();
            if (commonQRParaId.getMalId().equals(uId)){
                audioMain.setCommonId(commonQRParaId.getCommonId());
            }
        }
        audionMainRepo.save(audioMain);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadAudioFirst(MultipartFile file, String uId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        AudioFirst audioFirst = new AudioFirst(fileName,fileUrl,uId);
        Optional<FirstTopicEng> firstTopicEnglishOptional =firstTopicEngRepo.findByfsUid(uId);
        Optional<FirstTopicMal> firstTopicMalayalamOptional =firstTopicMalRepo.findByFsUid(uId);
        if (firstTopicEnglishOptional.isPresent()){
            FirstTopicEng firstTopicEng = firstTopicEnglishOptional.get();
            String mUid = firstTopicEng.getMainUid();
            if (mUid!=null){

                audioFirst.setMainEngId(uId);
                SubComId subComIdOptional =subComIdRepo.findByfsEngId(uId);
                if (subComIdOptional.getFsEngId().equals(uId)){
                    audioFirst.setFsCommonId(subComIdOptional.getFsCommonId());
                }
                String id = "No Data";
                audioFirst.setMainMalId(id);
            }
        }else {
            if (firstTopicMalayalamOptional.isPresent()){
                FirstTopicMal firstTopicMal = firstTopicMalayalamOptional.get();
                String mUid = firstTopicMal.getMainUid();
                if (mUid!=null){
                    audioFirst.setMainMalId(mUid);
                    SubComId subComIdOptional =subComIdRepo.findByfsMalId(uId);
                    if (subComIdOptional.getFsMalId().equals(uId)){
                        audioFirst.setFsCommonId(subComIdOptional.getFsCommonId());
                    }
                    String id = "No Data";
                    audioFirst.setMainEngId(id);
                }
            }
        }
        audioFirstRepo.save(audioFirst);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public MediaTypeDTO uploadVideoMain(String uId, MultipartFile file, String engId, String malId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        VideoMain videoMain = new VideoMain(fileName,fileUrl,uId);
        if (uId!=null){
            videoMain.setEngId(engId);
            videoMain.setMalId(malId);
        }
        videoMainRepo.save(videoMain);
        return new MediaTypeDTO(fileName,fileUrl,uId);
    }

    public ResponseEntity<?> uploadVideoFirst(String uId, MultipartFile file, String engId, String malId) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
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



//    public ResponseEntity<?> updateAudioMain(String malId, String commonId, MultipartFile file) throws IOException{
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        // Use the S3Service's uploadLargeFile method to upload the file
//        s3Service.uploadLargeFile(fileName, fileObj);
//        fileObj.delete();
//        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//        // Retrieve the file URL from S3
//        String fileUrl = s3Service.getFileUrl(fileName);
//
//        Optional<AudioMain>audioMainOptional = audionMainRepo.findByCommonIdAndDtId(commonId,malId);
//        if (audioMainOptional.isPresent()){
//            AudioMain audioMain =audioMainOptional.get();
//            if (audioMain.getDtId().equals(malId)){
//                audioMain.setFName(fileName);
//                audioMain.setFUrl(fileUrl);
//                audionMainRepo.save(audioMain);
//                return new ResponseEntity<>(audioMain,HttpStatus.OK);
//            }
//        }
//
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    public AudioMain updateAudioMain(String malId, String commonId, MultipartFile file, Integer ids)throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<AudioMain>audioMainOptional=audionMainRepo.findByDtIdAndId(malId,ids);
        if (audioMainOptional.isPresent()){
            AudioMain audioMain= audioMainOptional.get();
            if (audioMain.getCommonId().equals(commonId)){
                audioMain.setFUrl(fileUrl);
                audioMain.setFName(fileName);
                audionMainRepo.save(audioMain);
                return audioMain;
            }
        }
        return null;
    }

    public AudioFirst updateAudioFirst(String malId, String commonId, MultipartFile file, Integer ids) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<AudioFirst>audioFirstOptional=audioFirstRepo.findByDtIdAndId(malId,ids);
        if (audioFirstOptional.isPresent()){
            AudioFirst audioFirst = audioFirstOptional.get();
            if (audioFirst.getFsCommonId().equals(commonId)){
                audioFirst.setFUrl(fileUrl);
                audioFirst.setFName(fileName);
                audioFirstRepo.save(audioFirst);
                return audioFirst;
            }
        }return null;
    }

//    public ResponseEntity<?> updateAudioFirst(String malId, String commonId, MultipartFile file) throws IOException {
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        // Use the S3Service's uploadLargeFile method to upload the file
//        s3Service.uploadLargeFile(fileName, fileObj);
//        fileObj.delete();
//        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//        // Retrieve the file URL from S3
//        String fileUrl = s3Service.getFileUrl(fileName);
//        Optional<AudioFirst>audioFirstOptional = audioFirstRepo.findByFsCommonIdAndDtId(commonId,malId);
//        if (audioFirstOptional.isPresent()){
//            AudioFirst audioFirst = audioFirstOptional.get();
//            if (audioFirst.getDtId().equals(malId)){
//                audioFirst.setFUrl(fileUrl);
//                audioFirst.setFName(fileName);
//                audioFirstRepo.save(audioFirst);
//                return new ResponseEntity<>(audioFirst,HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    public VideoMain updateVideoMain(String commonId, MultipartFile file, Integer ids) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<VideoMain>videoMainOptional=videoMainRepo.findByDtIdAndId(commonId,ids);
        if (videoMainOptional.isPresent()){
            VideoMain videoMain = videoMainOptional.get();
            videoMain.setFName(fileName);
            videoMain.setFUrl(fileUrl);
            videoMainRepo.save(videoMain);
            return videoMain;
        }
        return null;
    }

    public VideoFirst updateVideoFirst(String commonId, MultipartFile file, Integer ids) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<VideoFirst>videoFirstOptional=videoFirstRepo.findByDtIdAndId(commonId,ids);
        if (videoFirstOptional.isPresent()){
            VideoFirst videoFirst = videoFirstOptional.get();
            videoFirst.setFUrl(fileUrl);
            videoFirst.setFName(fileName);
            videoFirstRepo.save(videoFirst);
            return videoFirst;
        }
        return null;
    }

//    public ResponseEntity<?> updateVideoMain(String malId, String commonId, MultipartFile file) throws IOException{
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        // Use the S3Service's uploadLargeFile method to upload the file
//        s3Service.uploadLargeFile(fileName, fileObj);
//        fileObj.delete();
//        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//        // Retrieve the file URL from S3
//        String fileUrl = s3Service.getFileUrl(fileName);
//        Optional<VideoMain>videoMainOptional =videoMainRepo.findByDtIdAndMalId(commonId,malId);
//        if (videoMainOptional.isPresent()){
//            VideoMain videoMain =videoMainOptional.get();
//            if (videoMain.getMalId().equals(malId)) {
//                videoMain.setFUrl(fileUrl);
//                videoMain.setFName(fileName);
//                videoMainRepo.save(videoMain);
//                return new ResponseEntity<>(videoMain, HttpStatus.OK);
//            }
//        }
//        Optional<VideoMain>videoMainOptional1 =videoMainRepo.findByDtIdAndEngId(commonId,malId);
//        if (videoMainOptional1.isPresent()){
//            VideoMain videoMain1 = videoMainOptional1.get();
//            if (videoMain1.getEngId().equals(malId)){
//                videoMain1.setFName(fileName);
//                videoMain1.setFUrl(fileUrl);
//                videoMainRepo.save(videoMain1);
//                return new ResponseEntity<>(videoMain1,HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    public ResponseEntity<?> updateVideoFirst(String malId, String commonId, MultipartFile file) throws IOException{
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
//        // Use the S3Service's uploadLargeFile method to upload the file
//        s3Service.uploadLargeFile(fileName, fileObj);
//        fileObj.delete();
//        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
//        // Retrieve the file URL from S3
//        String fileUrl = s3Service.getFileUrl(fileName);
//        List<VideoFirst>videoFirstOptional = videoFirstRepo.findBydtId(commonId);
//        if (!videoFirstOptional.isEmpty()){
//            for (VideoFirst videoFirst : videoFirstOptional){
//                videoFirst.setFName(fileName);
//                videoFirst.setFUrl(fileUrl);
//                videoFirstRepo.save(videoFirst);
//                return new ResponseEntity<>(videoFirst,HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    public int deleteAudioMain(String malId, String commonId) {
        Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
            if (commonQRParaId.getMalId().equals(malId)){
                List<AudioMain> audioMainList = audionMainRepo.findBydtId(malId);
                if (!audioMainList.isEmpty()){
                    for (AudioMain audioMain : audioMainList){
                        String fileName = audioMain.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audionMainRepo.delete(audioMain);
                        return 1;
                    }
                }
            } else if (commonQRParaId.getEngId().equals(malId)) {
                List<AudioMain> audioMainList = audionMainRepo.findBydtId(malId);
                if (!audioMainList.isEmpty()){
                    for (AudioMain audioMain : audioMainList){
                        String fileName = audioMain.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audionMainRepo.delete(audioMain);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public int deleteAudioFirst(String malId, String commonId) {
        Optional<SubComId>subComIdOptional=subComIdRepo.findByFsCommonId(commonId);
        if (subComIdOptional.isPresent()){
            SubComId subComId = subComIdOptional.get();
            if (subComId.getFsMalId().equals(malId)){
                List<AudioFirst> audioFirstList = audioFirstRepo.findBydtId(malId);
                if (!audioFirstList.isEmpty()){
                    for (AudioFirst audioFirst : audioFirstList){
                        String fileName = audioFirst.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audioFirstRepo.delete(audioFirst);
                        return 1;
                    }
                }
            } else if (subComId.getFsEngId().equals(malId)) {
                List<AudioFirst> audioFirstList = audioFirstRepo.findBydtId(malId);
                if (!audioFirstList.isEmpty()){
                    for (AudioFirst audioFirst : audioFirstList){
                        String fileName = audioFirst.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audioFirstRepo.delete(audioFirst);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    public int deleteVideoByCommonId(String commonId) {
        Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
        Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
        if (commonQRParaIdOptional.isPresent()){
            Optional<VideoMain>videoMainOptional = videoMainRepo.findBydtId(commonId);
            if (videoMainOptional.isPresent()){
                VideoMain videoMain = videoMainOptional.get();
                if (videoMain.getDtId().equals(commonId)){
                    String fileName = videoMain.getFName();
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                    videoMainRepo.delete(videoMain);
                    return 1;
                }
            }
        } else if (subComIdOptional.isPresent()) {
            List<VideoFirst>videoFirstOptional =videoFirstRepo.findBydtId(commonId);
            if (!videoFirstOptional.isEmpty()){
                for (VideoFirst videoFirst : videoFirstOptional){
                    String fileName = videoFirst.getFName();
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                    videoFirstRepo.delete(videoFirst);
                    return 1;
                }
            }
        }
        return 0;
    }

    public ResponseEntity<?> uploadPDF(String uId, MultipartFile file) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Service.uploadLargePdf(fileName, fileObj);
        fileObj.delete();
        String fileUrl = s3Service.getFileUrl(fileName);
        PdfData pdfData = new PdfData(fileName,fileUrl,uId);
        Optional<CommonQRParaId>commonQRParaIdOptional =  commonQRParaIdRepo.findByEngId(uId);
        Optional<CommonQRParaId>commonQRParaIdOptional1 = commonQRParaIdRepo.findByMalId(uId);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
            if (commonQRParaId.getEngId().equals(uId)){
                pdfData.setCommonId(commonQRParaId.getCommonId());
            }
        } else if (commonQRParaIdOptional1.isPresent()) {
            CommonQRParaId commonQRParaId = commonQRParaIdOptional1.get();
            if (commonQRParaId.getMalId().equals(uId)){
                pdfData.setCommonId(commonQRParaId.getCommonId());
            }
        }else {
            pdfData.setCommonId(null);
        }
        pdfRepo.save(pdfData);
        return new ResponseEntity<>(pdfData,HttpStatus.OK);
    }

    public PdfData updatePDF(String malId, String commonId, MultipartFile file, Integer ids) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        //s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        // Use the S3Service's uploadLargeFile method to upload the file
        s3Service.uploadLargePdf(fileName, fileObj);
        fileObj.delete();
        //String fileUrl = s3Client.getUrl(bucketName,fileName).toString();
        // Retrieve the file URL from S3
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<PdfData>pdfDataOptional=pdfRepo.findByuIdAndId(malId,ids);
        if (pdfDataOptional.isPresent()){
            PdfData pdfData  = pdfDataOptional.get();
            if (pdfData.getCommonId().equals(commonId)){
                pdfData.setFName(fileName);
                pdfData.setFUrl(fileUrl);
                pdfRepo.save(pdfData);
                return pdfData;
            }
        }
        return null;
    }

    public ResponseEntity<?> deletePdfMain(String malId) {
        List<PdfData>pdfDataList=pdfRepo.findByuId(malId);
        if (!pdfDataList.isEmpty()){
            for (PdfData pdfData :pdfDataList){
                String fileName =pdfData.getFName();
                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                pdfRepo.delete(pdfData);
                return new ResponseEntity<>("Pdf is deleted",HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteAudioSingleItem(String engId, Integer itemId) {
        Optional<AudioMain>audioMainOptional=audionMainRepo.findByDtIdAndId(engId,itemId);
        Optional<AudioFirst>audioFirstOptional=audioFirstRepo.findByDtIdAndId(engId,itemId);
        if (audioMainOptional.isPresent()){
            AudioMain audioMain = audioMainOptional.get();
            String fileName = audioMain.getFName();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
            audionMainRepo.delete(audioMain);
            return new ResponseEntity<>("Audio is deleted. ",HttpStatus.OK);
        } else if (audioFirstOptional.isPresent()) {
            AudioFirst audioFirst = audioFirstOptional.get();
            String fileName = audioFirst.getFName();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
            audioFirstRepo.delete(audioFirst);
            return new ResponseEntity<>("Audio is deleted. ",HttpStatus.OK);
        }return new ResponseEntity<>("UId : "+engId+"  and itemId : "+itemId+" is not matching. ",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteVideoSingleItem(String commonId, Integer itemId) {
        Optional<VideoMain>videoMainOptional=videoMainRepo.findByDtIdAndId(commonId,itemId);
        Optional<VideoFirst>videoFirstOptional=videoFirstRepo.findByDtIdAndId(commonId,itemId);
        if (videoMainOptional.isPresent()){
            VideoMain videoMain = videoMainOptional.get();
            String fileName = videoMain.getFName();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
            videoMainRepo.delete(videoMain);
            return new ResponseEntity<>("Video is deleted. ",HttpStatus.OK);
        } else if (videoFirstOptional.isPresent()) {
            VideoFirst videoFirst = videoFirstOptional.get();
            String fileName = videoFirst.getFName();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
            videoFirstRepo.delete(videoFirst);
            return new ResponseEntity<>("Video is deleted. ",HttpStatus.OK);
        }return new ResponseEntity<>("CommonId : "+commonId+"  and itemId : "+itemId+" is not matching. ",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deletePdfSingleItem(String malId, Integer itemId) {
        Optional<PdfData>pdfDataOptional=pdfRepo.findByuIdAndId(malId,itemId);
        if (pdfDataOptional.isPresent()){
            PdfData pdfData = pdfDataOptional.get();
            String fileName = pdfData.getFName();
            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
            pdfRepo.delete(pdfData);
            return new ResponseEntity<>("PDF is deleted. ",HttpStatus.OK);
        }return new ResponseEntity<>("UId : "+malId+"  and itemId : "+itemId+" is not matching. ",HttpStatus.NOT_FOUND);
    }
}
