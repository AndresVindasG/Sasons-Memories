package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaSeleccionModoVisual extends JFrame {
    public VentanaSeleccionModoVisual() {
        super("Sason's Memories | Selección de modo");
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

        JPanel seleccion = new JPanel();
        seleccion.setBackground(EstiloMockups.CREMA);
        seleccion.setLayout(new BoxLayout(seleccion, BoxLayout.Y_AXIS));
        seleccion.setBorder(new EmptyBorder(93, 0, 24, 0));
        JLabel titulo = EstiloMockups.titulo("Elige el modo de juego", 34, Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        seleccion.add(titulo);
        seleccion.add(Box.createVerticalStrut(35));
        JButton modoPatrones = EstiloMockups.botonRedondeado("Patrones", EstiloMockups.AZUL, 185, 58, 15);
        JButton modoMemoria = EstiloMockups.botonRedondeado("Memoria", EstiloMockups.AZUL, 185, 58, 15);
        modoPatrones.setAlignmentX(Component.CENTER_ALIGNMENT);

        modoMemoria.setAlignmentX(Component.CENTER_ALIGNMENT);

        seleccion.add(modoPatrones);
        seleccion.add(Box.createVerticalStrut(17));
        seleccion.add(modoMemoria);
        pantalla.add(seleccion, BorderLayout.CENTER);

        JPanel premios = new JPanel();
        premios.setBackground(EstiloMockups.AZUL);
        premios.setLayout(new BoxLayout(premios, BoxLayout.Y_AXIS));
        premios.setPreferredSize(new Dimension(800, 250));
        premios.setBorder(new EmptyBorder(46, 0, 24, 0));
        JLabel ranking = EstiloMockups.titulo("¿Quienes son los Top Nice de Sason’s Memories?", 21, Color.WHITE);
        ranking.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton verPremios = EstiloMockups.botonRedondeado("Ver", EstiloMockups.GRIS, 185, 58, 15);
        verPremios.setAlignmentX(Component.CENTER_ALIGNMENT);

        premios.add(ranking);
        premios.add(Box.createVerticalStrut(31));
        premios.add(verPremios);
        pantalla.add(premios, BorderLayout.SOUTH);
        return pantalla;
    }

    private void abrirVista(JFrame vista) {
        vista.setVisible(true);
        dispose();
    }
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaSeleccionModoVisual().setVisible(true));
    }
}
