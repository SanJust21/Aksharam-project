package com.example.MuseumTicketing.Guide.DELETE.secondSub;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/deleteSecond")
@CrossOrigin
public class SecondSubDeleteController {
    @Autowired
    private SecondSubDeleteService secondSubDeleteService;

    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @Autowired
    private ErrorService errorService;

    @DeleteMapping(path = "/commonIdSecond/{id}")
    public ResponseEntity<?> commonIdSecond(@PathVariable String id){
        try {
            int count =secondSubDeleteService.commonIdSecond(id);
            if (count>0){
                return new ResponseEntity<>("All details are deleted with "+id,HttpStatus.OK);
            }else {
                return new ResponseEntity<>("No data found with "+id,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/deleteCommonIdSS")
    public ResponseEntity<?>deleteCommonIdSecondSub(@RequestParam String commonId){
        try {
            Optional<CommonIdSs> commonIdSsOptional = commonIdSsRepo.findBySsCommonId(commonId);
            if (commonIdSsOptional.isPresent()){
                return secondSubDeleteService.deleteCommonIdSecondSub(commonId);
            }else {
                return new ResponseEntity<>("CommonId is not present. ",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}