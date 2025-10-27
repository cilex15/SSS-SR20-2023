package com.example.projekat.DTOS;

// DTO za prijem podataka sa Front-enda (Thymeleaf forme) za kreiranje/ažuriranje Grada
public class GradRequest {

    // Jedino polje koje je potrebno uneti za Grad je naziv
    private String naziv;

    // Standardni prazan konstruktor (za Spring/Jackson)
    public GradRequest() {
    }

    // Konstruktor sa poljima
    public GradRequest(String naziv) {
        this.naziv = naziv;
    }

    // Getteri i Setteri

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}