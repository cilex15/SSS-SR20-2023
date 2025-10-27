package com.example.projekat.DTOS;

import java.math.BigDecimal;

public class DeonicaRequest {

    private Long id;
    private String naziv;
    private double duzinaKm;
    private Long gradPolazakId;
    private Long gradDolazakId;
    private double baznaCena;


    public DeonicaRequest() {}

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

    public Long getGradPolazakId() {
        return gradPolazakId;
    }

    public void setGradPolazakId(Long gradPolazakId) {
        this.gradPolazakId = gradPolazakId;
    }

    public Long getGradDolazakId() {
        return gradDolazakId;
    }

    public void setGradDolazakId(Long gradDolazakId) {
        this.gradDolazakId = gradDolazakId;
    }

    public double getBaznaCena() {
        return baznaCena;
    }

    public void setBaznaCena(double baznaCena) {
        this.baznaCena = baznaCena;
    }
}