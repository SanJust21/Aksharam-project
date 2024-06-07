package com.example.MuseumTicketing.Guide.DELETE.firstSub;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
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

    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    //Delete from Database anD aws3Bucket
//    @DeleteMapping(path = "/commonIdAll/{commonId}")
//    public ResponseEntity<?> deleteAllByCommonId(@PathVariable String commonId){
//        try {
//            int count =firstSubDeleteService.deleteAllByCommonId1(commonId);
//            if (count>0){
//                return new ResponseEntity<>("All Details are deleted",HttpStatus.OK);
//            }else {
//                return new ResponseEntity<>("No details found with commonId:"+commonId,HttpStatus.NOT_FOUND);
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @DeleteMapping(path = "/commonIdAll/{commonId}")
    public ResponseEntity<?> deleteAllByCommonId(@PathVariable String commonId){
        try {
            if (commonId == null || "undefined".equalsIgnoreCase(commonId)||commonId.isEmpty()) {
                return new ResponseEntity<>("Common ID is required", HttpStatus.BAD_REQUEST);
            }else {
                CommonIdFs commonIdFs = fsCommonIdRepo.findByfsCommonId(commonId);
                if (commonIdFs.getFsCommonId().equals(commonId)){
                    int count =firstSubDeleteService.deleteAllByCommonId1(commonId);
                    if (count>0){
                        return new ResponseEntity<>("All Details are deleted",HttpStatus.OK);
                    }
                }else {
                    return new ResponseEntity<>("No details found with commonId:"+commonId,HttpStatus.NOT_FOUND);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}