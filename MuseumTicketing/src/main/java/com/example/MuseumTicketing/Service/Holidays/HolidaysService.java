package com.example.MuseumTicketing.Service.Holidays;

import com.example.MuseumTicketing.Model.Holidays;
import com.example.MuseumTicketing.Repo.HolidaysRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HolidaysService {

    @Autowired
    private HolidaysRepo holidaysRepo;
    public Holidays addHolidays(Holidays holiDays) {
        try {
            Holidays holiDays1 = holidaysRepo.save(holiDays);
            return holiDays1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<List<Holidays>> getAllHoliDays() {
        try {
            return new ResponseEntity<>(holidaysRepo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


    public Holidays updateHolidaysById(Integer id, Holidays updateDays) {
        Holidays existingData = holidaysRepo.findById(id).orElse(null);
        if (existingData== null){
            return null;
        }else {
            existingData.setName(updateDays.getName());
            existingData.setDate(updateDays.getDate());
            return holidaysRepo.save(existingData);
        }
    }

    public void deleteHolidaysById(int id) {
        holidaysRepo.deleteById(id);
    }
}