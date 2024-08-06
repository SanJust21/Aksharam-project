package com.example.MuseumTicketing.Guide.UPDATE.SecondSub;

import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglish;
import com.example.MuseumTicketing.Guide.SecondSubHeading.english.SecondSubEnglishRepo;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalam;
import com.example.MuseumTicketing.Guide.SecondSubHeading.malayalam.SecondSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.img.secondSubHeading.ImgSubSecondRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mpFileData.mp3.secondSub.Mp3Data2Repo;
import com.example.MuseumTicketing.Guide.mpFileData.mp4.secondSub.Mp4Data2Repo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecondSubUpdateService {
    @Autowired
    private SecondSubMalayalamRepo secondSubMalayalamRepo;
    @Autowired
    private SecondSubEnglishRepo secondSubEnglishRepo;
    @Autowired
    private ImgSubSecondRepo imgSubSecondRepo;
    @Autowired
    private Mp3Data2Repo mp3Data2Repo;
    @Autowired
    private Mp4Data2Repo mp4Data2Repo;
    @Autowired
    private ErrorService errorService;


    public ResponseEntity<?> updateSecondSubDataMalayalam(String uId, MainDTO mainDTO) {
        try {
            Optional<SecondSubMalayalam> secondSubMalayalam = secondSubMalayalamRepo.findByssUid(uId);
            if (secondSubMalayalam.isPresent()){
                SecondSubMalayalam secondSubMalayalam1 = secondSubMalayalam.get();
                if (secondSubMalayalam1.getSsUid().equals(uId)){
                    secondSubMalayalam1.setTitle(mainDTO.getTitle());
                    secondSubMalayalam1.setDescription(mainDTO.getDescription());
                    secondSubMalayalam1.setRef(mainDTO.getReferenceURL());
                    secondSubMalayalamRepo.save(secondSubMalayalam1);
                    //return new ResponseEntity<>(secondSubMalayalam1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<SecondSubMalayalam> orderedList = secondSubMalayalamRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateSecondSubDataEnglish(String uId, MainDTO mainDTO) {
        try {
            Optional<SecondSubEnglish> secondSubEnglish = secondSubEnglishRepo.findByssUid(uId);
            if (secondSubEnglish.isPresent()){
                SecondSubEnglish secondSubEnglish1 = secondSubEnglish.get();
                if (secondSubEnglish1.getSsUid().equals(uId)){
                    secondSubEnglish1.setTitle(mainDTO.getTitle());
                    secondSubEnglish1.setDescription(mainDTO.getDescription());
                    secondSubEnglish1.setRef(mainDTO.getReferenceURL());
                    secondSubEnglishRepo.save(secondSubEnglish1);
                    //return new ResponseEntity<>(secondSubEnglish1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<SecondSubEnglish> orderedList = secondSubEnglishRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
