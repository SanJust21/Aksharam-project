package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CalendarRepo extends JpaRepository<CalendarEvent, Integer> {

    List<CalendarEvent> findByStartDate(LocalDate date);
    CalendarEvent findByCapacityGreaterThanEqualAndStartDateAndStartTime(Integer capacity, LocalDate bookDate, LocalTime slotName);

    CalendarEvent findFirstByStartDateAndStartTime(LocalDate bookDte, LocalTime sName);
}
