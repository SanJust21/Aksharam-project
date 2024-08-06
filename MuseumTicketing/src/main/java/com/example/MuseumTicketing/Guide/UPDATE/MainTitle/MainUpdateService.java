package com.example.MuseumTicketing.Guide.UPDATE.MainTitle;

import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.mainHeading.MainDTO;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEng;
import com.example.MuseumTicketing.Guide.mainHeading.mainEng.MainTitleEngRepo;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMal;
import com.example.MuseumTicketing.Guide.mainHeading.mainMal.MainTitleMalRepo;
import com.example.MuseumTicketing.Guide.util.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MainUpdateService {
    @Autowired
    private MainTitleEngRepo mainTitleEngRepo;
    @Autowired
    private MainTitleMalRepo mainTitleMalRepo;
    @Autowired
    private ErrorService errorService;



    public ResponseEntity<?> updateDataMalyalam(String uId, MainDTO mainDTO) {
        try {
            Optional<MainTitleMal> mainTitleMal = mainTitleMalRepo.findBymMalUid(uId);
            if (mainTitleMal.isPresent()){
                MainTitleMal mainTitleMal1 = mainTitleMal.get();
                mainTitleMal1.setTitle(mainDTO.getTitle());
                mainTitleMal1.setDescription(mainDTO.getDescription());
                mainTitleMal1.setRef(mainDTO.getReferenceURL());
                mainTitleMalRepo.save(mainTitleMal1);
                //return new ResponseEntity<>(mainTitleMal1,HttpStatus.OK);
                // Fetch all records ordered by primary key
                List<MainTitleMal> orderedList = mainTitleMalRepo.findAll();
                orderedList.sort(Comparator.comparing(MainTitleMal::getMMalId));

                return new ResponseEntity<>(orderedList, HttpStatus.OK);
            }
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> updateDataEnglish(String uId, MainDTO mainDTO) {
        try {
            MainTitleEng mainTitleEng = mainTitleEngRepo.findBymEngUid(uId);
            mainTitleEng.setTitle(mainDTO.getTitle());
            mainTitleEng.setDescription(mainDTO.getDescription());
            mainTitleEng.setRef(mainDTO.getReferenceURL());
            mainTitleEngRepo.save(mainTitleEng);
            //return new ResponseEntity<>(mainTitleEng,HttpStatus.OK);
            // Fetch all records ordered by primary key
            List<MainTitleEng> orderedList = mainTitleEngRepo.findAll();
            orderedList.sort(Comparator.comparing(MainTitleEng::getMEngId));

            return new ResponseEntity<>(orderedList, HttpStatus.OK);
        }catch (Exception e){
            //e.printStackTrace();
            return errorService.handlerException(e);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
