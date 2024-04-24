package com.example.MuseumTicketing.Service.Email;

import com.example.MuseumTicketing.DTO.QR.BookingQrRequest;
import com.example.MuseumTicketing.DTO.QR.QrCodeResponse;
import com.example.MuseumTicketing.Model.InstitutionDetails;
import com.example.MuseumTicketing.Model.PublicDetails;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMultipart;


import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PublicDetailsRepo publicDetailsRepo;

    @Autowired
    private InstitutionDetailsRepo institutionDetailsRepo;

    @Autowired
    private ForeignerDetailsRepo foreignerDetailsRepo;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendQrCodeEmail(String paymentid, QrCodeResponse qrCodeResponse) {

        String ticketId = extractTicketId(qrCodeResponse.getUserDetails());

        String subject = "Your Booking QR Code";
        String text = "Thank you for booking! Your Ticket ID is " + ticketId + ". Please find your QR code below.";

        try {
            String to = getEmailByPaymentid(paymentid);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(new InternetAddress(senderEmail));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);


            // Embed the QR code image in the email
            helper.addInline("qrCodeImage", new ByteArrayResource(qrCodeResponse.getQrCodeImage()), "image/png");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();

            }
    }

    public void sendTicketEmail(byte[] pdfData, String paymentid) {
        String subject = "Your Ticket PDF!";
        String text = "Thank you for booking! Please find your ticket PDF attached.";

        try {

            String to = getEmailByPaymentid(paymentid);

            // Create a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Enable multipart messages
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set sender email address
            helper.setFrom(new InternetAddress(senderEmail));

            // Set subject and text
            helper.setSubject(subject);
            helper.setText(text);

            // Attach PDF to the email
            helper.addAttachment("ticket.pdf", new ByteArrayResource(pdfData));

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    private String getEmailByPaymentid(String paymentId) {


         if (institutionDetailsRepo.existsByPaymentid(paymentId)) {
             return institutionDetailsRepo.findByPaymentid(paymentId).getEmail();
         } else if (publicDetailsRepo.existsByPaymentid(paymentId)) {
             return publicDetailsRepo.findByPaymentid(paymentId).getEmail();
         }else if (foreignerDetailsRepo.existsByPaymentid(paymentId)) {
             return foreignerDetailsRepo.findByPaymentid(paymentId).getEmail();
         }
       return "Email not found!";
   }
    private String extractTicketId(String userDetails) {

        if (userDetails != null && !userDetails.isEmpty()) {
            // Split the qrCodeDetails string by commas or any other delimiter used in formatting
            String[] parts = userDetails.split(",");

            for (String part : parts) {
                // Trim the part to remove leading and trailing whitespace
                String trimmedPart = part.trim();

                if (trimmedPart.startsWith("Booking ID")) {
                    // Extract the ticket ID from the part
                    String[] keyValue = trimmedPart.split(":");
                    if (keyValue.length == 2) {
                        // Return the ticket ID after trimming leading and trailing whitespace
                        return keyValue[1].trim();
                    }
                }
            }
        }

        return null;
    }

    public void sendRefundInitiationEmail(String recipientEmail, String orderId, double refundedAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Refund Initiated for Order #" + orderId);

        String emailContent = "Dear User,\n\n";
        emailContent += "We are writing to inform you that a refund has been initiated for your order #" + orderId + ".\n";
        emailContent += "Refunded Amount: " + refundedAmount + "\n\n";
        emailContent += "Thank you for your patience.\n";
        emailContent += "Best regards,\nYour Museum Ticketing Team";

        message.setText(emailContent);

        javaMailSender.send(message);
    }

   }

