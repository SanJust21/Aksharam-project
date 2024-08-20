package com.example.MuseumTicketing.Guide.SecondSubHeading;

import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImg;
import com.example.MuseumTicketing.Guide.img.backgroundImg.BackgroundImgRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SecondSubService {
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;
    @Autowired
    private Mp3Data2Repo mp3Data2Repo;
    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private CommonIdSsRepo commonIdSsRepo;
    @Autowired
    private BackgroundImgRepo backgroundImgRepo;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    public ResponseEntity<?> addSubDataEnglish(String uId, MainDTO mainDTO) {
        try {
            Optional<SecondSubEnglish> existingTitle = secondSubEnglishRepo.findBytitleIgnoreCase(mainDTO.getTitle());
            if (existingTitle.isPresent()){
                SecondSubEnglish secondSubEnglish = existingTitle.get();
                if (secondSubEnglish.getFsUid().equals(uId)){
                    String titleData = mainDTO.getTitle();
                    return new ResponseEntity<>(titleData+" is already exist in the database",HttpStatus.CONFLICT);
                }
            }
            String randomId = alphaNumeric.generateRandomNumber();
            SecondSubEnglish secondSubEnglish = new SecondSubEnglish();
            secondSubEnglish.setSsUid(randomId);
            secondSubEnglish.setFsUid(uId);
            secondSubEnglish.setTitle(mainDTO.getTitle());

            secondSubEnglish.setDescription(mainDTO.getDescription());
            secondSubEnglish.setRef(mainDTO.getReferenceURL());
            secondSubEnglishRepo.save(secondSubEnglish);
            return new ResponseEntity<>(secondSubEnglish,HttpStatus.OK);
        }catch (Exception e){
           // e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> addSubDataMalayalam(String uId, MainDTO mainDTO) {
        try {

            Optional<SecondSubMalayalam> existingTitle = secondSubMalayalamRepo.findBytitleIgnoreCase(mainDTO.getTitle());
            if (existingTitle.isPresent()){
                SecondSubMalayalam secondSubMalayalam = existingTitle.get();
                if (secondSubMalayalam.getFsUid().equals(uId) && secondSubMalayalam.getTitle().equalsIgnoreCase(mainDTO.getTitle())){
                    String titleData = mainDTO.getTitle();
                    return new ResponseEntity<>(titleData+" is already exist in the database",HttpStatus.CONFLICT);
                }
            }
            String randomId = alphaNumeric.generateRandomNumber();
            SecondSubMalayalam secondSubMalayalam = new SecondSubMalayalam();
            secondSubMalayalam.setSsUid(randomId);
            secondSubMalayalam.setFsUid(uId);
            secondSubMalayalam.setTitle(mainDTO.getTitle());

            secondSubMalayalam.setDescription(mainDTO.getDescription());
            secondSubMalayalam.setRef(mainDTO.getReferenceURL());
            secondSubMalayalamRepo.save(secondSubMalayalam);
            return new ResponseEntity<>(secondSubMalayalam, HttpStatus.OK);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> generateCommonSs(String englishId, String malId) {
        CommonIdSs commonIdSs = new CommonIdSs();
        commonIdSs.setSsEngId(englishId);
        commonIdSs.setSsMalId(malId);
        commonIdSs.setSsCommonId(alphaNumeric.generateRandomNumber());
        commonIdSsRepo.save(commonIdSs);
        return new ResponseEntity<>(commonIdSs,HttpStatus.OK);
    }

    public ResponseEntity<List<CombinedDataSubSub>> getCombinedList(String ssCommonId) {
        try {
            CommonIdSs commonIdSs = commonIdSsRepo.findByssCommonId(ssCommonId);
            if (commonIdSs != null) {
                List<SecondSubEnglish> secondSubEnglishList = secondSubEnglishRepo.findBySsUid(commonIdSs.getSsEngId());
                secondSubEnglishList.sort(Comparator.comparing(SecondSubEnglish::getId));
                List<CombinedDataSubSub> combinedDataSubSubList = new ArrayList<>();

                secondSubEnglishList.forEach(secondSubEnglish -> {
                    CombinedDataSubSub combinedDataSubSub = new CombinedDataSubSub();
                    // Set details of SecondSubEnglish
                    combinedDataSubSub.setTitle(secondSubEnglish.getTitle());
                    combinedDataSubSub.setDescription(secondSubEnglish.getDescription());
                    combinedDataSubSub.setReferenceUrl(secondSubEnglish.getRef());
                    combinedDataSubSub.setuId(secondSubEnglish.getSsUid());
                    combinedDataSubSub.setmUid(secondSubEnglish.getFsUid());
                    String fsUid = secondSubEnglish.getFsUid();
                    CommonIdFs commonIdFs=fsCommonIdRepo.findByfsEngId(fsUid);
                    String fsCommonId = commonIdFs.getFsCommonId();
                    combinedDataSubSub.setFsCommonId(fsCommonId);

                    CommonIdSs commonIdSsEng = commonIdSsRepo.findBySsEngId(secondSubEnglish.getSsUid());
                    if (commonIdSsEng != null){
                        combinedDataSubSub.setSsCommonId(commonIdSsEng.getSsCommonId());
                        combinedDataSubSub.setSsEngId(commonIdSsEng.getSsEngId());
                        combinedDataSubSub.setSsMalId(commonIdSsEng.getSsMalId());
                    }

                    // Fetching images for SecondSubEnglish
                    List<ImgSubSecond> imgSubSecondList = imgSubSecondRepo.findByengId(secondSubEnglish.getSsUid());
                    imgSubSecondList.sort(Comparator.comparing(ImgSubSecond::getImgID));
                    combinedDataSubSub.setImgData2List(imgSubSecondList);

                    List<BackgroundImg>backgroundImgs=backgroundImgRepo.findByengId(secondSubEnglish.getSsUid());
                    combinedDataSubSub.setBackgroundImgList(backgroundImgs);

                    // Fetching audio for SecondSubEnglish
                    List<Mp3Data2> mp3Data2List = mp3Data2Repo.findBydtId(secondSubEnglish.getSsUid());
                    mp3Data2List.sort(Comparator.comparing(Mp3Data2::getId));
                    combinedDataSubSub.setMp3Data2List(mp3Data2List);
                    // Fetching video for SecondSubEnglish
                    List<Mp4Data2> mp4Data2List = mp4Data2Repo.findBydtId(commonIdSsEng.getSsCommonId());
                    mp4Data2List.sort(Comparator.comparing(Mp4Data2::getId));
                    combinedDataSubSub.setMp4Data2List(mp4Data2List);

                    combinedDataSubSubList.add(combinedDataSubSub);
                });

                return new ResponseEntity<>(combinedDataSubSubList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<CombinedDataSubSub>> getCombinedListMal(String ssCommonId) {
        try {

            CommonIdSs commonIdSs = commonIdSsRepo.findByssCommonId(ssCommonId);
            if (commonIdSs != null) {
                List<SecondSubMalayalam> secondSubMalayalamList = secondSubMalayalamRepo.findBySsUid(commonIdSs.getSsMalId());
                secondSubMalayalamList.sort(Comparator.comparing(SecondSubMalayalam::getId));
                List<CombinedDataSubSub> combinedDataSubSubList = new ArrayList<>();

                secondSubMalayalamList.forEach(secondSubMalayalam -> {
                    CombinedDataSubSub combinedDataSubSub = new CombinedDataSubSub();
                    // Set details of SecondSubEnglish
                    combinedDataSubSub.setTitle(secondSubMalayalam.getTitle());
                    combinedDataSubSub.setDescription(secondSubMalayalam.getDescription());
                    combinedDataSubSub.setReferenceUrl(secondSubMalayalam.getRef());
                    combinedDataSubSub.setuId(secondSubMalayalam.getSsUid());
                    combinedDataSubSub.setmUid(secondSubMalayalam.getFsUid());
                    String fsMalId = secondSubMalayalam.getFsUid();
                    CommonIdFs commonIdFs=fsCommonIdRepo.findByfsMalId(fsMalId);
                    combinedDataSubSub.setFsCommonId(commonIdFs.getFsCommonId());

                    CommonIdSs commonIdSsEng = commonIdSsRepo.findBySsMalId(secondSubMalayalam.getSsUid());
                    if (commonIdSsEng != null){
                        combinedDataSubSub.setSsCommonId(commonIdSsEng.getSsCommonId());
                        combinedDataSubSub.setSsEngId(commonIdSsEng.getSsEngId());
                        combinedDataSubSub.setSsMalId(commonIdSsEng.getSsMalId());
                    }

                    // Fetching images for SecondSubEnglish
                    List<ImgSubSecond> imgSubSecondList = imgSubSecondRepo.findBymalId(secondSubMalayalam.getSsUid());
                    imgSubSecondList.sort(Comparator.comparing(ImgSubSecond::getImgID));
                    combinedDataSubSub.setImgData2List(imgSubSecondList);

                    List<BackgroundImg>backgroundImgs=backgroundImgRepo.findBymalId(secondSubMalayalam.getSsUid());
                    combinedDataSubSub.setBackgroundImgList(backgroundImgs);

                    // Fetching audio for SecondSubEnglish
                    List<Mp3Data2> mp3Data2List = mp3Data2Repo.findBydtId(secondSubMalayalam.getSsUid());
                    mp3Data2List.sort(Comparator.comparing(Mp3Data2::getId));
                    combinedDataSubSub.setMp3Data2List(mp3Data2List);
                    // Fetching video for SecondSubEnglish
                    List<Mp4Data2> mp4Data2List = mp4Data2Repo.findBydtId(commonIdSsEng.getSsCommonId());
                    mp4Data2List.sort(Comparator.comparing(Mp4Data2::getId));
                    combinedDataSubSub.setMp4Data2List(mp4Data2List);

                    combinedDataSubSubList.add(combinedDataSubSub);
                });

                return new ResponseEntity<>(combinedDataSubSubList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
