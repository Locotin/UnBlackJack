package com.example;

import com.example.vista.BlackjackGUI;
import com.example.controlador.BlackjackControlador;

public class App {
    public static void main(String[] args) {
        System.out.println("DEBUG: Iniciando App.main()");
        javax.swing.SwingUtilities.invokeLater(() -> {
            BlackjackGUI gui = new BlackjackGUI();
            BlackjackControlador controlador = new BlackjackControlador(gui);
            gui.setControlador(controlador);
            gui.registrarListeners();
            gui.setVisible(true);
        });
    }
} 