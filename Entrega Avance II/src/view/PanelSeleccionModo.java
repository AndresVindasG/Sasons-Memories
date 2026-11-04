package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Selector funcional de los modos Patrones, Memoria y premios. */
public class PanelSeleccionModo extends JPanel {
    private final JButton botonPatrones = EstiloInterfaz.boton(
            "Patrones", EstiloInterfaz.AZUL, 270, 70, 20);
    private final JButton botonMemoria = EstiloInterfaz.boton(
            "Memoria", EstiloInterfaz.AZUL, 270, 70, 20);
    private final JButton botonPremios = EstiloInterfaz.boton(
            "Ver premios", EstiloInterfaz.GRIS, 270, 64, 18);

    public PanelSeleccionModo() {
        super(new BorderLayout(0, 0));
        setBackground(EstiloInterfaz.FONDO);

        JPanel seleccion = new JPanel();
        seleccion.setOpaque(false);
        seleccion.setLayout(new BoxLayout(seleccion, BoxLayout.Y_AXIS));
        seleccion.setBorder(new EmptyBorder(48, 20, 30, 20));
        JLabel titulo = EstiloInterfaz.titulo("Elige el modo de juego", 38);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        seleccion.add(Box.createVerticalGlue());
        seleccion.add(titulo);
        seleccion.add(Box.createVerticalStrut(36));
        botonPatrones.setAlignmentX(Component.CENTER_ALIGNMENT);
        seleccion.add(botonPatrones);
        seleccion.add(Box.createVerticalStrut(22));
        botonMemoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        seleccion.add(botonMemoria);
        seleccion.add(Box.createVerticalGlue());
        add(seleccion, BorderLayout.CENTER);

        JPanel franjaPremios = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(EstiloInterfaz.AZUL);
                lienzo.fillRoundRect(12, 0, getWidth() - 24, getHeight() + 30, 46, 46);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        franjaPremios.setOpaque(false);
        franjaPremios.setPreferredSize(new Dimension(800, 250));
        JPanel contenidoPremios = new JPanel();
        contenidoPremios.setOpaque(false);
        contenidoPremios.setLayout(new BoxLayout(contenidoPremios, BoxLayout.Y_AXIS));
        JLabel tituloPremios = EstiloInterfaz.titulo(
                "¿Quiénes son los Top Nice de Sason’s Memories?", 25);
        tituloPremios.setForeground(Color.WHITE);
        tituloPremios.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoPremios.add(tituloPremios);
        contenidoPremios.add(Box.createVerticalStrut(24));
        botonPremios.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoPremios.add(botonPremios);
        franjaPremios.add(contenidoPremios);
        add(franjaPremios, BorderLayout.SOUTH);
    }

    public void accionPatrones(Runnable accion) { botonPatrones.addActionListener(evento -> accion.run()); }
    public void accionMemoria(Runnable accion) { botonMemoria.addActionListener(evento -> accion.run()); }
    public void accionPremios(Runnable accion) { botonPremios.addActionListener(evento -> accion.run()); }
}
