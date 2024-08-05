package com.example.MuseumTicketing.Guide.SecondSubHeading;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/DataEntry3")
@CrossOrigin
public class SecondSubController {
    @Autowired
    private SecondSubService secondSubService;
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private ErrorService errorService;

    @PostMapping(path = "/secondSub")
    public ResponseEntity<?> addSecondSubData(@RequestParam String uId,
                                             @RequestBody MainDTO mainDTO){
        try {

            if (uId == null || uId.isEmpty()|| "undefined".equalsIgnoreCase(uId) || uId.isBlank()) {
                return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
            }

            Optional<FirstSubMalayalam> malOptional = firstSubMalayalamRepo.findByFsUid(uId);

            FirstSubEnglish firstSubEng = firstSubEnglishRepo.findByFsUid(uId);
            if (firstSubEng!=null){
                String engId = firstSubEng.getFsUid();
                if (engId.equals(uId)){
                    return secondSubService.addSubDataEnglish(uId,mainDTO);
                }
            }else {
                if (malOptional.isPresent()){
                    FirstSubMalayalam firstSubMal = malOptional.get();
                    String malId = firstSubMal.getFsUid();
                    if (uId.equals(malId)){
                        return secondSubService.addSubDataMalayalam(uId, mainDTO);
                    }
                }

            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/generateSSid")
    public ResponseEntity<?>generateCommonSs(@RequestParam String englishId,
                                             @RequestParam String malId){
        try {
            if (englishId == null || englishId.isEmpty()|| "undefined".equalsIgnoreCase(englishId) || englishId.isBlank() ||
                    malId==null || malId.isEmpty()|| "undefined".equalsIgnoreCase(malId) || malId.isBlank()) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }
            return secondSubService.generateCommonSs(englishId,malId);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getSecondSub")
    public ResponseEntity<List<CombinedDataSubSub>>getSecondTitleData(@RequestParam Integer dtId, @RequestParam String ssCommonId){
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)){
                    return secondSubService.getCombinedList(ssCommonId);
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return secondSubService.getCombinedListMal(ssCommonId);
                }else {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
