package com.example.modelo;

public class Crupier extends Persona {
    private Carta cartaOculta;

    public Crupier() {
        super("Crupier");
    }

    public void recibirCartaVisible(Carta carta) {
        System.out.println(nombre + " recibe carta: " + carta);
        recibirCarta(carta);
    }

    public void recibirCartaOculta(Carta carta) {
        cartaOculta = carta;
        cartas.add(carta);  // la carta igual se agrega, solo que no se muestra hasta después
        System.out.println(nombre + " recibe carta: [CARTA OCULTA]");
    }
    
    public void jugarTurno(Mazo mazo) {
        System.out.println("\n--- Turno del Crupier ---");
        System.out.println("La carta oculta era: " + cartaOculta);
        // Imprimir la carta oculta como si la recibiera en este momento
        // (opcional, para coherencia visual)
        // System.out.println(nombre + " recibe carta: " + cartaOculta);

        while (calcularPuntos() < 17) {
            Carta carta = mazo.repartirCarta();
            recibirCarta(carta);
            System.out.println(nombre + " tiene " + calcularPuntos() + " puntos.");
        }

        System.out.println("Crupier termina su turno con " + calcularPuntos() + " puntos.");
    }
} 