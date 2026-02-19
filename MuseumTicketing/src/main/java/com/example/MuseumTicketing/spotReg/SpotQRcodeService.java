package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.onlineTicket.qrCodeData.QRCodeUserData;
import com.example.MuseumTicketing.onlineTicket.qrCodeData.QRCodeUserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SpotQRcodeService {
    @Autowired
    private QRCodeUserRepository repository;
    public byte[] generateQRCode(String ticketId) throws WriterException, IOException{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(ticketId, BarcodeFormat.QR_CODE,200,200);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"png",byteArrayOutputStream);
        QRCodeUserData userData = new QRCodeUserData();
        userData.setTicketId(ticketId);
        userData.setQrCodeData(byteArrayOutputStream.toByteArray());
        userData.setCreatedAt(LocalDateTime.now());
        repository.save(userData);
        log.info("qrData is stored",userData);
        return byteArrayOutputStream.toByteArray();
    }
}
