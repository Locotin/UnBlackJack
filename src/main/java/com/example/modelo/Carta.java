package com.example.modelo;

public class Carta {
    private String palo;
    private String valor;
    private int puntos;

    public Carta(String palo, String valor, int puntos) {
        this.palo = palo;
        this.valor = valor;
        this.puntos = puntos;
    }

    public int getPuntosBase() {
        return puntos;
    }

    public boolean esAs() {
        return valor.equals("A");
    }

    public String getValor() {
        return valor;
    }

    public String getPaloLetra() {
        switch (palo) {
            case "Picas": return "S";
            case "Corazones": return "H";
            case "Diamantes": return "D";
            case "Treboles": return "C";
            default: return "?";
        }
    }

    public String toString() {
        return valor + " de " + palo;
    }
} 