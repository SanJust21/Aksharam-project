package com.example.MuseumTicketing.Service.Payment;
import com.example.MuseumTicketing.Model.ForeignerDetails;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import com.razorpay.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class PaymentService {

    private static String razorpayKeyId = "rzp_test_Lh738g2oARGFbD";


    private static String razorpayKeySecret = "iOSGwx2YAmHsl2dNuzfi1bSa";

    private static final String currency = "INR";

    private static final String BOOKING_ID_PREFIX = "AKM";

    private InstitutionDetailsRepo institutionDetailsRepo;
    private PublicDetailsRepo publicDetailsRepo;

    private ForeignerDetailsRepo foreignerDetailsRepo;

    @Autowired
    public PaymentService(
            InstitutionDetailsRepo institutionDetailsRepo,
            PublicDetailsRepo publicDetailsRepo,
            ForeignerDetailsRepo foreignerDetailsRepo) {
        this.institutionDetailsRepo = institutionDetailsRepo;
        this.publicDetailsRepo = publicDetailsRepo;
        this.foreignerDetailsRepo = foreignerDetailsRepo;
    }


    public String createOrder(double amount) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amount * 100); // Razorpay expects amount in paisa
            options.put("currency", currency);
            options.put("receipt", "order_rcptid_" + System.currentTimeMillis());
            options.put("payment_capture", 1); // Auto capture

            Order order = razorpayClient.orders.create(options);
            return order.get("id");
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    public boolean verifyPayment(String orderId, String paymentId, String signature) throws RazorpayException {
        try {

            if (!verifySignature(orderId, paymentId, signature)) {

                throw new RazorpayException("Signature verification failed.");
            }

            //Signature is verified, proceed with Razorpay API verification
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Fetch payment details by payment ID
            Payment payment = razorpayClient.payments.fetch(paymentId);

            // Check the payment status
            //return "captured".equals(payment.get("status"));
            boolean paymentStatus = "captured".equals(payment.get("status"));
            if (paymentStatus) {
                // Payment is successful, generate ticketId and set it into the respective repository
                String ticketId = generateTicketId();
                setTicketIdAndPaymentStatus(orderId, paymentId, ticketId);
            }

            return paymentStatus;
        } catch (RazorpayException e) {
            e.printStackTrace();
            throw new RazorpayException("Payment verification failed.", e);
        }
    }
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        String secret = "iOSGwx2YAmHsl2dNuzfi1bSa";

        // Concatenate orderId, paymentId, and your secret
        String generatedSignature = orderId + "|" + paymentId;
        generatedSignature = HmacUtils.hmacSha256Hex(secret, generatedSignature);

        // Compare generated signature with the received signature
        return generatedSignature.equals(signature);
    }


    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // Generates a random number between 10000 and 99999
        return String.valueOf(randomNumber);
    }

    private String generateTicketId() {

        return BOOKING_ID_PREFIX + generateRandomNumber();
    }

    private void setTicketIdAndPaymentStatus(String orderId, String paymentId, String ticketId) {
        // Determine the type of payment and set ticketId and paymentStatus in the respective repository
        Optional<InstitutionDetails> institutionDetailsOpt = institutionDetailsRepo.findByOrderId(orderId);
        Optional<PublicDetails> publicDetailsOpt = publicDetailsRepo.findByOrderId(orderId);
        Optional<ForeignerDetails> foreignerDetailsOpt = foreignerDetailsRepo.findByOrderId(orderId);

        if (institutionDetailsOpt.isPresent()) {
            InstitutionDetails institutionDetails = institutionDetailsOpt.get();
            institutionDetails.setTicketId(ticketId);
            institutionDetails.setPaymentid(paymentId);
            institutionDetails.setPaymentStatus(true);
            institutionDetailsRepo.save(institutionDetails);
        } else if (publicDetailsOpt.isPresent()) {
            PublicDetails publicDetails = publicDetailsOpt.get();
            publicDetails.setTicketId(ticketId);
            publicDetails.setPaymentid(paymentId);
            publicDetails.setPaymentStatus(true);
            publicDetailsRepo.save(publicDetails);
        } else if (foreignerDetailsOpt.isPresent()) {
            ForeignerDetails foreignerDetails = foreignerDetailsOpt.get();
            foreignerDetails.setTicketId(ticketId);
            foreignerDetails.setPaymentid(paymentId);
            foreignerDetails.setPaymentStatus(true);
            foreignerDetailsRepo.save(foreignerDetails);
        }
    }

    public boolean initiateRefund(String orderId, String paymentId, double amount) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject refundRequest = new JSONObject();
            refundRequest.put("payment_id", paymentId);
            refundRequest.put("amount", amount * 100); // Amount in paisa

            Refund refund = razorpayClient.payments.refund(refundRequest);

            // Handle refund response
            if ("processed".equals(refund.get("status"))) {
                // Refund is successful, update your database or logic accordingly
                updateRefundStatus(orderId, paymentId, amount);
                return true;
            } else {
                // Refund failed, handle accordingly
                return false;
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
            throw new RazorpayException("Refund failed.", e);
        }
    }

    private void updateRefundStatus(String orderId, String paymentId, double amount) {
        // Implement logic to update refund status in your database
        // You may want to mark the payment as refunded or update any other relevant information
    }

}

