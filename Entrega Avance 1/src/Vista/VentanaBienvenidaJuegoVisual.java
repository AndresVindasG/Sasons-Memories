package Vista;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Vista Swing estática de bienvenida al juego. */
public class VentanaBienvenidaJuegoVisual extends JFrame {
    public VentanaBienvenidaJuegoVisual() {
        super("Sason's Memories | Bienvenida al juego");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(816, 638);
        setResizable(true);
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel fondo = new PanelFondoRayado();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        fondo.setBorder(new EmptyBorder(70, 0, 45, 0));
        JLabel logo = EstiloMockups.logo(220, 205);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titulo = EstiloMockups.titulo("Sason’s Memories", 39, new Color(174, 124, 91));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton comenzar = EstiloMockups.botonRedondeado("Metámosle mano!", EstiloMockups.AZUL, 185, 58, 15);
        comenzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(logo);
        fondo.add(Box.createVerticalStrut(14));
        fondo.add(titulo);
        fondo.add(Box.createVerticalStrut(30));
        comenzar.addActionListener(evento -> {
            VentanaSeleccionModoVisual selector = new VentanaSeleccionModoVisual();
            selector.setVisible(true);
            dispose();
        });
        fondo.add(comenzar);
        return fondo;
    }

    private static final class PanelFondoRayado extends JPanel {
        PanelFondoRayado() { setOpaque(false); }
        @Override protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(EstiloMockups.CREMA);
            lienzo.fillRect(0, 0, getWidth(), getHeight());
            lienzo.setColor(EstiloMockups.AZUL);
            for (int x = 28; x < getWidth(); x += 55) lienzo.fillRect(x, 0, 28, getHeight());
            lienzo.setStroke(new BasicStroke(27));
            for (int y = 39; y < getHeight(); y += 52) lienzo.drawLine(0, y, getWidth(), y);
            lienzo.dispose();
        }
    }

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaBienvenidaJuegoVisual().setVisible(true));
    }
}