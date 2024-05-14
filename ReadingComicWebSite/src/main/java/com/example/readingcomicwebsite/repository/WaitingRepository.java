package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Integer> {
}
