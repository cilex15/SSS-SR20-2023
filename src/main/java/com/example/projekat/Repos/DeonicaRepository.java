package com.example.projekat.Repos;

import com.example.projekat.Model.Deonica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // DODAJTE
import org.springframework.stereotype.Repository;

import java.util.List; // DODAJTE

@Repository
public interface DeonicaRepository extends JpaRepository<Deonica, Long> {

    @Query("SELECT d FROM Deonica d JOIN FETCH d.gradPolazak JOIN FETCH d.gradDolazak")
    List<Deonica> findAllWithDetails();
}