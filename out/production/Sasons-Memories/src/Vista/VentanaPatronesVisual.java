package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPatronesVisual extends JFrame {
    public VentanaPatronesVisual() {
        super("Sason's Memories | Patrones");
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
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(EstiloMockups.CREMA);
        cabecera.setBorder(new EmptyBorder(0, 28, 0, 28));
        cabecera.setPreferredSize(new Dimension(800, 120));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 28));
        acciones.setOpaque(false);
        JButton volver = EstiloMockups.botonCircularConIcono("/referencias/iconos/icono_regresar.png", 60, "Volver al selector de modo");
        JButton sonido = EstiloMockups.botonCircularConIcono("/referencias/iconos/icono_sonido.png", 60, "Sonido activado");

        sonido.addActionListener(evento -> sonido.setToolTipText(
                "Sonido " + ("Sonido activado".equals(sonido.getToolTipText()) ? "desactivado" : "activado")));
        acciones.add(volver);
        acciones.add(sonido);
        cabecera.add(acciones, BorderLayout.WEST);
        JLabel titulo = EstiloMockups.titulo("Patrones", 23, Color.BLACK);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel nivel = EstiloMockups.titulo("Nivel de secuencia: 3", 20, Color.BLACK);
        nivel.setHorizontalAlignment(SwingConstants.RIGHT);
        cabecera.add(titulo, BorderLayout.CENTER);
        cabecera.add(nivel, BorderLayout.EAST);
        pantalla.add(cabecera, BorderLayout.NORTH);
        pantalla.add(construirTablero(), BorderLayout.CENTER);
        return pantalla;
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
        SwingUtilities.invokeLater(() -> new VentanaPatronesVisual().setVisible(true));
    }
}
