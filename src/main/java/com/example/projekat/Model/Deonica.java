package com.example.projekat.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "deonica")
public class Deonica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Polja iz originalnog modela
    private String naziv;
    private double duzinaKm;

    // Nove JPA relacije
    @ManyToOne
    @JoinColumn(name = "grad_polazak_id", nullable = false)
    private Grad gradPolazak;

    @ManyToOne
    @JoinColumn(name = "grad_dolazak_id", nullable = false)
    private Grad gradDolazak;

    // Dodatno polje za cenu
    @Column(nullable = false)
    private double baznaCena;

    // Standardni konstruktor
    public Deonica() {
    }

    // Konstruktor sa poljima (bez ID-a)
    public Deonica(String naziv, double duzinaKm, Grad gradPolazak, Grad gradDolazak, double baznaCena) {
        this.naziv = naziv;
        this.duzinaKm = duzinaKm;
        this.gradPolazak = gradPolazak;
        this.gradDolazak = gradDolazak;
        this.baznaCena = baznaCena;
    }

    // Getteri i Setteri
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

    public double getDuzinaKm() {
        return duzinaKm;
    }

    public void setDuzinaKm(double duzinaKm) {
        this.duzinaKm = duzinaKm;
    }

    public Grad getGradPolazak() {
        return gradPolazak;
    }

    public void setGradPolazak(Grad gradPolazak) {
        this.gradPolazak = gradPolazak;
    }

    public Grad getGradDolazak() {
        return gradDolazak;
    }

    public void setGradDolazak(Grad gradDolazak) {
        this.gradDolazak = gradDolazak;
    }

    public double getBaznaCena() {
        return baznaCena;
    }

    public void setBaznaCena(double baznaCena) {
        this.baznaCena = baznaCena;
    }
}