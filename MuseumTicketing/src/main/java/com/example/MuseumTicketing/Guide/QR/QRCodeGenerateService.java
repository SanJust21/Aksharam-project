package com.example.MuseumTicketing.Guide.QR;

import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class QRCodeGenerateService {

    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;

    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;

    @Autowired
    private CommonIdQRCodeRepo commonIdQRCodeRepo;

    @Autowired
    private AlphaNumeric alphaNumeric;

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private File convertBytesToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }



//    public QRCodeResponse generateQRCodeAndSave(String mMalUid, String mEngUid) throws WriterException,IOException{
//        MainTitleMal mainTitleMal = mainTitleMalRepo.findBymMalUid(mMalUid).orElse(null);
//        Optional<MainTitleEng> mainTitleEng = Optional.ofNullable(mainTitleEngRepo.findBymEngUid(mEngUid));
//
//        if (mainTitleMal == null || mainTitleEng == null) {
//            throw new IllegalArgumentException("Data not found!");
//        }
//
//        String commonId = alphaNumeric.generateRandomNumber();
//        String qrContent = "CommonId: " + commonId;
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
//
//        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//        byte[] qrCode = pngOutputStream.toByteArray();
//
//        String fileName = "qr_" + commonId + ".png";
//        File qrCodeFile = convertBytesToFile(qrCode, fileName);
//        s3Client.putObject(new PutObjectRequest(bucketName, fileName, qrCodeFile));
//        qrCodeFile.delete();
//        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
//
//        CommonIdQRCode commonIdQRCode = new CommonIdQRCode();
//        commonIdQRCode.setCommonId(commonId);
//        commonIdQRCode.setMalId(mMalUid);
//        commonIdQRCode.setEngId(mEngUid);
//        commonIdQRCode.setFName(fileName);
//        commonIdQRCode.setQrCodeUrl(fileUrl);
//        commonIdQRCode.setQrCodeImage(qrCode);
//
//
//        commonIdQRCodeRepo.save(commonIdQRCode);
//
//        return new QRCodeResponse(qrCode,commonId);
//
//    }

    public byte[] generateQRCodeAndSave(String mMalUid, String mEngUid) throws WriterException, IOException {
        MainTitleMal mainTitleMal = mainTitleMalRepo.findBymMalUid(mMalUid).orElse(null);
        Optional<MainTitleEng> mainTitleEng = Optional.ofNullable(mainTitleEngRepo.findBymEngUid(mEngUid));

        if (mainTitleMal == null || mainTitleEng == null) {
            throw new IllegalArgumentException("Data not found!");
        }

        String commonId = alphaNumeric.generateRandomNumber();
        String qrContent = "CommonId: " + commonId;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] qrCode = pngOutputStream.toByteArray();

        String fileName = "qr_" + commonId + ".png";
        File qrCodeFile = convertBytesToFile(qrCode, fileName);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, qrCodeFile));
        qrCodeFile.delete();
        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

        CommonIdQRCode commonIdQRCode = new CommonIdQRCode();
        commonIdQRCode.setCommonId(commonId);
        commonIdQRCode.setMalId(mMalUid);
        commonIdQRCode.setEngId(mEngUid);
        commonIdQRCode.setFName(fileName);
        commonIdQRCode.setQrCodeUrl(fileUrl);
        commonIdQRCode.setQrCodeImage(qrCode);


        commonIdQRCodeRepo.save(commonIdQRCode);

        return qrCode;
    }


    public String getCommonId(String mMalUid, String mEngUid) {
        Optional<CommonIdQRCode> commonIdQRCodeOptional = commonIdQRCodeRepo.findByMalIdAndEngId(mMalUid,mEngUid);
        if (commonIdQRCodeOptional.isPresent()){
            CommonIdQRCode commonIdQRCode = commonIdQRCodeOptional.get();
            if (commonIdQRCode.getMalId().equals(mMalUid) && commonIdQRCode.getEngId().equals(mEngUid)){
                return commonIdQRCode.getCommonId();
            }
        }else {
            return null;
        }return null;
    }
}
