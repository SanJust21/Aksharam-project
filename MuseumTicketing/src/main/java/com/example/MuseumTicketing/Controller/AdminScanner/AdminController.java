package com.example.MuseumTicketing.Controller.AdminScanner;

import com.example.MuseumTicketing.DTO.AdminScanner.*;
import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.DTO.AdminScanner.SignUpRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.UpdateRoleRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.UpdateScannerPasswordRequest;
import com.example.MuseumTicketing.DTO.PriceRequest;
import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Users;
import com.example.MuseumTicketing.Service.AdminScanner.AuthenticationService;
import com.example.MuseumTicketing.Service.BasePrice.PriceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;

    private final PriceRequestService priceRequestService;

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hai Admin");
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/addEmployee")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        try {
            ResponseEntity<?> users = authenticationService.signup(signUpRequest);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException ex) {
            CustomResponse customResponse = new CustomResponse("Name and email are required", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse);
        } catch (Exception ex) {
            CustomResponse customResponse = new CustomResponse("Error occurred during signup", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
    }


    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/employees")
    public ResponseEntity<List<Users>> getAllEmployees() {
        List<Users> employees = authenticationService.getAllUsersByRole(Role.EMPLOYEE);
        employees.sort(Comparator.comparingLong(Users::getId));
        return ResponseEntity.ok(employees);
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/scanners")
    public ResponseEntity<List<Users>> getAllScanners() {
        List<Users> scanners = authenticationService.getAllUsersByRole(Role.SCANNER);
        scanners.sort(Comparator.comparingLong(Users::getId));
        return ResponseEntity.ok(scanners);
    }

    @GetMapping("/employees-and-scanners")
    public ResponseEntity<List<Users>> getAllEmployeesAndScanners() {
        List<Users> roles = authenticationService.getAllUsersByRoles(Arrays.asList(Role.EMPLOYEE, Role.SCANNER));
        roles.sort(Comparator.comparingLong(Users::getId));
        return ResponseEntity.ok(roles);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/allTickets")
    public ResponseEntity<List<Object>> getAllTickets() {
        List<Object> allTickets = authenticationService.getAllTickets();
        return ResponseEntity.ok(allTickets);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/uploadImg/{employeeId}")
    public ResponseEntity<?> uploadImageToFIleSystem(@PathVariable String employeeId, @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = authenticationService.uploadImageToFileSystem(file, employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/downloadImg/{employeeId}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String employeeId) throws IOException {
        byte[] imageData = authenticationService.downloadImageFromFileSystem(employeeId);
        if (imageData != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found for user ID: " + employeeId);
        }
    }


    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PutMapping("/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable String employeeId, @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.updateEmployee(employeeId, signUpRequest);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId) {
        String  message = authenticationService.deleteEmployee(employeeId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete-by-name/{name}")
    public ResponseEntity<String> deleteEmployeeByName(@PathVariable String name) {
        String message = authenticationService.deleteEmployeeByName(name);
        return ResponseEntity.ok(message);
    }

//    @CrossOrigin(origins = AppConfig.BASE_URL)
//    @PutMapping("/updateRole/{employeeId}")
//    public ResponseEntity<String> updateEmployeeRole(@PathVariable String employeeId, @RequestParam Role newRole) {
//        String message = authenticationService.updateEmployeeRole(employeeId, newRole);
//        return ResponseEntity.ok(message);
//    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PutMapping("/updateRole/{employeeId}")
    public ResponseEntity<String> updateEmployeeRole(@PathVariable String employeeId, @RequestBody UpdateRoleRequest updateRequest) {
        Role newRole = updateRequest.getNewRole();
        String newPassword = updateRequest.getNewPassword();
        String message = authenticationService.updateEmployeeRole(employeeId, newRole, newPassword);
        return ResponseEntity.ok(message);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PutMapping("/updateScannerPassword")
    public ResponseEntity<String> updateScannerPassword(@RequestBody UpdateScannerPasswordRequest request) {
        String employeeId = request.getEmployeeId();
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match");
        }

        String message = authenticationService.updateScannerPassword(employeeId, newPassword);
        return ResponseEntity.ok(message);
    }
   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/addPrice")
    public ResponseEntity<?> addPrice(@RequestBody PriceRequest priceRequest) {
        try {
            PriceRequest addedPrice = priceRequestService.addPrice(priceRequest);
            return ResponseEntity.ok(addedPrice);
        } catch (IllegalArgumentException e) {
            CustomResponse customResponse = new CustomResponse("Price with the same type and category already exists", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse);
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @DeleteMapping("/deletePrice/{id}")
    public ResponseEntity<?> deletePrice(@PathVariable Integer id) {
        try {
            priceRequestService.deletePriceById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable Integer id, @RequestBody PriceRequest priceRequest) {
        try {
            PriceRequest updatedPrice = priceRequestService.updatePrice(id, priceRequest);
            return ResponseEntity.ok(updatedPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }

    @PostMapping("/deletePrice")
    public void deletePrice(@RequestParam("type") String type, @RequestParam("category") String category) {
        priceRequestService.deletePriceByTypeAndCategory(type, category);
    }

    @PutMapping("/updatePrice")
    public PriceRequest updatePrice(@RequestParam("type") String type, @RequestParam("category") String category, @RequestBody PriceRequest priceRequest) {
        return priceRequestService.updatePriceByTypeAndCategory(type, category, priceRequest);
    }

}
