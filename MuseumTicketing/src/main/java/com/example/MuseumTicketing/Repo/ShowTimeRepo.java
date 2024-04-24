package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeRepo extends JpaRepository<ShowTime, Integer> {
}
