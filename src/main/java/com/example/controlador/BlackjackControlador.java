package com.example.controlador;

import com.example.modelo.*;
import com.example.vista.*;
import java.util.List;
import java.util.ArrayList;

public class BlackjackControlador {
    private BlackjackGUI vista;
    private Mazo mazo;
    private JugadorApostador jugador;
    private Crupier crupier;
    private int saldo;
    private int apuesta;
    private boolean turnoJugador;

    public BlackjackControlador(BlackjackGUI vista) {
        this.vista = vista;
        this.saldo = 1000;
        this.apuesta = 100; // Por ahora apuesta fija
        vista.actualizarSaldo(saldo);
        vista.mostrarMensaje("Haz clic en 'Nueva partida' para comenzar.");
    }

    public void nuevaPartida() {
        System.out.println("DEBUG: Se llamó a nuevaPartida()");
        if (saldo <= 0) {
            vista.mostrarMensajeImportante("No tienes suficiente saldo para apostar. Juego terminado.");
            return;
        }
        vista.limpiarMensajes(); // Limpiar mensajes antes de iniciar
        // Pedir apuesta al usuario
        String apuestaStr = javax.swing.JOptionPane.showInputDialog(vista, "¿Cuánto deseas apostar? (Saldo: $" + saldo + ")");
        if (apuestaStr == null) return; // Cancelado
        try {
            apuesta = Integer.parseInt(apuestaStr);
            if (apuesta <= 0 || apuesta > saldo) {
                vista.mostrarMensaje("Apuesta inválida. Intenta de nuevo.");
                return;
            }
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Apuesta inválida. Intenta de nuevo.");
            return;
        }
        mazo = new Mazo();
        jugador = new JugadorApostador("Jugador", saldo);
        crupier = new Crupier();
        saldo -= apuesta;
        vista.actualizarSaldo(saldo);
        vista.mostrarMensaje("\n=== NUEVA PARTIDA ===");
        vista.mostrarMensaje("Apuesta: $" + apuesta);
        jugador.getCartas().clear();
        crupier.getCartas().clear();
        // Repartir cartas iniciales
        jugador.recibirCarta(mazo.repartirCarta());
        crupier.recibirCartaOculta(mazo.repartirCarta()); // PRIMERO la oculta
        jugador.recibirCarta(mazo.repartirCarta());
        crupier.recibirCartaVisible(mazo.repartirCarta()); // LUEGO la visible
        turnoJugador = true;
        mostrarCartas();
        if (jugador.calcularPuntos() == 21) {
            vista.mostrarMensaje("¡BLACKJACK NATURAL!");
            terminarRonda(true);
        } else {
            vista.mostrarMensaje("Tu turno. Elige: Pedir carta o Plantarse.");
        }
    }

    public void pedirCarta() {
        System.out.println("DEBUG: Se llamó a pedirCarta()");
        if (!turnoJugador) return;
        jugador.recibirCarta(mazo.repartirCarta());
        mostrarCartas();
        int puntos = jugador.calcularPuntos();
        if (puntos > 21) {
            vista.mostrarMensajeImportante("Te pasaste de 21. Pierdes la ronda.");
            terminarRonda(false);
        } else if (puntos == 21) {
            vista.mostrarMensaje("¡Tienes 21!");
            plantarse();
        }
    }

    public void plantarse() {
        System.out.println("DEBUG: Se llamó a plantarse()");
        if (!turnoJugador) return;
        turnoJugador = false;
        vista.mostrarMensaje("\n--- Turno del Crupier ---");
        crupier.jugarTurno(mazo);
        mostrarCartas(); // Mostrar todas las cartas y puntaje real del crupier
        int puntosJugador = jugador.calcularPuntos();
        int puntosCrupier = crupier.calcularPuntos();
        if (puntosCrupier > 21 || puntosJugador > puntosCrupier) {
            vista.mostrarMensajeImportante("¡Ganaste la ronda!");
            saldo += apuesta * 2;
        } else if (puntosJugador == puntosCrupier) {
            vista.mostrarMensajeImportante("Empate. Recuperas tu apuesta.");
            saldo += apuesta;
        } else {
            vista.mostrarMensajeImportante("El crupier gana la ronda.");
        }
        vista.actualizarSaldo(saldo);
        terminarRonda(false);
    }

    private void terminarRonda(boolean blackjackNatural) {
        turnoJugador = false;
        if (blackjackNatural) {
            vista.mostrarMensajeImportante("¡Ganaste con Blackjack! Premio especial.");
            saldo += apuesta * 2 + apuesta / 2;
            vista.actualizarSaldo(saldo);
        }
        vista.mostrarMensaje("Haz clic en 'Nueva partida' para jugar otra ronda.");
    }

    private void mostrarCartas() {
        // Mostrar cartas del jugador (todas)
        List<String> cartasJugador = new ArrayList<>();
        for (Carta c : jugador.getCartas()) {
            cartasJugador.add(nombreArchivoCarta(c));
        }
        vista.mostrarCartasJugador(cartasJugador);
        vista.actualizarPuntajeJugador(jugador.calcularPuntos());

        // Mostrar cartas del crupier
        List<String> cartasCrupier = new ArrayList<>();
        if (crupier.getCartas().size() >= 2 && turnoJugador) {
            // Mostrar la primera como reverso y la segunda como real
            cartasCrupier.add("BACK.png");
            cartasCrupier.add(nombreArchivoCarta(crupier.getCartas().get(1)));
        } else {
            // Mostrar todas las cartas reales (fin de ronda)
            for (Carta c : crupier.getCartas()) {
                cartasCrupier.add(nombreArchivoCarta(c));
            }
        }
        vista.mostrarCartasCrupier(cartasCrupier, false);

        // Mostrar puntaje del crupier
        if (turnoJugador && crupier.getCartas().size() >= 2) {
            int valorVisible = crupier.getCartas().get(1).getPuntosBase();
            vista.actualizarPuntajeCrupier("? + " + valorVisible);
        } else {
            vista.actualizarPuntajeCrupier(String.valueOf(crupier.calcularPuntos()));
        }
    }

    // Utilidad para obtener el nombre de archivo de una carta
    private String nombreArchivoCarta(Carta carta) {
        String valor = carta.getValor();
        String paloLetra = carta.getPaloLetra();
        return valor + "-" + paloLetra + ".png";
    }
} 