package com.example.projekat.Controllers;

import com.example.projekat.Model.ProlazakVozila;
import com.example.projekat.DTOS.DeonicaRequest;
import com.example.projekat.DTOS.NaplatnaStanicaRequest;
import com.example.projekat.DTOS.ZaposlenikStanicaRequest;
import com.example.projekat.DTOS.ZaposlenikResponse;
import com.example.projekat.Model.Grad;
import com.example.projekat.Model.NaplatnaStanica;
import com.example.projekat.Model.Zaposlenik;
import com.example.projekat.Services.DeonicaService;
import com.example.projekat.Services.GradService;
import com.example.projekat.Services.NaplatnaStanicaService;
import com.example.projekat.Services.ZaposlenikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/app/stanice")
public class StrukturaWebController {

    private final DeonicaService deonicaService;
    private final GradService gradService;
    private final NaplatnaStanicaService stanicaService;
    private final ZaposlenikService zaposlenikService;

    // AŽURIRAN KONSTRUKTOR
    public StrukturaWebController(DeonicaService deonicaService, GradService gradService,
                                  NaplatnaStanicaService stanicaService, ZaposlenikService zaposlenikService) {
        this.deonicaService = deonicaService;
        this.gradService = gradService;
        this.stanicaService = stanicaService;
        this.zaposlenikService = zaposlenikService;
    }

    // POMOĆNA METODA ZA RUČNO RUKOVANJE ID-JEM (sprečava grešku "For input string: 'null'")
    private Long parsePathId(String idString) throws NumberFormatException {
        if (idString != null && !idString.equalsIgnoreCase("null") && !idString.isEmpty()) {
            return Long.valueOf(idString);
        }
        return null;
    }

    // ===================================
    // RUTA ZA AJAX (Gradovi po Deonici)
    // ===================================
    @GetMapping("/gradovi-po-deonici/{deonicaId}")
    @ResponseBody
    public List<Grad> getGradoviByDeonicaId(@PathVariable Long deonicaId) {
        if (deonicaId == null) {
            return List.of();
        }
        return gradService.findGradoviByDeonicaId(deonicaId);
    }

    // ===================================
    // 1. GLAVNA STRANICA: PRIKAZ STRUKTURE
    // ===================================
    @GetMapping
    public String showStrukturaPanel(Model model) {
        model.addAttribute("gradovi", gradService.findAllGradovi());
        model.addAttribute("deonice", deonicaService.findAllDeonice());
        model.addAttribute("stanice", stanicaService.findAllStanice());

        return "struktura-admin";
    }

    // =========================================================
    // 2. RUTE ZA DODELU ZAPOSLENIKA STANICI (AŽURIRANA PROVERA ID-JA)
    // =========================================================

    @GetMapping("/dodeli-zaposlenika/{stanicaId}")
    public String showDodelaForm(@PathVariable("stanicaId") String stanicaIdString,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Long stanicaId = parsePathId(stanicaIdString);

            if (stanicaId == null) {
                redirectAttributes.addFlashAttribute("error", "ID stanice nije validan za dodelu osoblja.");
                return "redirect:/app/stanice";
            }

            // 1. Proveri postojanje stanice
            NaplatnaStanica stanica = stanicaService.findStanicaById(stanicaId);

            // 2. Dohvati sve zaposlenike (za padajući meni)
            List<ZaposlenikResponse> sviZaposlenici = zaposlenikService.findAllZaposlenici();

            // 3. Pripremi DTO
            ZaposlenikStanicaRequest request = new ZaposlenikStanicaRequest();
            request.setStanicaId(stanicaId); // Fiksiraj ID stanice

            model.addAttribute("stanica", stanica);
            model.addAttribute("sviZaposlenici", sviZaposlenici);
            model.addAttribute("dodelaRequest", request);

            return "naplatna-stanica-dodela";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri pronalaženju stanice: " + e.getReason());
            return "redirect:/app/stanice";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "ID stanice mora biti numerička vrednost.");
            return "redirect:/app/stanice";
        }
    }

    @PostMapping("/dodeli-zaposlenika")
    public String dodeliZaposlenikaStanici(@ModelAttribute("dodelaRequest") ZaposlenikStanicaRequest request,
                                           RedirectAttributes redirectAttributes) {
        try {
            if (request.getZaposlenikId() == null || request.getStanicaId() == null) {
                throw new ResponseStatusException(BAD_REQUEST, "Morate izabrati zaposlenika.");
            }

            // Metoda servisa vraća ENTITET (Zaposlenik)
            Zaposlenik zaposlenik = zaposlenikService.dodeliZaposlenikaStanici(
                    request.getZaposlenikId(),
                    request.getStanicaId()
            );

            redirectAttributes.addFlashAttribute("message",
                    "Zaposlenik " + zaposlenik.getIme() + " uspešno dodeljen stanici " + zaposlenik.getNaplatnaStanica().getNaziv() + ".");

            return "redirect:/app/stanice";

        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri dodeli: " + e.getReason());
            return "redirect:/app/stanice";
        }
    }

    // =========================================================
    // 3. RUTE ZA DEONICE: Dodavanje, Izmena i Brisanje
    // =========================================================

    @GetMapping("/dodaj-deonicu")
    public String showAddDeonicaForm(Model model) {
        if (!model.containsAttribute("deonicaRequest")) {
            model.addAttribute("deonicaRequest", new DeonicaRequest());
        }
        model.addAttribute("gradovi", gradService.findAllGradovi());
        model.addAttribute("isEdit", false); // Dodajemo flag za Kreiranje
        return "deonica-form";
    }

    // RUTA ZA PRIKAZ FORME ZA IZMENU DEONICE
    // Ostavljamo Long id jer pretpostavljamo da Deonice u bazi nemaju null ID i da link radi (kao što ste potvrdili)
    @GetMapping("/izmeni-deonicu/{id}")
    public String showEditDeonicaForm(@PathVariable Long id, Model model) {
        try {
            // Metoda mora vratiti DTO request tip
            DeonicaRequest request = deonicaService.findDeonicaRequestById(id);

            model.addAttribute("deonicaRequest", request);
            model.addAttribute("gradovi", gradService.findAllGradovi());
            model.addAttribute("isEdit", true); // Flag za Izmenu

            return "deonica-form";
        } catch (ResponseStatusException e) {
            // U slučaju da deonica sa tim ID-jem nije nađena
            model.addAttribute("error", "Deonica nije pronađena: " + e.getReason());
            return "redirect:/app/stanice";
        }
    }

    // RUTA ZA ČUVANJE NOVE DEONICE
    @PostMapping("/dodaj-deonicu")
    public String saveDeonica(@ModelAttribute("deonicaRequest") DeonicaRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            deonicaService.createDeonica(request);
            redirectAttributes.addFlashAttribute("message", "Deonica uspešno dodata!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri dodavanju deonice: " + e.getReason());
            redirectAttributes.addFlashAttribute("deonicaRequest", request); // Vraćanje DTO-a sa podacima
            return "redirect:/app/stanice/dodaj-deonicu";
        }
    }

    // RUTA ZA ČUVANJE IZMENJENE DEONICE
    @PostMapping("/izmeni-deonicu/{id}")
    public String updateDeonica(@PathVariable Long id,
                                @ModelAttribute("deonicaRequest") DeonicaRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            deonicaService.updateDeonica(id, request);
            redirectAttributes.addFlashAttribute("message", "Deonica uspešno ažurirana!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri ažuriranju deonice: " + e.getReason());
            redirectAttributes.addFlashAttribute("deonicaRequest", request);
            // Preusmeravanje nazad na formu za izmenu
            return "redirect:/app/stanice/izmeni-deonicu/" + id;
        }
    }

    // RUTA ZA BRISANJE DEONICE
    @PostMapping("/obrisi-deonicu/{id}")
    public String deleteDeonica(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            deonicaService.deleteDeonica(id);
            redirectAttributes.addFlashAttribute("message", "Deonica uspešno obrisana!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri brisanju deonice: " + e.getReason());
            return "redirect:/app/stanice";
        }
    }

    // =========================================================
    // 4. RUTE ZA NAPLATNE STANICE: Dodavanje, Izmena i Brisanje (AŽURIRANA GET RUTA)
    // =========================================================

    @GetMapping("/dodaj-stanicu")
    public String showAddStanicaForm(Model model) {
        if (!model.containsAttribute("stanicaRequest")) {
            model.addAttribute("stanicaRequest", new NaplatnaStanicaRequest());
        }
        model.addAttribute("deonice", deonicaService.findAllDeonice());
        model.addAttribute("gradovi", gradService.findAllGradovi());
        model.addAttribute("isEdit", false); // Dodajemo flag za Kreiranje
        return "naplatna-stanica-form";
    }

    // RUTA ZA PRIKAZ FORME ZA IZMENU STANICE
    /**
     * AŽURIRANA RUTA: PRIMA STRING ID DA BI RUČNO RUKOVALA GRESKOM 'For input string: "null"'.
     */
    @GetMapping("/izmeni-stanicu/{id}")
    public String showEditStanicaForm(
            @PathVariable("id") String idString,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Koristimo pomoćnu metodu da sigurno parsiramo ID
            Long id = parsePathId(idString);

            if (id == null) {
                redirectAttributes.addFlashAttribute("error", "ID stanice za izmenu nije definisan ili je neispravan.");
                return "redirect:/app/stanice";
            }

            // 1. DOHVAT DTO-a za formu
            NaplatnaStanicaRequest request = stanicaService.findNaplatnaStanicaRequestById(id);

            // PROVERITE: Ako ste zaboravili u Servisu, ovo je ključno za POST rutu
            request.setId(id);

            model.addAttribute("stanicaRequest", request);
            model.addAttribute("deonice", deonicaService.findAllDeonice());
            model.addAttribute("gradovi", gradService.findAllGradovi());
            model.addAttribute("isEdit", true);

            return "naplatna-stanica-form";

        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri učitavanju stanice: " + e.getReason());
            return "redirect:/app/stanice";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "ID stanice mora biti numerička vrednost.");
            return "redirect:/app/stanice";
        }
    }


    // RUTA ZA ČUVANJE NOVE STANICE
    @PostMapping("/dodaj-stanicu")
    public String saveStanica(@ModelAttribute("stanicaRequest") NaplatnaStanicaRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            stanicaService.createNaplatnaStanica(request);

            redirectAttributes.addFlashAttribute("message", "Naplatna Stanica uspešno dodata!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri dodavanju stanice: " + e.getReason());
            redirectAttributes.addFlashAttribute("stanicaRequest", request); // Vraćanje DTO-a sa podacima
            return "redirect:/app/stanice/dodaj-stanicu";
        }
    }

    // RUTA ZA ČUVANJE IZMENJENE STANICE
    @PostMapping("/izmeni-stanicu/{id}")
    public String updateStanica(@PathVariable Long id,
                                @ModelAttribute("stanicaRequest") NaplatnaStanicaRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            stanicaService.updateNaplatnaStanica(id, request);
            redirectAttributes.addFlashAttribute("message", "Naplatna Stanica uspešno ažurirana!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri ažuriranju stanice: " + e.getReason());
            redirectAttributes.addFlashAttribute("stanicaRequest", request);
            // Preusmeravanje nazad na formu za izmenu
            return "redirect:/app/stanice/izmeni-stanicu/" + id;
        }
    }

    // RUTA ZA BRISANJE STANICE
    @PostMapping("/obrisi-stanicu/{id}")
    public String deleteStanica(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stanicaService.deleteNaplatnaStanica(id);
            redirectAttributes.addFlashAttribute("message", "Naplatna Stanica uspešno obrisana!");
            return "redirect:/app/stanice";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri brisanju stanice: " + e.getReason());
            return "redirect:/app/stanice";
        }
    }
    @GetMapping("/simulacija-naplate")
    public String showSimulacijaForm(
            @RequestParam(required = false) Long stanicaId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("stanice", stanicaService.findAllStanice());
        model.addAttribute("selektovanaStanica", null); // Inicijalno je null (Stanje 1: Izbor stanice)

        if (stanicaId != null) {
            try {
                // Ako je ID poslat, prelazimo na Stanje 2 (Unos podataka o vozilu)
                NaplatnaStanica stanica = stanicaService.findStanicaById(stanicaId);
                model.addAttribute("selektovanaStanica", stanica);
            } catch (ResponseStatusException e) {
                // Ako stanica nije nađena, vraćamo se na listu
                redirectAttributes.addFlashAttribute("error", "Stanica sa ID " + stanicaId + " nije pronađena.");
                return "redirect:/app/stanice/simulacija-naplate";
            }
        }

        return "simulacija";
    }

    // POST - Obrada prolaska vozila (Koristi se nova ruta)
    @PostMapping("/simulacija-obrada")
    public String handleSimulacijaNaplate(@RequestParam Long stanicaId,
                                          @RequestParam String registracija,
                                          @RequestParam String tipVozila,
                                          RedirectAttributes redirectAttributes) {
        try {
            // Logika obrade je u Servisu
            ProlazakVozila prolazak = stanicaService.propustiVozilo(stanicaId, registracija, tipVozila);

            // Prosleđivanje objekta nazad preko flash atributa da bi se prikazao na GET formi
            redirectAttributes.addFlashAttribute("prolazak", prolazak);
            redirectAttributes.addFlashAttribute("message",
                    "Vozilo " + registracija + " uspešno prošlo! Naplaćeno: " +
                            String.format("%.2f", prolazak.getNaplacenaCena()) + " RSD. Rampa podignuta.");

            // Vraćamo se na GET rutu, ali BEZ ID-ja stanice, da bismo prikazali rezultat, ali se vratili na Stanje 1
            return "redirect:/app/stanice/simulacija-naplate";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri simulaciji: " + e.getReason());
            // Vraćamo se na GET rutu, ali sa ID-jem stanice da se korisnik ne mora ponovo birati
            return "redirect:/app/stanice/simulacija-naplate?stanicaId=" + stanicaId;
        }
    }
}