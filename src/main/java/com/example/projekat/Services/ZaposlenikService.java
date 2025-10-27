package com.example.projekat.Services;

import com.example.projekat.DTOS.ZaposlenikRequest;
import com.example.projekat.DTOS.ZaposlenikResponse;
import com.example.projekat.Model.NaplatnaStanica;
import com.example.projekat.Model.Zaposlenik;
import com.example.projekat.Repos.NaplatnaStanicaRepository;
import com.example.projekat.Repos.ZaposlenikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class ZaposlenikService {

    private final ZaposlenikRepository zaposlenikRepository;
    private final NaplatnaStanicaRepository stanicaRepository;

    @Autowired
    public ZaposlenikService(ZaposlenikRepository zaposlenikRepository, NaplatnaStanicaRepository stanicaRepository) {
        this.zaposlenikRepository = zaposlenikRepository;
        this.stanicaRepository = stanicaRepository;
    }

    /**
     * Pomoćna metoda za mapiranje ZaposlenikRequestDto u Zaposlenik Entitet.
     * Pronalazi Naplatnu Stanicu u bazi koristeći ID iz DTO-a (ako ID nije NULL).
     */
    private Zaposlenik mapRequestToEntity(ZaposlenikRequest dto, Zaposlenik zaposlenik) {

        NaplatnaStanica stanica = null;

        if (dto.getNaplatnaStanicaId() != null) {
            // Ako je ID stanice poslat, pokušavamo je pronaći
            stanica = stanicaRepository.findById(dto.getNaplatnaStanicaId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Naplatna stanica sa ID: " + dto.getNaplatnaStanicaId() + " nije pronađena."));
        }
        // else: stanica ostaje null, što dozvoljava Zaposlenik entitetu da ima NULL vrednost.

        // 2. Mapiranje polja
        zaposlenik.setIme(dto.getIme());
        zaposlenik.setPrezime(dto.getPrezime());
        zaposlenik.setPozicija(dto.getPozicija());
        // 🚨 NAPOMENA: Nedostaje setovanje username/password/jedinstveni id

        // Postavljanje reference na Entitet (može biti NULL)
        zaposlenik.setNaplatnaStanica(stanica);

        return zaposlenik;
    }
    public List<Zaposlenik> findZaposleniciByStanicaId(Long stanicaId) {
        return zaposlenikRepository.findByNaplatnaStanicaId(stanicaId);
    }

    // =================================================================
    // NOVO: METODA ZA DODELU (KORIŠĆENA OD STRANE STRUKTURA CONTROLLERA)
    // =================================================================
    /**
     * Dodeljuje zaposlenika određenoj naplatnoj stanici koristeći samo ID-jeve.
     * Vraća sačuvani entitet Zaposlenika.
     */
    @Transactional
    public Zaposlenik dodeliZaposlenikaStanici(Long zaposlenikId, Long stanicaId) {

        // 1. Pronađi zaposlenika
        Zaposlenik zaposlenik = zaposlenikRepository.findById(zaposlenikId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Zaposlenik sa ID: " + zaposlenikId + " nije pronađen."));

        // 2. Pronađi stanicu
        NaplatnaStanica stanica = stanicaRepository.findById(stanicaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Naplatna stanica sa ID: " + stanicaId + " nije pronađena."));

        // 3. Postavi referencu
        zaposlenik.setNaplatnaStanica(stanica);

        // 4. Sačuvaj
        return zaposlenikRepository.save(zaposlenik);
    }
    // =================================================================


    // 1. CREATE - Kreiranje novog zaposlenika
    @Transactional
    public ZaposlenikResponse createZaposlenik(ZaposlenikRequest request) {
        Zaposlenik noviZaposlenik = new Zaposlenik();
        noviZaposlenik = mapRequestToEntity(request, noviZaposlenik);

        Zaposlenik savedEntity = zaposlenikRepository.save(noviZaposlenik);

        return ZaposlenikResponse.fromEntity(savedEntity);
    }

    // 2. READ All - Dohvat svih zaposlenika (Vraća DTO)
    public List<ZaposlenikResponse> findAllZaposlenici() {
        // 🎯 KLJUČNA IZMENA: Pretpostavljamo da metoda findAllWithDetails() radi,
        // ali za potrebe StrukturWebControllera (gde je došlo do greške)
        // ova metoda mora vratiti DTO.
        return zaposlenikRepository.findAllWithDetails().stream()
                .map(ZaposlenikResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 3. READ By ID - Dohvat zaposlenika po ID-ju
    public ZaposlenikResponse findZaposlenikById(Long id) {
        Zaposlenik zaposlenik = zaposlenikRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Zaposlenik sa ID: " + id + " nije pronađen."));

        return ZaposlenikResponse.fromEntity(zaposlenik);
    }

    // 4. UPDATE - Ažuriranje postojećeg zaposlenika
    @Transactional
    public ZaposlenikResponse updateZaposlenik(Long id, ZaposlenikRequest request) {
        Zaposlenik existingZaposlenik = zaposlenikRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Zaposlenik sa ID: " + id + " nije pronađen."));

        existingZaposlenik = mapRequestToEntity(request, existingZaposlenik);

        Zaposlenik updatedEntity = zaposlenikRepository.save(existingZaposlenik);

        return ZaposlenikResponse.fromEntity(updatedEntity);
    }

    // 5. DELETE - Brisanje zaposlenika
    @Transactional
    public void deleteZaposlenik(Long id) {
        if (!zaposlenikRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Zaposlenik sa ID: " + id + " nije pronađen.");
        }
        zaposlenikRepository.deleteById(id);
    }
}