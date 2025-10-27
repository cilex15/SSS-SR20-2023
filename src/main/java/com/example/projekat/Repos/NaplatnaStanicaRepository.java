// NaplatnaStanicaRepository.java
package com.example.projekat.Repos;

import com.example.projekat.Model.NaplatnaStanica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaplatnaStanicaRepository extends JpaRepository<NaplatnaStanica, Long> {

    // Koristimo FETCH JOIN da bismo izbegli LazyInitializationException u Thymeleafu.
    @Query("SELECT s FROM NaplatnaStanica s LEFT JOIN FETCH s.deonica d LEFT JOIN FETCH s.grad g")
    List<NaplatnaStanica> findAllWithDetails();
}