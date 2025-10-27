package com.example.projekat.Repos;

import com.example.projekat.Model.Zaposlenik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // 👈 OBAVEZAN IMPORT
import org.springframework.stereotype.Repository;

import java.util.List; // 👈 OBAVEZAN IMPORT

@Repository
public interface ZaposlenikRepository extends JpaRepository<Zaposlenik, Long> {

    /**
     * Koristi FETCH JOIN za učitavanje Zaposlenika zajedno sa povezanim
     * entitetom NaplatnaStanica (koji je LAZY).
     * LEFT JOIN FETCH se koristi jer je veza sa Stanicom opciona (nullable = true).
     */
    @Query("SELECT z FROM Zaposlenik z LEFT JOIN FETCH z.naplatnaStanica")
    List<Zaposlenik> findAllWithDetails();
    // U ZaposlenikRepository.java
    List<Zaposlenik> findByNaplatnaStanicaId(Long stanicaId);
}