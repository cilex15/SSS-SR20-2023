package com.example.projekat.Model;
// Ili u novom paketu, zavisno od Vaše strukture

public enum TipVozila {
    // TIP(koeficijent)
    AUTOMOBIL(1.0), // Ili 1.0 ako je ovo osnovna klasa za cenu
    KAMION(2.0),
    AUTOBUS(1.5); // Primer

    private final double koeficijent;

    TipVozila(double koeficijent) {
        this.koeficijent = koeficijent;
    }

    public double getKoeficijent() {
        return koeficijent;
    }
}