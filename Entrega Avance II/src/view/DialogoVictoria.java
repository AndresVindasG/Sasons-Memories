package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/** Diálogo festivo con partículas animadas al terminar el modo Memoria. */
public final class DialogoVictoria extends JDialog {
    private final Timer animacion;

    public DialogoVictoria(Window propietario, String titulo, String subtitulo, Runnable accionVolver) {
        super(propietario, "Partida terminada", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        PanelCelebracion celebracion = new PanelCelebracion();
        celebracion.setLayout(new BoxLayout(celebracion, BoxLayout.Y_AXIS));
        celebracion.setBorder(new EmptyBorder(42, 32, 28, 32));

        JLabel icono = new JLabel("★", SwingConstants.CENTER);
        icono.setFont(new Font("Dialog", Font.BOLD, 58));
        icono.setForeground(new Color(255, 191, 67));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel etiquetaTitulo = EstiloInterfaz.titulo(titulo, 29);
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel etiquetaSubtitulo = EstiloInterfaz.texto(subtitulo, 15);
        etiquetaSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaSubtitulo.setForeground(EstiloInterfaz.CARBON);
        JButton botonVolver = EstiloInterfaz.boton("Volver a modos", EstiloInterfaz.AZUL, 210, 48, 16);
        botonVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonVolver.addActionListener(evento -> {
            dispose();
            if (accionVolver != null) accionVolver.run();
        });

        celebracion.add(icono);
        celebracion.add(Box.createVerticalStrut(8));
        celebracion.add(etiquetaTitulo);
        celebracion.add(Box.createVerticalStrut(8));
        celebracion.add(etiquetaSubtitulo);
        celebracion.add(Box.createVerticalGlue());
        celebracion.add(botonVolver);
        add(celebracion);

        animacion = new Timer(35, evento -> celebracion.moverParticulas());
        addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent evento) { animacion.start(); }
            @Override public void windowClosed(WindowEvent evento) { animacion.stop(); }
        });
        setSize(520, 350);
        setLocationRelativeTo(propietario);
    }

    private static final class PanelCelebracion extends JPanel {
        private static final int CANTIDAD_PARTICULAS = 75;
        private final int[] posicionesX = new int[CANTIDAD_PARTICULAS];
        private final int[] posicionesY = new int[CANTIDAD_PARTICULAS];
        private final int[] velocidades = new int[CANTIDAD_PARTICULAS];
        private final int[] tamanos = new int[CANTIDAD_PARTICULAS];
        private final Color[] colores = new Color[CANTIDAD_PARTICULAS];
        private final Random azar = new Random();

        private PanelCelebracion() {
            setOpaque(true);
            setBackground(EstiloInterfaz.FONDO);
            Color[] paleta = {EstiloInterfaz.AZUL, new Color(255, 191, 67),
                    new Color(218, 112, 102), new Color(129, 177, 143)};
            for (int indice = 0; indice < CANTIDAD_PARTICULAS; indice++) {
                posicionesX[indice] = azar.nextInt(520);
                posicionesY[indice] = azar.nextInt(350);
                velocidades[indice] = 2 + azar.nextInt(5);
                tamanos[indice] = 4 + azar.nextInt(7);
                colores[indice] = paleta[azar.nextInt(paleta.length)];
            }
        }

        private void moverParticulas() {
            int alto = Math.max(1, getHeight());
            for (int indice = 0; indice < CANTIDAD_PARTICULAS; indice++) {
                posicionesY[indice] += velocidades[indice];
                if (posicionesY[indice] > alto) {
                    posicionesY[indice] = -tamanos[indice];
                    posicionesX[indice] = azar.nextInt(Math.max(1, getWidth()));
                }
            }
            repaint();
        }

        @Override protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int indice = 0; indice < CANTIDAD_PARTICULAS; indice++) {
                lienzo.setColor(colores[indice]);
                lienzo.fillRoundRect(posicionesX[indice], posicionesY[indice], tamanos[indice],
                        tamanos[indice] * 2, 3, 3);
            }
            lienzo.dispose();
        }
    }
}
