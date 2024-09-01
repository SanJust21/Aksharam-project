package com.example.MuseumTicketing.tribal;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/tribalData")
@CrossOrigin
public class TribalController {
    @Autowired
    private TribalService tribalService;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private TribalEnglishRepo tribalEnglishRepo;
    @Autowired
    private TribalMalayalamRepo tribalMalayalamRepo;
    @Autowired
    private TribalCommonIdRepo tribalCommonIdRepo;
    @Autowired
    private TribalVideoRepo tribalVideoRepo;

    @PostMapping(path = "/addData")
    public ResponseEntity<?>addData(@RequestParam Integer dtId,@RequestBody TribalDto tribalDto){
        try {
            Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String dType= dataType.getTalk();
                if ("Malayalam".equalsIgnoreCase(dType)){
                    return tribalService.addTribalMalayalamData(tribalDto);
                } else if ("English".equalsIgnoreCase(dType)) {
                    return tribalService.addTribalEnglishData(tribalDto);
                }else {
                    return new ResponseEntity<>("DataType is not valid", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Data type isn't valid",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
           return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/commonId")
    public ResponseEntity<?>generateCommonId(@RequestParam String malId,
                                             @RequestParam String engId){
        try {
            Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(malId);
            Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(engId);
            if (tribalEnglishOptional.isPresent()&&tribalMalayalamOptional.isPresent()){
                return tribalService.generateCommonId(malId,engId);
            }return new ResponseEntity<>("Id's are not valid",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/uploadVideo")
    public ResponseEntity<?>uploadVideo(@RequestParam String commonId, @RequestParam MultipartFile[] file){
        try {
            Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByCommonId(commonId);
            if (tribalCommonIdOptional.isPresent()){
                TribalCommonId tribalCommonId =tribalCommonIdOptional.get();
                String malId = tribalCommonId.getMalayalamId();
                String engId = tribalCommonId.getEnglishId();
                return tribalService.uploadVideo(malId,engId,commonId,file);
            }return new ResponseEntity<>("CommonId : "+commonId+" isn't valid",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getDetails")
    public ResponseEntity<List<CombinedTribalData>>getDetailsByCommonId(@RequestParam String commonId,@RequestParam Integer dType){
        try{
            Optional<TribalCommonId>tribalCommonIdOptional=tribalCommonIdRepo.findByCommonId(commonId);
            if (tribalCommonIdOptional.isPresent()){
                TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                String malId = tribalCommonId.getMalayalamId();
                String engId = tribalCommonId.getEnglishId();
                Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dType);
                if (dataTypeOptional.isPresent()){
                    DataType dataType = dataTypeOptional.get();
                    String type = dataType.getTalk();
                    if ("Malayalam".equalsIgnoreCase(type)){
                        return tribalService.getMalayalamDetails(commonId,malId);
                    } else if ("English".equalsIgnoreCase(type)) {
                        return tribalService.getEnglishDetails(commonId,engId);
                    }
                }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
            }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "getDetailsByDataType")
    public ResponseEntity<List<CombinedTribalData>>getAllDetailsByLanguage(@RequestParam Integer dType){
        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dType);
        if (dataTypeOptional.isPresent()){
            return tribalService.getDetailsByLanguage(dType);
        }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/getAllUidData")
    public ResponseEntity<List<CombinedTribal>>getAllUniqueIdData(@RequestParam Integer dtId){
        try {
            return tribalService.getAllUidDetails(dtId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateData")
    public ResponseEntity<?>updateData(@RequestParam String uniqueId,@RequestBody TribalDto tribalDto){
        try {
            Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(uniqueId);
            Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(uniqueId);
            if (tribalEnglishOptional.isPresent()){
                return tribalService.updateTribalData(uniqueId,tribalDto);
            } else if (tribalMalayalamOptional.isPresent()) {
                return tribalService.updateTribalData(uniqueId,tribalDto);
            }
            return new ResponseEntity<>("Enter a valid uniqueId",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PutMapping(path = "/updateVideo")
    public ResponseEntity<?>updateVideo(@RequestParam String commonId,@RequestParam Integer tId,
                                        @RequestParam MultipartFile file){
        try {
            Optional<TribalVideo>tribalVideoOptional=tribalVideoRepo.findByCommonIdAndId(commonId,tId);
            if (tribalVideoOptional.isPresent()){
                TribalVideo tribalVideo = tribalService.updateTribalVideo(commonId,tId,file);
                return new ResponseEntity<>(tribalVideo,HttpStatus.OK);
            }return new ResponseEntity<>("Enter a valid commonId and id",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteByCommonId")
    public ResponseEntity<?>deleteDataByCommonId(@RequestParam String commonId){
        try {
            Optional<TribalCommonId>tribalCommonIdOptional= tribalCommonIdRepo.findByCommonId(commonId);
            if (tribalCommonIdOptional.isPresent()){
                TribalCommonId tribalCommonId = tribalCommonIdOptional.get();
                String malId = tribalCommonId.getMalayalamId();
                String engId = tribalCommonId.getEnglishId();
                return tribalService.deleteDataByCommonId(commonId,malId,engId);
            }
            return new ResponseEntity<>("CommonId isn't present",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteByUId")
    public ResponseEntity<?>deleteDataByUid(@RequestParam String uId){
        try {
            Optional<TribalMalayalam>tribalMalayalamOptional=tribalMalayalamRepo.findByTribMalUid(uId);
            Optional<TribalEnglish>tribalEnglishOptional=tribalEnglishRepo.findByTribEngUid(uId);
            if (tribalEnglishOptional.isPresent()){
                return tribalService.deleteByUniqueId(uId);
            } else if (tribalMalayalamOptional.isPresent()) {
                return tribalService.deleteByUniqueId(uId);
            }return new ResponseEntity<>("uniqueId isn't present",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteVideo")
    public ResponseEntity<?>deleteVideo(@RequestParam String commonId,@RequestParam Integer tId){
        try {
            Optional<TribalVideo>tribalVideoOptional=tribalVideoRepo.findByCommonIdAndId(commonId,tId);
            if (tribalVideoOptional.isPresent()){
                return tribalService.deleteVideo(commonId,tId);
            }return new ResponseEntity<>("Id is not present",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
}
