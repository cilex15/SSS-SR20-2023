package com.example.projekat.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "grad")
public class Grad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String naziv;

    // Standardni konstruktor
    public Grad() {
    }

    // Konstruktor sa poljem
    public Grad(String naziv) {
        this.naziv = naziv;
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
}