package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.ShowTime;
import com.example.MuseumTicketing.Model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Integer> {
   List<ShowTime> findAll();

    boolean existsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

    Optional<ShowTime> findByStartTime(LocalTime slotName);
}
