package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.userData.SpotPaymentDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUpdateDto;
import com.example.MuseumTicketing.spotReg.userData.SpotUserDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.GetUserData_;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.SlotIdDto;
import com.example.MuseumTicketing.spotReg.userData.dashboardDTO.count.VisitsCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PutMapping(path = "/userRegModify")
    public ResponseEntity<?>userModification(@RequestParam Integer categoryId, @RequestBody SpotUpdateDto spotUpdateDto,
                                             @RequestParam String orderId){
        try {
            return spotRegService.UserDetailsUpdate(categoryId,spotUpdateDto,orderId);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/confirmPayment")
    public ResponseEntity<?> confirmPayment(@RequestParam String orderId, @RequestBody SpotPaymentDto spotPaymentDto,
                                            @RequestParam Integer totalUserCount){
        try {
            return spotRegService.confirmPaymentDetails(orderId,spotPaymentDto,totalUserCount);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
    
    @GetMapping(path = "/getAllUser")
    public ResponseEntity<?>getAllUser(@RequestParam(required = false) Integer categoryId){
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
                }return spotRegService.getAllForeigner();
            }else {
                //return spotRegService.getAllUserDetails();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getUserDetailsByDate")
    public ResponseEntity<List<GetUserData_>> getUserDetailsByDate(@RequestParam LocalDate visitDate, @RequestParam Integer categoryId){
        try {
            return spotRegService.getUserDetailsByDate(visitDate,categoryId);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/totalRevenueByDate")
    public ResponseEntity<?>CategoryBasedTotalRevenueByDate(@RequestParam LocalDate visitDate){
        try {
            return spotRegService.CategoryBasedTotalRevenueByDate(visitDate);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/visitorsCountByDate")
    public ResponseEntity<?> totalVisitorsCountByDate(@RequestParam LocalDate vDate,@RequestParam(required =false) Integer categoryId){
        try {
            Optional<CategoryData> categoryDataOptional = categoryRepo.findById(categoryId);
            if (categoryDataOptional.isPresent()){
                CategoryData categoryData = categoryDataOptional.get();
                String name = categoryData.getCategory();
                if ("Public".equalsIgnoreCase(name)){
                    return spotRegService.totalPublicVisitorsCountByDate(vDate);
                } else if ("Institution".equalsIgnoreCase(name)) {
                    //return spotRegService.totalInstitutionVisitorsCountByDate(vDate);
                } else if ("Foreigner".equalsIgnoreCase(name)) {
                    //return spotRegService.totalForeignerVisitorsCountByDate(vDate);
                }
            }
            return spotRegService.totalVisitorSCountByDate(vDate);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/visitorsCountByRangeOfDate")
    public ResponseEntity<List<VisitsCountDto>> visitorsCountByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        try {
            return spotRegService.visitorsCountByDateRange(startDate,endDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }
}
