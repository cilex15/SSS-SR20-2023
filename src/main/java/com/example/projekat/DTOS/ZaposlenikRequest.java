package com.example.projekat.DTOS;

// DTO za prijem podataka sa Front-enda (POST/PUT)
public class ZaposlenikRequest {

    private String ime;
    private String prezime;
    private String pozicija;

    // Koristimo Long tip, usklađeno sa NaplatnaStanica.java
    private Long naplatnaStanicaId;

    // Prazan konstruktor
    public ZaposlenikRequest() {}

    // Konstruktor sa svim poljima (opciono, ali korisno)
    public ZaposlenikRequest(String ime, String prezime, String pozicija, Long naplatnaStanicaId) {
        this.ime = ime;
        this.prezime = prezime;
        this.pozicija = pozicija;
        this.naplatnaStanicaId = naplatnaStanicaId;
    }

    // Getteri i Setteri

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

    // ISPRAVLJENO: Vraća Long
    public Long getNaplatnaStanicaId() {
        return naplatnaStanicaId;
    }

    // Setovanje je već bilo Long, ali zadržavamo radi jasnoće
    public void setNaplatnaStanicaId(Long naplatnaStanicaId) {
        this.naplatnaStanicaId = naplatnaStanicaId;
    }
}