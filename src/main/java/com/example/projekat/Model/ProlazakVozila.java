package com.example.projekat.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prolazak_vozila")
public class ProlazakVozila {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registarskaOznaka;
    private String tipVozila;
    private double naplacenaCena;

    // Relacija: Mnogi prolasci se dešavaju na jednoj stanici
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naplatna_stanica_id", nullable = false)
    private NaplatnaStanica naplatnaStanica;

    @Column(name = "datum_i_vreme")
    private LocalDateTime datumIVreme;

    // Konstruktori
    public ProlazakVozila() {
    }

    public ProlazakVozila(String registarskaOznaka, String tipVozila, double naplacenaCena, NaplatnaStanica naplatnaStanica, LocalDateTime datumIVreme) {
        this.registarskaOznaka = registarskaOznaka;
        this.tipVozila = tipVozila;
        this.naplacenaCena = naplacenaCena;
        this.naplatnaStanica = naplatnaStanica;
        this.datumIVreme = datumIVreme;
    }

    // Getteri i Setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistarskaOznaka() {
        return registarskaOznaka;
    }

    public void setRegistarskaOznaka(String registarskaOznaka) {
        this.registarskaOznaka = registarskaOznaka;
    }

    public String getTipVozila() {
        return tipVozila;
    }

    public void setTipVozila(String tipVozila) {
        this.tipVozila = tipVozila;
    }

    public double getNaplacenaCena() {
        return naplacenaCena;
    }

    public void setNaplacenaCena(double naplacenaCena) {
        this.naplacenaCena = naplacenaCena;
    }

    public NaplatnaStanica getNaplatnaStanica() {
        return naplatnaStanica;
    }

    public void setNaplatnaStanica(NaplatnaStanica naplatnaStanica) {
        this.naplatnaStanica = naplatnaStanica;
    }

    public LocalDateTime getDatumIVreme() {
        return datumIVreme;
    }

    public void setDatumIVreme(LocalDateTime datumIVreme) {
        this.datumIVreme = datumIVreme;
    }
}