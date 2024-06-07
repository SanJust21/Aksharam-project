package com.example.MuseumTicketing.Guide.mpType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileTypeService {
    @Autowired
    private FileTypeRepo fileTypeRepo;

    public FileType addFileType(FileType fileType) {
        try {
            return fileTypeRepo.save(fileType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<List<FileType>> getAllType() {
        try {
            return new ResponseEntity<>(fileTypeRepo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

}
