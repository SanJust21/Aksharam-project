package com.example.MuseumTicketing.Service.QR;

import com.example.MuseumTicketing.Service.Details.InstitutionDetailsService;
import com.example.MuseumTicketing.DTO.QR.BookingQrRequest;
import com.example.MuseumTicketing.DTO.QR.QrCodeResponse;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.example.MuseumTicketing.Service.Details.InstitutionDetailsService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;

@Service
public class BookingQrService {

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private InstitutionDetailsService institutionDetailsService;

    @Autowired
    private PublicDetailsRepo publicDetailsRepo;

    @Autowired
    private InstitutionDetailsRepo institutionDetailsRepo;

    @Autowired
    private ForeignerDetailsRepo foreignerDetailsRepo;

   // public byte[] generateAndFetchQrCode(BookingQrRequest bookingQrRequest) throws WriterException, IOException {
//   public QrCodeResponse generateAndFetchQrCode(BookingQrRequest bookingQrRequest) throws WriterException, IOException {
//
//        String userType = determineUserType(bookingQrRequest.getPaymentid());
//
//        String qrCodeDetails;
//        if ("institution".equalsIgnoreCase(bookingQrRequest.getType())) {
//            InstitutionDetails institutionDetails = institutionDetailsRepo.findByPaymentid(bookingQrRequest.getPaymentid());
//            qrCodeDetails = createBookingInfo(institutionDetails);
//        } else {
//            PublicDetails publicDetails = publicDetailsRepo.findByPaymentid(bookingQrRequest.getPaymentid());
//            qrCodeDetails = createBookingInfo(publicDetails);
//        }
//
//        // Generate QR code
//        //return qrCodeService.generateQrCode(qrCodeDetails);
//       byte[] qrCodeImage = qrCodeService.generateQrCode(qrCodeDetails);
//
//       // Create and return the DTO
//       QrCodeResponse response = new QrCodeResponse();
//       response.setQrCodeImage(qrCodeImage);
//       response.setUserDetails(qrCodeDetails);
//       return response;
//    }

//    private static final String BOOKING_ID_PREFIX = "AKM";
//
//    // Method to generate a random 5-digit number
//    private String generateRandomNumber() {
//        Random random = new Random();
//        int randomNumber = random.nextInt(90000) + 10000; // Generates a random number between 10000 and 99999
//        return String.valueOf(randomNumber);
//    }
//
//    // Method to generate the booking ID
//    private String generateticketId() {
//        return BOOKING_ID_PREFIX + generateRandomNumber();
//    }
    public QrCodeResponse generateAndFetchQrCode(BookingQrRequest bookingQrRequest) throws WriterException, IOException {
        String paymentId = bookingQrRequest.getPaymentid();
        //String ticketId = BOOKING_ID_PREFIX + generateRandomNumber();

        // Search in InstitutionDetails
        InstitutionDetails institutionDetails = findInstitutionDetails(paymentId);
        if (institutionDetails != null) {
//            institutionDetails.setTicketId(ticketId);
//            institutionDetailsRepo.save(institutionDetails);
            String qrCodeDetails = createBookingInfo(institutionDetails);
            return generateQrCodeResponse(qrCodeDetails);
        }

        // If not found in InstitutionDetails, search in PublicDetails
        PublicDetails publicDetails = findPublicDetails(paymentId);
        if (publicDetails != null) {
//            publicDetails.setTicketId(ticketId);
//            publicDetailsRepo.save(publicDetails);
            String qrCodeDetails = createBookingInfo(publicDetails);
            return generateQrCodeResponse(qrCodeDetails);
        }

        ForeignerDetails foreignerDetails = findForeignerDetails(paymentId);
        if (foreignerDetails != null) {
//            foreignerDetails.setTicketId(ticketId);
//            foreignerDetailsRepo.save(foreignerDetails);
            String qrCodeDetails = createBookingInfo(foreignerDetails);
            return generateQrCodeResponse(qrCodeDetails);
        }

        // If paymentId is not found in either table
        return generateErrorResponse("Details not found for given paymentId");
    }


    private QrCodeResponse generateQrCodeResponse(String qrCodeDetails) throws WriterException, IOException {
        byte[] qrCodeImage = qrCodeService.generateQrCode(qrCodeDetails);


        QrCodeResponse response = new QrCodeResponse();
        response.setQrCodeImage(qrCodeImage);
        response.setUserDetails(qrCodeDetails);
        return response;
    }


    private InstitutionDetails findInstitutionDetails(String paymentId) {
        return institutionDetailsRepo.findByPaymentid(paymentId);
    }

    private PublicDetails findPublicDetails(String paymentId) {
        return publicDetailsRepo.findByPaymentid(paymentId);
    }

    private ForeignerDetails findForeignerDetails(String paymentId) {
        return foreignerDetailsRepo.findByPaymentid(paymentId);
    }

    private QrCodeResponse generateErrorResponse(String errorMessage) {
        QrCodeResponse response = new QrCodeResponse();
        response.setErrorMessage(errorMessage);
        return response;
    }


    private String createBookingInfo(InstitutionDetails institutionDetails) {
        LocalTime slotName = institutionDetails.getSlotName();
        return String.format(
                "Name of Institution: %s, Students: %d, Teachers: %d, Date: %s, Amount: %.2f, Booking ID: %s, Slot Name: %02d:%02d:%02d",
                institutionDetails.getInstitutionName(),
                institutionDetails.getNumberOfStudents(),
                institutionDetails.getNumberOfTeachers(),
                institutionDetails.getVisitDate(),
                institutionDetails.getTotalPrice(),
                institutionDetails.getTicketId(),
                slotName.getHour(),
                slotName.getMinute(),
                slotName.getSecond()
        );
    }

    private String createBookingInfo(PublicDetails publicDetails) {
        LocalTime slotName = publicDetails.getSlotName();
        return String.format(
                "Name: %s, Adults: %d, Children: %d, Seniors: %d, Date: %s, Amount: %.2f, Booking ID: %s, Slot Name: %02d:%02d:%02d",
                publicDetails.getName(),
                publicDetails.getNumberOfAdults(),
                publicDetails.getNumberOfChildren(),
                publicDetails.getNumberOfSeniors(),
                publicDetails.getVisitDate(),
                publicDetails.getTotalPrice(),
                publicDetails.getTicketId(),
                slotName.getHour(),
                slotName.getMinute(),
                slotName.getSecond()
        );
    }

    private String createBookingInfo(ForeignerDetails foreignerDetails) {
        LocalTime slotName = foreignerDetails.getSlotName();
        return String.format(
                "Name: %s, Adults: %d, Children: %d, Date: %s, Amount: %.2f, Booking ID: %s, Slot Name: %02d:%02d:%02d",
                foreignerDetails.getName(),
                foreignerDetails.getNumberOfAdults(),
                foreignerDetails.getNumberOfChildren(),
                foreignerDetails.getVisitDate(),
                foreignerDetails.getTotalPrice(),
                foreignerDetails.getTicketId(),
                slotName.getHour(),
                slotName.getMinute(),
                slotName.getSecond()
        );
    }

    private String determineUserType(String paymentId) {
        // Check if paymentId exists in institution or public table
        if (institutionDetailsRepo.existsByPaymentid(paymentId)) {
            return "institution";
        } else if (publicDetailsRepo.existsByPaymentid(paymentId)) {
            return "public";
        } else {
            // Handle the case when paymentId is not found in either table
            throw new IllegalArgumentException("Invalid paymentId: " + paymentId);
        }
    }
}