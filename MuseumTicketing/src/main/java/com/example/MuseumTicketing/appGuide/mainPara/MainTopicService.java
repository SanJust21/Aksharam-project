package com.example.MuseumTicketing.appGuide.mainPara;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirstRepo;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMainRepo;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirstRepo;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMainRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.*;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirstRepo;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import com.example.MuseumTicketing.appGuide.video.main.VideoMainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MainTopicService {
    @Autowired
    private MainTopicEngRepo mainTopicEngRepo;
    @Autowired
    private MainTopicMalRepo mainTopicMalRepo;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;
    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;
    @Autowired
    AlphaNumeric alphaNumeric;
    @Autowired
    private AudioMainRepo audioMainRepo;
    @Autowired
    private AudioFirstRepo audioFirstRepo;
    @Autowired
    private VideoMainRepo videoMainRepo;
    @Autowired
    private VideoFirstRepo videoFirstRepo;
    @Autowired
    private ImgDataMainRepo imgDataMainRepo;
    @Autowired
    private ImgDataFirstRepo imgDataFirstRepo;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private ErrorService errorService;


    public ResponseEntity<?> addMainParaData(MainParaDTO mainParaDTO, Integer dId) {
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dId);
            if (dataTypeOptional.isPresent()){
                DataType dataType1 = dataTypeOptional.get();
                String type = dataType1.getTalk();
                if (type!=null&&"English".equalsIgnoreCase(type)){

                    Optional<MainTopicEng> existingTopic = mainTopicEngRepo.findByTopic(mainParaDTO.getTopic());
                    Optional<MainTopicMal> existingMal = mainTopicMalRepo.findByTopic(mainParaDTO.getTopic());
                    if(existingTopic.isPresent()){
                        String existTopic = mainParaDTO.getTopic();
                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
                    }
                    if (existingMal.isPresent()){
                        String existTopic = mainParaDTO.getTopic();
                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
                    }
                    String randomId = alphaNumeric.generateRandomNumber();
                    MainTopicEng mainTopicEng = new MainTopicEng();
                    mainTopicEng.setTopic(mainParaDTO.getTopic());
                    mainTopicEng.setDescription(mainParaDTO.getDescription());
                    mainTopicEng.setRef(mainParaDTO.getRefURL());
                    mainTopicEng.setMEngUid(randomId);
                    mainTopicEngRepo.save(mainTopicEng);
                    return new ResponseEntity<>(mainTopicEng, HttpStatus.OK);
                } else if (type!=null && "Malayalam".equalsIgnoreCase(type)) {
                    Optional<MainTopicMal> existingTopic = mainTopicMalRepo.findByTopic(mainParaDTO.getTopic());
                    if(existingTopic.isPresent())
                    {
                        String existTopic = mainParaDTO.getTopic();
                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
                    }
                    String randomId = alphaNumeric.generateRandomNumber();
                    MainTopicMal mainTopicMal= new MainTopicMal();
                    mainTopicMal.setTopic(mainParaDTO.getTopic());
                    mainTopicMal.setDescription(mainParaDTO.getDescription());
                    mainTopicMal.setRef(mainParaDTO.getRefURL());
                    mainTopicMal.setMMalUid(randomId);
                    mainTopicMalRepo.save(mainTopicMal);
                    return new ResponseEntity<>(mainTopicMal, HttpStatus.OK);
                }
            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> addFirstParaData(MainParaDTO mainParaDTO, Integer dId, String uId) {
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dId);
            if (dataTypeOptional.isPresent()){
                DataType dataType1 = dataTypeOptional.get();
                String type = dataType1.getTalk();
                if (type!=null&&"English".equalsIgnoreCase(type)){
//                    Optional<FirstTopicEng> existingTopic = firstTopicEngRepo.findByTopic(mainParaDTO.getTopic());
//                    if(existingTopic.isPresent()){
//                        String existTopic = mainParaDTO.getTopic();
//                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
//                    }
                    String randomId = alphaNumeric.generateRandomNumber();
                    FirstTopicEng firstTopicEng = new FirstTopicEng();
                    firstTopicEng.setTopic(mainParaDTO.getTopic());
                    firstTopicEng.setDescription(mainParaDTO.getDescription());
                    firstTopicEng.setRef(mainParaDTO.getRefURL());
                    firstTopicEng.setFsUid(randomId);
                    Optional<MainTopicEng>mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(uId);
                    if (mainTopicEngOptional.isPresent()){
                        MainTopicEng mainTopicEng = mainTopicEngOptional.get();
                        firstTopicEng.setMainUid(mainTopicEng.getMEngUid());
                    }
                    firstTopicEngRepo.save(firstTopicEng);
                    return new ResponseEntity<>(firstTopicEng, HttpStatus.OK);

                } else if (type!=null && "Malayalam".equalsIgnoreCase(type)) {
//                    Optional<FirstTopicMal> existingTopic = firstTopicMalRepo.findByTopic(mainParaDTO.getTopic());
//                    if(existingTopic.isPresent()){
//                        String existTopic = mainParaDTO.getTopic();
//                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
//                    }
                    String randomId = alphaNumeric.generateRandomNumber();
                    FirstTopicMal firstTopicMal = new FirstTopicMal();
                    firstTopicMal.setTopic(mainParaDTO.getTopic());
                    firstTopicMal.setDescription(mainParaDTO.getDescription());
                    firstTopicMal.setRef(mainParaDTO.getRefURL());
                    firstTopicMal.setFsUid(randomId);
                    Optional<MainTopicMal>mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(uId);
                    if (mainTopicMalOptional.isPresent()){
                        MainTopicMal mainTopicMal = mainTopicMalOptional.get();
                        firstTopicMal.setMainUid(mainTopicMal.getMMalUid());
                    }
                    firstTopicMalRepo.save(firstTopicMal);
                    return new ResponseEntity<>(firstTopicMal, HttpStatus.OK);
                }
            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> generateComId(String engId, String malId) {
        try {
            Optional<SubComId> subComIdOptional = subComIdRepo.findByFsEngIdAndFsMalId(engId,malId);
            if (subComIdOptional.isPresent()){
                SubComId subComId = subComIdOptional.get();
                if (subComId.getFsEngId().equals(engId)&&subComId.getFsMalId().equals(malId)){
                    return new ResponseEntity<>("CommonId : "+subComId.getFsCommonId(),HttpStatus.OK);
                }
            }else {
                SubComId comId = new SubComId();
                comId.setFsMalId(malId);
                comId.setFsEngId(engId);
                String genId = alphaNumeric.generateRandomNumber();
                comId.setFsCommonId(genId);
                subComIdRepo.save(comId);
                return new ResponseEntity<>(comId,HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<?> updateMainPara(String malId, String commonId, MainParaDTO mainParaDTO) {
        try {
            Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
            if (commonQRParaIdOptional.isPresent()){
                CommonQRParaId commonQRParaId =commonQRParaIdOptional.get();
                if (commonQRParaId.getMalId().equals(malId)){
                    Optional<MainTopicMal>mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(malId);
                    if (mainTopicMalOptional.isPresent()){
                        MainTopicMal mainTopicMal = mainTopicMalOptional.get();
                        mainTopicMal.setTopic(mainParaDTO.getTopic());
                        mainTopicMal.setDescription(mainParaDTO.getDescription());
                        mainTopicMal.setRef(mainParaDTO.getRefURL());
                        mainTopicMalRepo.save(mainTopicMal);
                        return new ResponseEntity<>(mainTopicMal.getTopic()+" is updated.",HttpStatus.OK);
                    }else {
                        return new ResponseEntity<>("Malayalam Uid is not present",HttpStatus.NO_CONTENT);
                    }
                } else if (commonQRParaId.getEngId().equals(malId)) {
                    Optional<MainTopicEng> mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(malId);
                    if (mainTopicEngOptional.isPresent()){
                        MainTopicEng mainTopicEng = mainTopicEngOptional.get();
                        mainTopicEng.setTopic(mainParaDTO.getTopic());
                        mainTopicEng.setDescription(mainParaDTO.getDescription());
                        mainTopicEng.setRef(mainParaDTO.getRefURL());
                        mainTopicEngRepo.save(mainTopicEng);
                        return new ResponseEntity<>(mainTopicEng.getTopic()+" is updated.",HttpStatus.OK);
                    }
                }else {
                    return new ResponseEntity<>("CommonId : "+commonId+"  and uniqueId : "+malId+"   are not matching",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateSubPara(String malId, String commonId, MainParaDTO mainParaDTO) {
        try {
            Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
            if (subComIdOptional.isPresent()){
                SubComId subComId = subComIdOptional.get();
                if (subComId.getFsMalId().equals(malId)){
                    Optional<FirstTopicMal>firstTopicMalOptional =firstTopicMalRepo.findByFsUid(malId);
                    if (firstTopicMalOptional.isPresent()){
                        FirstTopicMal firstTopicMal =firstTopicMalOptional.get();
                        firstTopicMal.setTopic(mainParaDTO.getTopic());
                        firstTopicMal.setDescription(mainParaDTO.getDescription());
                        firstTopicMal.setRef(mainParaDTO.getRefURL());
                        firstTopicMalRepo.save(firstTopicMal);
                        return new ResponseEntity<>(firstTopicMal.getTopic()+" is updated. ",HttpStatus.OK);
                    }
                } else if (subComId.getFsEngId().equals(malId)) {
                   Optional<FirstTopicEng>firstTopicEngOptional = firstTopicEngRepo.findByfsUid(malId);
                   if (firstTopicEngOptional.isPresent()){
                       FirstTopicEng firstTopicEng =firstTopicEngOptional.get();
                       firstTopicEng.setTopic(mainParaDTO.getTopic());
                       firstTopicEng.setDescription(mainParaDTO.getDescription());
                       firstTopicEng.setRef(mainParaDTO.getRefURL());
                       firstTopicEngRepo.save(firstTopicEng);
                       return new ResponseEntity<>(firstTopicEng.getTopic()+" is updated. ",HttpStatus.OK);
                   }
                }else {
                    return new ResponseEntity<>("CommonId : "+commonId+"  and uniqueId : "+malId+"   are not matching",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    public ResponseEntity<?> DeleteMainPara(String malId, String engId, String commonId) {
        Optional<MainTopicEng>mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(engId);
        Optional<MainTopicMal>mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(malId);
        if (mainTopicEngOptional.isPresent()){
            MainTopicEng mainTopicEng = mainTopicEngOptional.get();
            if (mainTopicEng.getMEngUid().equals(engId)){
                mainTopicEngRepo.delete(mainTopicEng);
            }
            List<AudioMain>audioMainList = audioMainRepo.findBydtId(engId);
            if (!audioMainList.isEmpty()){
                for (AudioMain audioMain : audioMainList){
                    String fileName = audioMain.getFName();
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                    audioMainRepo.delete(audioMain);
                }
            }
            List<FirstTopicEng>firstTopicEngList = firstTopicEngRepo.findByMainUid(engId);
            if (!firstTopicEngList.isEmpty()){
                for (FirstTopicEng firstTopicEng : firstTopicEngList){
                    String fsUid = firstTopicEng.getFsUid();
                    List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(engId);
                    if (!audioFirstList.isEmpty()){
                        for (AudioFirst audioFirst : audioFirstList){
                            String fileName = audioFirst.getFName();
                            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                            audioFirstRepo.delete(audioFirst);
                        }
                    }
                    SubComId subComIdOptional = subComIdRepo.findByfsEngId(fsUid);
                    if (subComIdOptional.getFsEngId().equals(fsUid)){
                        String fsCommonId = subComIdOptional.getFsCommonId();
                        List<ImgDataFirst>imgDataFirstList = imgDataFirstRepo.findByCommonId(fsCommonId);
                        if (!imgDataFirstList.isEmpty()){
                            for (ImgDataFirst imgDataFirst : imgDataFirstList){
                                String fileName = imgDataFirst.getFName();
                                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                                imgDataFirstRepo.delete(imgDataFirst);
                            }
                        }
                        List<VideoFirst>videoFirstList = videoFirstRepo.findBydtId(fsCommonId);
                        if (!videoFirstList.isEmpty()){
                            for (VideoFirst videoFirst : videoFirstList){
                                String fileName = videoFirst.getFName();
                                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                                videoFirstRepo.delete(videoFirst);
                            }
                        }
                    }
                    firstTopicEngRepo.delete(firstTopicEng);
                }
            }

            if (mainTopicMalOptional.isPresent()){
                MainTopicMal mainTopicMal = mainTopicMalOptional.get();
                if (mainTopicMal.getMMalUid().equals(malId)){
                    mainTopicMalRepo.delete(mainTopicMal);
                }
                List<AudioMain>audioMainListMal = audioMainRepo.findBydtId(malId);
                if (!audioMainListMal.isEmpty()){
                    for (AudioMain audioMain : audioMainListMal){
                        String fileName = audioMain.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audioMainRepo.delete(audioMain);
                    }
                }
                List<FirstTopicMal>firstTopicMalList = firstTopicMalRepo.findByMainUid(malId);
                if (!firstTopicMalList.isEmpty()){
                    for (FirstTopicMal firstTopicMal : firstTopicMalList){
                        String fsMalId = firstTopicMal.getFsUid();
                        List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(malId);
                        if (!audioFirstList.isEmpty()){
                            for (AudioFirst audioFirst : audioFirstList){
                                String fileName = audioFirst.getFName();
                                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                                audioFirstRepo.delete(audioFirst);
                            }
                        }
                        SubComId subComIdOptional = subComIdRepo.findByfsMalId(fsMalId);
                        if (subComIdOptional.getFsMalId().equals(fsMalId)){
                            subComIdRepo.delete(subComIdOptional);
                        }
                        firstTopicMalRepo.delete(firstTopicMal);
                    }
                }
            }

            Optional<VideoMain>videoMainOptional=videoMainRepo.findBydtId(commonId);
            if (videoMainOptional.isPresent()){
                VideoMain videoMain = videoMainOptional.get();
                String fileName = videoMain.getFName();
                s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                videoMainRepo.delete(videoMain);
            }
            List<ImgDataMain>imgDataMainList = imgDataMainRepo.findByCommonId(commonId);
            if (!imgDataMainList.isEmpty()){
                for (ImgDataMain imgDataMain : imgDataMainList){
                    String fileName = imgDataMain.getFName();
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                    imgDataMainRepo.delete(imgDataMain);
                }
            }
            Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
            if (commonQRParaIdOptional.isPresent()){
                CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                if (commonQRParaId.getEngId().equals(engId)&&commonQRParaId.getMalId().equals(malId)){
                    commonQRParaIdRepo.delete(commonQRParaId);
                }
            }
            return new ResponseEntity<>("Main topic is delete",HttpStatus.OK);
        }
        return new ResponseEntity<>("Main topic isn't deleted.Id isn't present ",HttpStatus.BAD_REQUEST);
    }


    @Transactional
    public ResponseEntity<?> DeleteSubPara(String malId, String engId, String commonId) {
        List<FirstTopicEng>firstTopicEngList = firstTopicEngRepo.findByMainUid(engId);
        if (!firstTopicEngList.isEmpty()){
            for (FirstTopicEng firstTopicEng : firstTopicEngList){
                String fsUid = firstTopicEng.getFsUid();
                List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(engId);
                if (!audioFirstList.isEmpty()){
                    for (AudioFirst audioFirst : audioFirstList){
                        String fileName = audioFirst.getFName();
                        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                        audioFirstRepo.delete(audioFirst);
                    }
                }
                SubComId subComIdOptional = subComIdRepo.findByfsEngId(fsUid);
                if (subComIdOptional.getFsEngId().equals(fsUid)){
                    String fsCommonId = subComIdOptional.getFsCommonId();
                    List<ImgDataFirst>imgDataFirstList = imgDataFirstRepo.findByCommonId(fsCommonId);
                    if (!imgDataFirstList.isEmpty()){
                        for (ImgDataFirst imgDataFirst : imgDataFirstList){
                            String fileName = imgDataFirst.getFName();
                            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                            imgDataFirstRepo.delete(imgDataFirst);
                        }
                    }
                    List<VideoFirst>videoFirstList = videoFirstRepo.findBydtId(fsCommonId);
                    if (!videoFirstList.isEmpty()){
                        for (VideoFirst videoFirst : videoFirstList){
                            String fileName = videoFirst.getFName();
                            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                            videoFirstRepo.delete(videoFirst);
                        }
                    }
                }
                firstTopicEngRepo.delete(firstTopicEng);
            }
            List<FirstTopicMal>firstTopicMalList = firstTopicMalRepo.findByMainUid(malId);
            if (!firstTopicMalList.isEmpty()){
                for (FirstTopicMal firstTopicMal : firstTopicMalList){
                    String fsMalId = firstTopicMal.getFsUid();
                    List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(malId);
                    if (!audioFirstList.isEmpty()){
                        for (AudioFirst audioFirst : audioFirstList){
                            String fileName = audioFirst.getFName();
                            s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
                            audioFirstRepo.delete(audioFirst);
                        }
                    }
                    SubComId subComIdOptional = subComIdRepo.findByfsMalId(fsMalId);
                    if (subComIdOptional.getFsMalId().equals(fsMalId)){
                        subComIdRepo.delete(subComIdOptional);
                    }
                    firstTopicMalRepo.delete(firstTopicMal);
                }
            }
            return new ResponseEntity<>("Sub topic is deleted.",HttpStatus.OK);
        }return new ResponseEntity<>("Sub topic isn't deleted.Id isn't present",HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> deleteMalayalamMainTopicByUid(String uId) {
        Optional<MainTopicMal>mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(uId);
        if (mainTopicMalOptional.isPresent()){
            MainTopicMal mainTopicMal =mainTopicMalOptional.get();
            if (mainTopicMal.getMMalUid().equals(uId)){
                List<AudioMain>audioMainList = audioMainRepo.findBydtId(uId);
                if (!audioMainList.isEmpty()){
                    for (AudioMain audioMain : audioMainList){
                        audioMainRepo.delete(audioMain);
                    }
                }
                List<FirstTopicMal>firstTopicMalList = firstTopicMalRepo.findByMainUid(uId);
                if (!firstTopicMalList.isEmpty()){
                    for (FirstTopicMal firstTopicMal : firstTopicMalList){
                        String fsMal = firstTopicMal.getFsUid();
                        firstTopicMalRepo.delete(firstTopicMal);

                        List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(fsMal);
                        if (!audioFirstList.isEmpty()){
                            for (AudioFirst audioFirst : audioFirstList){
                                audioFirstRepo.delete(audioFirst);
                            }
                        }
                    }
                }
                mainTopicMalRepo.delete(mainTopicMal);
                return new ResponseEntity<>("Main topic is Deleted.",HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Main topic malayalam is not deleted....",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteEnglishMainTopicByUid(String uId) {
        Optional<MainTopicEng>mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(uId);
        if (mainTopicEngOptional.isPresent()){
            MainTopicEng mainTopicEng = mainTopicEngOptional.get();
            if (mainTopicEng.getMEngUid().equals(uId)){
                List<AudioMain>audioMainList =audioMainRepo.findBydtId(uId);
                if (!audioMainList.isEmpty()){
                    for (AudioMain audioMain : audioMainList){
                        audioMainRepo.delete(audioMain);
                    }
                }
                List<FirstTopicEng>firstTopicEngList = firstTopicEngRepo.findByMainUid(uId);
                if (!firstTopicEngList.isEmpty()){
                    for (FirstTopicEng firstTopicEng :firstTopicEngList){
                        String fsEngId = firstTopicEng.getFsUid();
                        List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(fsEngId);
                        if (!audioFirstList.isEmpty()){
                            for (AudioFirst audioFirst : audioFirstList){
                                audioFirstRepo.delete(audioFirst);
                            }
                        }
                        firstTopicEngRepo.delete(firstTopicEng);
                    }
                }
                mainTopicEngRepo.delete(mainTopicEng);
                return new ResponseEntity<>("Main topic english is deleted",HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Main topic english isn't deleted",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteSubTopicByCommonId(String commonId) {
        Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
        if (subComIdOptional.isPresent()){
            SubComId subComId = subComIdOptional.get();
            if (subComId.getFsCommonId().equals(commonId)){
                String fsEngId = subComId.getFsEngId();
                String fsMalId = subComId.getFsMalId();
                Optional<FirstTopicEng>firstTopicEngOptional = firstTopicEngRepo.findByfsUid(fsEngId);
                if (firstTopicEngOptional.isPresent()){
                    FirstTopicEng firstTopicEng = firstTopicEngOptional.get();
                    List<AudioFirst>audioFirstList = audioFirstRepo.findBydtId(fsEngId);
                    if (!audioFirstList.isEmpty()){
                        for (AudioFirst audioFirst : audioFirstList){
                            audioFirstRepo.delete(audioFirst);
                        }
                    }
                    List<ImgDataFirst>imgDataFirstOptional = imgDataFirstRepo.findByCommonId(commonId);
                    if (!imgDataFirstOptional.isEmpty()){
                        for (ImgDataFirst imgDataFirst : imgDataFirstOptional){
                            imgDataFirstRepo.delete(imgDataFirst);
                        }
                    }
                    List<VideoFirst>videoFirstList = videoFirstRepo.findBydtId(commonId);
                    if (!videoFirstList.isEmpty()){
                        for (VideoFirst videoFirst : videoFirstList){
                            videoFirstRepo.delete(videoFirst);
                        }
                    }
                    firstTopicEngRepo.delete(firstTopicEng);
                }
                Optional<FirstTopicMal>firstTopicMalOptional = firstTopicMalRepo.findByFsUid(fsMalId);
                if (firstTopicMalOptional.isPresent()){
                    FirstTopicMal firstTopicMal = firstTopicMalOptional.get();
                    List<AudioFirst>audioFirstList =audioFirstRepo.findBydtId(fsMalId);
                    if (!audioFirstList.isEmpty()){
                        for (AudioFirst audioFirst : audioFirstList){
                            audioFirstRepo.delete(audioFirst);
                        }
                    }
                    firstTopicMalRepo.delete(firstTopicMal);
                }
                subComIdRepo.delete(subComId);
                return new ResponseEntity<>("First sub deleted successfully",HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>("CommonId is not correct",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("FirstSub is not deleted.Try agin",HttpStatus.BAD_REQUEST);
    }
}
