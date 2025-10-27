package com.example.projekat.Services;

import com.example.projekat.Model.ProlazakVozila;
import com.example.projekat.Repos.ProlazakVozilaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servis odgovoran isključivo za rukovanje podacima (CRUD operacije)
 * vezanim za ProlazakVozila entitet, koristeći JPA repozitorijum.
 */
@Service
@Transactional(readOnly = true)
public class ProlazakVozilaService {

    private final ProlazakVozilaRepository prolazakVozilaRepository;

    @Autowired
    public ProlazakVozilaService(ProlazakVozilaRepository prolazakVozilaRepository) {
        // Injektovanje pravog JPA Repozitorijuma
        this.prolazakVozilaRepository = prolazakVozilaRepository;
    }

    /**
     * Beleži novi prolazak vozila.
     * @param prolazak Objekat ProlazakVozila sa popunjenim podacima.
     * @return Sačuvan objekat ProlazakVozila.
     */
    @Transactional
    public ProlazakVozila saveProlazak(ProlazakVozila prolazak) {
        return prolazakVozilaRepository.save(prolazak);
    }

    // Vraća listu svih prolazaka
    public List<ProlazakVozila> findAllProlasci() {
        return prolazakVozilaRepository.findAll();
    }

    // Dodatna metoda za pronalaženje po ID-u, ako je potrebna
    public ProlazakVozila findProlazakById(Long id) {
        return prolazakVozilaRepository.findById(id)
                .orElse(null); // Ili bacite ResponseStatusException
    }
}