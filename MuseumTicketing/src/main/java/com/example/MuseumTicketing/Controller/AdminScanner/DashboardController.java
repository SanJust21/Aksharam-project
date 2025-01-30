package com.example.MuseumTicketing.Controller.AdminScanner;

import com.example.MuseumTicketing.DTO.AdminScanner.CategoryVisitorDTO;
import com.example.MuseumTicketing.DTO.AdminScanner.TotalIncomeDTO;
import com.example.MuseumTicketing.DTO.AdminScanner.TotalTicketsDTO;
import com.example.MuseumTicketing.DTO.DetailsRequest;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.example.MuseumTicketing.Repo.ShowTimeRepo;
import com.example.MuseumTicketing.Service.AdminScanner.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private final ShowTimeRepo showTimeRepo;

    private final PublicDetailsRepo publicDetailsRepo;

    private final InstitutionDetailsRepo institutionDetailsRepo;

    private final ForeignerDetailsRepo foreignerDetailsRepo;

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/currentDayList")
    public ResponseEntity<List<DetailsRequest>> getCurrentDayDetails() {
        List<DetailsRequest> currentDayDetails = dashboardService.getCurrentDayDetails();
        return ResponseEntity.ok(currentDayDetails);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/Date")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsForDate(date);
        return ResponseEntity.ok(visitorDTOs);
    }

    // Controller method for getting total visitors for the current week
    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/week")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsForWeek() {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsForWeek();
        return ResponseEntity.ok(visitorDTOs);
    }

    // Controller method for getting total visitors for a custom week
   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/week/custom")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsForCustomWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsForDateRange(startDate, endDate);
        return ResponseEntity.ok(visitorDTOs);
    }
   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/month")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsForMonth(
            @RequestParam String monthName,
            @RequestParam int year) {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsForMonth(monthName, year);
        return ResponseEntity.ok(visitorDTOs);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/year")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsForYear(
            @RequestParam int year) {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsForYear(year);
        return ResponseEntity.ok(visitorDTOs);
    }

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalVisitors/upToNow")
    public ResponseEntity<List<CategoryVisitorDTO>> getTotalVisitorsUpToNow() {
        List<CategoryVisitorDTO> visitorDTOs = dashboardService.getTotalVisitorsUpToNow();
        return ResponseEntity.ok(visitorDTOs);
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalTickets")
    public ResponseEntity<TotalTicketsDTO> getTotalTickets() {
        TotalTicketsDTO totalTicketsDTO = dashboardService.getTotalTickets();
        return ResponseEntity.ok(totalTicketsDTO);
    }

    @GetMapping("/totalTickets/date")
    public ResponseEntity<TotalTicketsDTO> getTotalTicketsForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TotalTicketsDTO totalTicketsDTO = dashboardService.getTotalTicketsForDate(date);
        return ResponseEntity.ok(totalTicketsDTO);
    }


    @GetMapping("/totalTickets/month")
    public ResponseEntity<TotalTicketsDTO> getTotalTicketsForMonth(
            @RequestParam String monthName,
            @RequestParam int year) {
        Month month = Month.valueOf(monthName.toUpperCase());

        TotalTicketsDTO totalTicketsDTO = dashboardService.getTotalTicketsForMonth(month, year);
        return ResponseEntity.ok(totalTicketsDTO);
    }

    @GetMapping("/totalTickets/year")
    public ResponseEntity<List<TotalTicketsDTO>> getTotalTicketsForYear(
            @RequestParam int year) {
        List<TotalTicketsDTO> totalTicketsList = dashboardService.getTotalTicketsForYear(year);
        return ResponseEntity.ok(totalTicketsList);
    }


    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalIncome")
    public ResponseEntity<TotalIncomeDTO> getTotalIncome() {
        TotalIncomeDTO totalIncomeDTO = dashboardService.getTotalIncome();
        return ResponseEntity.ok(totalIncomeDTO);
    }

    @GetMapping("/totalIncome/date")
    public ResponseEntity<TotalIncomeDTO> getTotalIncomeForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TotalIncomeDTO totalIncomeDTO = dashboardService.getTotalIncomeForDate(date);
        return ResponseEntity.ok(totalIncomeDTO);
    }

    @GetMapping("/totalIncome/month")
    public ResponseEntity<TotalIncomeDTO> getTotalIncomeForMonth(
            @RequestParam String monthName,
            @RequestParam int year) {
        // Parse the month name to get the corresponding Month enum value
        Month month = Month.valueOf(monthName.toUpperCase());

        TotalIncomeDTO totalIncomeDTO = dashboardService.getTotalIncomeForMonth(month, year);
        return ResponseEntity.ok(totalIncomeDTO);
    }



    // @CrossOrigin(origins = AppConfig.BASE_URL)
    @GetMapping("/totalIncome/year")
    public ResponseEntity<List<TotalIncomeDTO>> getTotalIncomeForYear(
            @RequestParam int year) {
        List<TotalIncomeDTO> totalIncomeList = dashboardService.getTotalIncomeForYear(year);
        return ResponseEntity.ok(totalIncomeList);
    }

    @GetMapping("/peakSlot")
    public ResponseEntity<List<LocalTime>> findPeakSlot() {

        List<ShowTime> showTimes = showTimeRepo.findAll();
        List<PublicDetails> publicDetails = publicDetailsRepo.findAll();
        List<InstitutionDetails> institutionDetails = institutionDetailsRepo.findAll();
        List<ForeignerDetails> foreignerDetails = foreignerDetailsRepo.findAll();

        return ResponseEntity.ok(dashboardService.findPeakSlot(showTimes, publicDetails, institutionDetails, foreignerDetails));
    }


}
