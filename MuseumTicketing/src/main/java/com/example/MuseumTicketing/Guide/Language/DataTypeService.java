package com.example.MuseumTicketing.Guide.Language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
