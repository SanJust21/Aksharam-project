package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.MuseumTicketing.Guide.Language.DataType;
import com.example.MuseumTicketing.Guide.Language.DataTypeRepo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.appGuide.Pdf.PdfData;
import com.example.MuseumTicketing.appGuide.Pdf.PdfRepo;
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
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEng;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicEngRepo;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMal;
import com.example.MuseumTicketing.appGuide.mainPara.main.MainTopicMalRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg.MobileReg;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.mobileReg.MobileRegRepo;
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
    @Autowired
    private MobileRegRepo mobileRegRepo;
    @Autowired
    private PdfRepo pdfRepo;

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

    public String getCommonId(String mMalUid, String mEngUid) {
        Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByMalIdAndEngId(mMalUid,mEngUid);
        if (commonQRParaIdOptional.isPresent()){
            CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
            return commonQRParaId.getCommonId();
        }return null;
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

                List<PdfData>pdfDataList=pdfRepo.findByuId(mainTopicEng.getMEngUid());
                if (!pdfDataList.isEmpty()){
                    pdfDataList.sort(Comparator.comparing(PdfData::getId));
                    combinedData1.setPdfDataList(pdfDataList);
                }

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

                List<PdfData>pdfDataList=pdfRepo.findByuId(mainTopicMal.getMMalUid());
                if (!pdfDataList.isEmpty()){
                    pdfDataList.sort(Comparator.comparing(PdfData::getId));
                    combinedData.setPdfDataList(pdfDataList);
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

    public ResponseEntity<?> userMobileReg(String phNumber, String email, String fullName) {
        if (phNumber.length()>=10 && email!=null && fullName!=null){
            MobileReg mobileReg = new MobileReg();
            mobileReg.setEmail(email);
            mobileReg.setFullName(fullName);
            mobileReg.setPhNumber(phNumber);
            mobileRegRepo.save(mobileReg);
            return new ResponseEntity<>(mobileReg,HttpStatus.OK);
        }
        return new ResponseEntity<>("Enter a valid data. Null filed is not allowed",HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<List<MobileReg>> getAllUsersData() {
        try {
            return new ResponseEntity<>(mobileRegRepo.findAll(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<CombinedGetAllPara>> getAllDetailsByDataType(Integer dtId) {
        Optional<DataType>dataTypeOptional=dataTypeRepo.findById(dtId);
        if (dataTypeOptional.isPresent()){
            DataType dataType=dataTypeOptional.get();
            String talk= dataType.getTalk();
            if ("Malayalam".equalsIgnoreCase(talk)){
                List<CombinedGetAllPara>combinedGetAllParaList=new ArrayList<>();
                List<MainTopicMal>mainTopicMalList=mainTopicMalRepo.findAll();
                mainTopicMalList.sort(Comparator.comparing(MainTopicMal::getMMalId));
                for (MainTopicMal mainTopicMal :mainTopicMalList){
                    CombinedGetAllPara combinedGetAllPara = new CombinedGetAllPara();
                    combinedGetAllPara.setTopic(mainTopicMal.getTopic());
                    combinedGetAllPara.setDescription(mainTopicMal.getDescription());
                    combinedGetAllPara.setRef(mainTopicMal.getRef());
                    combinedGetAllPara.setMainUniqueId(mainTopicMal.getMMalUid());
                    Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByMalId(mainTopicMal.getMMalUid());
                    if (commonQRParaIdOptional.isPresent()){
                        CommonQRParaId commonQRParaId =commonQRParaIdOptional.get();
                        combinedGetAllPara.setQrCodeImage(commonQRParaId.getQrCodeImage());
                        combinedGetAllPara.setQrCodeUrl(commonQRParaId.getQrCodeUrl());
                        combinedGetAllPara.setMainCommonId(commonQRParaId.getCommonId());
                        combinedGetAllPara.setEngUniqueId(commonQRParaId.getEngId());
                        combinedGetAllPara.setMalUniqueId(commonQRParaId.getMalId());
                    }

                    List<PdfData>pdfDataList=pdfRepo.findByuId(mainTopicMal.getMMalUid());
                    if (!pdfDataList.isEmpty()){
                        pdfDataList.sort(Comparator.comparing(PdfData::getId));
                        combinedGetAllPara.setPdfDataList(pdfDataList);
                    }
                    List<ImgDataMain>imgDataMainOptional=imgDataMainRepo.findByMalId(mainTopicMal.getMMalUid());
                    if (!imgDataMainOptional.isEmpty()){
                        imgDataMainOptional.sort(Comparator.comparing(ImgDataMain::getId));
                        combinedGetAllPara.setImgDataMainList(imgDataMainOptional);
                    }
                    List<AudioMain>audioMainList=audioMainRepo.findBydtId(mainTopicMal.getMMalUid());
                    if (!audioMainList.isEmpty()){
                        audioMainList.sort(Comparator.comparing(AudioMain::getId));
                        combinedGetAllPara.setAudioMainList(audioMainList);
                    }
                    List<VideoMain>videoMainList=videoMainRepo.findBymalId(mainTopicMal.getMMalUid());
                    if (!videoMainList.isEmpty()){
                        videoMainList.sort(Comparator.comparing(VideoMain::getId));
                        combinedGetAllPara.setVideoMainList(videoMainList);
                    }

                    List<FirstTopicMal>firstTopicMalList=firstTopicMalRepo.findByMainUid(mainTopicMal.getMMalUid());
                    List<CombinedAllSubPara>combinedAllSubParaList=new ArrayList<>();
                    if (!firstTopicMalList.isEmpty()){
                        firstTopicMalList.sort(Comparator.comparing(FirstTopicMal::getId));
                        for (FirstTopicMal firstTopicMal : firstTopicMalList){
                            CombinedAllSubPara combinedAllSubPara = new CombinedAllSubPara();
                            combinedAllSubPara.setTopic(firstTopicMal.getTopic());
                            combinedAllSubPara.setDescription(firstTopicMal.getDescription());
                            combinedAllSubPara.setReference(firstTopicMal.getRef());
                            combinedAllSubPara.setSubUniqueId(firstTopicMal.getFsUid());
                            combinedAllSubPara.setMainUniqueId(firstTopicMal.getMainUid());
                            SubComId subComIdOptional=subComIdRepo.findByfsMalId(firstTopicMal.getFsUid());
                            combinedAllSubPara.setSubCommonId(subComIdOptional.getFsCommonId());
                            combinedAllSubPara.setEngUniqueId(subComIdOptional.getFsEngId());
                            combinedAllSubPara.setMalUniqueId(subComIdOptional.getFsMalId());
                            List<ImgDataFirst>imgDataFirstList=imgDataFirstRepo.findByCommonId(subComIdOptional.getFsCommonId());
                            if (!imgDataFirstList.isEmpty()){
                                imgDataFirstList.sort(Comparator.comparing(ImgDataFirst::getId));
                                combinedAllSubPara.setImgDataFirstList(imgDataFirstList);
                            }
                            List<AudioFirst>audioFirstList=audioFirstRepo.findBydtId(firstTopicMal.getFsUid());
                            if (!audioFirstList.isEmpty()){
                                audioFirstList.sort(Comparator.comparing(AudioFirst::getId));
                                combinedAllSubPara.setAudioFirstList(audioFirstList);
                            }
                            List<VideoFirst>videoFirstList=videoFirstRepo.findByfsMalId(firstTopicMal.getFsUid());
                            if (!videoFirstList.isEmpty()){
                                videoFirstList.sort(Comparator.comparing(VideoFirst::getId));
                                combinedAllSubPara.setVideoFirstList(videoFirstList);
                            }
                            combinedAllSubParaList.add(combinedAllSubPara);
                        }
                    }
                    combinedGetAllPara.setCombinedAllSubParaList(combinedAllSubParaList);
                    combinedGetAllParaList.add(combinedGetAllPara);
                }
                return new ResponseEntity<>(combinedGetAllParaList,HttpStatus.OK);
            }else if ("English".equalsIgnoreCase(talk)){
                List<CombinedGetAllPara>combinedGetAllParaList=new ArrayList<>();
                List<MainTopicEng>mainTopicEngList=mainTopicEngRepo.findAll();
                mainTopicEngList.sort(Comparator.comparing(MainTopicEng::getMEngId));
                for (MainTopicEng mainTopicEng : mainTopicEngList){
                    CombinedGetAllPara combinedGetAllPara = new CombinedGetAllPara();
                    combinedGetAllPara.setTopic(mainTopicEng.getTopic());
                    combinedGetAllPara.setDescription(mainTopicEng.getDescription());
                    combinedGetAllPara.setRef(mainTopicEng.getRef());
                    combinedGetAllPara.setMainUniqueId(mainTopicEng.getMEngUid());
                    Optional<CommonQRParaId>commonQRParaIdOptional=commonQRParaIdRepo.findByEngId(mainTopicEng.getMEngUid());
                    if (commonQRParaIdOptional.isPresent()){
                        CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                        combinedGetAllPara.setMainCommonId(commonQRParaId.getCommonId());
                        combinedGetAllPara.setEngUniqueId(commonQRParaId.getEngId());
                        combinedGetAllPara.setMalUniqueId(commonQRParaId.getMalId());
                        combinedGetAllPara.setQrCodeUrl(commonQRParaId.getQrCodeUrl());
                        combinedGetAllPara.setQrCodeImage(commonQRParaId.getQrCodeImage());
                    }

                    List<PdfData>pdfDataList=pdfRepo.findByuId(mainTopicEng.getMEngUid());
                    if (!pdfDataList.isEmpty()){
                        pdfDataList.sort(Comparator.comparing(PdfData::getId));
                        combinedGetAllPara.setPdfDataList(pdfDataList);
                    }
                    List<ImgDataMain>imgDataMainOptional=imgDataMainRepo.findByEngId(mainTopicEng.getMEngUid());
                    if (!imgDataMainOptional.isEmpty()){
                        imgDataMainOptional.sort(Comparator.comparing(ImgDataMain::getId));
                        combinedGetAllPara.setImgDataMainList(imgDataMainOptional);
                    }
                    List<AudioMain>audioMainList=audioMainRepo.findBydtId(mainTopicEng.getMEngUid());
                    if (!audioMainList.isEmpty()){
                        audioMainList.sort(Comparator.comparing(AudioMain::getId));
                        combinedGetAllPara.setAudioMainList(audioMainList);
                    }
                    List<VideoMain>videoMainList=videoMainRepo.findByengId(mainTopicEng.getMEngUid());
                    if (!videoMainList.isEmpty()){
                        videoMainList.sort(Comparator.comparing(VideoMain::getId));
                        combinedGetAllPara.setVideoMainList(videoMainList);
                    }
                    List<FirstTopicEng>firstTopicEngList=firstTopicEngRepo.findByMainUid(mainTopicEng.getMEngUid());
                    List<CombinedAllSubPara>combinedAllSubParaList = new ArrayList<>();
                    if (!firstTopicEngList.isEmpty()){
                        firstTopicEngList.sort(Comparator.comparing(FirstTopicEng::getId));
                        for (FirstTopicEng firstTopicEng : firstTopicEngList){
                            CombinedAllSubPara combinedAllSubPara = new CombinedAllSubPara();
                            combinedAllSubPara.setTopic(firstTopicEng.getTopic());
                            combinedAllSubPara.setDescription(firstTopicEng.getDescription());
                            combinedAllSubPara.setReference(firstTopicEng.getRef());
                            combinedAllSubPara.setSubUniqueId(firstTopicEng.getFsUid());
                            combinedAllSubPara.setMainUniqueId(firstTopicEng.getMainUid());
                            SubComId subComIdOptional=subComIdRepo.findByfsEngId(firstTopicEng.getFsUid());
                            combinedAllSubPara.setSubCommonId(subComIdOptional.getFsCommonId());
                            combinedAllSubPara.setEngUniqueId(subComIdOptional.getFsEngId());
                            combinedAllSubPara.setMalUniqueId(subComIdOptional.getFsMalId());
                            List<ImgDataFirst>imgDataFirstList=imgDataFirstRepo.findByEngId(firstTopicEng.getFsUid());
                            if (!imgDataFirstList.isEmpty()){
                                imgDataFirstList.sort(Comparator.comparing(ImgDataFirst::getId));
                                combinedAllSubPara.setImgDataFirstList(imgDataFirstList);
                            }
                            List<AudioFirst>audioFirstList=audioFirstRepo.findBydtId(firstTopicEng.getFsUid());
                            if (!audioFirstList.isEmpty()){
                                audioFirstList.sort(Comparator.comparing(AudioFirst::getId));
                                combinedAllSubPara.setAudioFirstList(audioFirstList);
                            }
                            List<VideoFirst>videoFirstList=videoFirstRepo.findByfsEngId(firstTopicEng.getFsUid());
                            if (!videoFirstList.isEmpty()){
                                videoFirstList.sort(Comparator.comparing(VideoFirst::getId));
                                combinedAllSubPara.setVideoFirstList(videoFirstList);
                            }
                            combinedAllSubParaList.add(combinedAllSubPara);
                        }
                    }
                    combinedGetAllPara.setCombinedAllSubParaList(combinedAllSubParaList);
                    combinedGetAllParaList.add(combinedGetAllPara);
                } return new ResponseEntity<>(combinedGetAllParaList,HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
            }
        }return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


}
