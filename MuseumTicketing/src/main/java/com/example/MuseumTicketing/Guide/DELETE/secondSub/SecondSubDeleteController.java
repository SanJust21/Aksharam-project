package com.example.MuseumTicketing.Guide.DELETE.secondSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/deleteSecond")
public class SecondSubDeleteController {
    @Autowired
    private SecondSubDeleteService secondSubDeleteService;

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