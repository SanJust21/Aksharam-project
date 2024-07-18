package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.CalendarEvent;
import com.example.MuseumTicketing.Model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CalendarRepo extends JpaRepository<CalendarEvent, Integer> {

    List<CalendarEvent> findByStartDate(LocalDate date);
    CalendarEvent findByCapacityGreaterThanEqualAndStartDateAndStartTime(Integer capacity, LocalDate bookDate, LocalTime slotName);

    CalendarEvent findFirstByStartDateAndStartTime(LocalDate bookDte, LocalTime sName);

    CalendarEvent findCapacityById(int i);

    @Modifying
    @Query("UPDATE CalendarEvent  c SET c.capacity=:capacity WHERE c.startDate = :startDate AND c.id = :id")

    void updateCapacity(@Param("id") Integer id, @Param("startDate") LocalDate startDate, @Param("capacity") Integer capacity);

    @Query("SELECT DISTINCT c.startDate FROM CalendarEvent c")
    List<LocalDate> findDistinctStartDate();

    CalendarEvent findByStartDateAndStartTime(LocalDate date, LocalTime startTime);
}
