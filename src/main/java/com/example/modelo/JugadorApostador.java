package com.example.modelo;

import java.util.Scanner;

public class JugadorApostador extends Persona {
    private int dinero;

    public JugadorApostador(String nombre, int dinero) {
        super(nombre);
        this.dinero = dinero;
    }

    public void jugarTurno(Mazo mazo) {
        Scanner scanner = new Scanner(System.in);
        String respuesta;

        do {
            System.out.println(nombre + " tienes " + calcularPuntos() + " puntos.");
            System.out.println("Deseas otra carta? (s para si / cualquier otra para plantarte):");
            respuesta = scanner.nextLine();

            if (respuesta.equalsIgnoreCase("s")) {
                Carta carta = mazo.repartirCarta();
                recibirCarta(carta);
            }

            if (calcularPuntos() >= 21) {
                break;
            }

        } while (respuesta.equalsIgnoreCase("s"));

        System.out.println(nombre + " termina su turno con " + calcularPuntos() + " puntos.");
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void ajustarDinero(int cantidad) {
        dinero += cantidad;
    }
} 