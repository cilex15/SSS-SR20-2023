package com.example.projekat.Controllers;

import com.example.projekat.DTOS.ZaposlenikRequest;
import com.example.projekat.DTOS.ZaposlenikResponse;
import com.example.projekat.Services.ZaposlenikService;
import com.example.projekat.Services.NaplatnaStanicaService;
import com.example.projekat.Model.NaplatnaStanica;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/app/korisnici")
public class ZaposlenikWebController {

    private final ZaposlenikService zaposlenikService;
    private final NaplatnaStanicaService stanicaService;

    public ZaposlenikWebController(ZaposlenikService zaposlenikService, NaplatnaStanicaService stanicaService) {
        this.zaposlenikService = zaposlenikService;
        this.stanicaService = stanicaService;
    }

    @GetMapping
    public String getAllZaposleniciWeb(Model model) {
        List<ZaposlenikResponse> zaposlenici = zaposlenikService.findAllZaposlenici();
        model.addAttribute("zaposlenici", zaposlenici);
        return "korisnici";
    }

    @PostMapping("/obrisi/{id}")
    public String deleteZaposlenik(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            zaposlenikService.deleteZaposlenik(id);
            redirectAttributes.addFlashAttribute("message", "Zaposlenik ID " + id + " uspešno obrisan!");
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška prilikom brisanja: " + e.getReason());
        }
        return "redirect:/app/korisnici";
    }

    @GetMapping("/dodaj")
    public String showAddForm(Model model) {
        model.addAttribute("zaposlenik", new ZaposlenikRequest());
        List<NaplatnaStanica> stanice = stanicaService.findAllStanice();
        model.addAttribute("stanice", stanice);

        return "dodaj-korisnika";
    }

    @PostMapping("/dodaj")
    public String saveZaposlenik(@ModelAttribute("zaposlenik") ZaposlenikRequest zaposlenikRequest,
                                 RedirectAttributes redirectAttributes) {
        try {
            zaposlenikService.createZaposlenik(zaposlenikRequest);
            redirectAttributes.addFlashAttribute("message", "Novi zaposlenik uspešno dodat!");
            return "redirect:/app/korisnici";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri dodavanju: " + e.getReason());

            return "redirect:/app/korisnici/dodaj";
        }
    }

    @GetMapping("/izmeni/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ZaposlenikResponse response = zaposlenikService.findZaposlenikById(id);
            ZaposlenikRequest request = new ZaposlenikRequest(
                    response.getIme(),
                    response.getPrezime(),
                    response.getPozicija(),
                    response.getNaplatnaStanicaId()
            );

            // Dodamo atribute u model
            model.addAttribute("zaposlenikId", id);
            model.addAttribute("zaposlenik", request);

            List<NaplatnaStanica> stanice = stanicaService.findAllStanice();
            model.addAttribute("stanice", stanice);

            return "dodaj-korisnika";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Zaposlenik za izmenu nije pronađen: " + e.getReason());
            return "redirect:/app/korisnici";
        }
    }
    @PostMapping("/izmeni/{id}")
    public String updateZaposlenik(@PathVariable Long id,
                                   @ModelAttribute("zaposlenik") ZaposlenikRequest zaposlenikRequest,
                                   RedirectAttributes redirectAttributes) {
        try {
            zaposlenikService.updateZaposlenik(id, zaposlenikRequest);
            redirectAttributes.addFlashAttribute("message", "Zaposlenik ID " + id + " uspešno ažuriran!");
            return "redirect:/app/korisnici";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri ažuriranju: " + e.getReason());


            return "redirect:/app/korisnici/izmeni/" + id;
        }
    }
}