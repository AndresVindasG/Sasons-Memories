package Vista;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

/** Mockup estático de confirmación. Sus controles son decorativos y no ejecutan acciones. */
public class VentanaConfirmacionVisual extends JFrame {
    public VentanaConfirmacionVisual() {
        super("Memory Game | Confirmación (mockup)");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        pack();
        setResizable(true);
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(250, 239, 222));
        panel.setBorder(new EmptyBorder(34, 60, 34, 60));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        URL recursoLogo = VentanaConfirmacionVisual.class.getResource("/referencias/logo-aplicacion.png");
        if (recursoLogo != null) {
            try {
                Image logoOriginal = ImageIO.read(recursoLogo);
                Image logoEscalado = logoOriginal.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                JLabel logo = new JLabel(new ImageIcon(logoEscalado));
                logo.setAlignmentX(CENTER_ALIGNMENT);
                panel.add(logo);
            } catch (IOException excepcion) {
                panel.add(Box.createVerticalStrut(120));
            }
        }
        panel.add(Box.createVerticalStrut(12));
        JLabel titulo = new JLabel("¿Confirmas tu registro?", SwingConstants.CENTER);
        titulo.setFont(TipografiaMockups.poppinsExtraBold(25));
        titulo.setForeground(new Color(53, 68, 78));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        JLabel descripcion = new JLabel("Revisa tus datos y continúa para configurar el acceso facial.", SwingConstants.CENTER);
        descripcion.setFont(TipografiaMockups.inter(15));
        descripcion.setForeground(new Color(65, 72, 77));
        descripcion.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(descripcion);
        panel.add(Box.createVerticalStrut(32));

        JButton confirmar = botonVisual("Confirmar registro", new Color(53, 68, 78));
        JButton volver = botonVisual("Volver a editar", new Color(105, 100, 94));
        confirmar.setAlignmentX(CENTER_ALIGNMENT);
        volver.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(confirmar);
        panel.add(Box.createVerticalStrut(12));
        panel.add(volver);
        return panel;
    }

    private JButton botonVisual(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(TipografiaMockups.poppinsExtraBold(15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createEmptyBorder(13, 35, 13, 35));
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(300, 52));
        boton.setMaximumSize(new Dimension(300, 52));
        return boton;
    }

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaConfirmacionVisual().setVisible(true));
    }
}