package com.example.projekat.DTOS;

import com.example.projekat.Model.Zaposlenik;

// DTO za slanje Front-endu (GET)
public class ZaposlenikResponse {

    private Long id;
    private String ime;
    private String prezime;
    private String pozicija;
    private Long naplatnaStanicaId;
    private String naplatnaStanicaNaziv;

    // Prazan konstruktor
    public ZaposlenikResponse() {}

    // Metoda za konverziju JPA Entiteta u DTO
    public static ZaposlenikResponse fromEntity(Zaposlenik entity) {
        ZaposlenikResponse dto = new ZaposlenikResponse();
        dto.setId(entity.getId());
        dto.setIme(entity.getIme());
        dto.setPrezime(entity.getPrezime());
        dto.setPozicija(entity.getPozicija());

        // Prikazujemo samo ID i Naziv, bez rizika od rekurzije
        if (entity.getNaplatnaStanica() != null) {
            dto.setNaplatnaStanicaId(entity.getNaplatnaStanica().getId());
            dto.setNaplatnaStanicaNaziv(entity.getNaplatnaStanica().getNaziv());
        }
        return dto;
    }

    // Getteri i Setteri

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPozicija() {
        return pozicija;
    }

    public void setPozicija(String pozicija) {
        this.pozicija = pozicija;
    }

    public Long getNaplatnaStanicaId() {
        return naplatnaStanicaId;
    }

    public void setNaplatnaStanicaId(Long naplatnaStanicaId) {
        this.naplatnaStanicaId = naplatnaStanicaId;
    }

    public String getNaplatnaStanicaNaziv() {
        return naplatnaStanicaNaziv;
    }

    public void setNaplatnaStanicaNaziv(String naplatnaStanicaNaziv) {
        this.naplatnaStanicaNaziv = naplatnaStanicaNaziv;
    }
}