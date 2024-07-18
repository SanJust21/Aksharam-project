package com.example.MuseumTicketing.appGuide.mainPara;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.appGuide.mainPara.Topics.TopicData;
import com.example.MuseumTicketing.appGuide.mainPara.Topics.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/guideApp")
@CrossOrigin
public class MainTopicController {
    @Autowired
    private MainTopicService mainTopicService;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private TopicRepo topicRepo;


    @PostMapping(path = "/mainPara")
    public ResponseEntity<?>addMainPara(@RequestParam Integer dId, @RequestBody MainParaDTO mainParaDTO){
        if (dId==null ){
            return new ResponseEntity<>("DataType is required", HttpStatus.BAD_REQUEST);
        }
        Optional<DataType> dataType = dataTypeRepo.findById(dId);
        if (dataType.isPresent()){
            DataType dataType1 = dataType.get();
            String type = dataType1.getTalk();
            if (type!=null&&"English".equalsIgnoreCase(type)){
                return mainTopicService.addMainParaData(mainParaDTO,dId);
                //return mainTopicService.addFirstParaData(mainParaDTO,dId);
            }else if (type!=null && "Malayalam".equalsIgnoreCase(type)){
                return mainTopicService.addMainParaData(mainParaDTO,dId);
                //return mainTopicService.addFirstParaData(mainParaDTO,dId);
            }
        }else {
            return new ResponseEntity<>("Language is not present",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/subPara")
    public ResponseEntity<?>addFirstPara(@RequestParam Integer dId,@RequestParam String uId,
                                         @RequestBody MainParaDTO mainParaDTO){
        if (dId==null ){
            return new ResponseEntity<>("DataType is required", HttpStatus.BAD_REQUEST);
        }
        Optional<DataType> dataType = dataTypeRepo.findById(dId);
        if (dataType.isPresent()){
            DataType dataType1 = dataType.get();
            String type = dataType1.getTalk();
            if (type!=null&&"English".equalsIgnoreCase(type)){
                return mainTopicService.addFirstParaData(mainParaDTO,dId,uId);
            }else if (type!=null && "Malayalam".equalsIgnoreCase(type)){
                return mainTopicService.addFirstParaData(mainParaDTO,dId,uId);
            }
        }else {
            return new ResponseEntity<>("Language is not present",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/genSubParaCommonId")
    private ResponseEntity<?> generateCommonId(@RequestParam String engId,
                                               @RequestParam String malId){
        try {
            if (engId == null || engId.isEmpty()|| "undefined".equalsIgnoreCase(engId) || engId.isBlank() ||
                    malId==null || malId.isEmpty()|| "undefined".equalsIgnoreCase(malId) || malId.isBlank()) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }
            return mainTopicService.generateComId(engId,malId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
