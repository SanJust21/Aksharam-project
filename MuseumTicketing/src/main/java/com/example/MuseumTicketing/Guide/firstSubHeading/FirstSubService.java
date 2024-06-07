package com.example.MuseumTicketing.Guide.firstSubHeading;

import com.example.MuseumTicketing.Guide.SecondSubHeading.CombinedDataSubSub;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSs;
import com.example.MuseumTicketing.Guide.SecondSubHeading.commonId.CommonIdSsRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.CommonIdFs;
import com.example.MuseumTicketing.Guide.firstSubHeading.FScommonId.FsCommonIdRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirst;
import com.example.MuseumTicketing.Guide.img.firstSubHeading.ImgSubFirstRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecond;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.firstSub.Mp3Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.firstSub.Mp4Data1Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FirstSubService {

    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private ImgSubFirstRepo imgSubFirstRepo;
    @Autowired
    private Mp3Data1Repo mp3Data1Repo;
    @Autowired
    private Mp4Data1Repo mp4Data1Repo;
    @Autowired
    private Mp3Data2Repo mp3Data2Repo;

    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private FsCommonIdRepo fsCommonIdRepo;

    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;

    @Autowired
    private CommonIdSsRepo commonIdSsRepo;

    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;

    public ResponseEntity<?> addSubDataMalayalam(String uId, MainDTO mainDTO) {
        try {

            Optional<FirstSubMalayalam> existingTitle = firstSubMalayalamRepo.findBytitle(mainDTO.getTitle());
            if (existingTitle.isPresent()){
                String titleData = mainDTO.getTitle();
                return new ResponseEntity<>(titleData+" is already exist in the database",HttpStatus.CONFLICT);
            }

            String randomId = alphaNumeric.generateRandomNumber();
            FirstSubMalayalam firstSubMalayalam = new FirstSubMalayalam();
            firstSubMalayalam.setFsUid(randomId);
            firstSubMalayalam.setMainUid(uId);
            firstSubMalayalam.setTitle(mainDTO.getTitle());
            firstSubMalayalam.setDescription(mainDTO.getDescription());
            firstSubMalayalam.setRef(mainDTO.getReferenceURL());
            firstSubMalayalamRepo.save(firstSubMalayalam);
            return new ResponseEntity<>(firstSubMalayalam,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> addSubDataEnglish(String uId, MainDTO mainDTO) {
        try {

            Optional<FirstSubEnglish> existingTitle = firstSubEnglishRepo.findBytitle(mainDTO.getTitle());
            if (existingTitle.isPresent()){
                String titleData = mainDTO.getTitle();
                return new ResponseEntity<>(titleData+" is already exist in the database",HttpStatus.CONFLICT);
            }

            String randomId = alphaNumeric.generateRandomNumber();
            FirstSubEnglish firstSubEnglish = new FirstSubEnglish();
            firstSubEnglish.setFsUid(randomId);
            firstSubEnglish.setMainUid(uId);
            firstSubEnglish.setTitle(mainDTO.getTitle());
            firstSubEnglish.setDescription(mainDTO.getDescription());
            firstSubEnglish.setRef(mainDTO.getReferenceURL());
            firstSubEnglishRepo.save(firstSubEnglish);
            return new ResponseEntity<>(firstSubEnglish,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<CombinedDataSub>> getCombinedListEng() {
        try {
            List<CombinedDataSub> combinedData = new ArrayList<>();
            List<FirstSubEnglish> firstSubEnglishes = firstSubEnglishRepo.findAll();
            firstSubEnglishes.sort(Comparator.comparing(FirstSubEnglish::getId));
            for (FirstSubEnglish firstSubEnglish : firstSubEnglishes){
                CombinedDataSub combinedData1 = new CombinedDataSub();
                combinedData1.setTitle(firstSubEnglish.getTitle());
                combinedData1.setDescription(firstSubEnglish.getDescription());
                combinedData1.setReferenceUrl(firstSubEnglish.getRef());
                combinedData1.setuId(firstSubEnglish.getFsUid());
                combinedData1.setmUid(firstSubEnglish.getMainUid());

                List<ImgSubFirst> imgData =imgSubFirstRepo.findByengId(firstSubEnglish.getFsUid());
                imgData.sort(Comparator.comparing(ImgSubFirst::getImgID));
                combinedData1.setImgDataList(imgData);

                List<Mp3Data1> mp3Data = mp3Data1Repo.findBydtId(firstSubEnglish.getFsUid());
                mp3Data.sort(Comparator.comparing(Mp3Data1::getId));
                combinedData1.setMp3DataList(mp3Data);

                List<Mp4Data1> mp4Data = mp4Data1Repo.findBydtId(firstSubEnglish.getFsUid());
                mp4Data.sort(Comparator.comparing(Mp4Data1::getId));
                combinedData1.setMp4DataList(mp4Data);

                combinedData.add(combinedData1);
            }
            return new ResponseEntity<>(combinedData,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<CombinedDataSub>> getCombinedListMal() {
        try {
            List<CombinedDataSub> combinedData = new ArrayList<>();
            List<FirstSubMalayalam> firstSubMalayalams = firstSubMalayalamRepo.findAll();
            firstSubMalayalams.sort(Comparator.comparing(FirstSubMalayalam::getId));
            for (FirstSubMalayalam firstSubMalayalam :firstSubMalayalams){
                CombinedDataSub combinedData1 = new CombinedDataSub();
                combinedData1.setTitle(firstSubMalayalam.getTitle());
                combinedData1.setDescription(firstSubMalayalam.getDescription());
                combinedData1.setReferenceUrl(firstSubMalayalam.getRef());
                combinedData1.setmUid(firstSubMalayalam.getMainUid());
                combinedData1.setuId(firstSubMalayalam.getFsUid());

                List<ImgSubFirst> imgData =imgSubFirstRepo.findBymalId(firstSubMalayalam.getFsUid());
                imgData.sort(Comparator.comparing(ImgSubFirst::getImgID));
                combinedData1.setImgDataList(imgData);

                List<Mp3Data1> mp3Data = mp3Data1Repo.findBydtId(firstSubMalayalam.getFsUid());
                mp3Data.sort(Comparator.comparing(Mp3Data1::getId));
                combinedData1.setMp3DataList(mp3Data);

                List<Mp4Data1> mp4Data = mp4Data1Repo.findBydtId(firstSubMalayalam.getFsUid());
                mp4Data.sort(Comparator.comparing(Mp4Data1::getId));
                combinedData1.setMp4DataList(mp4Data);

                combinedData.add(combinedData1);
            }
            return new ResponseEntity<>(combinedData,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> generateCommonIdFs(String engId, String malId) {
        try {
            CommonIdFs commonIdFs = new CommonIdFs();
            commonIdFs.setFsMalId(malId);
            commonIdFs.setFsEngId(engId);
            String genId = alphaNumeric.generateRandomNumber();
            commonIdFs.setFsCommonId(genId);
            fsCommonIdRepo.save(commonIdFs);
            return new ResponseEntity<>(commonIdFs,HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> getFirstSubCombinedListEng(String id) {
        try {
            List<CombinedDataSub> combinedDataSubList = new ArrayList<>();
            CommonIdFs commonIdFs = fsCommonIdRepo.findByfsCommonId(id);
            String fsMalId = commonIdFs.getFsMalId();
            if (commonIdFs!=null){
                Optional<FirstSubEnglish> firstSubEnglish = firstSubEnglishRepo.findByfsUid(commonIdFs.getFsEngId());
                if (firstSubEnglish.isPresent()){
                    FirstSubEnglish firstSubEnglish1 = firstSubEnglish.get();
                    CombinedDataSub combinedDataSub = new CombinedDataSub();
                    combinedDataSub.setFsEngId(firstSubEnglish1.getFsUid());
                    combinedDataSub.setTitle(firstSubEnglish1.getTitle());
                    combinedDataSub.setDescription(firstSubEnglish1.getDescription());
                    combinedDataSub.setReferenceUrl(firstSubEnglish1.getRef());
                    combinedDataSub.setFsMalId(fsMalId);
                    combinedDataSub.setFsCommonId(id);

                    List<ImgSubFirst> imgSubFirsts = imgSubFirstRepo.findByEngId(firstSubEnglish1.getFsUid());
                    imgSubFirsts.sort(Comparator.comparing(ImgSubFirst::getImgID));
                    combinedDataSub.setImgDataList(imgSubFirsts);

                    List<Mp3Data1> mp3Data1List = mp3Data1Repo.findBydtId(firstSubEnglish1.getFsUid());
                    mp3Data1List.sort(Comparator.comparing(Mp3Data1::getId));
                    combinedDataSub.setMp3DataList(mp3Data1List);

                    List<Mp4Data1> mp4Data1List = mp4Data1Repo.findBydtId(firstSubEnglish1.getFsUid());
                    mp4Data1List.sort(Comparator.comparing(Mp4Data1::getId));
                    combinedDataSub.setMp4DataList(mp4Data1List);

                    List<SecondSubEnglish> secondSubEnglishList = secondSubEnglishRepo.findByfsUid(firstSubEnglish1.getFsUid());
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

                        Optional<CommonIdSs> commonIdSs = commonIdSsRepo.findByssEngId(secondSubEnglish.getSsUid());
                        if (commonIdSs.isPresent()){
                            CommonIdSs commonIdSs1 = commonIdSs.get();
                            combinedDataSubSub.setSsCommonId(commonIdSs1.getSsCommonId());
                            combinedDataSubSub.setSsEngId(commonIdSs1.getSsEngId());
                            combinedDataSubSub.setSsMalId(commonIdSs1.getSsMalId());
                        }
                        // Fetching images for SecondSubEnglish
                        List<ImgSubSecond> imgSubSecondList = imgSubSecondRepo.findByengId(secondSubEnglish.getSsUid());
                        imgSubSecondList.sort(Comparator.comparing(ImgSubSecond::getImgID));
                        combinedDataSubSub.setImgData2List(imgSubSecondList);
                        // Fetching audio for SecondSubEnglish
                        List<Mp3Data2> mp3Data2List = mp3Data2Repo.findBydtId(secondSubEnglish.getSsUid());
                        mp3Data2List.sort(Comparator.comparing(Mp3Data2::getId));
                        combinedDataSubSub.setMp3Data2List(mp3Data2List);
                        // Fetching video for SecondSubEnglish
                        List<Mp4Data2> mp4Data2List = mp4Data2Repo.findBydtId(secondSubEnglish.getSsUid());
                        mp4Data2List.sort(Comparator.comparing(Mp4Data2::getId));
                        combinedDataSubSub.setMp4Data2List(mp4Data2List);

                        combinedDataSubSubList.add(combinedDataSubSub);

                    });
                    combinedDataSub.setCombinedDataSubSubList(combinedDataSubSubList);

                    combinedDataSubList.add(combinedDataSub);
                }
                return new ResponseEntity<>(combinedDataSubList,HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> getFirstSubCombinedListMal(String id) {
        try {
            List<CombinedDataSub> combinedDataSubList = new ArrayList<>();
            CommonIdFs commonIdFs = fsCommonIdRepo.findByfsCommonId(id);
            String fsEngId = commonIdFs.getFsEngId();
            if (commonIdFs!=null){
                Optional<FirstSubMalayalam> firstSubMalayalam = firstSubMalayalamRepo.findByfsUid(commonIdFs.getFsMalId());
                if (firstSubMalayalam.isPresent()){
                    FirstSubMalayalam firstSubMalayalam1 =firstSubMalayalam.get();
                    CombinedDataSub combinedDataSub = new CombinedDataSub();
                    combinedDataSub.setFsMalId(firstSubMalayalam1.getFsUid());
                    combinedDataSub.setTitle(firstSubMalayalam1.getTitle());
                    combinedDataSub.setDescription(firstSubMalayalam1.getDescription());
                    combinedDataSub.setReferenceUrl(firstSubMalayalam1.getRef());
                    combinedDataSub.setFsEngId(fsEngId);
                    combinedDataSub.setFsCommonId(commonIdFs.getFsCommonId());
                    combinedDataSub.setFsMalId(commonIdFs.getFsMalId());

                    List<ImgSubFirst> imgSubFirsts = imgSubFirstRepo.findByEngId(firstSubMalayalam1.getFsUid());
                    imgSubFirsts.sort(Comparator.comparing(ImgSubFirst::getImgID));
                    combinedDataSub.setImgDataList(imgSubFirsts);

                    List<Mp3Data1> mp3Data1List = mp3Data1Repo.findBydtId(firstSubMalayalam1.getFsUid());
                    mp3Data1List.sort(Comparator.comparing(Mp3Data1::getId));
                    combinedDataSub.setMp3DataList(mp3Data1List);

                    List<Mp4Data1> mp4Data1List = mp4Data1Repo.findBydtId(firstSubMalayalam1.getFsUid());
                    mp4Data1List.sort(Comparator.comparing(Mp4Data1::getId));
                    combinedDataSub.setMp4DataList(mp4Data1List);

                    List<SecondSubMalayalam> secondSubMalayalamList = secondSubMalayalamRepo.findByfsUid(firstSubMalayalam1.getFsUid());
                    secondSubMalayalamList.sort(Comparator.comparing(SecondSubMalayalam::getId));
                    List<CombinedDataSubSub> combinedDataSubSubList = new ArrayList<>();
                    secondSubMalayalamList.forEach(secondSubMalayalam -> {
                        CombinedDataSubSub combinedDataSubSub = new CombinedDataSubSub();
                        // Set details of SecondSubMalayalam
                        combinedDataSubSub.setTitle(secondSubMalayalam.getTitle());
                        combinedDataSubSub.setDescription(secondSubMalayalam.getDescription());
                        combinedDataSubSub.setReferenceUrl(secondSubMalayalam.getRef());
                        combinedDataSubSub.setuId(secondSubMalayalam.getSsUid());
                        combinedDataSubSub.setmUid(secondSubMalayalam.getFsUid());

                        Optional<CommonIdSs> commonIdSs = commonIdSsRepo.findByssMalId(secondSubMalayalam.getSsUid());
                        if (commonIdSs.isPresent()){
                            CommonIdSs commonIdSs1 = commonIdSs.get();
                            combinedDataSubSub.setSsCommonId(commonIdSs1.getSsCommonId());
                            combinedDataSubSub.setSsEngId(commonIdSs1.getSsEngId());
                            combinedDataSubSub.setSsMalId(commonIdSs1.getSsMalId());
                        }
                        // Fetching images for SecondSubMalayalam
                        List<ImgSubSecond> imgSubSecondList = imgSubSecondRepo.findBymalId(secondSubMalayalam.getSsUid());
                        imgSubSecondList.sort(Comparator.comparing(ImgSubSecond::getImgID));
                        combinedDataSubSub.setImgData2List(imgSubSecondList);
                        // Fetching audio for SecondSubMalayalam
                        List<Mp3Data2> mp3Data2List = mp3Data2Repo.findBydtId(secondSubMalayalam.getSsUid());
                        mp3Data2List.sort(Comparator.comparing(Mp3Data2::getId));
                        combinedDataSubSub.setMp3Data2List(mp3Data2List);
                        // Fetching video for SecondSubMalayalam
                        List<Mp4Data2> mp4Data2List = mp4Data2Repo.findBydtId(secondSubMalayalam.getSsUid());
                        mp4Data2List.sort(Comparator.comparing(Mp4Data2::getId));
                        combinedDataSubSub.setMp4Data2List(mp4Data2List);
                        combinedDataSubSubList.add(combinedDataSubSub);
                    });
                    combinedDataSubList.add(combinedDataSub);
                }
                return new ResponseEntity<>(combinedDataSubList,HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
