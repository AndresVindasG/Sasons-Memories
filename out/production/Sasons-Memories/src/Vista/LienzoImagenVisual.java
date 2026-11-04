package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;

/** Panel de fondo reutilizable en las composiciones de las vistas. */
public class LienzoImagenVisual extends JPanel {
    public LienzoImagenVisual() {
        setBackground(EstiloMockups.CREMA);
        setOpaque(true);
    }

    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Sason's Memories | Lienzo visual");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setIconImage(EstiloMockups.iconoVentana());
            ventana.setContentPane(new LienzoImagenVisual());
            ventana.setSize(800, 600);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}