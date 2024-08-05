package com.example.MuseumTicketing.Guide.firstSubHeading;

import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/DataEntry2")
@CrossOrigin
public class FirstSubController {
    @Autowired
    private FirstSubService firstSubService;
    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;
    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private ErrorService errorService;

    @PostMapping(path = "/firstSub")
    public ResponseEntity<?>addFirstSubData(@RequestParam String uId,
                                            @RequestBody MainDTO mainDTO){
        try {

            if (uId == null || uId.isEmpty()|| "undefined".equalsIgnoreCase(uId) || uId.isBlank()) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }

            Optional<MainTitleMal> malOptional =mainTitleMalRepo.findBymMalUid(uId);



            MainTitleEng mainTitleEng = mainTitleEngRepo.findBymEngUid(uId);
            if (mainTitleEng!=null){
                String engId = mainTitleEng.getMEngUid();
                if (engId.equals(uId)){
                    return firstSubService.addSubDataEnglish(uId,mainDTO);
                }
            }else {
                if (malOptional.isPresent()){
                    MainTitleMal mainTitleMal = malOptional.get();
                    String malId = mainTitleMal.getMMalUid();
                    if (uId.equals(malId)){
                        return firstSubService.addSubDataMalayalam(uId,mainDTO);
                    }
                }

            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getFirstSubComplete")
    public ResponseEntity<List<CombinedDataSub>>getAllMainTitleData(@RequestParam Integer dtId){
        try {

            if (dtId == null) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }

            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)){
                    return firstSubService.getCombinedListEng();
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return firstSubService.getCombinedListMal();
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


    @GetMapping(path = "/genCommonId")
    private ResponseEntity<?> generateCommonId(@RequestParam String engId,
                                               @RequestParam String malId){
        try {
            if (engId == null || engId.isEmpty()|| "undefined".equalsIgnoreCase(engId) || engId.isBlank() ||
            malId==null || malId.isEmpty()|| "undefined".equalsIgnoreCase(malId) || malId.isBlank()) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }
            return firstSubService.generateCommonIdFs(engId,malId);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getAllByCommonId/{id}")
    public ResponseEntity<?>getDetailsByCommonId(@PathVariable String id,@RequestParam Integer dtId){
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)){
                    return firstSubService.getFirstSubCombinedListEng(id);
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return firstSubService.getFirstSubCombinedListMal(id);
                }else {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
