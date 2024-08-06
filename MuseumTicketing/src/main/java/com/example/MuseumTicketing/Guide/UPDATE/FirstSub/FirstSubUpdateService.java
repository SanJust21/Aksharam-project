package com.example.MuseumTicketing.Guide.UPDATE.FirstSub;

import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglish;
import com.example.MuseumTicketing.Guide.firstSubHeading.english.FirstSubEnglishRepo;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalam;
import com.example.MuseumTicketing.Guide.firstSubHeading.malayalam.FirstSubMalayalamRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirstSubUpdateService {
    @Autowired
    private FirstSubMalayalamRepo firstSubMalayalamRepo;
    @Autowired
    private FirstSubEnglishRepo firstSubEnglishRepo;
    @Autowired
    private ErrorService errorService;

    public ResponseEntity<?> updateFirstSubDataMalyalam(String uId, MainDTO mainDTO) {
        try {
            Optional<FirstSubMalayalam> firstSubMalayalam = firstSubMalayalamRepo.findByfsUid(uId);
            if (firstSubMalayalam.isPresent()){
                FirstSubMalayalam firstSubMalayalam1 = firstSubMalayalam.get();
                if (firstSubMalayalam1.getFsUid().equals(uId)){
                    firstSubMalayalam1.setTitle(mainDTO.getTitle());
                    firstSubMalayalam1.setDescription(mainDTO.getDescription());
                    firstSubMalayalam1.setRef(mainDTO.getReferenceURL());
                    firstSubMalayalamRepo.save(firstSubMalayalam1);
                    //return new ResponseEntity<>(firstSubMalayalam1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<FirstSubMalayalam> orderedList = firstSubMalayalamRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> updateFirstSubDataEnglish(String uId, MainDTO mainDTO) {
        try {
            Optional<FirstSubEnglish> firstSubEnglish = firstSubEnglishRepo.findByfsUid(uId);
            if (firstSubEnglish.isPresent()){
                FirstSubEnglish firstSubEnglish1 = firstSubEnglish.get();
                if (firstSubEnglish1.getFsUid().equals(uId)){
                    firstSubEnglish1.setTitle(mainDTO.getTitle());
                    firstSubEnglish1.setDescription(mainDTO.getDescription());
                    firstSubEnglish1.setRef(mainDTO.getReferenceURL());
                    firstSubEnglishRepo.save(firstSubEnglish1);
                   // return new ResponseEntity<>(firstSubEnglish1,HttpStatus.OK);
                    // Fetch all records ordered by primary key
                    List<FirstSubEnglish> orderedList = firstSubEnglishRepo.findAllByOrderByIdAsc();
                    return new ResponseEntity<>(orderedList, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
