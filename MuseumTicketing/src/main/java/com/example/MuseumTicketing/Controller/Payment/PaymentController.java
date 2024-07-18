package com.example.MuseumTicketing.Controller.Payment;
import com.example.MuseumTicketing.DTO.Payment.OrderRequest;
import com.example.MuseumTicketing.DTO.Payment.OrderResponse;
import com.example.MuseumTicketing.DTO.Payment.VerifyPaymentRequest;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.example.MuseumTicketing.Service.Email.EmailService;
import com.example.MuseumTicketing.Service.Payment.PaymentService;
import com.razorpay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {


    private static String razorpayKeyId = "rzp_test_Lh738g2oARGFbD";


    private static String razorpayKeySecret = "iOSGwx2YAmHsl2dNuzfi1bSa";

    private static final String currency = "INR";

    private PaymentService paymentService;

    private InstitutionDetailsRepo institutionDetailsRepo;
    private PublicDetailsRepo publicDetailsRepo;

    private ForeignerDetailsRepo foreignerDetailsRepo;

    private EmailService emailService;


    @Autowired
    public PaymentController(
            PaymentService paymentService,
            InstitutionDetailsRepo institutionDetailsRepo,
            PublicDetailsRepo publicDetailsRepo, ForeignerDetailsRepo foreignerDetailsRepo, EmailService emailService) {
        this.paymentService = paymentService;
        this.institutionDetailsRepo = institutionDetailsRepo;
        this.publicDetailsRepo = publicDetailsRepo;
        this.foreignerDetailsRepo = foreignerDetailsRepo;
        this.emailService =emailService;
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            double amount = orderRequest.getAmount();
            String sessionId = orderRequest.getSessionId();
            String uniqueId = orderRequest.getUniqueId();

            String orderId = paymentService.createOrder(amount);
            System.out.println(orderId);


            // Search the Institution and Public tables by sessionId
            Optional<InstitutionDetails> institutionDetails = institutionDetailsRepo.findByuniqueId(uniqueId);
            Optional<PublicDetails> publicDetails = publicDetailsRepo.findByuniqueId(uniqueId);
            Optional<ForeignerDetails> foreignerDetails = foreignerDetailsRepo.findByuniqueId(uniqueId);

            InstitutionDetails institutionDetailsEntity = institutionDetails.orElse(null);
            PublicDetails publicDetailsEntity = publicDetails.orElse(null);
            ForeignerDetails foreignerDetailsEntity = foreignerDetails.orElse(null);

            if (institutionDetailsEntity != null) {
                institutionDetailsEntity.setOrderId(orderId);
                institutionDetailsEntity.setSessionId(sessionId);
                institutionDetailsRepo.save(institutionDetailsEntity);
            } else if (publicDetailsEntity != null) {
                publicDetailsEntity.setOrderId(orderId);
                publicDetailsEntity.setSessionId(sessionId);
                publicDetailsRepo.save(publicDetailsEntity);
            } else if (foreignerDetailsEntity != null) {
                foreignerDetailsEntity.setOrderId(orderId);
                foreignerDetailsEntity.setSessionId(sessionId);
                foreignerDetailsRepo.save(foreignerDetailsEntity);
            } else {
                return ResponseEntity.badRequest().body("No corresponding details found for sessionId: " + sessionId);
            }
            OrderResponse orderResponse = new OrderResponse(razorpayKeyId, amount*100, currency, orderId, sessionId);
            System.out.println(orderResponse);
            return ResponseEntity.ok(orderResponse);
        } catch (RazorpayException e) {
            e.printStackTrace();
            String errorMessage = "Failed to create order. " + e.getMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/verify-payment")
    public ResponseEntity<Object> verifyPayment(
            @RequestBody VerifyPaymentRequest verifyPaymentRequest) {
        try {

            String orderId = verifyPaymentRequest.getOrderId();
            String paymentId = verifyPaymentRequest.getPaymentId();
            String signature = verifyPaymentRequest.getSignature();

            boolean paymentVerified = paymentService.verifyPayment(orderId, paymentId, signature);

            if (paymentVerified) {
                String ticketId = null;

                Optional<InstitutionDetails> institutionDetails = institutionDetailsRepo.findByOrderId(orderId);
                Optional<PublicDetails> publicDetails = publicDetailsRepo.findByOrderId(orderId);
                Optional<ForeignerDetails> foreignerDetails = foreignerDetailsRepo.findByOrderId(orderId);

                if (institutionDetails.isPresent()) {
                    ticketId = institutionDetails.get().getTicketId();
                } else if (publicDetails.isPresent()) {
                    ticketId = publicDetails.get().getTicketId();
                } else if (foreignerDetails.isPresent()) {
                    ticketId = foreignerDetails.get().getTicketId();
                } else {
                    return ResponseEntity.badRequest().body("No corresponding details found for orderId: " + orderId);
                }

                return ResponseEntity.ok("Payment successful. Order ID: " + orderId + ", Payment ID: " + paymentId + ", Ticket ID: " + ticketId);
            } else {
                return ResponseEntity.badRequest().body("Payment verification failed.");
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Payment verification failed. ");
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<Object> initiateRefund(@RequestParam String ticketId) {
        try {
            Optional<ForeignerDetails> foreignerDetailsOpt = foreignerDetailsRepo.findByticketId(ticketId);
            Optional<InstitutionDetails> institutionDetailsOpt = institutionDetailsRepo.findByticketId(ticketId);
            Optional<PublicDetails> publicDetailsOpt = publicDetailsRepo.findByticketId(ticketId);

            // Check which type of ticket it is and get orderId, paymentId, and amount accordingly
            String orderId = null;
            String paymentId = null;
            double amount = 0.0;
            String recipientEmail = null;

            if (foreignerDetailsOpt.isPresent()) {
                ForeignerDetails foreignerDetails = foreignerDetailsOpt.get();
                orderId = foreignerDetails.getOrderId();
                paymentId = foreignerDetails.getPaymentid();
                amount = foreignerDetails.getTotalPrice();
                recipientEmail = foreignerDetails.getEmail();
            } else if (institutionDetailsOpt.isPresent()) {
                InstitutionDetails institutionDetails = institutionDetailsOpt.get();
                orderId = institutionDetails.getOrderId();
                paymentId = institutionDetails.getPaymentid();
                amount = institutionDetails.getTotalPrice();
                recipientEmail =institutionDetails.getEmail();
            } else if (publicDetailsOpt.isPresent()) {
                PublicDetails publicDetails = publicDetailsOpt.get();
                orderId = publicDetails.getOrderId();
                paymentId = publicDetails.getPaymentid();
                amount = publicDetails.getTotalPrice();
                recipientEmail = publicDetails.getEmail();
            } else {
                return ResponseEntity.badRequest().body("Ticket with ID: " + ticketId + " not found.");
            }

            boolean refundInitiated = paymentService.initiateRefund(orderId, paymentId, amount);

            if (refundInitiated) {

                emailService.sendRefundInitiationEmail(recipientEmail, orderId, amount);

                return ResponseEntity.ok("Refund initiated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Refund initiation failed.");
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Refund initiation failed: " + e.getMessage());
        }
    }

}
