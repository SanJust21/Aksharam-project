package com.example.MuseumTicketing.appGuide.mainPara;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.*;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    AlphaNumeric alphaNumeric;


    public ResponseEntity<?> addMainParaData(MainParaDTO mainParaDTO, Integer dId) {
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dId);
            if (dataTypeOptional.isPresent()){
                DataType dataType1 = dataTypeOptional.get();
                String type = dataType1.getTalk();
                if (type!=null&&"English".equalsIgnoreCase(type)){

                    Optional<MainTopicEng> existingTopic = mainTopicEngRepo.findByTopic(mainParaDTO.getTopic());
                    if(existingTopic.isPresent()){
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
            e.printStackTrace();
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
                    Optional<FirstTopicEng> existingTopic = firstTopicEngRepo.findByTopic(mainParaDTO.getTopic());
                    if(existingTopic.isPresent()){
                        String existTopic = mainParaDTO.getTopic();
                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
                    }
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
                    Optional<FirstTopicMal> existingTopic = firstTopicMalRepo.findByTopic(mainParaDTO.getTopic());
                    if(existingTopic.isPresent()){
                        String existTopic = mainParaDTO.getTopic();
                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
                    }
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
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);

    }

//    public ResponseEntity<?> addFirstParaData(MainParaDTO mainParaDTO, Integer dId) {
//        try {
//            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dId);
//            if (dataTypeOptional.isPresent()){
//                DataType dataType1 = dataTypeOptional.get();
//                String type = dataType1.getTalk();
//                if (type!=null&&"English".equalsIgnoreCase(type)){
//                    Optional<FirstTopicEng> existingTopic = firstTopicEngRepo.findByTopic(mainParaDTO.getTopic());
//                    if(existingTopic.isPresent()){
//                        String existTopic = mainParaDTO.getTopic();
//                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
//                    }
//                    String randomId = alphaNumeric.generateRandomNumber();
//                    FirstTopicEng firstTopicEng = new FirstTopicEng();
//                    firstTopicEng.setTopic(mainParaDTO.getTopic());
//                    firstTopicEng.setDescription(mainParaDTO.getDescription());
//                    firstTopicEng.setRef(mainParaDTO.getRefURL());
//                    firstTopicEng.setFsUid(randomId);
//                    //firstTopicEng.setMainUid();
//                    firstTopicEngRepo.save(firstTopicEng);
//                    return new ResponseEntity<>(firstTopicEng, HttpStatus.OK);
//
//                } else if (type!=null && "Malayalam".equalsIgnoreCase(type)) {
//                    Optional<FirstTopicMal> existingTopic = firstTopicMalRepo.findByTopic(mainParaDTO.getTopic());
//                    if(existingTopic.isPresent()){
//                        String existTopic = mainParaDTO.getTopic();
//                        return new ResponseEntity<>(existTopic+" is already present in database", HttpStatus.CONFLICT);
//                    }
//                    String randomId = alphaNumeric.generateRandomNumber();
//                    FirstTopicMal firstTopicMal = new FirstTopicMal();
//                    firstTopicMal.setTopic(mainParaDTO.getTopic());
//                    firstTopicMal.setDescription(mainParaDTO.getDescription());
//                    firstTopicMal.setRef(mainParaDTO.getRefURL());
//                    firstTopicMal.setFsUid(randomId);
//                    firstTopicMalRepo.save(firstTopicMal);
//                    return new ResponseEntity<>(firstTopicMal, HttpStatus.OK);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }



    public ResponseEntity<?> generateComId(String engId, String malId) {
        try {
            SubComId comId = new SubComId();
            comId.setFsMalId(malId);
            comId.setFsEngId(engId);
            String genId = alphaNumeric.generateRandomNumber();
            comId.setFsCommonId(genId);
            subComIdRepo.save(comId);
            return new ResponseEntity<>(comId,HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
