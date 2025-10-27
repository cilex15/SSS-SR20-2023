package com.example.projekat.Services;

// Uklanjamo GradRequest uvoz jer se gradovi ne kreiraju
import com.example.projekat.Model.Deonica;
import com.example.projekat.Model.Grad;
import com.example.projekat.Repos.GradRepository;
import com.example.projekat.Repos.DeonicaRepository; // NOVO: Uvoz DeonicaRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class GradService {

    private final GradRepository gradRepository;
    private final DeonicaRepository deonicaRepository; // NOVO: Injektujemo DeonicaRepository

    @Autowired
    public GradService(GradRepository gradRepository, DeonicaRepository deonicaRepository) {
        this.gradRepository = gradRepository;
        this.deonicaRepository = deonicaRepository;
    }

    // ===========================================
    // MANIPULACIJA PODACIMA (UKLONJENA)
    // ===========================================

    // 🎯 UKLONJENO: public Grad createGrad(GradRequest request)
    // 🎯 UKLONJENO: public void deleteGrad(Long id)

    // ===========================================
    // ČITANJE PODATAKA
    // ===========================================

    /**
     * Dohvat svih gradova.
     * @return Lista svih gradova.
     */
    public List<Grad> findAllGradovi() {
        return gradRepository.findAll();
    }

    /**
     * Pronalazi Grad po ID-ju.
     * @param id ID Grada (Long).
     * @return Grad entitet.
     * @throws ResponseStatusException ako grad nije pronađen.
     */
    public Grad findGradById(Long id) {
        return gradRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Grad sa ID: " + id + " nije pronađen."));
    }

    // ===========================================
    // METODA ZA DINAMIČKI PADajući MENI (AJAX)
    // ===========================================

    /**
     * Pronalazi gradove (Polazak i Dolazak) povezane sa određenom deonicom.
     * Koristi se za dinamičko popunjavanje padajućeg menija u formi Naplatne stanice.
     */
    public List<Grad> findGradoviByDeonicaId(Long deonicaId) {
        if (deonicaId == null) {
            return List.of();
        }

        // 1. Pronađi deonicu po ID-ju (potrebno za dobijanje referenci na Gradove)
        Deonica deonica = deonicaRepository.findById(deonicaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Deonica sa ID: " + deonicaId + " nije pronađena."));

        // 2. Kreiraj listu i dodaj grad polaska i grad dolaska
        List<Grad> gradovi = new ArrayList<>();

        if (deonica.getGradPolazak() != null) {
            gradovi.add(deonica.getGradPolazak());
        }

        // Dodaj grad dolaska, ali samo ako nije isti kao grad polaska
        if (deonica.getGradDolazak() != null && !deonica.getGradDolazak().equals(deonica.getGradPolazak())) {
            gradovi.add(deonica.getGradDolazak());
        }

        return gradovi;
    }
}