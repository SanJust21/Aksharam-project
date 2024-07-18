package com.example.MuseumTicketing.Guide.DELETE.secondSub;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/deleteSecond")
@CrossOrigin
public class SecondSubDeleteController {
    @Autowired
    private SecondSubDeleteService secondSubDeleteService;

    @Autowired
    private CommonIdSsRepo commonIdSsRepo;
    @DeleteMapping(path = "/commonIdSecond/{id}")
    public ResponseEntity<?> commonIdSecond(@PathVariable String id){
        try {
            int count =secondSubDeleteService.commonIdSecond(id);
            if (count>0){
                return new ResponseEntity<>("All details deleted with "+id,HttpStatus.OK);
            }else {
                return new ResponseEntity<>("No data found with "+id,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}