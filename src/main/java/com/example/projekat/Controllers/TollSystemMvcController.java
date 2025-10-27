package com.example.projekat.Controllers;

import com.example.projekat.DTOS.NaplatnaStanicaRequest;
import com.example.projekat.DTOS.ProlazakVozilaRequest;
import com.example.projekat.DTOS.ZaposlenikRequest;
import com.example.projekat.Model.ProlazakVozila;
import com.example.projekat.Model.TipVozila; // DODANO: Import za TipVozila
import com.example.projekat.Services.DeonicaService;
import com.example.projekat.Services.GradService;
import com.example.projekat.Services.NaplatnaStanicaService;
import com.example.projekat.Services.ProlazakVozilaService;
import com.example.projekat.Services.ZaposlenikService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/app") // Osnovna putanja za sve MVC rute
public class TollSystemMvcController {

    private final ZaposlenikService zaposlenikService;
    private final NaplatnaStanicaService stanicaService;
    private final ProlazakVozilaService prolazakVozilaService;
    private final DeonicaService deonicaService;
    private final GradService gradService;

    // Konstruktor sa svim injektovanim servisima
    public TollSystemMvcController(ZaposlenikService zaposlenikService, NaplatnaStanicaService stanicaService,
                                   ProlazakVozilaService prolazakVozilaService, DeonicaService deonicaService,
                                   GradService gradService) {
        this.zaposlenikService = zaposlenikService;
        this.stanicaService = stanicaService;
        this.prolazakVozilaService = prolazakVozilaService;
        this.deonicaService = deonicaService;
        this.gradService = gradService;
    }

    // =================================================================
    // 0. Početna strana
    // =================================================================

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    // =================================================================
    // 1. Simulacija Naplate - GET (Prikaz forme)
    // =================================================================
    @GetMapping("/simulacija-naplate")
    public String simulacijaNaplate(Model model) {
        // DODANO: Dodavanje ENUM vrednosti u model za padajući meni
        model.addAttribute("tipoviVozila", TipVozila.values());

        // Postojeći kod
        model.addAttribute("prolazakRequest", new ProlazakVozilaRequest());
        model.addAttribute("stanice", stanicaService.findAllStanice());
        model.addAttribute("prolasci", prolazakVozilaService.findAllProlasci());
        return "simulacija";
    }

    // =================================================================
    // 2. Simulacija Naplate - POST (Obračun cene)
    // =================================================================
    @PostMapping("/simulacija-naplate")
    public String simulacijaNaplateSubmit(@ModelAttribute ProlazakVozilaRequest request, RedirectAttributes redirectAttributes) {
        try {
            ProlazakVozila prolaz = stanicaService.propustiVozilo(
                    request.getStanicaId(),
                    request.getRegistarskaOznaka(),
                    request.getTipVozila()
            );
            // Koristimo formatDecimal za ispravan prikaz cene u poruci
            // U idealnom slučaju bi trebalo da koristite DecimalFormat u servisu, ali ovo radi za brzi test.
            String formatiranaCena = String.format("%.2f", prolaz.getNaplacenaCena());
            redirectAttributes.addFlashAttribute("success", "Vozilo " + prolaz.getRegistarskaOznaka() + " uspešno naplaćeno: " + formatiranaCena + " RSD.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška: " + e.getMessage());
        }
        // ISPRAVKA: Preusmeravanje na ispravan GET endpoint
        return "redirect:/app/simulacija-naplate";
    }
}
