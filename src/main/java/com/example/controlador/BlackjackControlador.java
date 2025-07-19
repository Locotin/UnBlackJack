package com.example.controlador;

import com.example.modelo.*;
import com.example.vista.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal del juego de Blackjack.
 * 
 * Esta clase implementa el patrón MVC (Modelo-Vista-Controlador) y actúa como
 * intermediario entre la interfaz gráfica (vista) y la lógica del juego (modelo).
 * 
 * Responsabilidades principales:
 * - Gestionar el estado del juego (turnos, saldo, apuestas)
 * - Coordinar las acciones del jugador y crupier
 * - Actualizar la interfaz gráfica con los cambios del juego
 * - Manejar la lógica de victoria/derrota
 */
public class BlackjackControlador {
    
    // === ATRIBUTOS PRINCIPALES ===
    
    /** Referencia a la interfaz gráfica del juego */
    private BlackjackGUI vista;
    
    /** Mazo de cartas del juego */
    private Mazo mazo;
    
    /** Jugador humano del juego */
    private JugadorApostador jugador;
    
    /** Crupier (dealer) del juego */
    private Crupier crupier;
    
    /** Saldo actual del jugador en dinero */
    private int saldo;
    
    /** Apuesta actual de la ronda */
    private int apuesta;
    
    /** Indica si es el turno del jugador (true) o del crupier (false) */
    private boolean turnoJugador;

    /**
     * Constructor del controlador.
     * 
     * @param vista La interfaz gráfica que se conectará con este controlador
     */
    public BlackjackControlador(BlackjackGUI vista) {
        this.vista = vista;
        this.saldo = 1000;  // Saldo inicial del jugador
        this.apuesta = 100; // Apuesta por defecto (se cambiará en cada partida)
        
        // Inicializar la vista con el saldo inicial
        vista.actualizarSaldo(saldo);
        vista.mostrarMensaje("Haz clic en 'Nueva partida' para comenzar.");
    }

    /**
     * Inicia una nueva partida de Blackjack.
     * 
     * Este método:
     * 1. Verifica que el jugador tenga saldo suficiente
     * 2. Solicita la apuesta al usuario
     * 3. Crea un nuevo mazo y reinicia jugador/crupier
     * 4. Reparte las cartas iniciales
     * 5. Verifica si hay Blackjack natural
     */
    public void nuevaPartida() {
        System.out.println("DEBUG: Se llamó a nuevaPartida()");
        
        // Verificar saldo suficiente
        if (saldo <= 0) {
            vista.mostrarMensajeImportante("No tienes suficiente saldo para apostar. Juego terminado.");
            return;
        }
        
        vista.limpiarMensajes(); // Limpiar mensajes anteriores
        
        // === SOLICITAR APUESTA AL USUARIO ===
        String apuestaStr = javax.swing.JOptionPane.showInputDialog(
            vista, 
            "¿Cuánto deseas apostar? (Saldo: $" + saldo + ")"
        );
        
        if (apuestaStr == null) return; // Usuario canceló
        
        // Validar que la apuesta sea un número válido
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
        
        // === INICIALIZAR NUEVA PARTIDA ===
        mazo = new Mazo();                    // Crear nuevo mazo mezclado
        jugador = new JugadorApostador("Jugador", saldo);  // Crear jugador
        crupier = new Crupier();              // Crear crupier
        
        saldo -= apuesta;  // Descontar apuesta del saldo
        vista.actualizarSaldo(saldo);
        
        // Mostrar información de la nueva partida
        vista.mostrarMensaje("\n=== NUEVA PARTIDA ===");
        vista.mostrarMensaje("Apuesta: $" + apuesta);
        
        // Limpiar cartas anteriores
        jugador.getCartas().clear();
        crupier.getCartas().clear();
        
        // === REPARTIR CARTAS INICIALES ===
        // Orden importante: jugador, crupier oculta, jugador, crupier visible
        jugador.recibirCarta(mazo.repartirCarta());           // 1ra carta jugador
        crupier.recibirCartaOculta(mazo.repartirCarta());     // 1ra carta crupier (oculta)
        jugador.recibirCarta(mazo.repartirCarta());           // 2da carta jugador
        crupier.recibirCartaVisible(mazo.repartirCarta());    // 2da carta crupier (visible)
        
        turnoJugador = true;  // Comenzar con el turno del jugador
        
        // Mostrar cartas en la interfaz
        mostrarCartas();
        
        // Verificar Blackjack natural (21 puntos con 2 cartas)
        if (jugador.calcularPuntos() == 21) {
            vista.mostrarMensaje("¡BLACKJACK NATURAL!");
            terminarRonda(true);
        } else {
            vista.mostrarMensaje("Tu turno. Elige: Pedir carta o Plantarse.");
        }
    }

    /**
     * Acción del jugador para pedir una carta adicional.
     * 
     * Este método:
     * 1. Verifica que sea el turno del jugador
     * 2. Reparte una carta al jugador
     * 3. Verifica si se pasó de 21 o llegó exactamente a 21
     * 4. Si llega a 21, automáticamente planta
     */
    public void pedirCarta() {
        System.out.println("DEBUG: Se llamó a pedirCarta()");
        
        if (!turnoJugador) return;  // No es turno del jugador
        
        // Repartir carta al jugador
        jugador.recibirCarta(mazo.repartirCarta());
        mostrarCartas();  // Actualizar vista
        
        int puntos = jugador.calcularPuntos();
        
        // Verificar resultado de pedir carta
        if (puntos > 21) {
            vista.mostrarMensajeImportante("Te pasaste de 21. Pierdes la ronda.");
            terminarRonda(false);
        } else if (puntos == 21) {
            vista.mostrarMensaje("¡Tienes 21!");
            plantarse();  // Automáticamente planta al llegar a 21
        }
    }

    /**
     * Acción del jugador para terminar su turno y pasar al crupier.
     * 
     * Este método:
     * 1. Verifica que sea el turno del jugador
     * 2. Cambia el turno al crupier
     * 3. Ejecuta la lógica del crupier
     * 4. Compara puntajes y determina el ganador
     * 5. Actualiza el saldo según el resultado
     */
    public void plantarse() {
        System.out.println("DEBUG: Se llamó a plantarse()");
        
        if (!turnoJugador) return;  // No es turno del jugador
        
        turnoJugador = false;  // Cambiar turno al crupier
        
        vista.mostrarMensaje("\n--- Turno del Crupier ---");
        
        // El crupier juega automáticamente según las reglas
        crupier.jugarTurno(mazo);
        
        // Mostrar todas las cartas (incluyendo la que estaba oculta)
        mostrarCartas();
        
        // === DETERMINAR GANADOR ===
        int puntosJugador = jugador.calcularPuntos();
        int puntosCrupier = crupier.calcularPuntos();
        
        if (puntosCrupier > 21 || puntosJugador > puntosCrupier) {
            // Jugador gana: crupier se pasó o jugador tiene más puntos
            vista.mostrarMensajeImportante("¡Ganaste la ronda!");
            saldo += apuesta * 2;  // Recupera apuesta + gana apuesta
        } else if (puntosJugador == puntosCrupier) {
            // Empate: jugador recupera su apuesta
            vista.mostrarMensajeImportante("Empate. Recuperas tu apuesta.");
            saldo += apuesta;
        } else {
            // Crupier gana: jugador pierde la apuesta
            vista.mostrarMensajeImportante("El crupier gana la ronda.");
        }
        
        vista.actualizarSaldo(saldo);
        terminarRonda(false);
    }

    /**
     * Finaliza la ronda actual y prepara para la siguiente.
     * 
     * @param blackjackNatural true si el jugador ganó con Blackjack natural
     */
    private void terminarRonda(boolean blackjackNatural) {
        turnoJugador = false;  // Asegurar que no sea turno del jugador
        
        if (blackjackNatural) {
            // Premio especial por Blackjack natural (1.5 veces la apuesta)
            vista.mostrarMensajeImportante("¡Ganaste con Blackjack! Premio especial.");
            saldo += apuesta * 2 + apuesta / 2;  // Apuesta + apuesta + mitad de apuesta
            vista.actualizarSaldo(saldo);
        }
        
        vista.mostrarMensaje("Haz clic en 'Nueva partida' para jugar otra ronda.");
    }

    /**
     * Actualiza la vista con las cartas actuales del jugador y crupier.
     * 
     * Este método es crucial para mantener sincronizada la interfaz gráfica
     * con el estado actual del juego. Maneja la lógica de mostrar cartas
     * ocultas del crupier durante el turno del jugador.
     */
    private void mostrarCartas() {
        // === MOSTRAR CARTAS DEL JUGADOR ===
        List<String> cartasJugador = new ArrayList<>();
        for (Carta c : jugador.getCartas()) {
            cartasJugador.add(nombreArchivoCarta(c));
        }
        vista.mostrarCartasJugador(cartasJugador);
        vista.actualizarPuntajeJugador(jugador.calcularPuntos());

        // === MOSTRAR CARTAS DEL CRUPIER ===
        List<String> cartasCrupier = new ArrayList<>();
        
        if (crupier.getCartas().size() >= 2 && turnoJugador) {
            // Durante el turno del jugador: mostrar primera carta como reverso
            cartasCrupier.add("BACK.png");  // Imagen del reverso de la carta
            cartasCrupier.add(nombreArchivoCarta(crupier.getCartas().get(1)));  // Segunda carta visible
        } else {
            // Fin de ronda: mostrar todas las cartas del crupier
            for (Carta c : crupier.getCartas()) {
                cartasCrupier.add(nombreArchivoCarta(c));
            }
        }
        vista.mostrarCartasCrupier(cartasCrupier, false);

        // === MOSTRAR PUNTAJE DEL CRUPIER ===
        if (turnoJugador && crupier.getCartas().size() >= 2) {
            // Durante turno del jugador: mostrar solo el valor de la carta visible
            int valorVisible = crupier.getCartas().get(1).getPuntosBase();
            vista.actualizarPuntajeCrupier("? + " + valorVisible);
        } else {
            // Fin de ronda: mostrar puntaje total real
            vista.actualizarPuntajeCrupier(String.valueOf(crupier.calcularPuntos()));
        }
    }

    /**
     * Genera el nombre del archivo de imagen para una carta específica.
     * 
     * Convierte una carta en el nombre de su archivo de imagen correspondiente.
     * Formato: "VALOR-PALO.png"
     * Ejemplos: "A-H.png" (As de corazones), "10-S.png" (10 de picas)
     * 
     * @param carta La carta para la cual generar el nombre del archivo
     * @return El nombre del archivo de imagen de la carta
     */
    private String nombreArchivoCarta(Carta carta) {
        String valor = carta.getValor();      // "A", "2", "3", ..., "10", "J", "Q", "K"
        String paloLetra = carta.getPaloLetra(); // "H" (hearts), "D" (diamonds), "C" (clubs), "S" (spades)
        return valor + "-" + paloLetra + ".png";
    }
} 