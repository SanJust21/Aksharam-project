package com.example.MuseumTicketing.Service.QR;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeService {

    public byte[] generateQrCode(String data) throws WriterException, IOException {

        String ticketId = extractTicketId(data);

        // Encode the data into a BitMatrix (2D array of bits)
        BitMatrix bitMatrix = new QRCodeWriter().encode(ticketId, BarcodeFormat.QR_CODE, 300, 300);

        // Convert the BitMatrix to a byte array (PNG image)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        } catch (IOException e) {
            // Handle the exception or rethrow it
            throw new IOException("Error writing QR code to stream", e);
        }

        // Return the byte array
        return byteArrayOutputStream.toByteArray();
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

}

