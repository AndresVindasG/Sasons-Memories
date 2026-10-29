package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaMemoriaVisual extends JFrame {
    public VentanaMemoriaVisual() {
        super("Sason's Memories | Memoria");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(816, 638);
        setResizable(true);
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setPreferredSize(new Dimension(800, 600));
        pantalla.add(construirCabecera(), BorderLayout.NORTH);
        pantalla.add(construirTablero(), BorderLayout.CENTER);
        return pantalla;
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(8, 0));
        cabecera.setBackground(EstiloMockups.CREMA);
        cabecera.setBorder(new EmptyBorder(0, 28, 0, 28));
        cabecera.setPreferredSize(new Dimension(800, 120));
        JPanel accionesIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 28));
        accionesIzquierda.setOpaque(false);
        JButton volver = EstiloMockups.botonCircularConIcono("/referencias/iconos/icono_regresar.png", 60, "Volver al selector de modo");
        JButton sonido = EstiloMockups.botonCircularConIcono("/referencias/iconos/icono_sonido.png", 60, "Sonido activado");

        sonido.addActionListener(evento -> sonido.setToolTipText(
                "Sonido " + ("Sonido activado".equals(sonido.getToolTipText()) ? "desactivado" : "activado")));
        accionesIzquierda.add(volver);
        accionesIzquierda.add(sonido);
        cabecera.add(accionesIzquierda, BorderLayout.WEST);

        JPanel estadoPartida = new JPanel();
        estadoPartida.setOpaque(false);
        estadoPartida.setLayout(new javax.swing.BoxLayout(estadoPartida, javax.swing.BoxLayout.Y_AXIS));
        JLabel titulo = EstiloMockups.titulo("Memoria", 23, Color.BLACK);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        JLabel tiempo = EstiloMockups.titulo("Tiempo Restante: 0:00", 16, Color.BLACK);
        tiempo.setAlignmentX(CENTER_ALIGNMENT);
        estadoPartida.add(javax.swing.Box.createVerticalStrut(39));
        estadoPartida.add(titulo);
        estadoPartida.add(javax.swing.Box.createVerticalStrut(4));
        estadoPartida.add(tiempo);
        cabecera.add(estadoPartida, BorderLayout.CENTER);

        JPanel puntajes = new JPanel();
        puntajes.setOpaque(false);
        puntajes.setLayout(new javax.swing.BoxLayout(puntajes, javax.swing.BoxLayout.Y_AXIS));
        puntajes.setBorder(new EmptyBorder(32, 0, 0, 0));
        puntajes.add(EstiloMockups.titulo("Jugador A: 2", 18, Color.BLACK));
        puntajes.add(javax.swing.Box.createVerticalStrut(5));
        puntajes.add(EstiloMockups.titulo("Jugador B: 3", 18, Color.BLACK));
        cabecera.add(puntajes, BorderLayout.EAST);
        return cabecera;
    }

    private JPanel construirTablero() {
        JPanel fondoTablero = new JPanel(new BorderLayout());
        fondoTablero.setBackground(EstiloMockups.AZUL);
        fondoTablero.setBorder(new EmptyBorder(28, 23, 18, 19));
        JPanel fichas = new JPanel(new GridLayout(6, 6, 5, 4));
        fichas.setOpaque(false);
        for (int indice = 0; indice < 36; indice++) fichas.add(new FichaVisual());
        fondoTablero.add(fichas, BorderLayout.CENTER);
        return fondoTablero;
    }

    private static final class FichaVisual extends JPanel {
        FichaVisual() { setOpaque(false); }
        @Override protected void paintComponent(Graphics graficos) {
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(EstiloMockups.GRIS_FICHA);
            lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            lienzo.dispose();
            super.paintComponent(graficos);
        }
    }

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaMemoriaVisual().setVisible(true));
    }
}
