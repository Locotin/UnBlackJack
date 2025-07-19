package com.example.vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import com.example.controlador.BlackjackControlador;

/**
 * Interfaz gráfica del juego de Blackjack.
 * 
 * Esta clase implementa la vista del patrón MVC y proporciona una interfaz
 * gráfica inspirada en una mesa de casino de Blackjack. Utiliza Java Swing
 * para crear una ventana con:
 * - Mesa verde tipo casino
 * - Áreas separadas para cartas del crupier y jugador
 * - Panel de puntajes
 * - Área de mensajes
 * - Botones de control del juego
 * 
 * Responsabilidades principales:
 * - Mostrar visualmente el estado del juego
 * - Proporcionar controles para las acciones del jugador
 * - Cargar y mostrar imágenes de cartas
 * - Mantener la interfaz actualizada con el estado del juego
 */
public class BlackjackGUI extends JFrame {
    
    // === COMPONENTES DE INFORMACIÓN ===
    
    /** Etiqueta que muestra el saldo actual del jugador */
    private JLabel saldoLabel;
    
    /** Etiqueta que muestra el puntaje actual del jugador */
    private JLabel puntajeJugadorLabel;
    
    /** Etiqueta que muestra el puntaje del crupier (puede ser "?" durante el juego) */
    private JLabel puntajeCrupierLabel;
    
    /** Área de texto donde se muestran los mensajes del juego */
    private JTextArea mensajesArea;
    
    // === BOTONES DE CONTROL ===
    
    /** Botón para pedir una carta adicional */
    private JButton pedirCartaBtn;
    
    /** Botón para terminar el turno del jugador */
    private JButton plantarseBtn;
    
    /** Botón para iniciar una nueva partida */
    private JButton nuevaPartidaBtn;
    
    // === REFERENCIA AL CONTROLADOR ===
    
    /** Referencia al controlador que maneja la lógica del juego */
    private BlackjackControlador controlador;
    
    // === PANELES DE LA MESA ===
    
    /** Panel principal que simula la mesa de casino (fondo verde) */
    private JPanel mesaPanel;
    
    /** Panel donde se muestran las cartas del crupier */
    private JPanel crupierPanel;
    
    /** Panel donde se muestran las cartas del jugador */
    private JPanel jugadorPanel;

    /**
     * Constructor de la interfaz gráfica.
     * 
     * Inicializa todos los componentes de la interfaz:
     * - Configura la ventana principal
     * - Crea los paneles de la mesa
     * - Configura las etiquetas de información
     * - Crea los botones de control
     * - Organiza el layout de todos los elementos
     */
    public BlackjackGUI() {
        System.out.println("DEBUG: Se creó la ventana BlackjackGUI");
        
        // === CONFIGURACIÓN DE LA VENTANA PRINCIPAL ===
        setTitle("UnBlackJack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);  // Centrar en pantalla
        setLayout(new BorderLayout());

        // === ETIQUETA DE SALDO (PARTE SUPERIOR) ===
        saldoLabel = new JLabel("Saldo: $1000");
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(saldoLabel, BorderLayout.NORTH);

        // === PANEL PRINCIPAL DE LA MESA ===
        mesaPanel = new JPanel();
        mesaPanel.setBackground(new Color(0, 80, 0));  // Verde oscuro tipo casino
        mesaPanel.setLayout(new BoxLayout(mesaPanel, BoxLayout.Y_AXIS));
        add(mesaPanel, BorderLayout.CENTER);

        // === PANEL DE PUNTAJES ===
        JPanel puntajesPanel = new JPanel();
        puntajesPanel.setOpaque(false);  // Fondo transparente
        
        puntajeCrupierLabel = new JLabel("Crupier: ?");
        puntajeCrupierLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntajeCrupierLabel.setForeground(Color.WHITE);  // Texto blanco para contraste
        
        puntajeJugadorLabel = new JLabel("Jugador: ?");
        puntajeJugadorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntajeJugadorLabel.setForeground(Color.WHITE);
        
        puntajesPanel.add(puntajeCrupierLabel);
        puntajesPanel.add(Box.createHorizontalStrut(40));  // Espacio entre etiquetas
        puntajesPanel.add(puntajeJugadorLabel);
        
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(puntajesPanel);

        // === PANEL DE CARTAS DEL CRUPIER ===
        crupierPanel = new JPanel();
        crupierPanel.setOpaque(false);
        crupierPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(crupierPanel);

        // Espacio entre crupier y jugador
        mesaPanel.add(Box.createVerticalStrut(40));

        // === PANEL DE CARTAS DEL JUGADOR ===
        jugadorPanel = new JPanel();
        jugadorPanel.setOpaque(false);
        jugadorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mesaPanel.add(jugadorPanel);

        // === ÁREA DE MENSAJES ===
        mensajesArea = new JTextArea(3, 40);
        mensajesArea.setEditable(false);  // Solo lectura
        mensajesArea.setLineWrap(true);   // Ajuste de línea automático
        mensajesArea.setWrapStyleWord(true);
        mensajesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mensajesArea.setBackground(new Color(0, 80, 0));  // Mismo verde que la mesa
        mensajesArea.setForeground(Color.WHITE);
        
        JScrollPane mensajesScroll = new JScrollPane(mensajesArea);
        mensajesScroll.setBorder(null);
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(mensajesScroll);

        // === PANEL DE BOTONES (PARTE INFERIOR) ===
        JPanel botonesPanel = new JPanel();
        pedirCartaBtn = new JButton("Pedir carta");
        plantarseBtn = new JButton("Plantarse");
        nuevaPartidaBtn = new JButton("Nueva partida");
        
        botonesPanel.add(pedirCartaBtn);
        botonesPanel.add(plantarseBtn);
        botonesPanel.add(nuevaPartidaBtn);
        add(botonesPanel, BorderLayout.SOUTH);
    }

    /**
     * Establece la referencia al controlador.
     * 
     * @param controlador El controlador que manejará la lógica del juego
     */
    public void setControlador(BlackjackControlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Registra los listeners de eventos para los botones.
     * 
     * Este método conecta los botones de la interfaz con los métodos
     * correspondientes del controlador. Se debe llamar DESPUÉS de
     * establecer el controlador para evitar errores de null pointer.
     */
    public void registrarListeners() {
        pedirCartaBtn.addActionListener(e -> {
            if (controlador != null) controlador.pedirCarta();
        });
        plantarseBtn.addActionListener(e -> {
            if (controlador != null) controlador.plantarse();
        });
        nuevaPartidaBtn.addActionListener(e -> {
            if (controlador != null) controlador.nuevaPartida();
        });
    }

    /**
     * Muestra un mensaje normal en el área de texto fija.
     * 
     * @param mensaje El mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        mensajesArea.append(mensaje + "\n");
        mensajesArea.setCaretPosition(mensajesArea.getDocument().getLength());  // Auto-scroll al final
    }

    /**
     * Muestra un mensaje importante como ventana emergente.
     * 
     * Se usa para mensajes críticos como resultados de partida,
     * errores importantes, etc.
     * 
     * @param mensaje El mensaje importante a mostrar
     */
    public void mostrarMensajeImportante(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Actualiza la etiqueta del saldo con el nuevo valor.
     * 
     * @param saldo El nuevo saldo del jugador
     */
    public void actualizarSaldo(int saldo) {
        saldoLabel.setText("Saldo: $" + saldo);
    }

    /**
     * Limpia todos los mensajes del área de texto.
     */
    public void limpiarMensajes() {
        mensajesArea.setText("");
    }

    /**
     * Actualiza la etiqueta del puntaje del jugador.
     * 
     * @param puntos Los puntos actuales del jugador
     */
    public void actualizarPuntajeJugador(int puntos) {
        puntajeJugadorLabel.setText("Jugador: " + puntos);
    }

    /**
     * Actualiza la etiqueta del puntaje del crupier.
     * 
     * Durante el juego puede mostrar "?" para la carta oculta,
     * y al final muestra el puntaje real.
     * 
     * @param texto El texto a mostrar (puede ser número o "? + X")
     */
    public void actualizarPuntajeCrupier(String texto) {
        puntajeCrupierLabel.setText("Crupier: " + texto);
    }

    /**
     * Muestra las cartas del crupier en su panel correspondiente.
     * 
     * @param nombresArchivos Lista de nombres de archivos de las cartas
     * @param ocultarPrimera Si es true, la primera carta se muestra como reverso
     */
    public void mostrarCartasCrupier(List<String> nombresArchivos, boolean ocultarPrimera) {
        crupierPanel.removeAll();  // Limpiar cartas anteriores
        
        for (int i = 0; i < nombresArchivos.size(); i++) {
            String nombre = nombresArchivos.get(i);
            JLabel carta;
            
            if (i == 0 && ocultarPrimera) {
                // Mostrar reverso de carta para la primera carta oculta
                carta = crearLabelCarta("BACK.png");
            } else {
                // Mostrar carta real
                carta = crearLabelCarta(nombre);
            }
            crupierPanel.add(carta);
        }
        
        // Actualizar la visualización
        crupierPanel.revalidate();
        crupierPanel.repaint();
    }

    /**
     * Muestra las cartas del jugador en su panel correspondiente.
     * 
     * @param nombresArchivos Lista de nombres de archivos de las cartas del jugador
     */
    public void mostrarCartasJugador(List<String> nombresArchivos) {
        jugadorPanel.removeAll();  // Limpiar cartas anteriores
        
        for (String nombre : nombresArchivos) {
            JLabel carta = crearLabelCarta(nombre);
            jugadorPanel.add(carta);
        }
        
        // Actualizar la visualización
        jugadorPanel.revalidate();
        jugadorPanel.repaint();
    }

    /**
     * Crea un JLabel con la imagen de una carta.
     * 
     * Este método:
     * 1. Busca la imagen en la carpeta de recursos
     * 2. Escala la imagen al tamaño apropiado (90x130 píxeles)
     * 3. Crea un JLabel con la imagen
     * 4. Si no encuentra la imagen, muestra "?" como fallback
     * 
     * @param nombreArchivo El nombre del archivo de imagen de la carta
     * @return Un JLabel con la imagen de la carta
     */
    private JLabel crearLabelCarta(String nombreArchivo) {
        try {
            // Buscar la imagen en la carpeta de recursos
            java.net.URL url = getClass().getResource("/cartas/" + nombreArchivo);
            
            if (url == null) {
                System.out.println("DEBUG: No se encontró la imagen de la carta: " + nombreArchivo);
                return new JLabel("?");  // Fallback si no encuentra la imagen
            }
            
            // Cargar y escalar la imagen
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(90, 130, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
            
        } catch (Exception e) {
            System.out.println("DEBUG: Error cargando la imagen de la carta: " + nombreArchivo);
            return new JLabel("?");  // Fallback en caso de error
        }
    }
} 