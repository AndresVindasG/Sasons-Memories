package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Pantalla Swing de bienvenida al juego, dibujada con componentes nítidos. */
public class PanelBienvenidaJuego extends JPanel {
    private final JButton botonContinuar = EstiloInterfaz.boton(
            "Metámosle mano!", EstiloInterfaz.AZUL, 300, 68, 20);

    public PanelBienvenidaJuego() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 20, 26, 20));
        add(Box.createVerticalGlue());

        JLabel logo = EstiloInterfaz.logo(360, 330);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logo);
        add(Box.createVerticalStrut(12));

        JLabel titulo = EstiloInterfaz.titulo("Sason’s Memories", 48);
        titulo.setForeground(new Color(174, 124, 91));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(42));

        botonContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(botonContinuar);
        add(Box.createVerticalGlue());
    }

    public void accionContinuar(Runnable accion) {
        botonContinuar.addActionListener(evento -> accion.run());
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D lienzo = (Graphics2D) graficos.create();
        lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        lienzo.setColor(EstiloInterfaz.FONDO);
        lienzo.fillRect(0, 0, getWidth(), getHeight());
        lienzo.setColor(EstiloInterfaz.AZUL);
        int anchoFranja = Math.max(26, getWidth() / 48);
        int separacionVertical = Math.max(50, getWidth() / 24);
        for (int x = 28; x < getWidth(); x += separacionVertical) {
            lienzo.fillRect(x, 0, anchoFranja, getHeight());
        }
        int altoFranja = Math.max(24, getHeight() / 24);
        int separacionHorizontal = Math.max(52, getHeight() / 18);
        for (int y = 36; y < getHeight(); y += separacionHorizontal) {
            lienzo.fillRect(0, y, getWidth(), altoFranja);
        }
        lienzo.dispose();
    }
}
