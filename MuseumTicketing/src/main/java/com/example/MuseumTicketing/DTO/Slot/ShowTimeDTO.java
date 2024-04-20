package com.example.MuseumTicketing.DTO.Slot;

import java.time.LocalTime;

public class ShowTimeDTO {
    Integer id;
    LocalTime startTime;
    LocalTime endTime;

    Integer capacity;

    Integer spotCapacity;

    Boolean status;

    public ShowTimeDTO() {
    }


    public ShowTimeDTO(Integer id, LocalTime startTime, LocalTime endTime, Integer capacity, Integer spotCapacity,Boolean status) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.spotCapacity = spotCapacity;
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
    }

    public Integer getSpotCapacity() {
        return spotCapacity;
    }

    public void setSpotCapacity(Integer spotCapacity) {
        this.spotCapacity = spotCapacity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}