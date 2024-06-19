package com.example.MuseumTicketing.Guide.mpType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/fileType")
@CrossOrigin
public class FileTypeController {
    @Autowired
    private FileTypeService fileTypeService;

    @PostMapping(path = "/addFiletype")
    public ResponseEntity<?> addFileType(@RequestBody FileType fileType){

        if (fileType == null || fileType.getFileType() == null || fileType.getFileType().isEmpty()) {
            return new ResponseEntity<>("File type is required", HttpStatus.BAD_REQUEST);
        }

        FileType fileType1 = fileTypeService.addFileType(fileType);
        return ResponseEntity.ok(fileType1);
    }

    @GetMapping(path = "/getFileType")
    public ResponseEntity<List<FileType>> getAllType(){
        return fileTypeService.getAllType();
    }
}
