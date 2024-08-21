package com.example.MuseumTicketing.appGuide.img;

import com.example.MuseumTicketing.Guide.QR.CommonIdQRCode;
import com.example.MuseumTicketing.Guide.img.mainHeading.ImgData;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.appGuide.img.first.ImgDataFirstRepo;
import com.example.MuseumTicketing.appGuide.img.main.ImgDataMainRepo;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/jpgData/")
@CrossOrigin
public class ImgMobDataController {
    @Autowired
    private ImgMobDataService imgMobDataService;

    @Autowired
    private CommonQRParaIdRepo commonQRParaIdRepo;

    @Autowired
    private SubComIdRepo subComIdRepo;
    @Autowired
    private ImgDataMainRepo imgDataMainRepo;
    @Autowired
    private ImgDataFirstRepo imgDataFirstRepo;
    @Autowired
    private ErrorService errorService;


    @PostMapping(path = "/jpgUpload")
    public ResponseEntity<?>uploadJpgData(@RequestParam MultipartFile[] files,@RequestParam String commonId,
                                          @RequestParam("imgName")String[] imgName,
                                          @RequestParam("imgUrls")String[] imgUrl ){
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
                    for (int i =0;i< files.length;i++){
                        String imageName =imgName[i];
                        String imageUrl = imgUrl[i];
                        MultipartFile file = files[i];
                        responses.add(imgMobDataService.uploadJPGMain(file,engId,malId,commonId,imageName,imageUrl));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
            }else if (subComIdOptional.isPresent()){
                SubComId commonIdFs = subComIdOptional.get();
                if (commonIdFs.getFsCommonId().equals(commonId)){
                    String engId = commonIdFs.getFsEngId();
                    String malId = commonIdFs.getFsMalId();
                    List<ImgDataFirst> responses = new ArrayList<>();
                    for (int i =0;i< files.length;i++){
                        String imageName = imgName[i];
                        String imageUrl = imgUrl[i];
                        MultipartFile file = files[i];
                        responses.add(imgMobDataService.uploadJPGFirst(file,engId,malId,commonId,imageName,imageUrl));
                    }
                    return new ResponseEntity<>(responses,HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("CommonId is not generated. Please generate it.",HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateJPG/{commonId}")
    public ResponseEntity<?>updateImages(@RequestParam(value = "files") MultipartFile[] files,
                                        @RequestParam List<Integer> imgIds,
                                        @PathVariable String commonId,
                                         @RequestParam("imgName")String[] imgName,
                                         @RequestParam("imgUrls")String[] imgUrl){
        try {
            if (commonId == null || imgIds.isEmpty() || files.length != imgIds.size()||"undefined".equalsIgnoreCase(commonId)) {
                return new ResponseEntity<>("Common ID, image IDs, and files are required, and the number of files must match the number of image IDs", HttpStatus.BAD_REQUEST);
            }
            List<ImgDataMain> existingImgDataMainList = imgDataMainRepo.findByCommonId(commonId);
            List<ImgDataFirst>existingImgDataFirstList = imgDataFirstRepo.findByCommonId(commonId);

            if (!existingImgDataMainList.isEmpty()) {
                List<ImgDataMain> responses = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    String imageName = imgName[i];
                    String imageUrl = imgUrl[i];
                    MultipartFile file = files[i];
                    responses.add(imgMobDataService.updateMainJPG(file, imgIds.get(i), commonId,imageName,imageUrl));
                }
                return new ResponseEntity<>(responses, HttpStatus.OK);
            } else if (!existingImgDataFirstList.isEmpty()) {
                List<ImgDataFirst> responses = new ArrayList<>();
                for (int i =0; i< files.length;i++){
                    String imageName = imgName[i];
                    String imageUrl = imgUrl[i];
                    MultipartFile file = files[i];
                    responses.add(imgMobDataService.updateFirstJPG(file,imgIds.get(i),commonId,imageName,imageUrl));
                }
                return new ResponseEntity<>(responses,HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No image data found for the provided Common ID", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/editNameAndUrl/{commonId}/{id}")
    public ResponseEntity<?>updateNameAndUrl(@PathVariable String commonId,@RequestParam String imgName,
                                             @RequestParam String imgUrl,@PathVariable Integer id){
        try {
            Optional<ImgDataMain>imgDataMainOptional=imgDataMainRepo.findByCommonIdAndId(commonId,id);
            Optional<ImgDataFirst>imgDataFirstOptional=imgDataFirstRepo.findByCommonIdAndId(commonId,id);
            if (imgDataMainOptional.isPresent()){
                return imgMobDataService.updateNameAndUrl(commonId,id,imgName,imgUrl);
            } else if (imgDataFirstOptional.isPresent()) {
                return imgMobDataService.updateNameAndUrl(commonId,id,imgName,imgUrl);
            }return new ResponseEntity<>("CommonId : "+commonId,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return errorService.handlerException(e);
        }

    }



    @DeleteMapping(path = "/deleteJPG/{commonId}")
    public ResponseEntity<?>deleteImages(@PathVariable String commonId,
                                         @RequestParam(required = false) List<Integer> imgIds){
        try {
            if (commonId == null || "undefined".equalsIgnoreCase(commonId)|| commonId.isEmpty()) {
                return new ResponseEntity<>("CommonId is required",HttpStatus.BAD_REQUEST);
            }else {
                Optional<CommonQRParaId> commonQRParaIdOptional = commonQRParaIdRepo.findByCommonId(commonId);
                Optional<SubComId>subComIdOptional = subComIdRepo.findByFsCommonId(commonId);
                if (commonQRParaIdOptional.isPresent()){
                    CommonQRParaId commonQRParaId = commonQRParaIdOptional.get();
                    if (commonQRParaId.getCommonId().equals(commonId)){
                        if (imgIds == null || imgIds.isEmpty()) {
                            // Delete all images associated with the commonId
                            int count = imgMobDataService.deleteImagesByCommonId(commonId);
                            if (count>0){
                                return new ResponseEntity<>("Images are deleted successfully",HttpStatus.OK);
                            }else {
                                return new ResponseEntity<>("Images are not deleted ",HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            // Delete specific images associated with the commonId
                            int count = imgMobDataService.deleteImagesByCommonIdAndIds(commonId, imgIds);
                            if (count>0){
                                return new ResponseEntity<>("Images are deleted successfully",HttpStatus.OK);
                            }else {
                                return new ResponseEntity<>("Images are not deleted ",HttpStatus.BAD_REQUEST);
                            }
                        }
                    } else if (subComIdOptional.isPresent()) {
                        SubComId subComId = subComIdOptional.get();
                        if (subComId.getFsCommonId().equals(commonId)){
                            if (imgIds == null || imgIds.isEmpty()) {
                                // Delete all images associated with the commonId
                                int count = imgMobDataService.deleteImagesByCommonId(commonId);
                                if (count>0){
                                    return new ResponseEntity<>("Images are deleted successfully",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Images are not deleted ",HttpStatus.BAD_REQUEST);
                                }
                            } else {
                                // Delete specific images associated with the commonId
                                int count = imgMobDataService.deleteImagesByCommonIdAndIds(commonId, imgIds);
                                if (count>0){
                                    return new ResponseEntity<>("Images are deleted successfully",HttpStatus.OK);
                                }else {
                                    return new ResponseEntity<>("Images are not deleted ",HttpStatus.BAD_REQUEST);
                                }
                            }
                        }
                    } else {
                        return new ResponseEntity<>("CommonId is not present in database",HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
