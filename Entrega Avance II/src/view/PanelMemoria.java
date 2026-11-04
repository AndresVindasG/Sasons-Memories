package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.BiConsumer;

/** Dos tableros de 6×6: uno propio para cada jugador. */
public class PanelMemoria extends JPanel {
    private static final int CANTIDAD_JUGADORES = 2;

    private final PanelControlesJuego controles = new PanelControlesJuego();
    private final JLabel etiquetaIndicacion = EstiloInterfaz.texto(
            "Completa tu matriz antes que tu rival. Si fallas, cedes el turno.", 12);
    private final JLabel etiquetaTiempo = EstiloInterfaz.texto("Tiempo: 0:00", 14);
    private final JLabel[] etiquetasJugadores = {
            EstiloInterfaz.titulo("Jugador A: 0", 17), EstiloInterfaz.titulo("Jugador B: 0", 17)
    };
    private final TableroCartas[] tableros = {new TableroCartas(), new TableroCartas()};
    private final boolean[][] parejasEncontradas = new boolean[CANTIDAD_JUGADORES][TableroCartas.CANTIDAD_CARTAS];

    public PanelMemoria() {
        super(new BorderLayout(0, 0));
        setBackground(EstiloInterfaz.FONDO);
        add(construirCabecera(), BorderLayout.NORTH);
        add(construirTableros(), BorderLayout.CENTER);
    }

    public PanelControlesJuego obtenerControles() { return controles; }

    public void accionSeleccionarCarta(BiConsumer<Integer, Integer> accion) {
        for (int jugador = 0; jugador < CANTIDAD_JUGADORES; jugador++) {
            final int jugadorSeleccionado = jugador;
            for (int indice = 0; indice < TableroCartas.CANTIDAD_CARTAS; indice++) {
                final int cartaSeleccionada = indice;
                tableros[jugador].obtenerCarta(indice).addActionListener(
                        evento -> accion.accept(jugadorSeleccionado, cartaSeleccionada));
            }
        }
    }

    public void establecerCaras(int jugador, int[] caras) {
        for (int indice = 0; indice < TableroCartas.CANTIDAD_CARTAS; indice++) {
            tableros[jugador].obtenerCarta(indice).establecerCara(caras[indice]);
        }
    }

    public void mostrarCarta(int jugador, int indice, int cara) {
        tableros[jugador].obtenerCarta(indice).mostrarCara(cara);
    }

    public void ocultarCarta(int jugador, int indice) {
        tableros[jugador].obtenerCarta(indice).mostrarReverso();
    }

    public void mostrarParejaEncontrada(int jugador, int indice, int cara) {
        FichaJuego carta = tableros[jugador].obtenerCarta(indice);
        carta.establecerColor(new Color(182, 212, 191));
        carta.establecerEncontrada(true);
        parejasEncontradas[jugador][indice] = true;
        carta.mostrarCara(cara);
    }

    public void restablecerTableros() {
        for (int jugador = 0; jugador < CANTIDAD_JUGADORES; jugador++) {
            for (int indice = 0; indice < TableroCartas.CANTIDAD_CARTAS; indice++) {
                FichaJuego carta = tableros[jugador].obtenerCarta(indice);
                carta.setEnabled(true);
                carta.establecerColor(EstiloInterfaz.GRIS_FICHA);
                carta.establecerEncontrada(false);
                carta.mostrarReverso();
                parejasEncontradas[jugador][indice] = false;
            }
        }
    }

    public void establecerMarcador(int puntosA, int puntosB, int jugadorActual, int segundos) {
        int[] puntos = {puntosA, puntosB};
        for (int jugador = 0; jugador < CANTIDAD_JUGADORES; jugador++) {
            String nombre = jugador == 0 ? "Jugador A" : "Jugador B";
            etiquetasJugadores[jugador].setText(nombre + ": " + puntos[jugador]
                    + (jugadorActual == jugador ? "  • TU TURNO" : ""));
            etiquetasJugadores[jugador].setForeground(jugadorActual == jugador
                    ? EstiloInterfaz.CARBON : EstiloInterfaz.CAMPO);
            for (int indice = 0; indice < TableroCartas.CANTIDAD_CARTAS; indice++) {
                tableros[jugador].obtenerCarta(indice)
                        .setEnabled(jugadorActual == jugador && !parejasEncontradas[jugador][indice]);
            }
        }
        etiquetaTiempo.setText("Tiempo: " + formatearTiempo(segundos));
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new GridBagLayout());
        cabecera.setBackground(EstiloInterfaz.FONDO);
        cabecera.setBorder(new EmptyBorder(12, 26, 10, 26));
        cabecera.setPreferredSize(new Dimension(900, 100));
        GridBagConstraints zona = new GridBagConstraints();
        zona.gridy = 0;
        zona.weightx = 1;
        zona.fill = GridBagConstraints.HORIZONTAL;
        zona.insets = new Insets(0, 0, 0, 0);
        zona.gridx = 0;
        zona.anchor = GridBagConstraints.WEST;
        cabecera.add(controles, zona);
        JPanel tituloYTiempo = new JPanel();
        tituloYTiempo.setOpaque(false);
        tituloYTiempo.setLayout(new BoxLayout(tituloYTiempo, BoxLayout.Y_AXIS));
        tituloYTiempo.add(Box.createVerticalGlue());
        JLabel titulo = EstiloInterfaz.titulo("Memoria", 28);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaIndicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaTiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloYTiempo.add(titulo);
        tituloYTiempo.add(Box.createVerticalStrut(2));
        tituloYTiempo.add(etiquetaIndicacion);
        tituloYTiempo.add(Box.createVerticalStrut(2));
        tituloYTiempo.add(etiquetaTiempo);
        tituloYTiempo.add(Box.createVerticalGlue());
        zona.gridx = 1;
        zona.anchor = GridBagConstraints.CENTER;
        cabecera.add(tituloYTiempo, zona);
        JPanel espacioEquilibrio = new JPanel();
        espacioEquilibrio.setOpaque(false);
        espacioEquilibrio.setPreferredSize(controles.getPreferredSize());
        zona.gridx = 2;
        zona.anchor = GridBagConstraints.EAST;
        cabecera.add(espacioEquilibrio, zona);
        return cabecera;
    }

    private JPanel construirTableros() {
        JPanel contenedor = new JPanel(new GridLayout(1, CANTIDAD_JUGADORES, 14, 0));
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(0, 20, 18, 20));
        for (int jugador = 0; jugador < CANTIDAD_JUGADORES; jugador++) {
            JPanel tableroJugador = new JPanel(new BorderLayout(0, 8));
            tableroJugador.setOpaque(false);
            etiquetasJugadores[jugador].setHorizontalAlignment(SwingConstants.CENTER);
            tableroJugador.add(etiquetasJugadores[jugador], BorderLayout.NORTH);
            tableroJugador.add(tableros[jugador], BorderLayout.CENTER);
            contenedor.add(tableroJugador);
        }
        return contenedor;
    }

    private String formatearTiempo(int segundos) {
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }
}
