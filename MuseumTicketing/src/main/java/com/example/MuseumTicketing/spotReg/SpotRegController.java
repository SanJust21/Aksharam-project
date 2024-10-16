package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.DTO.PriceRequest;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.Service.BasePrice.PriceRequestService;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/spotData")
@CrossOrigin
public class SpotRegController {
    @Autowired
    private SpotRegService spotRegService;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private CategoryRepo categoryRepo;


    @PostMapping(path = "/userReg")
    public ResponseEntity<?>userReg(@RequestParam Integer category,@RequestBody SpotUserDto spotUserDto){
        try {
            Optional<CategoryData> categoryDataOptional = categoryRepo.findById(category);
            if (categoryDataOptional.isPresent()){
                CategoryData categoryData = categoryDataOptional.get();
                String categoryName = categoryData.getCategory();
                if ("Public".equalsIgnoreCase(categoryName)){
                    return spotRegService.publicUserReg(spotUserDto,category);
                } else if ("Institution".equalsIgnoreCase(categoryName)) {
                    return spotRegService.institutionReg(spotUserDto,category);
                } else if ("Foreigner".equalsIgnoreCase(categoryName)) {
                    return spotRegService.ForeignerReg(spotUserDto,category);
                }
            }
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
    
    @GetMapping(path = "/getAllUser")
    public ResponseEntity<List<SpotUserDto>>getAllUser(@RequestParam(required = false) Integer categoryId){
        try {
            Optional<CategoryData>categoryDataOptional=categoryRepo.findById(categoryId);
            if (categoryDataOptional.isPresent()){
                CategoryData categoryData = categoryDataOptional.get();
                String name = categoryData.getCategory();
                if ("Public".equalsIgnoreCase(name)){
                    return spotRegService.getAllPublic();
                } else if ("Institution".equalsIgnoreCase(name)) {
                    return spotRegService.getAllInstitution();
                } else if ("Foreigner".equalsIgnoreCase(name)) {
                    return spotRegService.getAllForeigner();                }
            }else {
                return spotRegService.getAllUserDetails();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
