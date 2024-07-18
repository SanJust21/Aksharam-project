package com.example.MuseumTicketing.appGuide.img;

import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirst;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMain;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.CommonQRParaIdRepo;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComId;
import com.example.MuseumTicketing.appGuide.mainPara.qrCode.first.SubComIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/jpgData/")
public class ImgMobDataController {
    @Autowired
    private ImgMobDataService imgMobDataService;

    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;

    @Autowired
    private SubComIdRepo subComIdRepo;


    @PostMapping(path = "/jpgUpload")
    public ResponseEntity<?>uploadJpgData(@RequestParam MultipartFile[] files,
                                          @RequestParam String commonId){
        try {
            if (commonId.isEmpty()||commonId.isBlank()||commonId==null||"undefined".equals(commonId)){
                return new ResponseEntity<>("CommonId is required",HttpStatus.BAD_REQUEST);
            }
            Optional<CommonQRParaId> commonIdQRCodeOptional = commonQRParaIdRepo.findByCommonId(commonId);
            Optional<SubComId> subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
            if (commonIdQRCodeOptional.isPresent()){
                CommonQRParaId commonIdQRCode = commonIdQRCodeOptional.get();
                if (commonIdQRCode.getCommonId().equals(commonId)){
                    String engId = commonIdQRCode.getEngId();
                    String malId = commonIdQRCode.getMalId();
                    List<ImgDataMain> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(imgMobDataService.uploadJPGMain(file,engId,malId,commonId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
            }else if (subComIdOptional.isPresent()){
                SubComId commonIdFs = subComIdOptional.get();
                if (commonIdFs.getFsCommonId().equals(commonId)){
                    String engId = commonIdFs.getFsEngId();
                    String malId = commonIdFs.getFsMalId();
                    List<ImgDataFirst> responses = new ArrayList<>();
                    for (MultipartFile file : files){
                        responses.add(imgMobDataService.uploadJPGFirst(file,engId,malId,commonId));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("CommonId is not generated. Please generate it.",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
