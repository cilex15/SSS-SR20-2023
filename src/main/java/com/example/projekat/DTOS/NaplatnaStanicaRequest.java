package com.example.projekat.DTOS;

// DTO za prijem podataka za kreiranje NaplatneStanice
public class NaplatnaStanicaRequest {

    private Long id;
    private String naziv;
    private Long deonicaId; // ID Deonice kojoj pripada
    private Long gradId;    // ID Grada (Opciono)

    // Konstruktori, Getteri i Setteri... (Logika je čista)

    public NaplatnaStanicaRequest() {}

    public NaplatnaStanicaRequest(String naziv, Long deonicaId, Long gradId) {
        this.naziv = naziv;
        this.deonicaId = deonicaId;
        this.gradId = gradId;
    }

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

    public Long getDeonicaId() {
        return deonicaId;
    }

    public void setDeonicaId(Long deonicaId) {
        this.deonicaId = deonicaId;
    }

    public Long getGradId() {
        return gradId;
    }

    public void setGradId(Long gradId) {
        this.gradId = gradId;
    }
}