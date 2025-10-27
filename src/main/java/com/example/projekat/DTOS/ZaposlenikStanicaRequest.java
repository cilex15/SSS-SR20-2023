package com.example.projekat.DTOS;

/**
 * DTO za prenos podataka prilikom dodele Zaposlenika
 * određenoj Naplatnoj Stanici.
 */
public class ZaposlenikStanicaRequest {

    // ID zaposlenika kojeg dodeljujemo
    private Long zaposlenikId;

    // ID stanice kojoj zaposlenika dodeljujemo (fiksno iz URL-a forme)
    private Long stanicaId;

    // Konstruktor bez argumenata (potreban za Spring/Thymeleaf)
    public ZaposlenikStanicaRequest() {
    }

    // Konstruktor sa argumentima (opciono, za testiranje)
    public ZaposlenikStanicaRequest(Long zaposlenikId, Long stanicaId) {
        this.zaposlenikId = zaposlenikId;
        this.stanicaId = stanicaId;
    }

    // ========================
    // Getteri i Setteri
    // ========================

    public Long getZaposlenikId() {
        return zaposlenikId;
    }

    public void setZaposlenikId(Long zaposlenikId) {
        this.zaposlenikId = zaposlenikId;
    }

    public Long getStanicaId() {
        return stanicaId;
    }

    public void setStanicaId(Long stanicaId) {
        this.stanicaId = stanicaId;
    }
}