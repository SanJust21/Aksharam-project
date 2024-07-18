package com.example.MuseumTicketing.Controller.AdminScanner;

import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.DTO.AdminScanner.ScanRequest;
import com.example.MuseumTicketing.Model.*;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.example.MuseumTicketing.Repo.UsersRepo;
import com.example.MuseumTicketing.Service.AdminScanner.ScannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/scanner")
@CrossOrigin
@RequiredArgsConstructor
public class ScannerController {

    private final ScannerService scannerService;

    private final UsersRepo usersRepo;

    private final ForeignerDetailsRepo foreignerDetailsRepo;

    private final InstitutionDetailsRepo institutionDetailsRepo;

    private final PublicDetailsRepo publicDetailsRepo;

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/hello/{employeeId}")
    public ResponseEntity<Object> sayHello(@PathVariable String employeeId) {

        Optional<Users> user = usersRepo.findByEmployeeId(employeeId);

        if (user.isPresent()) {
            Users emp = user.get();
            return ResponseEntity.ok(new CustomResponse(emp.getName(), HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse("Employee with ID " + employeeId + " not found", HttpStatus.NOT_FOUND.value()));
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/scan")
    public ResponseEntity<?> scanTicket(@RequestBody ScanRequest scanRequest) {
         LocalDateTime scannedTime;

        Optional<ForeignerDetails> foreignerDetails = foreignerDetailsRepo.findByticketId(scanRequest.getTicketId());
        Optional<InstitutionDetails> institutionDetails = institutionDetailsRepo.findByticketId(scanRequest.getTicketId());
        Optional<PublicDetails> publicDetails = publicDetailsRepo.findByticketId(scanRequest.getTicketId());

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            scannedTime = LocalDateTime.parse(scanRequest.getScannedTime(), formatter);
        } catch (DateTimeParseException e) {
            CustomResponse customResponse = new CustomResponse("Invalid scanned time format", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse);
        }

        ResponseEntity<?> response = scannerService.identifyUserAndGetDetails(scanRequest.getTicketId(), scannedTime);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            if (foreignerDetails.isPresent() && !foreignerDetails.get().getVisitDate().isEqual(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("Visit date is different. " + foreignerDetails.get().getVisitDate(), HttpStatus.BAD_REQUEST.value()) );
            } else if (institutionDetails.isPresent() && !institutionDetails.get().getVisitDate().isEqual(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("Visit date is different. " + institutionDetails.get().getVisitDate(), HttpStatus.BAD_REQUEST.value()));
            } else if (publicDetails.isPresent() && !publicDetails.get().getVisitDate().isEqual(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("Visit date is different. " + publicDetails.get().getVisitDate(), HttpStatus.BAD_REQUEST.value()));
            }
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse("Visit date is different.", HttpStatus.BAD_REQUEST.value()));
        }
        return response;
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/scannedList")
    public ResponseEntity<List<ScannedDetails>> getScannedDetailsForToday() {
        // Retrieve scanned details for today from the service
        List<ScannedDetails> scannedDetailsList = scannerService.getScannedDetailsForToday();

        // Sort the scanned details based on scanned time
        scannedDetailsList.sort(Comparator.comparing(ScannedDetails::getScannedTime));

        return ResponseEntity.ok(scannedDetailsList);
    }
}
