package com.example.MuseumTicketing.Guide.DELETE.firstSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/deleteByFirstSub")
public class FirstSubDeleteController {
    @Autowired
    private FirstSubDeleteService firstSubDeleteService;
    //Delete from Database anD aws3Bucket
    @DeleteMapping(path = "/commonIdAll/{commonId}")
    public ResponseEntity<?> deleteAllByCommonId(@PathVariable String commonId){
        try {
            int count =firstSubDeleteService.deleteAllByCommonId1(commonId);
            if (count>0){
                return new ResponseEntity<>("All Details are deleted",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("No details found with commonId:"+commonId,HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}