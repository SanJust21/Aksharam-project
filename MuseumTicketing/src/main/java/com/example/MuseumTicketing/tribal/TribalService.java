package com.example.MuseumTicketing.tribal;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Guide.util.S3Service;
import com.example.MuseumTicketing.tribal.commonId.TribalCommonId;
import com.example.MuseumTicketing.tribal.commonId.TribalCommonIdRepo;
import com.example.MuseumTicketing.tribal.tribEnglish.TribalEnglish;
import com.example.MuseumTicketing.tribal.tribEnglish.TribalEnglishRepo;
import com.example.MuseumTicketing.tribal.tribMalayalam.CombinedTribal;
import com.example.MuseumTicketing.tribal.tribMalayalam.TribalMalayalam;
import com.example.MuseumTicketing.tribal.tribMalayalam.TribalMalayalamRepo;
import com.example.MuseumTicketing.tribal.tribalVideo.TribalVideo;
import com.example.MuseumTicketing.tribal.tribalVideo.TribalVideoDto;
import com.example.MuseumTicketing.tribal.tribalVideo.TribalVideoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TribalService {
    @Autowired
    private TribalMalayalamRepo tribalMalayalamRepo;
    @Autowired
    private TribalEnglishRepo tribalEnglishRepo;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private TribalCommonIdRepo tribalCommonIdRepo;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private TribalVideoRepo tribalVideoRepo;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;


    public ResponseEntity<?> addTribalMalayalamData(TribalDto tribalDto) {
        Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTitle(tribalDto.getTitle());
        if (tribalMalayalamOptional.isPresent()){
            TribalMalayalam tribalMalayalam = tribalMalayalamOptional.get();
            String title = tribalMalayalam.getTitle();
            return new ResponseEntity<>(title+" is present", HttpStatus.CONFLICT);
        }
        TribalMalayalam tribalMalayalam = new TribalMalayalam();
        tribalMalayalam.setTitle(tribalDto.getTitle());
        tribalMalayalam.setDescription(tribalDto.getDescription());
        tribalMalayalam.setTribMalUid(alphaNumeric.generateRandomNumber());
        tribalMalayalamRepo.save(tribalMalayalam);
        return new ResponseEntity<>(tribalMalayalam,HttpStatus.OK);
    }

    public ResponseEntity<?> addTribalEnglishData(TribalDto tribalDto) {
        Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTitle(tribalDto.getTitle());
        if (tribalEnglishOptional.isPresent()){
            TribalEnglish tribalEnglish=tribalEnglishOptional.get();
            String title=tribalEnglish.getTitle();
            return new ResponseEntity<>(title+" is already present.",HttpStatus.CONFLICT);
        }
        TribalEnglish tribalEnglish =new TribalEnglish();
        tribalEnglish.setTitle(tribalDto.getTitle());
        tribalEnglish.setDescription(tribalDto.getDescription());
        tribalEnglish.setTribEngUid(alphaNumeric.generateRandomNumber());
        tribalEnglishRepo.save(tribalEnglish);
        return new ResponseEntity<>(tribalEnglish,HttpStatus.OK);
    }

    public ResponseEntity<?> generateCommonId(String malId, String engId) {
        Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByMalayalamIdAndEnglishId(malId,engId);
        if (tribalCommonIdOptional.isPresent()){
            TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
            return new ResponseEntity<>(tribalCommonId,HttpStatus.OK);
        }else {
            TribalCommonId tribalCommonId =new TribalCommonId();
            tribalCommonId.setEnglishId(engId);
            tribalCommonId.setMalayalamId(malId);
            tribalCommonId.setCommonId(alphaNumeric.generateRandomNumber());
            tribalCommonIdRepo.save(tribalCommonId);
            return new ResponseEntity<>(tribalCommonId,HttpStatus.OK);
        }
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

    public ResponseEntity<?> uploadVideo(String malId, String engId, String commonId, MultipartFile[] file) throws IOException{
        List<TribalVideoDto> response = new ArrayList<>();
        for (MultipartFile files : file){
            TribalVideoDto tribalVideoDto = uploadTribalVideo(files,commonId,malId,engId);
            response.add(tribalVideoDto);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private TribalVideoDto uploadTribalVideo(MultipartFile files, String commonId, String malId, String engId) throws IOException{
        File fileObj = convertMultiPartFileToFile(files);
        String fileName =System.currentTimeMillis()+"_"+files.getOriginalFilename();
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        String fileUrl = s3Service.getFileUrl(fileName);
        TribalVideo tribalVideo = new TribalVideo(fileName,fileUrl,commonId);
        tribalVideo.setEnglishId(engId);
        tribalVideo.setMalayalamId(malId);
        tribalVideoRepo.save(tribalVideo);
        return new TribalVideoDto(fileName,fileUrl,commonId,malId,engId);
    }

    public ResponseEntity<List<CombinedTribalData>> getMalayalamDetails(String commonId, String malId) {
        List<CombinedTribalData>combinedTribalDataList=new ArrayList<>();
        List<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findBytribMalUid(malId);
        if (!tribalMalayalamOptional.isEmpty()){
            for (TribalMalayalam tribalMalayalam : tribalMalayalamOptional){
                CombinedTribalData combinedTribalData =new CombinedTribalData();
                combinedTribalData.setTitle(tribalMalayalam.getTitle());
                combinedTribalData.setDescription(tribalMalayalam.getDescription());
                combinedTribalData.setUniqueId(tribalMalayalam.getTribMalUid());

                Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByCommonId(commonId);
                if (tribalCommonIdOptional.isPresent()){
                    TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                    combinedTribalData.setTribalCommonId(tribalCommonId.getCommonId());
                    combinedTribalData.setMalayalamId(tribalCommonId.getMalayalamId());
                    combinedTribalData.setEnglishId(tribalCommonId.getEnglishId());
                }

                List<TribalVideo>tribalVideoList=tribalVideoRepo.findBycommonId(commonId);
                if (!tribalVideoList.isEmpty()){
                    tribalVideoList.sort(Comparator.comparing(TribalVideo::getId));
                    combinedTribalData.setTribalVideoList(tribalVideoList);
                }
                combinedTribalDataList.add(combinedTribalData);
            }
        }
        return new ResponseEntity<>(combinedTribalDataList,HttpStatus.OK);
    }

    public ResponseEntity<List<CombinedTribalData>> getEnglishDetails(String commonId, String engId) {
        List<CombinedTribalData>combinedTribalDataList=new ArrayList<>();
        List<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findBytribEngUid(engId);
        if (!tribalEnglishOptional.isEmpty()){
            for (TribalEnglish tribalEnglish:tribalEnglishOptional){
                CombinedTribalData combinedTribalData =new CombinedTribalData();
                combinedTribalData.setTitle(tribalEnglish.getTitle());
                combinedTribalData.setDescription(tribalEnglish.getDescription());
                combinedTribalData.setUniqueId(tribalEnglish.getTribEngUid());
                Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByCommonId(commonId);
                if (tribalCommonIdOptional.isPresent()){
                    TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                    combinedTribalData.setTribalCommonId(tribalCommonId.getCommonId());
                    combinedTribalData.setMalayalamId(tribalCommonId.getMalayalamId());
                    combinedTribalData.setEnglishId(tribalCommonId.getEnglishId());
                }
                List<TribalVideo>tribalVideoList=tribalVideoRepo.findBycommonId(commonId);
                if (!tribalVideoList.isEmpty()){
                    tribalVideoList.sort(Comparator.comparing(TribalVideo::getId));
                    combinedTribalData.setTribalVideoList(tribalVideoList);
                }
                combinedTribalDataList.add(combinedTribalData);
            }
        }
        return new ResponseEntity<>(combinedTribalDataList,HttpStatus.OK);
    }


    public ResponseEntity<List<CombinedTribalData>> getDetailsByLanguage(Integer dType) {
        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dType);
        if (dataTypeOptional.isPresent()){
            DataType dataType = dataTypeOptional.get();
            String type= dataType.getTalk();
            if ("Malayalam".equalsIgnoreCase(type)){
                List<CombinedTribalData>combinedTribalDataList=new ArrayList<>();
                List<TribalMalayalam>tribalMalayalamList=tribalMalayalamRepo.findAll();

                if (!tribalMalayalamList.isEmpty()){
                    for (TribalMalayalam tribalMalayalam:tribalMalayalamList){
                        CombinedTribalData combinedTribalData =new CombinedTribalData();
                        combinedTribalData.setTitle(tribalMalayalam.getTitle());
                        combinedTribalData.setDescription(tribalMalayalam.getDescription());
                        combinedTribalData.setUniqueId(tribalMalayalam.getTribMalUid());

                        Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByMalayalamId(tribalMalayalam.getTribMalUid());
                        if (tribalCommonIdOptional.isPresent()){
                            TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                            combinedTribalData.setTribalCommonId(tribalCommonId.getCommonId());
                            combinedTribalData.setMalayalamId(tribalCommonId.getMalayalamId());
                            combinedTribalData.setEnglishId(tribalCommonId.getEnglishId());
                        }

                        List<TribalVideo>tribalVideoList=tribalVideoRepo.findBymalayalamId(tribalMalayalam.getTribMalUid());
                        if (!tribalVideoList.isEmpty()){
                            tribalVideoList.sort(Comparator.comparing(TribalVideo::getId));
                            combinedTribalData.setTribalVideoList(tribalVideoList);
                        }
                        combinedTribalDataList.add(combinedTribalData);
                    }
                }
                return new ResponseEntity<>(combinedTribalDataList,HttpStatus.OK);

            } else if ("English".equalsIgnoreCase(type)) {
                List<CombinedTribalData>combinedTribalDataList=new ArrayList<>();
                List<TribalEnglish> tribalEnglishOptional=tribalEnglishRepo.findAll();

                if (!tribalEnglishOptional.isEmpty()){
                    for (TribalEnglish tribalEnglish : tribalEnglishOptional){
                        CombinedTribalData combinedTribalData =new CombinedTribalData();
                        combinedTribalData.setTitle(tribalEnglish.getTitle());
                        combinedTribalData.setDescription(tribalEnglish.getDescription());
                        combinedTribalData.setUniqueId(tribalEnglish.getTribEngUid());

                        Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByEnglishId(tribalEnglish.getTribEngUid());
                        if (tribalCommonIdOptional.isPresent()){
                            TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                            combinedTribalData.setTribalCommonId(tribalCommonId.getCommonId());
                            combinedTribalData.setMalayalamId(tribalCommonId.getMalayalamId());
                            combinedTribalData.setEnglishId(tribalCommonId.getEnglishId());
                        }

                        List<TribalVideo>tribalVideoList=tribalVideoRepo.findByenglishId(tribalEnglish.getTribEngUid());
                        if (!tribalVideoList.isEmpty()){
                            tribalVideoList.sort(Comparator.comparing(TribalVideo::getId));
                            combinedTribalData.setTribalVideoList(tribalVideoList);
                        }
                        combinedTribalDataList.add(combinedTribalData);
                    }
                }
                return new ResponseEntity<>(combinedTribalDataList,HttpStatus.OK);
            }
        }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<CombinedTribal>> getAllUidDetails(Integer dtId) {
        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dtId);
        List<CombinedTribal>combinedTribalList=new ArrayList<>();
        CombinedTribal combinedTribal = new CombinedTribal();
        if (dataTypeOptional.isPresent()){
            DataType dataType = dataTypeOptional.get();
            String type = dataType.getTalk();
            if ("Malayalam".equalsIgnoreCase(type)){
                List<TribalMalayalam>tribalMalayalamList=tribalMalayalamRepo.findAll();
                if (!tribalMalayalamList.isEmpty()){
                    TribalMalayalam tribalMalayalam = tribalMalayalamList.get(0);
                    combinedTribal.setTitle(tribalMalayalam.getTitle());
                    combinedTribal.setDescription(tribalMalayalam.getDescription());
                    combinedTribal.setUniqueId(tribalMalayalam.getTribMalUid());

                }
                combinedTribalList.add(combinedTribal);
                return new ResponseEntity<>(combinedTribalList,HttpStatus.OK);
            } else if ("English".equalsIgnoreCase(type)) {
                List<TribalEnglish>tribalEnglishList=tribalEnglishRepo.findAll();
                if (!tribalEnglishList.isEmpty()){
                    TribalEnglish tribalEnglish = tribalEnglishList.get(0);
                    combinedTribal.setTitle(tribalEnglish.getTitle());
                    combinedTribal.setDescription(tribalEnglish.getDescription());
                    combinedTribal.setUniqueId(tribalEnglish.getTribEngUid());
                }
                combinedTribalList.add(combinedTribal);
                return new ResponseEntity<>(combinedTribalList,HttpStatus.OK);
            }
        }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateTribalData(String uniqueId, TribalDto tribalDto) {
        Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(uniqueId);
        Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(uniqueId);
        if (tribalEnglishOptional.isPresent()){
            TribalEnglish tribalEnglish = tribalEnglishOptional.get();
            tribalEnglish.setTitle(tribalDto.getTitle());
            tribalEnglish.setDescription(tribalDto.getDescription());
            tribalEnglishRepo.save(tribalEnglish);
            return new ResponseEntity<>(tribalEnglish,HttpStatus.OK);
        } else if (tribalMalayalamOptional.isPresent()) {
            TribalMalayalam tribalMalayalam = tribalMalayalamOptional.get();
            tribalMalayalam.setTitle(tribalDto.getTitle());
            tribalMalayalam.setDescription(tribalDto.getDescription());
            tribalMalayalamRepo.save(tribalMalayalam);
            return new ResponseEntity<>(tribalMalayalam,HttpStatus.OK);
        }
        return new ResponseEntity<>("UniqueId isn't valid",HttpStatus.NOT_FOUND);
    }

    public TribalVideo updateTribalVideo(String commonId, Integer tId, MultipartFile file) throws IOException{
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Service.uploadLargeFile(fileName, fileObj);
        fileObj.delete();
        String fileUrl = s3Service.getFileUrl(fileName);
        Optional<TribalVideo>tribalVideoOptional=tribalVideoRepo.findByCommonIdAndId(commonId,tId);
        if (tribalVideoOptional.isPresent()){
            TribalVideo tribalVideo = tribalVideoOptional.get();
            tribalVideo.setFileUrl(fileUrl);
            tribalVideo.setFileName(fileName);
            tribalVideoRepo.save(tribalVideo);
            return tribalVideo;
        }else {
            return null;
        }
    }

    @Transactional
    public ResponseEntity<?> deleteDataByCommonId(String commonId, String malId, String engId) {
        Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(malId);
        if (tribalMalayalamOptional.isPresent()){
            TribalMalayalam tribalMalayalam = tribalMalayalamOptional.get();
            tribalMalayalamRepo.delete(tribalMalayalam);
        }

        Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(engId);
        if (tribalEnglishOptional.isPresent()){
            TribalEnglish tribalEnglish = tribalEnglishOptional.get();
            tribalEnglishRepo.delete(tribalEnglish);
        }

        Optional<TribalVideo>tribalVideoOptional=tribalVideoRepo.findByCommonId(commonId);
        if (tribalVideoOptional.isPresent()){
            TribalVideo tribalVideo=tribalVideoOptional.get();
            tribalVideoRepo.delete(tribalVideo);
        }

        Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByCommonId(commonId);
        if (tribalCommonIdOptional.isPresent()){
            TribalCommonId tribalCommonId =tribalCommonIdOptional.get();
            tribalCommonIdRepo.delete(tribalCommonId);
        }
        return new ResponseEntity<>("All details are Deleted. ",HttpStatus.OK);
    }

    public ResponseEntity<?> deleteByUniqueId(String uId) {
        Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(uId);
        Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(uId);
        if (tribalEnglishOptional.isPresent()){
            TribalEnglish tribalEnglish = tribalEnglishOptional.get();
            tribalEnglishRepo.delete(tribalEnglish);
            return new ResponseEntity<>("Data is deleted",HttpStatus.OK);
        } else if (tribalMalayalamOptional.isPresent()) {
            TribalMalayalam tribalMalayalam = tribalMalayalamOptional.get();
            tribalMalayalamRepo.delete(tribalMalayalam);
            return new ResponseEntity<>("Data is deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id isn't present",HttpStatus.BAD_REQUEST);
    }

    public void deleteFileFromS3(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            // Handle Amazon Service Exception
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Handle SDK Client Exception
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> deleteVideo(String commonId, Integer tId) {
        Optional<TribalVideo>tribalVideoOptional=tribalVideoRepo.findByCommonIdAndId(commonId,tId);
        if (tribalVideoOptional.isPresent()){
            TribalVideo tribalVideo = tribalVideoOptional.get();
            String fileName = tribalVideo.getFileName();
            deleteFileFromS3(fileName);
            tribalVideoRepo.delete(tribalVideo);
            return new ResponseEntity<>("Video is deleted successfully",HttpStatus.OK);
        }return new ResponseEntity<>("Video isn't deleted",HttpStatus.BAD_REQUEST);
    }



}
