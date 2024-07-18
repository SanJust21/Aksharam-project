package com.example.MuseumTicketing.Service.Email;

import com.example.MuseumTicketing.DTO.QR.QrCodeResponse;
import com.example.MuseumTicketing.Repo.ForeignerDetailsRepo;
import com.example.MuseumTicketing.Repo.InstitutionDetailsRepo;
import com.example.MuseumTicketing.Repo.PublicDetailsRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;


import org.springframework.mail.javamail.MimeMessageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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
        String text = "Dear User,<br><br>";
        text += "Thank you for booking!<br>";
        text+= "Your Ticket ID is <b>" + ticketId + "</b>.<br>";
        text+= "Please find your QR code below.<br><br>";
        text+= "Best regards,<br>Your Museum Ticketing Team!";

        try {
            String to = getEmailByPaymentid(paymentid);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(new InternetAddress(senderEmail));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            ByteArrayInputStream bis = new ByteArrayInputStream(qrCodeResponse.getQrCodeImage());
            BufferedImage bufferedImage = ImageIO.read(bis);

            // Create a ByteArrayOutputStream to store the PNG image data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Write the BufferedImage as PNG to the ByteArrayOutputStream
            ImageIO.write(bufferedImage, "png", baos);

            // Convert the ByteArrayOutputStream to byte array
            byte[] pngImageData = baos.toByteArray();

            // Close streams
            bis.close();
            baos.close();
            //sendTicketEmailWithPDF(paymentid, qrCodeResponse.getUserDetails());

             //Embed the QR code image in the email
            ByteArrayResource qrCodeResource = new ByteArrayResource(pngImageData) {
                @Override
                public String getFilename() {
                    return ticketId + ".png";
                }
            };
            //helper.addInline("qrCodeImage", qrCodeResource, "image/png");
            helper.addAttachment(ticketId + ".png", qrCodeResource);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();

            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTicketEmail(byte[] pdfData, String paymentid) {

        String ticketId = getTicketIdByPaymentid(paymentid);

        String subject = "Your Ticket PDF!";
        String text = "Dear User,<br><br>";
        text += "Thank you for booking!<br>";
        text+= "Your Ticket ID is <b>" + ticketId + "</b>.<br>";
        text+= "Please find your QR code below.<br><br>";
        text+= "Best regards,<br>Your Museum Ticketing Team!";

        try {

            String to = getEmailByPaymentid(paymentid);

            // Create a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Enable multipart messages
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set sender email address
            helper.setFrom(new InternetAddress(senderEmail));
            helper.setTo(to);

            // Set subject and text
            helper.setSubject(subject);
            helper.setText(text, true);

            // Attach PDF to the email
            helper.addAttachment("Ticket.pdf", new ByteArrayResource(pdfData));

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public byte[] generateTicketPDF(String ticketId, String bookingDetails) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Add logo image
                BufferedImage logoImage = ImageIO.read(new File("C:/Users/azhym/Pictures/Screenshots/Aksharam Logo.png"));
                PDImageXObject logoXObject = LosslessFactory.createFromImage(document, logoImage);
                contentStream.drawImage(logoXObject, 50, 700);

                // Add ticket ID
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("Ticket ID: " + ticketId);
                contentStream.endText();

                // Add booking details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 630);
                contentStream.showText(bookingDetails);
                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    public void sendTicketEmailWithPDF(String paymentid, String bookingDetails) {
        try {
            String ticketId = getTicketIdByPaymentid(paymentid);
            byte[] pdfData = generateTicketPDF(ticketId, bookingDetails);

            // Send email with PDF attachment
            sendTicketEmail(pdfData, paymentid);
        } catch (IOException e) {
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

    private String getTicketIdByPaymentid(String paymentId) {


        if (institutionDetailsRepo.existsByPaymentid(paymentId)) {
            return institutionDetailsRepo.findByPaymentid(paymentId).getTicketId();
        } else if (publicDetailsRepo.existsByPaymentid(paymentId)) {
            return publicDetailsRepo.findByPaymentid(paymentId).getTicketId();
        }else if (foreignerDetailsRepo.existsByPaymentid(paymentId)) {
            return foreignerDetailsRepo.findByPaymentid(paymentId).getTicketId();
        }
        return "No ticket Id!";
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

