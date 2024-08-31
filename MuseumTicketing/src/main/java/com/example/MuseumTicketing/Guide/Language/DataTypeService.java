package com.example.MuseumTicketing.Guide.Language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataTypeService {
    @Autowired
    private DataTypeRepo dataTypeRepo;

    public DataType addTalk(DataType dataType) {
        try {
            return dataTypeRepo.save(dataType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<List<DataType>> getAllTalk() {
        try {
            return new ResponseEntity<>(dataTypeRepo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteTalk(Integer id) {
        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(id);
        if (dataTypeOptional.isPresent()){
            DataType dataType =dataTypeOptional.get();
            dataTypeRepo.delete(dataType);
            return new ResponseEntity<>("Language is deleted",HttpStatus.OK);
        }return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
    }
}
