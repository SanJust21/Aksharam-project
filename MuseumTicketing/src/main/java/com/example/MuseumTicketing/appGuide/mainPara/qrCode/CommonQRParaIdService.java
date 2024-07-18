package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirst;
import com.example.MuseumTicketing.appGuide.audio.first.AudioFirstRepo;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMain;
import com.example.MuseumTicketing.appGuide.audio.main.AudioMainRepo;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirstRepo;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMainRepo;
import com.example.MuseumTicketing.appGuide.mainPara.CombinedPara;
import com.example.MuseumTicketing.appGuide.mainPara.first.*;
import com.example.MuseumTicketing.newFile.appGuide.mainPara.first.*;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirst;
import com.example.MuseumTicketing.appGuide.video.first.VideoFirstRepo;
import com.example.MuseumTicketing.appGuide.video.main.VideoMain;
import com.example.MuseumTicketing.appGuide.video.main.VideoMainRepo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CommonQRParaIdService {
    @Autowired
    private MainTopicEngRepo mainTopicEngRepo;

    @Autowired
    private MainTopicMalRepo mainTopicMalRepo;

    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;

    @Autowired
    private AlphaNumeric alphaNumeric;

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private FirstTopicEngRepo firstTopicEngRepo;
    @Autowired
    private FirstTopicMalRepo firstTopicMalRepo;
    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private ImgDataMainRepo imgDataMainRepo;
    @Autowired
    private ImgDataFirstRepo imgDataFirstRepo;
    @Autowired
    private AudioMainRepo audioMainRepo;
    @Autowired
    private VideoMainRepo videoMainRepo;
    @Autowired
    private AudioFirstRepo audioFirstRepo;
    @Autowired
    private VideoFirstRepo videoFirstRepo;
    @Autowired
    private DataTypeRepo dataTypeRepo;

    private File convertBytesToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }

    public byte[] generateQRCodeAndSave(String mMalUid, String mEngUid) throws WriterException, IOException {
//        MainTopicMal mainTopicMal = mainTopicMalRepo.findBymMalUid(mMalUid);
//        MainTopicEng mainTopicEng = mainTopicEngRepo.findBymEngUid(mEngUid);
        Optional<MainTopicMal> mainTopicMalOptional = mainTopicMalRepo.findBymMalUid(mMalUid);
        Optional<MainTopicEng>mainTopicEngOptional = mainTopicEngRepo.findBymEngUid(mEngUid);

        if (mainTopicMalOptional.isEmpty() || mainTopicEngOptional .isEmpty()) {
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

        CommonQRParaId commonIdQR = new CommonQRParaId();
        commonIdQR.setCommonId(commonId);
        commonIdQR.setMalId(mMalUid);
        commonIdQR.setEngId(mEngUid);
        commonIdQR.setFName(fileName);
        commonIdQR.setQrCodeUrl(fileUrl);
        commonIdQR.setQrCodeImage(qrCode);


        commonQRParaIdRepo.save(commonIdQR);

        return qrCode;
    }


    public ResponseEntity<List<CombinedPara>> getCombinedList(String mainId) {
        try {
            List<CombinedPara> combinedDataList = new ArrayList<>();
            Optional<MainTopicEng> mainTopicEngs = mainTopicEngRepo.findBymEngUid(mainId);
            mainTopicEngs.ifPresent(mainTopicEng -> {
                CombinedPara combinedData1 = new CombinedPara();
                combinedData1.setTopic(mainTopicEng.getTopic());
                combinedData1.setDescription(mainTopicEng.getDescription());
                combinedData1.setReferenceUrl(mainTopicEng.getRef());
                combinedData1.setUId(mainTopicEng.getMEngUid());

                Optional<CommonQRParaId> commonIdQR = commonQRParaIdRepo.findByEngId(mainTopicEng.getMEngUid());
                if (commonIdQR.isPresent()) {
                    combinedData1.setCommonId(commonIdQR.get().getCommonId());
                    combinedData1.setQrCodeUrl(commonIdQR.get().getQrCodeUrl());
                    combinedData1.setEngId(commonIdQR.get().getEngId());
                    combinedData1.setMalId(commonIdQR.get().getMalId());
                    combinedData1.setQrCodeImage(commonIdQR.get().getQrCodeImage());
                }

                List<FirstTopicEng> subParaEngs = firstTopicEngRepo.findByMainUid(mainId);
                subParaEngs.sort(Comparator.comparing(FirstTopicEng::getId));
                List<CombinedSubPara> combinedDataSubList = new ArrayList<>();
                subParaEngs.forEach(subEng -> {
                    CombinedSubPara combinedDataSub = new CombinedSubPara();

                    // Set sub paragraphs details
                    combinedDataSub.setTopic(subEng.getTopic());
                    combinedDataSub.setDescription(subEng.getDescription());
                    combinedDataSub.setReferenceUrl(subEng.getRef());
                    combinedDataSub.setUId(subEng.getFsUid());
                    combinedDataSub.setMUid(mainId);

                    SubComId comId = subComIdRepo.findByfsEngId(subEng.getFsUid());
                    if (comId!=null){
                        combinedDataSub.setFsCommonId(comId.getFsCommonId());
                        combinedDataSub.setFsEngId(comId.getFsEngId());
                        combinedDataSub.setFsMalId(comId.getFsMalId());
                    }

                    // Fetching images for subpara
                    List<ImgDataFirst> subImgList = imgDataFirstRepo.findByEngId(subEng.getFsUid());
                    subImgList.sort(Comparator.comparing(ImgDataFirst::getId));
                    combinedDataSub.setImgList(subImgList);

                    // Fetching audio for subpara
                    List<AudioFirst> subAudio = audioFirstRepo.findBydtId(subEng.getFsUid());
                    subAudio.sort(Comparator.comparing(AudioFirst::getId));
                    combinedDataSub.setAudioList(subAudio);

                    // Fetching video for subpara
                    List<VideoFirst> subVideo = videoFirstRepo.findByfsEngId(subEng.getFsUid());
                    subVideo.sort(Comparator.comparing(VideoFirst::getId));
                    combinedDataSub.setVideoList(subVideo);



                    combinedDataSubList.add(combinedDataSub);
                });

                combinedData1.setCombinedDataSubList(combinedDataSubList);


                List<ImgDataMain> mainImage =imgDataMainRepo.findByEngId(mainTopicEng.getMEngUid());
                mainImage.sort(Comparator.comparing(ImgDataMain::getId));
                combinedData1.setImgList(mainImage);

                // Fetching audio for mainpara
                List<AudioMain> mainAudio = audioMainRepo.findBydtId(mainTopicEng.getMEngUid());
                mainAudio.sort(Comparator.comparing(AudioMain::getId));
                combinedData1.setAudioList(mainAudio);

                // Fetching video for mainpara
                List<VideoMain> mainVideo = videoMainRepo.findByengId(mainTopicEng.getMEngUid());
                mainVideo.sort(Comparator.comparing(VideoMain::getId));
                combinedData1.setVideoList(mainVideo);

                combinedDataList.add(combinedData1);
            });
            return new ResponseEntity<>(combinedDataList, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<CombinedPara>> getCombinedListMal(String mainId) {
        try {
            List<CombinedPara> combinedDataList = new ArrayList<>();
            Optional<MainTopicMal> mainTopicMals = mainTopicMalRepo.findBymMalUid(mainId);
            mainTopicMals.ifPresent(mainTopicMal -> {
                CombinedPara combinedData = new CombinedPara();
                combinedData.setTopic(mainTopicMal.getTopic());
                combinedData.setDescription(mainTopicMal.getDescription());
                combinedData.setReferenceUrl(mainTopicMal.getRef());
                combinedData.setUId(mainTopicMal.getMMalUid());

                Optional<CommonQRParaId> commonIdQR = commonQRParaIdRepo.findByMalId(mainTopicMal.getMMalUid());
                if (commonIdQR.isPresent()) {
                    combinedData.setCommonId(commonIdQR.get().getCommonId());
                    combinedData.setQrCodeUrl(commonIdQR.get().getQrCodeUrl());
                    combinedData.setEngId(commonIdQR.get().getEngId());
                    combinedData.setMalId(commonIdQR.get().getMalId());
                    combinedData.setQrCodeImage(commonIdQR.get().getQrCodeImage());
                }

                List<FirstTopicMal> subParaMals = firstTopicMalRepo.findByMainUid(mainId);
                subParaMals.sort(Comparator.comparing(FirstTopicMal::getId));
                List<CombinedSubPara> combinedDataSubList = new ArrayList<>();
                subParaMals.forEach(subMal -> {
                    CombinedSubPara combinedDataSub = new CombinedSubPara();

                    // Set first subheading details
                    combinedDataSub.setTopic(subMal.getTopic());
                    combinedDataSub.setDescription(subMal.getDescription());
                    combinedDataSub.setReferenceUrl(subMal.getRef());
                    combinedDataSub.setUId(subMal.getFsUid());
                    combinedDataSub.setMUid(mainId);

                    SubComId comId = subComIdRepo.findByfsMalId(subMal.getFsUid());
                    if (comId!=null){
                        combinedDataSub.setFsCommonId(comId.getFsCommonId());
                        combinedDataSub.setFsEngId(comId.getFsEngId());
                        combinedDataSub.setFsMalId(comId.getFsMalId());
                    }

                    // Fetching images for subpara
                    List<ImgDataFirst> subImgList = imgDataFirstRepo.findByMalId(subMal.getFsUid());
                    subImgList.sort(Comparator.comparing(ImgDataFirst::getId));
                    combinedDataSub.setImgList(subImgList);


                    // Fetching audio for subpara
                    List<AudioFirst> subAudio = audioFirstRepo.findBydtId(subMal.getFsUid());
                    subAudio.sort(Comparator.comparing(AudioFirst::getId));
                    combinedDataSub.setAudioList(subAudio);

                    // Fetching video for subpara
                    List<VideoFirst> subVideo = videoFirstRepo.findByfsMalId(subMal.getFsUid());
                    subVideo.sort(Comparator.comparing(VideoFirst::getId));
                    combinedDataSub.setVideoList(subVideo);



                    combinedDataSubList.add(combinedDataSub);
                });

                combinedData.setCombinedDataSubList(combinedDataSubList);

                List<ImgDataMain> mainImage =imgDataMainRepo.findByMalId(mainTopicMal.getMMalUid());
                mainImage.sort(Comparator.comparing(ImgDataMain::getId));
                combinedData.setImgList(mainImage);

                // Fetching audio for mainpara
                List<AudioMain> mainAudio = audioMainRepo.findBydtId(mainTopicMal.getMMalUid());
                mainAudio.sort(Comparator.comparing(AudioMain::getId));
                combinedData.setAudioList(mainAudio);

                // Fetching video for mainpara
                List<VideoMain> mainVideo = videoMainRepo.findBymalId(mainTopicMal.getMMalUid());
                mainVideo.sort(Comparator.comparing(VideoMain::getId));
                combinedData.setVideoList(mainVideo);


                combinedDataList.add(combinedData);

            });
            return new ResponseEntity<>(combinedDataList,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<CombinedPara>> getCombinedDataByCommonId(Integer dtId, String commonId) {
        try {
            Optional<CommonQRParaId> commonIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
            if (commonIdOptional.isPresent()) {
                CommonQRParaId commonIdQR = commonIdOptional.get();

                Optional<DataType> dataTypeOptional = dataTypeRepo.findById(dtId);
                if (dataTypeOptional.isPresent()) {
                    DataType dataType = dataTypeOptional.get();
                    String tData = dataType.getTalk();

                    if ("English".equalsIgnoreCase(tData)) {
                        return getCombinedList(commonIdQR.getEngId());
                    } else if ("Malayalam".equalsIgnoreCase(tData)) {
                        return getCombinedListMal(commonIdQR.getMalId());
                    } else {
                        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
