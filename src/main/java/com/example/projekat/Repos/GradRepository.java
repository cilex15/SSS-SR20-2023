package com.example.projekat.Repos;

import com.example.projekat.Model.Grad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozitorijum za entitet Grad. Omogućava CRUD operacije.
 */
@Repository
// Koristi Long za primarni ključ
public interface GradRepository extends JpaRepository<Grad, Long> {

}