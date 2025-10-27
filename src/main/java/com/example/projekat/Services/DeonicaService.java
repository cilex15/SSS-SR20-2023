package com.example.projekat.Services;

import com.example.projekat.DTOS.DeonicaRequest;
import com.example.projekat.DTOS.DeonicaResponse;
import com.example.projekat.Model.Deonica;
import com.example.projekat.Model.Grad;
import com.example.projekat.Repos.DeonicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Transactional(readOnly = true)
public class DeonicaService {

    private final DeonicaRepository deonicaRepository;
    private final GradService gradService;
    // Opciono: private final NaplatnaStanicaService stanicaService; // Za proveru zavisnosti

    @Autowired
    public DeonicaService(DeonicaRepository deonicaRepository, GradService gradService) {
        this.deonicaRepository = deonicaRepository;
        this.gradService = gradService;
    }

    // =================================================================
    // POMOĆNE METODE ZA DOHVAT I MAPIRANJE
    // =================================================================

    /**
     * Pomoćna metoda: Dohvata Deonicu po ID-ju ili baca NOT_FOUND.
     */
    public Deonica findDeonicaById(Long id) {
        return deonicaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Deonica sa ID: " + id + " nije pronađena."));
    }

    /**
     * Pomoćna metoda: Mapira DTO u Entitet i čuva ga. Koristi se za CREATE i UPDATE.
     */
    private Deonica mapRequestToEntity(DeonicaRequest dto, Deonica deonica) {
        // 1. Pronalaženje entiteta Grad (Baca NOT_FOUND ako ne postoji)
        Grad polazak = gradService.findGradById(dto.getGradPolazakId());
        Grad dolazak = gradService.findGradById(dto.getGradDolazakId());

        // 2. Mapiranje polja
        deonica.setNaziv(dto.getNaziv());
        deonica.setDuzinaKm(dto.getDuzinaKm());
        deonica.setBaznaCena(dto.getBaznaCena());
        deonica.setGradPolazak(polazak);
        deonica.setGradDolazak(dolazak);

        return deonicaRepository.save(deonica);
    }

    // =================================================================
    // READ FOR UPDATE (POTREBNO ZA KONTROLER)
    // =================================================================

    /**
     * Dohvata postojeću deonicu i mapira je u Request DTO za popunjavanje forme za izmenu.
     */
    public DeonicaRequest findDeonicaRequestById(Long id) {
        Deonica deonica = findDeonicaById(id);

        DeonicaRequest request = new DeonicaRequest();
        request.setId(deonica.getId()); // ID je ključan za update!
        request.setNaziv(deonica.getNaziv());
        request.setDuzinaKm(deonica.getDuzinaKm());
        request.setBaznaCena(deonica.getBaznaCena());

        // Mapiranje ID-jeva povezanih entiteta
        if (deonica.getGradPolazak() != null) {
            request.setGradPolazakId(deonica.getGradPolazak().getId());
        }
        if (deonica.getGradDolazak() != null) {
            request.setGradDolazakId(deonica.getGradDolazak().getId());
        }

        return request;
    }

    // =================================================================
    // 1. CREATE
    // =================================================================
    @Transactional
    public DeonicaResponse createDeonica(DeonicaRequest request) {
        Deonica novaDeonica = new Deonica();
        Deonica savedEntity = mapRequestToEntity(request, novaDeonica);

        return DeonicaResponse.fromEntity(savedEntity);
    }

    // 2. READ All
    public List<DeonicaResponse> findAllDeonice() {
        // Koristimo .findAll().stream() da konvertujemo sve entitete u Response DTO.
        return deonicaRepository.findAll().stream()
                .map(DeonicaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // =================================================================
    // 3. UPDATE (KONTROLER POZIVA OVU METODU)
    // =================================================================
    @Transactional
    public Deonica updateDeonica(Long id, DeonicaRequest request) {
        // 1. Provera postojanja
        Deonica existingDeonica = findDeonicaById(id);

        // 2. Provera konzistentnosti ID-ja
        if (!id.equals(request.getId())) {
            throw new ResponseStatusException(BAD_REQUEST, "ID u URL-u se ne poklapa sa ID-jem u telu zahteva.");
        }

        // 3. Ažuriranje i čuvanje
        return mapRequestToEntity(request, existingDeonica);
    }

    // =================================================================
    // 4. DELETE (KONTROLER POZIVA OVU METODU)
    // =================================================================
    @Transactional
    public void deleteDeonica(Long id) {
        if (!deonicaRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Deonica sa ID: " + id + " nije pronađena.");
        }

        // *OVDE BI TREBALO DODATI PROVERU DA LI POSTOJE POVEZANE STANICE PRE BRISANJA*

        deonicaRepository.deleteById(id);
    }
}