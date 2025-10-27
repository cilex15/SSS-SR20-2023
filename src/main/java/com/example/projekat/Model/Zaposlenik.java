package com.example.projekat.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "zaposlenik") // Ime tabele u bazi
public class Zaposlenik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    private String pozicija;

    // JPA Relacija: Mnogi zaposleni mogu biti dodeljeni jednoj stanici.
    // Relacija je OPCIONA (nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naplatna_stanica_id", nullable = true)
    private NaplatnaStanica naplatnaStanica;

    // Konstruktori
    public Zaposlenik() {
    }

    public Zaposlenik(String ime, String prezime, String pozicija, NaplatnaStanica naplatnaStanica) {
        this.ime = ime;
        this.prezime = prezime;
        this.pozicija = pozicija;
        this.naplatnaStanica = naplatnaStanica;
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

    public NaplatnaStanica getNaplatnaStanica() {
        return naplatnaStanica;
    }

    public void setNaplatnaStanica(NaplatnaStanica naplatnaStanica) {
        this.naplatnaStanica = naplatnaStanica;
    }
}