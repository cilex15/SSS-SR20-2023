package com.example.projekat.Services;

import com.example.projekat.DTOS.NaplatnaStanicaRequest;
import com.example.projekat.Model.Deonica;
import com.example.projekat.Model.Grad;
import com.example.projekat.Model.NaplatnaStanica;
import com.example.projekat.Model.ProlazakVozila;
import com.example.projekat.Model.TipVozila; // DODATO: Import za novi Enum
import com.example.projekat.Repos.NaplatnaStanicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class NaplatnaStanicaService {

    private final NaplatnaStanicaRepository stanicaRepository;
    private final ProlazakVozilaService prolazakVozilaService;
    private final DeonicaService deonicaService;
    private final GradService gradService;

    @Autowired
    public NaplatnaStanicaService(NaplatnaStanicaRepository stanicaRepository, ProlazakVozilaService prolazakVozilaService, DeonicaService deonicaService, GradService gradService) {
        this.stanicaRepository = stanicaRepository;
        this.prolazakVozilaService = prolazakVozilaService;
        this.deonicaService = deonicaService;
        this.gradService = gradService;
    }

    // =================================================================
    // DOHVAT STANICE (READ METODE)
    // =================================================================

    public NaplatnaStanica findStanicaById(Long id) {
        return stanicaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Naplatna stanica sa ID: " + id + " nije pronađena."));
    }

    public NaplatnaStanicaRequest findNaplatnaStanicaRequestById(Long id) {
        NaplatnaStanica stanica = findStanicaById(id);

        NaplatnaStanicaRequest request = new NaplatnaStanicaRequest();
        request.setNaziv(stanica.getNaziv());

        if (stanica.getDeonica() != null) {
            request.setDeonicaId(stanica.getDeonica().getId());
        }
        if (stanica.getGrad() != null) {
            request.setGradId(stanica.getGrad().getId());
        }

        return request;
    }

    public List<NaplatnaStanica> findAllStanice() {
        List<NaplatnaStanica> stanice = stanicaRepository.findAll();

        // Rešavanje Lazy Loadinga
        for (NaplatnaStanica stanica : stanice) {
            if (stanica.getZaposlenici() != null) {
                stanica.getZaposlenici().size();
            }
        }

        return stanice;
    }

    // =================================================================
    // KREIRANJE, AŽURIRANJE, BRISANJE
    // =================================================================

    @Transactional
    public NaplatnaStanica createNaplatnaStanica(NaplatnaStanicaRequest request) {
        NaplatnaStanica stanica = new NaplatnaStanica();
        stanica.setBrojRampi(1);
        stanica.setRampaPodignuta(true);
        return mapRequestToEntity(request, stanica);
    }

    @Transactional
    public NaplatnaStanica updateNaplatnaStanica(Long id, NaplatnaStanicaRequest request) {
        NaplatnaStanica existingStanica = findStanicaById(id);
        return mapRequestToEntity(request, existingStanica);
    }

    @Transactional
    public void deleteNaplatnaStanica(Long id) {
        if (!stanicaRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Naplatna stanica sa ID: " + id + " nije pronađena.");
        }
        stanicaRepository.deleteById(id);
    }

    private NaplatnaStanica mapRequestToEntity(NaplatnaStanicaRequest request, NaplatnaStanica stanica) {
        Deonica deonica = deonicaService.findDeonicaById(request.getDeonicaId());

        Grad grad = null;
        if (request.getGradId() != null) {
            grad = gradService.findGradById(request.getGradId());
        }

        stanica.setNaziv(request.getNaziv());
        stanica.setDeonica(deonica);
        stanica.setGrad(grad);

        return stanicaRepository.save(stanica);
    }


    // =================================================================
    // LOGIKA PROLASKA VOZILA (NOVA LOGIKA NAPLATE)
    // =================================================================

    /**
     * Propusta vozilo, obračunava cenu koristeći Baznu cenu Deonice i Koeficijent Tipa Vozila, i kreira evidenciju prolaska.
     */
    @Transactional
    public ProlazakVozila propustiVozilo(Long stanicaId, String registracija, String tipVozila) {
        // 1. Pronađi stanicu
        NaplatnaStanica stanica = findStanicaById(stanicaId);

        // 2. Pronađi Deonicu i njenu baznu cenu
        Deonica deonica = stanica.getDeonica();
        if (deonica == null) {
            throw new ResponseStatusException(NOT_FOUND, "Stanica sa ID: " + stanicaId + " nije dodeljena deonici. Naplata nije moguća.");
        }

        // **Pretpostavljamo da Deonica.java ima metodu getBaznaCena()**
        double baznaCena = deonica.getBaznaCena();

        // 3. Logika naplate (NOVA LOGIKA)
        // Konvertujemo string u ENUM i preuzimamo koeficijent
        // Budući da dolazimo iz Dropdown-a, pretpostavljamo da je vrednost uvek validna.
        double koeficijent = TipVozila.valueOf(tipVozila.toUpperCase()).getKoeficijent();
        double konacnaCena = baznaCena * koeficijent;

        // 4. Kreiranje evidencije
        ProlazakVozila prolaz = new ProlazakVozila();
        prolaz.setRegistarskaOznaka(registracija);
        prolaz.setTipVozila(tipVozila);
        prolaz.setNaplacenaCena(konacnaCena); // Korištenje nove cene
        prolaz.setNaplatnaStanica(stanica);
        prolaz.setDatumIVreme(LocalDateTime.now());

        // 5. Sačuvaj prolazak
        prolaz = prolazakVozilaService.saveProlazak(prolaz);

        return prolaz;
    }
}