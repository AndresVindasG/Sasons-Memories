package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaMemoriaVisual extends JFrame {
    private FichaVisual[][] fichasJugadorA = new FichaVisual[6][6];
    private FichaVisual[][] fichasJugadorB = new FichaVisual[6][6];

    public VentanaMemoriaVisual() {
        super("Sason's Memories | Memoria");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(1000, 700);
        setResizable(true);
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setPreferredSize(new Dimension(800, 600));
        pantalla.add(construirCabecera(), BorderLayout.NORTH);
        pantalla.add(construirTableros(), BorderLayout.CENTER);
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

    private JPanel construirTableros() {
        JPanel fondoTablero = new JPanel(new GridLayout(1, 2, 18, 0));
        fondoTablero.setBackground(EstiloMockups.AZUL);
        fondoTablero.setBorder(new EmptyBorder(20, 20, 20, 20));
        fondoTablero.add(construirTableroJugador(fichasJugadorA, "Jugador A"));
        fondoTablero.add(construirTableroJugador(fichasJugadorB, "Jugador B"));
        return fondoTablero;
    }
    private JPanel construirTableroJugador(FichaVisual[][] fichas, String nombreJugador) {
        JPanel panelJugador = new JPanel(new BorderLayout(0, 12));
        panelJugador.setOpaque(false);
        JLabel tituloJugador = EstiloMockups.titulo(nombreJugador, 20, Color.BLACK
        );
        tituloJugador.setHorizontalAlignment(SwingConstants.CENTER);
        panelJugador.add(tituloJugador, BorderLayout.NORTH);
        JPanel panelFichas = new JPanel(new GridLayout(6, 6, 5, 5));
        panelFichas.setOpaque(false);

        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                fichas[fila][columna] = new FichaVisual();
                panelFichas.add(fichas[fila][columna]);
            }
        }
        panelJugador.add(panelFichas, BorderLayout.CENTER);
        return panelJugador;
    }
    public void mostrarImagen(int jugador, int fila, int columna, int imagen) {
        if (jugador == 1) {
            fichasJugadorA[fila][columna].setText(String.valueOf(imagen));
        } else if (jugador == 2) {
            fichasJugadorB[fila][columna].setText(String.valueOf(imagen));
        }
    }
    private static final class FichaVisual extends JButton {
        FichaVisual() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }
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
