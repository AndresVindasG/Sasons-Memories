package Vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Vista Swing estática del mockup de reconocimiento facial. */
public class ReconocimientoFacialCompleto extends JFrame {
    public ReconocimientoFacialCompleto() {
        super("Sason's Memories | Reconocimiento facial");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(800, 638);
        setResizable(true);
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout(0, 22));
        pantalla.setBackground(EstiloMockups.CREMA);
        pantalla.setBorder(new EmptyBorder(48, 28, 28, 28));
        pantalla.add(construirVisorFacial(), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 0));
        acciones.setOpaque(false);
        acciones.add(EstiloMockups.botonRedondeado("Volver", Color.BLACK, 168, 58, 15));
        acciones.add(EstiloMockups.botonRedondeado("Cancelar", EstiloMockups.GRIS, 168, 58, 15));
        acciones.add(EstiloMockups.botonRedondeado("Aceptar", EstiloMockups.AZUL, 168, 58, 15));
        pantalla.add(acciones, BorderLayout.SOUTH);
        return pantalla;
    }

    private JPanel construirVisorFacial() {
        JPanel visor = new PanelVisorNegro();
        visor.setLayout(new BoxLayout(visor, BoxLayout.Y_AXIS));
        visor.setBorder(new EmptyBorder(23, 20, 18, 20));
        JLabel titulo = EstiloMockups.titulo("Sason’s Memories – Reconocimiento Facial", 23, Color.WHITE);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        JLabel descripcion = EstiloMockups.texto("ⓘ  El registro facial permite acceder a su cámara para que pueda iniciar sesión de manera rápida y segura", 13, Color.WHITE);
        descripcion.setAlignmentX(CENTER_ALIGNMENT);
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);
        visor.add(titulo);
        visor.add(Box.createVerticalStrut(8));
        visor.add(descripcion);
        visor.add(Box.createVerticalGlue());
        PanelDibujoRostro dibujo = new PanelDibujoRostro();
        dibujo.setAlignmentX(CENTER_ALIGNMENT);
        visor.add(dibujo);
        visor.add(Box.createVerticalGlue());
        return visor;
    }

    private static final class PanelVisorNegro extends JPanel {
        PanelVisorNegro() { setOpaque(false); }
        @Override protected void paintComponent(Graphics graficos) {
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(Color.BLACK);
            lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            lienzo.dispose();
            super.paintComponent(graficos);
        }
    }

    private static final class PanelDibujoRostro extends JPanel {
        PanelDibujoRostro() {
            setOpaque(false);
            setPreferredSize(new Dimension(190, 190));
            setMaximumSize(new Dimension(190, 190));
        }
        @Override protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(Color.WHITE);
            lienzo.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int x = 27, y = 23, lado = 136, tramo = 34;
            lienzo.drawLine(x, y + tramo, x, y); lienzo.drawLine(x, y, x + tramo, y);
            lienzo.drawLine(x + lado - tramo, y, x + lado, y); lienzo.drawLine(x + lado, y, x + lado, y + tramo);
            lienzo.drawLine(x, y + lado - tramo, x, y + lado); lienzo.drawLine(x, y + lado, x + tramo, y + lado);
            lienzo.drawLine(x + lado - tramo, y + lado, x + lado, y + lado); lienzo.drawLine(x + lado, y + lado - tramo, x + lado, y + lado);
            lienzo.fillOval(57, 72, 8, 12); lienzo.fillOval(112, 72, 8, 12);
            lienzo.drawArc(78, 74, 21, 33, 245, 150);
            lienzo.drawArc(62, 92, 64, 47, 205, 130);
            lienzo.dispose();
        }
    }

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new ReconocimientoFacialCompleto().setVisible(true));
    }
}