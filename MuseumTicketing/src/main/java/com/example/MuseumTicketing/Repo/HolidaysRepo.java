package com.example.MuseumTicketing.Repo;
import com.example.MuseumTicketing.Model.Holidays;
import com.example.MuseumTicketing.Model.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidaysRepo extends JpaRepository<Holidays,Integer> {
}
