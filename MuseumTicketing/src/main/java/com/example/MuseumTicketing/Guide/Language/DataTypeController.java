package com.example.MuseumTicketing.Guide.Language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/dataType1")
@CrossOrigin
public class DataTypeController {
    @Autowired
    private DataTypeService dataTypeService;
    @PostMapping(path = "/addtalk")
    public ResponseEntity<?> addTalk(@RequestBody DataType dataType){

        if (dataType == null || dataType.getTalk() == null) {
            return new ResponseEntity<>("Talk data cannot be null", HttpStatus.BAD_REQUEST);
        }

        DataType dataType1 = dataTypeService.addTalk(dataType);
        return ResponseEntity.ok(dataType1);
    }

    @GetMapping(path = "/getTalk")
    public ResponseEntity<List<DataType>> getAllTalk(){
        return dataTypeService.getAllTalk();
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?>deleteById(@PathVariable Integer id){
        return dataTypeService.deleteTalk(id);
    }

}
