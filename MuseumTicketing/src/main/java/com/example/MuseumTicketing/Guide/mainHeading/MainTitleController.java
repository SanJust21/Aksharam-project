package com.example.MuseumTicketing.Guide.mainHeading;

import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.QR.CommonIdQRCodeRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.CombinedDataSub;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.GetDtoSub;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/DataEntry1")
@CrossOrigin
public class MainTitleController {
    @Autowired
    private MainTitleService mainTitleService;
    @Autowired
    private DataTypeRepo dataTypeRepo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    @PostMapping(path = "/mainT")
    public ResponseEntity<?> addMainTitle(@RequestParam Integer dId, @RequestBody MainDTO mainDTO){
        try {

            if (dId == null) {
                return new ResponseEntity<>("ID is required!", HttpStatus.BAD_REQUEST);
            }

            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dId);
            if (dataTypeOptional.isPresent()) {
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)) {
                    return mainTitleService.addMainTitleEng(mainDTO);
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return mainTitleService.addMainTitleMal(mainDTO);
                } else {
                    return new ResponseEntity<>("Language is not present", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Language is not present", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong! ",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getMainComplete")
    public ResponseEntity<List<CombinedData>>getAllMainTitleData(@RequestParam Integer dtId){
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)){
                    return mainTitleService.getCombinedList();
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return mainTitleService.getCombinedListMal();
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

    @GetMapping(path = "/getMainId")
    public ResponseEntity<List<CombinedData>>getAllMainTitleData(@RequestParam Integer dtId, @RequestParam String mainId){
        try {
            Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if (tData != null && "English".equalsIgnoreCase(tData)){
                    return mainTitleService.getCombinedList(mainId);
                } else if (tData != null && "Malayalam".equalsIgnoreCase(tData)) {
                    return mainTitleService.getCombinedListMal(mainId);
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

    @GetMapping(path = "/getSubDataByCommonId")
    public ResponseEntity<List<GetDtoSub>>getDetailsFirstSub(@RequestParam Integer dtId, @RequestParam String commonId){
        try {
            Optional<DataType>dataTypeOptional = dataTypeRepo.findById(dtId);
            if (dataTypeOptional.isPresent()){
                DataType dataType = dataTypeOptional.get();
                String tData = dataType.getTalk();
                if ("Malayalam".equalsIgnoreCase(tData)){
                    Optional<CommonIdQRCode>commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
                    Optional<CommonIdFs>commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
                    if (commonIdQRCodeOptional.isPresent()){
                        CommonIdQRCode commonIdQRCode=commonIdQRCodeOptional.get();
                        if (commonIdQRCode.getCommonId().equals(commonId)){
                            String mainMalId = commonIdQRCode.getMalId();
                            return mainTitleService.getSubDataDetailsByCommonId(mainMalId,commonId);
                        }else {
                            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                        }
                    } else if (commonIdFsOptional.isPresent()) {
                        CommonIdFs commonIdFs = commonIdFsOptional.get();
                        if (commonIdFs.getFsCommonId().equals(commonId)){
                            String fsMalId = commonIdFs.getFsMalId();
                            return mainTitleService.getSubSubDetailsByCommonId(fsMalId,commonId);
                        }else {
                            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                        }
                    }else {
                        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                    }
                } else if ("English".equalsIgnoreCase(tData)) {
                    Optional<CommonIdQRCode>commonIdQRCodeOptional = commonIdQRCodeRepo.findByCommonId(commonId);
                    Optional<CommonIdFs>commonIdFsOptional = fsCommonIdRepo.findByFsCommonId(commonId);
                    if (commonIdQRCodeOptional.isPresent()){
                        CommonIdQRCode commonIdQRCode=commonIdQRCodeOptional.get();
                        if (commonIdQRCode.getCommonId().equals(commonId)){
                            String mainEngId = commonIdQRCode.getEngId();
                            return mainTitleService.getSubDataDetailsEnglishByCommonId(mainEngId,commonId);
                        }else {
                            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                        }
                    } else if (commonIdFsOptional.isPresent()) {
                        CommonIdFs commonIdFs = commonIdFsOptional.get();
                        if (commonIdFs.getFsCommonId().equals(commonId)){
                            String fsEngId = commonIdFs.getFsEngId();
                            return mainTitleService.getSubDetailsEng(fsEngId,commonId);
                        }else {
                            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                        }
                    }else {
                        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
                    }
                }else {
                    return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
                }
            }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
