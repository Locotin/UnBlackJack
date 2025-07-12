package com.example.vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import com.example.controlador.BlackjackControlador;

public class BlackjackGUI extends JFrame {
    private JLabel saldoLabel;
    private JLabel puntajeJugadorLabel;
    private JLabel puntajeCrupierLabel;
    private JTextArea mensajesArea;
    private JButton pedirCartaBtn;
    private JButton plantarseBtn;
    private JButton nuevaPartidaBtn;
    private BlackjackControlador controlador;

    private JPanel mesaPanel;
    private JPanel crupierPanel;
    private JPanel jugadorPanel;

    public BlackjackGUI() {
        System.out.println("DEBUG: Se creó la ventana BlackjackGUI");
        setTitle("UnBlackJack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        saldoLabel = new JLabel("Saldo: $1000");
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(saldoLabel, BorderLayout.NORTH);

        // Panel principal tipo mesa (fondo verde)
        mesaPanel = new JPanel();
        mesaPanel.setBackground(new Color(0, 80, 0));
        mesaPanel.setLayout(new BoxLayout(mesaPanel, BoxLayout.Y_AXIS));
        add(mesaPanel, BorderLayout.CENTER);

        // Panel de puntajes
        JPanel puntajesPanel = new JPanel();
        puntajesPanel.setOpaque(false);
        puntajeCrupierLabel = new JLabel("Crupier: ?");
        puntajeCrupierLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntajeCrupierLabel.setForeground(Color.WHITE);
        puntajeJugadorLabel = new JLabel("Jugador: ?");
        puntajeJugadorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntajeJugadorLabel.setForeground(Color.WHITE);
        puntajesPanel.add(puntajeCrupierLabel);
        puntajesPanel.add(Box.createHorizontalStrut(40));
        puntajesPanel.add(puntajeJugadorLabel);
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(puntajesPanel);

        // Panel de cartas del crupier
        crupierPanel = new JPanel();
        crupierPanel.setOpaque(false);
        crupierPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(crupierPanel);

        // Espacio entre crupier y jugador
        mesaPanel.add(Box.createVerticalStrut(40));

        // Panel de cartas del jugador
        jugadorPanel = new JPanel();
        jugadorPanel.setOpaque(false);
        jugadorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mesaPanel.add(jugadorPanel);

        // Área de mensajes fija
        mensajesArea = new JTextArea(3, 40);
        mensajesArea.setEditable(false);
        mensajesArea.setLineWrap(true);
        mensajesArea.setWrapStyleWord(true);
        mensajesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        mensajesArea.setBackground(new Color(0, 80, 0));
        mensajesArea.setForeground(Color.WHITE);
        JScrollPane mensajesScroll = new JScrollPane(mensajesArea);
        mensajesScroll.setBorder(null);
        mesaPanel.add(Box.createVerticalStrut(10));
        mesaPanel.add(mensajesScroll);

        // Panel de botones
        JPanel botonesPanel = new JPanel();
        pedirCartaBtn = new JButton("Pedir carta");
        plantarseBtn = new JButton("Plantarse");
        nuevaPartidaBtn = new JButton("Nueva partida");
        botonesPanel.add(pedirCartaBtn);
        botonesPanel.add(plantarseBtn);
        botonesPanel.add(nuevaPartidaBtn);
        add(botonesPanel, BorderLayout.SOUTH);
    }

    public void setControlador(BlackjackControlador controlador) {
        this.controlador = controlador;
    }

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

    // Mensajes normales en el área fija
    public void mostrarMensaje(String mensaje) {
        mensajesArea.append(mensaje + "\n");
        mensajesArea.setCaretPosition(mensajesArea.getDocument().getLength());
    }

    // Mensajes importantes como ventana emergente
    public void mostrarMensajeImportante(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void actualizarSaldo(int saldo) {
        saldoLabel.setText("Saldo: $" + saldo);
    }

    public void limpiarMensajes() {
        mensajesArea.setText("");
    }

    public void actualizarPuntajeJugador(int puntos) {
        puntajeJugadorLabel.setText("Jugador: " + puntos);
    }

    public void actualizarPuntajeCrupier(String texto) {
        puntajeCrupierLabel.setText("Crupier: " + texto);
    }

    public void mostrarCartasCrupier(List<String> nombresArchivos, boolean ocultarPrimera) {
        crupierPanel.removeAll();
        for (int i = 0; i < nombresArchivos.size(); i++) {
            String nombre = nombresArchivos.get(i);
            JLabel carta;
            if (i == 0 && ocultarPrimera) {
                carta = crearLabelCarta("BACK.png");
            } else {
                carta = crearLabelCarta(nombre);
            }
            crupierPanel.add(carta);
        }
        crupierPanel.revalidate();
        crupierPanel.repaint();
    }

    public void mostrarCartasJugador(List<String> nombresArchivos) {
        jugadorPanel.removeAll();
        for (String nombre : nombresArchivos) {
            JLabel carta = crearLabelCarta(nombre);
            jugadorPanel.add(carta);
        }
        jugadorPanel.revalidate();
        jugadorPanel.repaint();
    }

    private JLabel crearLabelCarta(String nombreArchivo) {
        try {
            java.net.URL url = getClass().getResource("/cartas/" + nombreArchivo);
            if (url == null) {
                System.out.println("DEBUG: No se encontró la imagen de la carta: " + nombreArchivo);
                return new JLabel("?");
            }
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(90, 130, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            // ya la encuentra en la carpeta cartas
            System.out.println("DEBUG: Error cargando la imagen de la carta: " + nombreArchivo);
            return new JLabel("?");
        }
    }
} 