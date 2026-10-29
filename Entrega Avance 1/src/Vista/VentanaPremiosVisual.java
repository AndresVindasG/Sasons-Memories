
package Vista;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Ventana que muestra el ranking de premios del juego. */
public class VentanaPremiosVisual extends JFrame {

    // Orden de los puestos para dejar el primer lugar en el centro.
    private static final String[] POSICIONES = {
            "4.", "2.", "1.", "3.", "5."
    };

    // Altura de cada puesto: el primer lugar es el más alto.
    private static final int[] ALTURAS = {
            232, 312, 356, 312, 232
    };

    public VentanaPremiosVisual() {
        super("Sason's Memories | Premios");

        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(816, 638);
        setResizable(true);
        setLocationRelativeTo(null);
    }

    // Organiza la cabecera, el podio y el texto inferior.
    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setPreferredSize(new Dimension(800, 600));

        // Cabecera con el botón para volver al selector de modo.
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(EstiloMockups.CREMA);
        cabecera.setPreferredSize(new Dimension(800, 105));

        JButton volver = EstiloMockups.botonCircularConIcono(
                "/referencias/iconos/icono_regresar.png",
                62,
                "Volver al selector de modo"
        );

        JPanel centrado = new JPanel();
        centrado.setOpaque(false);

        // Al presionar el botón, abre el selector y cierra esta ventana.
        volver.addActionListener(evento -> {
            VentanaSeleccionModoVisual selector =
                    new VentanaSeleccionModoVisual();

            selector.setVisible(true);
            dispose();
        });

        centrado.add(volver);
        cabecera.add(centrado, BorderLayout.CENTER);
        pantalla.add(cabecera, BorderLayout.NORTH);

        // Panel principal donde se muestra la clasificación.
        JPanel clasificacion = new JPanel(new BorderLayout());
        clasificacion.setBackground(EstiloMockups.AZUL);

        JLabel titulo = EstiloMockups.titulo(
                "Top Nice de Sason’s Memories", 23, Color.WHITE
        );
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(20, 0, 8, 0));

        clasificacion.add(titulo, BorderLayout.NORTH);
        clasificacion.add(construirPodio(), BorderLayout.CENTER);

        JLabel pie = EstiloMockups.titulo(
                "Todos los derechos reservados, Patito S.A. 2026",
                13,
                EstiloMockups.CARBON
        );
        pie.setHorizontalAlignment(SwingConstants.CENTER);
        pie.setBorder(new EmptyBorder(8, 0, 20, 0));

        clasificacion.add(pie, BorderLayout.SOUTH);
        pantalla.add(clasificacion, BorderLayout.CENTER);

        return pantalla;
    }

    // Crea los cinco puestos usando un ciclo para evitar repetir código.
    private JPanel construirPodio() {
        JPanel podio = new JPanel(new GridBagLayout());
        podio.setOpaque(false);

        for (int indice = 0; indice < POSICIONES.length; indice++) {
            GridBagConstraints posicion = new GridBagConstraints();

            // Cada puesto ocupa una columna y todos quedan en la misma fila.
            posicion.gridx = indice;
            posicion.gridy = 0;
            posicion.weightx = 1;
            posicion.weighty = 1;
            posicion.anchor = GridBagConstraints.SOUTH;
            posicion.insets = new Insets(0, 5, 0, 5);

            // Usa el mismo índice para obtener el puesto y su altura.
            podio.add(
                    new PanelPodio(POSICIONES[indice], ALTURAS[indice]),
                    posicion
            );
        }

        return podio;
    }

    // Representa cada bloque del podio con su puesto, usuario y monedas.
    private static final class PanelPodio extends JPanel {

        private final int alto;

        PanelPodio(String posicion, int alto) {
            this.alto = alto;

            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(14, 8, 14, 8));

            JLabel rango = EstiloMockups.titulo(
                    posicion, 22, Color.WHITE
            );
            rango.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel usuario = EstiloMockups.titulo(
                    "User1", 15, Color.WHITE
            );
            usuario.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel monedas = EstiloMockups.titulo(
                    "TicoCoins", 15, Color.WHITE
            );
            monedas.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(rango);
            add(Box.createVerticalStrut(2));
            add(usuario);
            add(Box.createVerticalGlue());
            add(monedas);
        }

        // Cada puesto tiene el mismo ancho, pero una altura diferente.
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(129, alto);
        }

        // Dibuja el fondo oscuro con las esquinas redondeadas.
        @Override
        protected void paintComponent(Graphics graficos) {
            Graphics2D lienzo = (Graphics2D) graficos.create();

            lienzo.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            lienzo.setColor(EstiloMockups.CARBON);
            lienzo.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 40, 40
            );

            lienzo.dispose();
            super.paintComponent(graficos);
        }
    }

    // Permite ejecutar la ventana directamente para probar el diseño.
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(
                () -> new VentanaPremiosVisual().setVisible(true)
        );
    }
}
