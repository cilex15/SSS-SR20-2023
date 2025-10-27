package com.example.projekat.Repos;

import com.example.projekat.Model.ProlazakVozila;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za entitet ProlazakVozila. Omogućava CRUD operacije.
 */
@Repository
public interface ProlazakVozilaRepository extends JpaRepository<ProlazakVozila, Long> {

}
