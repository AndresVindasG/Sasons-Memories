package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.IntConsumer;

/** Tablero nativo del modo Patrones con 36 fichas activables. */
public class PanelPatrones extends JPanel {
    private static final int CANTIDAD_FICHAS = TableroCartas.CANTIDAD_CARTAS;
    private static final Color COLOR_ACTIVO = new Color(255, 205, 91);

    private final PanelControlesJuego controles = new PanelControlesJuego();
    private final JLabel etiquetaNivel = EstiloInterfaz.titulo("Nivel de secuencia: 3", 22);
    private final JLabel etiquetaEstado = EstiloInterfaz.texto("Observa la secuencia y repítela.", 15);
    private final TableroCartas tablero = new TableroCartas();
    private final int[] carasFichas = new int[CANTIDAD_FICHAS];

    public PanelPatrones() {
        super(new BorderLayout(0, 0));
        setBackground(EstiloInterfaz.FONDO);
        add(construirCabecera(), BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);
        JPanel pie = new JPanel(new GridBagLayout());
        pie.setBackground(EstiloInterfaz.FONDO);
        pie.setPreferredSize(new Dimension(800, 48));
        etiquetaEstado.setForeground(EstiloInterfaz.CAMPO);
        pie.add(etiquetaEstado);
        add(pie, BorderLayout.SOUTH);
    }

    public PanelControlesJuego obtenerControles() { return controles; }

    public void accionSeleccionarFicha(IntConsumer accion) {
        for (int indice = 0; indice < CANTIDAD_FICHAS; indice++) {
            int fichaSeleccionada = indice;
            tablero.obtenerCarta(indice).addActionListener(evento -> accion.accept(fichaSeleccionada));
        }
    }

    public void establecerCaras(int[] caras) {
        System.arraycopy(caras, 0, carasFichas, 0, CANTIDAD_FICHAS);
        for (int indice = 0; indice < CANTIDAD_FICHAS; indice++) {
            tablero.obtenerCarta(indice).establecerCara(carasFichas[indice]);
        }
    }

    public void establecerNivel(int nivel) {
        etiquetaNivel.setText("Nivel de secuencia: " + nivel);
    }

    public void establecerEstado(String mensaje) { etiquetaEstado.setText(mensaje); }

    public void establecerEntradaHabilitada(boolean habilitada) {
        for (int indice = 0; indice < CANTIDAD_FICHAS; indice++) tablero.obtenerCarta(indice).setEnabled(habilitada);
    }

    public void iluminarFicha(int indice) {
        if (indice >= 0 && indice < CANTIDAD_FICHAS) {
            tablero.obtenerCarta(indice).establecerColor(COLOR_ACTIVO);
            tablero.obtenerCarta(indice).mostrarCara(carasFichas[indice]);
        }
    }

    public void apagarFichas() {
        for (int indice = 0; indice < CANTIDAD_FICHAS; indice++) {
            tablero.obtenerCarta(indice).establecerColor(EstiloInterfaz.GRIS_FICHA);
            tablero.obtenerCarta(indice).mostrarReverso();
        }
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(12, 0));
        cabecera.setBackground(EstiloInterfaz.FONDO);
        cabecera.setBorder(new EmptyBorder(12, 26, 10, 26));
        cabecera.setPreferredSize(new Dimension(900, 100));
        cabecera.add(controles, BorderLayout.WEST);
        JLabel titulo = EstiloInterfaz.titulo("Patrones", 28);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(titulo, BorderLayout.CENTER);
        etiquetaNivel.setHorizontalAlignment(SwingConstants.RIGHT);
        cabecera.add(etiquetaNivel, BorderLayout.EAST);
        return cabecera;
    }

}