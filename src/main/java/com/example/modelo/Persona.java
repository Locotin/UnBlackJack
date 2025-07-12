package com.example.modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Persona {
    protected String nombre;
    protected List<Carta> cartas;

    public Persona(String nombre) {
        this.nombre = nombre;
        this.cartas = new ArrayList<>();
    }

    public void recibirCarta(Carta carta) {
        cartas.add(carta);
        // Solo imprimir si la carta no es oculta (no imprimir si el nombre es "Crupier" y la carta es la oculta)
        // La carta oculta se maneja en Crupier, que ya imprime un mensaje especial
        if (!(this instanceof Crupier)) {
            System.out.println(nombre + " recibe carta: " + carta);
        }
    }

    public int calcularPuntos() {
        int puntos = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            puntos += carta.getPuntosBase();
            if (carta.esAs()) {
                ases++;
            }
        }

        // Si hay Ases y el puntaje pasa de 21, contamos algunos As como 1 en vez de 11
        while (puntos > 21 && ases > 0) {
            puntos -= 10;
            ases--;
        }

        return puntos;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public abstract void jugarTurno(Mazo mazo);
} 