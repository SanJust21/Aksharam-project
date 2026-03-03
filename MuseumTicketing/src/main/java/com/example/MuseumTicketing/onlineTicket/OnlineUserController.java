package com.example.MuseumTicketing.onlineTicket;

import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.userData.SpotUpdateDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/beta/api/onlineBooking")
@CrossOrigin
public class OnlineUserController {
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private CategoryRepo categoryRepo;

    @PostMapping(path = "/onlineUserReg")
    public ResponseEntity<Map<String,Object>>onlineUserRegistration(@RequestParam Integer category,@RequestBody OnlineUserDataDto onlineUserDto){
        Map<String,Object>response = new HashMap<>();
        try {
            String categoryName = categoryRepo.findById(category).map(CategoryData::getCategory).orElse(null);
            if ("Public".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlinePublicTicketBooking(onlineUserDto,category);
            }
            if ("Institution".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlineInstitutionTicketBooking(onlineUserDto,category);
            }
            if ("Foreigner".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlineForeignerTicketBooking(onlineUserDto,category);
            }
            response.put("Error","CategoryName is not present");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/onlineUserRegModify")
    public ResponseEntity<Map<String,Object>>onlineUserRegModify(@RequestParam Integer categoryId,@RequestBody SpotUpdateDto spotUpdateDto,
                                                                 @RequestParam String sessionId){
        Map<String,Object> response = new HashMap<>();
        try {
            String categoryName = categoryRepo.findById(categoryId).map(CategoryData::getCategory).orElse(null);
            if ("Public".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlinePublicUserRegModify(categoryId,spotUpdateDto,sessionId);
            }
            if ("Institution".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlineInstitutionUserRegModify(categoryId,spotUpdateDto,sessionId);
            }
            if ("Foreigner".equalsIgnoreCase(categoryName)){
                return onlineUserService.onlineForeignerUserRegModify(categoryId,spotUpdateDto,sessionId);
            }
            response.put("Error","categoryName is not present");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/makePayment")
    public ResponseEntity<Map<String,Object>>makeOnlinePayment(@RequestParam Integer categoryId,@RequestParam String sessionId,
                                                               @RequestParam double payAmount){
        Map<String,Object>response = new HashMap<>();
        try {
            response=onlineUserService.makeOnlinePayment(categoryId,sessionId,payAmount);
            if (response.containsKey("Error")){
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/verifyPayment")
    public ResponseEntity<Map<String,Object>>verifyPaymentDetails(@RequestParam String orderId,@RequestParam String paymentId,
                                                                  @RequestParam String signatureData,@RequestParam Integer categoryId){
        Map<String,Object> response = new HashMap<>();
        try {
            response=onlineUserService.verifyUserPayment(orderId,paymentId,signatureData,categoryId);
            if (response.containsKey("Error")){
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/generateTicketQrCode")
    public ResponseEntity<Map<String,Object>>generateTicketQrCode(@RequestParam Integer categoryId,@RequestParam String paymentId){
        Map<String,Object> response = new HashMap<>();
        try {
            response=onlineUserService.generateTicketQrCode(categoryId,paymentId);
            if (response.containsKey("Error")){
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getCurrentDateTime")
    public ResponseEntity<Map<String,Object>>getCurrentDateTime(){
        Map<String,Object> response = new HashMap<>();
        try {
            response=onlineUserService.getCurrentDateTime();
            if (response.containsKey("Error")){
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
