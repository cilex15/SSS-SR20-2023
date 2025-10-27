package com.example.projekat.DTOS;

import com.example.projekat.Model.Deonica;
import java.math.BigDecimal;

// DTO za slanje Front-endu (GET) za Deonicu
public class DeonicaResponse {

    private Long id;
    private String naziv;
    private double duzinaKm;
    private String gradPolazakNaziv;
    private String gradDolazakNaziv;
    private double baznaCena;

    // Standardni konstruktor
    public DeonicaResponse() {}

    // Metoda za konverziju JPA Entiteta u DTO
    public static DeonicaResponse fromEntity(Deonica entity) {
        DeonicaResponse dto = new DeonicaResponse();
        dto.setId(entity.getId());
        dto.setNaziv(entity.getNaziv());
        dto.setDuzinaKm(entity.getDuzinaKm());
        dto.setBaznaCena(entity.getBaznaCena());

        // Proveravamo da li su Gradovi učitani (Potreban FETCH JOIN u Repozitorijumu!)
        if (entity.getGradPolazak() != null) {
            dto.setGradPolazakNaziv(entity.getGradPolazak().getNaziv());
        }
        if (entity.getGradDolazak() != null) {
            dto.setGradDolazakNaziv(entity.getGradDolazak().getNaziv());
        }
        return dto;
    }

    // Getteri i Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public double getDuzinaKm() { return duzinaKm; }
    public void setDuzinaKm(double duzinaKm) { this.duzinaKm = duzinaKm; }
    public String getGradPolazakNaziv() { return gradPolazakNaziv; }
    public void setGradPolazakNaziv(String gradPolazakNaziv) { this.gradPolazakNaziv = gradPolazakNaziv; }
    public String getGradDolazakNaziv() { return gradDolazakNaziv; }
    public void setGradDolazakNaziv(String gradDolazakNaziv) { this.gradDolazakNaziv = gradDolazakNaziv; }
    public double getBaznaCena() { return baznaCena; }
    public void setBaznaCena(double baznaCena) { this.baznaCena = baznaCena; }
}