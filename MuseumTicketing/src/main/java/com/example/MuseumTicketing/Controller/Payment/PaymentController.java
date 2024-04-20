package com.example.MuseumTicketing.Controller.Payment;
import com.example.MuseumTicketing.Config.AppConfig;
import com.example.MuseumTicketing.DTO.Payment.OrderRequest;
import com.example.MuseumTicketing.DTO.Payment.OrderResponse;
import com.example.MuseumTicketing.DTO.Payment.VerifyPaymentRequest;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.example.MuseumTicketing.Service.Payment.PaymentService;
import com.razorpay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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


    @Autowired
    public PaymentController(
            PaymentService paymentService,
            InstitutionDetailsRepo institutionDetailsRepo,
            PublicDetailsRepo publicDetailsRepo, ForeignerDetailsRepo foreignerDetailsRepo) {
        this.paymentService = paymentService;
        this.institutionDetailsRepo = institutionDetailsRepo;
        this.publicDetailsRepo = publicDetailsRepo;
        this.foreignerDetailsRepo = foreignerDetailsRepo;
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            double amount = orderRequest.getAmount();
            String sessionId = orderRequest.getSessionId();

            String orderId = paymentService.createOrder(amount);
            System.out.println(orderId);


            // Search the Institution and Public tables by sessionId
            Optional<InstitutionDetails> institutionDetails = institutionDetailsRepo.findBySessionId(sessionId);
            Optional<PublicDetails> publicDetails = publicDetailsRepo.findBySessionId(sessionId);
            Optional<ForeignerDetails> foreignerDetails = foreignerDetailsRepo.findBySessionId(sessionId);

            InstitutionDetails institutionDetailsEntity = institutionDetails.orElse(null);
            PublicDetails publicDetailsEntity = publicDetails.orElse(null);
            ForeignerDetails foreignerDetailsEntity = foreignerDetails.orElse(null);

            if (institutionDetailsEntity != null) {
                institutionDetailsEntity.setOrderId(orderId);
                institutionDetailsRepo.save(institutionDetailsEntity);
            } else if (publicDetailsEntity != null) {
                publicDetailsEntity.setOrderId(orderId);
                publicDetailsRepo.save(publicDetailsEntity);
            } else if (foreignerDetailsEntity != null) {
                foreignerDetailsEntity.setOrderId(orderId);
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

}
