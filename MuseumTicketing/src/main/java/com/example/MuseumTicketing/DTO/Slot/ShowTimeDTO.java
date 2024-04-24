package com.example.MuseumTicketing.DTO.Slot;

import lombok.Data;

import java.time.LocalTime;
@Data
public class ShowTimeDTO {
    Integer id;
    LocalTime startTime;
    LocalTime endTime;

    Integer capacity;

    Integer spotCapacity;

    Integer totalCapacity;

    Boolean status;

    public ShowTimeDTO() {
    }


    public ShowTimeDTO(Integer id, LocalTime startTime, LocalTime endTime, Integer capacity, Integer spotCapacity, Integer totalCapacity, Boolean status) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.spotCapacity = spotCapacity;
        this.totalCapacity = totalCapacity;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
        this.totalCapacity = capacity;
    }

    public Integer getSpotCapacity() {
        return spotCapacity;
    }

    public void setSpotCapacity(Integer spotCapacity) {
        this.spotCapacity = spotCapacity;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

//    public void setTotalCapacity(Integer totalCapacity) {
//        this.totalCapacity = totalCapacity;
//    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}