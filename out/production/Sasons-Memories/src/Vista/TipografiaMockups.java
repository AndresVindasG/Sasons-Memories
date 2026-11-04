package Vista;

import java.awt.Font;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

/** Tipografías de presentación incluidas con los mockups. */
final class TipografiaMockups {
    private static final Font POPPINS = cargar("/referencias/fonts/Poppins-ExtraBold.ttf", Font.BOLD);
    private static final Font INTER = cargar("/referencias/fonts/Inter.ttf", Font.PLAIN);

    private TipografiaMockups() { }

    static Font poppinsExtraBold(float tamano) { return POPPINS.deriveFont(Font.BOLD, tamano); }
    static Font inter(float tamano) { return INTER.deriveFont(tamano); }


    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaConfirmacionVisual().setVisible(true));
    }
    private static Font cargar(String recurso, int estilo) {
        try (InputStream archivo = TipografiaMockups.class.getResourceAsStream(recurso)) {
            if (archivo == null) return new Font("Dialog", estilo, 14);
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, archivo);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fuente);
            return fuente;
        } catch (Exception excepcion) {
            return new Font("Dialog", estilo, 14);
        }
    }
}