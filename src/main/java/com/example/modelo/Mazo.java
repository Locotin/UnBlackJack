package com.example.modelo;

import java.util.Vector;
import java.util.Random;

public class Mazo {
    private Vector<Carta> cartas;

    public Mazo() {
        cartas = new Vector<>();
        inicializarMazo();
        barajar();
    }

    private void inicializarMazo() {
        String[] palos = {"Corazones", "Diamantes", "Treboles", "Picas"};
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String palo : palos) {
            for (String valor : valores) {
                int puntos;
                switch (valor) {
                    case "A": puntos = 11; break;
                    case "J":
                    case "Q":
                    case "K": puntos = 10; break;
                    default: puntos = Integer.parseInt(valor);
                }
                cartas.add(new Carta(palo, valor, puntos));
            }
        }
    }

    public void barajar() {
        Random rand = new Random();
        for (int i = 0; i < cartas.size(); i++) {
            int j = rand.nextInt(cartas.size());
            Carta temp = cartas.get(i);
            cartas.set(i, cartas.get(j));
            cartas.set(j, temp);
        }
    }

    public Carta repartirCarta() {
        if (cartas.size() > 0) {
            return cartas.remove(0);
        }
        return null;
    }
} 