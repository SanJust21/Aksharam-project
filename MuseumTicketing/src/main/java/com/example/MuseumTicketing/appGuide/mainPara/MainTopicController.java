package com.example.MuseumTicketing.appGuide.mainPara;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.first.FirstTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
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
    private CommonQRParaIdRepo commonQRParaIdRepo;
    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;


    @PostMapping(path = "/mainPara")
    public ResponseEntity<?>addMainPara(@RequestParam Integer dId, @RequestBody MainParaDTO mainParaDTO){

        try {
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
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/subPara")
    public ResponseEntity<?>addFirstPara(@RequestParam Integer dId,@RequestParam String uId,
                                         @RequestBody MainParaDTO mainParaDTO){
        try {
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
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/genSubParaCommonId")
    private ResponseEntity<?> generateCommonId(@RequestParam String engId,
                                               @RequestParam String malId){
        try {
            if (engId == null || engId.isEmpty()|| "undefined".equalsIgnoreCase(engId) || engId.isBlank() ||
                    malId==null || malId.isEmpty()|| "undefined".equalsIgnoreCase(malId) || malId.isBlank()) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }
            Optional<FirstTopicEng>firstTopicEngOptional=firstTopicEngRepo.findByfsUid(engId);
            if (firstTopicEngOptional.isPresent()){
                Optional<FirstTopicMal>firstTopicMalOptional=firstTopicMalRepo.findByFsUid(malId);
                if (firstTopicMalOptional.isPresent()){
                    return mainTopicService.generateComId(engId,malId);
                }return new ResponseEntity<>("malayalam uniqueId is not valid. "+malId,HttpStatus.BAD_REQUEST);
            }return new ResponseEntity<>("English uniqueId is not valid. "+engId,HttpStatus.BAD_REQUEST);

        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateData/{commonId}")
    public ResponseEntity<?>updateDataPara(@PathVariable String commonId,@RequestParam Integer dType,
                                           @RequestBody MainParaDTO mainParaDTO){
        try {
            Optional<CommonQRParaId>commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
            Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
            if (commonQRParaIdOptional.isPresent()){
                CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                String engId = commonQRParaId.getEngId();
                String malId = commonQRParaId.getMalId();
                Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dType);
                if (dataTypeOptional.isPresent()){
                    DataType dataType = dataTypeOptional.get();
                    String dataTypeTalk = dataType.getTalk();
                    if ("Malayalam".equalsIgnoreCase(dataTypeTalk)){
                        return  mainTopicService.updateMainPara(malId,commonId,mainParaDTO);
                    } else if ("English".equalsIgnoreCase(dataTypeTalk)) {
                        return mainTopicService.updateMainPara(engId,commonId,mainParaDTO);
                    }else {
                        return new ResponseEntity<>("Out Of boundary",HttpStatus.BAD_REQUEST);
                    }
                }else {
                    return new ResponseEntity<>("Data Type is not selected",HttpStatus.BAD_REQUEST);
                }
            } else if (subComIdOptional.isPresent()) {
                SubComId subComId = subComIdOptional.get();
                String engId = subComId.getFsEngId();
                String malId = subComId.getFsMalId();
                Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dType);
                if (dataTypeOptional.isPresent()){
                    DataType dataType = dataTypeOptional.get();
                    String dataTypeTalk = dataType.getTalk();
                    if ("Malayalam".equalsIgnoreCase(dataTypeTalk)){
                        return  mainTopicService.updateSubPara(malId,commonId,mainParaDTO);
                    } else if ("English".equalsIgnoreCase(dataTypeTalk)) {
                        return mainTopicService.updateSubPara(engId,commonId,mainParaDTO);
                    }else {
                        return new ResponseEntity<>("Out Of boundary",HttpStatus.BAD_REQUEST);
                    }
                }else {
                    return new ResponseEntity<>("Data Type is not selected",HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity<>("CommonId "+commonId+" is not valid",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
       // return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "deleteByCommonId/{commonId}")
    public ResponseEntity<?>deleteMainTopicByCommonId(@PathVariable String commonId){
        try {
            Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByCommonId(commonId);
            Optional<SubComId>subComIdOptional =subComIdRepo.findByFsCommonId(commonId);
            if (commonQRParaIdOptional.isPresent()){
                CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                String engId = commonQRParaId.getEngId();
                String malId =commonQRParaId.getMalId();
                return mainTopicService.DeleteMainPara(malId,engId,commonId);
            } else if (subComIdOptional.isPresent()) {
                SubComId subComId = subComIdOptional.get();
                String engId = subComId.getFsEngId();
                String malId = subComId.getFsMalId();
                return mainTopicService.DeleteSubPara(malId,engId,commonId);
            }else {
                return new ResponseEntity<>("CommonId is not valid ",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong,",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteSubByCommonId/{commonId}")
    public ResponseEntity<?>deleteSubTopicByCommonId(@PathVariable String commonId){
        try {
            Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
            if (subComIdOptional.isPresent()){
                SubComId subComId = subComIdOptional.get();
                if (subComId.getFsCommonId().equals(commonId)){
                    return mainTopicService.deleteSubTopicByCommonId(commonId);
                }
            }else {
                return new ResponseEntity<>("CommonId is not correct",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteMainTitleByUid/{uId}")
    public ResponseEntity<?>deleteMainTitleByUid(@PathVariable String uId,@RequestParam Integer dId){
        try{
            Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String data = dataType.getTalk();
                if ("Malayalam".equalsIgnoreCase(data)){
                    return mainTopicService.deleteMalayalamMainTopicByUid(uId);
                }else if ("English".equalsIgnoreCase(data)){
                    return mainTopicService.deleteEnglishMainTopicByUid(uId);
                }
            }else {
                return new ResponseEntity<>("Data Type is required",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
