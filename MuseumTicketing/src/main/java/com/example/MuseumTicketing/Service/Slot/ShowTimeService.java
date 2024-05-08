package com.example.MuseumTicketing.Service.Slot;

import com.example.MuseumTicketing.DTO.Slot.ShowTimeDTO;
import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Repo.ShowTimeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepo showTimeRepo;
    public ShowTime addShowTime(ShowTimeDTO showTimeDTO) {
        if (showTimeRepo.existsByStartTimeAndEndTime(showTimeDTO.getStartTime(), showTimeDTO.getEndTime())) {
            throw new IllegalArgumentException("Slot time already exists");
        }
        ShowTime showTime = new ShowTime();
        showTime.setId(showTimeDTO.getId());
        showTime.setStartTime(showTimeDTO.getStartTime());
        showTime.setEndTime(showTimeDTO.getEndTime());
        showTime.setCapacity(showTimeDTO.getCapacity());
        showTime.setSpotCapacity(showTimeDTO.getSpotCapacity());
        showTime.setStatus(showTimeDTO.getStatus());
        showTime.setTotalCapacity(showTimeDTO.getCapacity());
        return showTimeRepo.save(showTime);
    }

    public ShowTime updateShowTime(Integer id, ShowTimeDTO updateShowTimeDTO) {
        ShowTime existingShowTime = showTimeRepo.findById(id).orElse(null);
        if (existingShowTime==null){
            return null;
        }
        existingShowTime.setStartTime(updateShowTimeDTO.getStartTime());
        existingShowTime.setEndTime(updateShowTimeDTO.getEndTime());
        existingShowTime.setCapacity(updateShowTimeDTO.getCapacity());
        existingShowTime.setSpotCapacity(updateShowTimeDTO.getSpotCapacity());
        existingShowTime.setStatus(updateShowTimeDTO.getStatus());
        return showTimeRepo.save(existingShowTime);
    }

    public ShowTime getShowTimeById(int i) {
        return showTimeRepo.findById(i).orElseThrow(() -> new EntityNotFoundException("Show time not found for id"+i));
    }

    public ResponseEntity<List<ShowTime>> getAllShowTime() {
        try {
            return new ResponseEntity<>(showTimeRepo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }
}