package com.example.projekat.Model;

import jakarta.persistence.*;
import java.util.List; // Potreban Import za Listu

// Označava klasu kao JPA entitet i mapira je na tabelu "naplatna_stanica"
@Entity
@Table(name = "naplatna_stanica")
public class NaplatnaStanica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatsko generisanje ID-a (auto-increment)
    private Long id;

    @Column(nullable = false) // Polje ne sme biti null u bazi
    private String naziv;

    // JPA Relacija: Mnoge stanice pripadaju jednoj deonici.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deonica_id", nullable = false)
    private Deonica deonica;

    // JPA Relacija: Stanica je povezana sa jednim gradom (lokacija).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grad_id", nullable = true)
    private Grad grad;

    @Column(nullable = false)
    private int brojRampi;

    @Column(nullable = false)
    private boolean rampaPodignuta;

    // 🌟 DODATO: Obrnuta relacija za Zaposlenike (One-to-Many)
    // 'mappedBy' ukazuje na polje 'naplatnaStanica' u entitetu Zaposlenik.
    // FetchType.EAGER se koristi da bi Thymeleaf mogao pristupiti podacima u findAllStanice().
    @OneToMany(mappedBy = "naplatnaStanica", fetch = FetchType.EAGER)
    private List<Zaposlenik> zaposlenici;


    // 1. Standardni (No-args) konstruktor - Obavezan za JPA
    public NaplatnaStanica() {
    }

    // 2. Konstruktor za kreiranje novog entiteta (bez ID-a)
    public NaplatnaStanica(String naziv, Deonica deonica, Grad grad, int brojRampi, boolean rampaPodignuta) {
        this.naziv = naziv;
        this.deonica = deonica;
        this.grad = grad;
        this.brojRampi = brojRampi;
        this.rampaPodignuta = rampaPodignuta;
    }

    // ===================================
    // Getteri i Setteri
    // ===================================

    // ... (Postojeći Getteri i Setteri)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Deonica getDeonica() {
        return deonica;
    }

    public void setDeonica(Deonica deonica) {
        this.deonica = deonica;
    }

    public Grad getGrad() {
        return grad;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    public int getBrojRampi() {
        return brojRampi;
    }

    public void setBrojRampi(int brojRampi) {
        this.brojRampi = brojRampi;
    }

    public boolean isRampaPodignuta() {
        return rampaPodignuta;
    }

    public void setRampaPodignuta(boolean rampaPodignuta) {
        this.rampaPodignuta = rampaPodignuta;
    }

    // 🌟 DODATO: Getteri i Setteri za Zaposlenike
    public List<Zaposlenik> getZaposlenici() {
        return zaposlenici;
    }

    public void setZaposlenici(List<Zaposlenik> zaposlenici) {
        this.zaposlenici = zaposlenici;
    }
}