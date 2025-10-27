package com.example.projekat.DTOS;

// Uklanjamo import za TipVozila, jer ovaj DTO prima String iz HTML forme.
// import com.example.projekat.Model.TipVozila;

/**
 * DTO za prihvatanje podataka o prolasku vozila iz web forme.
 * Polje tipVozila je String jer se tako šalje iz Thymeleaf-a.
 */
public class ProlazakVozilaRequest {

    private Long stanicaId;
    private String registarskaOznaka;

    // ISPRAVKA: Polje je String, kompatibilno sa podacima iz HTML forme
    private String tipVozila;

    public ProlazakVozilaRequest() {
        // Obavezan default konstruktor za automatsko popunjavanje forme
    }

    // Uklonjen je konstruktor sa parametrima jer se DTO obično popunjava preko settera
    // prilikom submitovanja forme.

    public Long getStanicaId() {
        return stanicaId;
    }

    public void setStanicaId(Long stanicaId) {
        this.stanicaId = stanicaId;
    }

    public String getRegistarskaOznaka() {
        return registarskaOznaka;
    }

    public void setRegistarskaOznaka(String registarskaOznaka) {
        this.registarskaOznaka = registarskaOznaka;
    }

    // ISPRAVKA: Getter vraća String
    public String getTipVozila() {
        return tipVozila;
    }

    // ISPRAVKA: Setter prima String
    public void setTipVozila(String tipVozila) {
        this.tipVozila = tipVozila;
    }
}
