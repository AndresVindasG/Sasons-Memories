package Vista;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.InputStream;

/** Recursos de estilo compartidos por las vistas de mockup Swing. */
public final class EstiloMockups {
    public static final Color CREMA = new Color(250, 239, 222);
    public static final Color AZUL = new Color(126, 160, 179);
    public static final Color CARBON = new Color(53, 68, 78);
    public static final Color GRIS = new Color(105, 100, 94);
    public static final Color GRIS_FICHA = new Color(217, 217, 217);
    private static final Font POPPINS = cargarFuente("/referencias/fonts/Poppins-ExtraBold.ttf", Font.BOLD);
    private static final Font INTER = cargarFuente("/referencias/fonts/Inter.ttf", Font.PLAIN);
    private static final Image LOGO = cargarLogo();

    private EstiloMockups() { }

    public static Font poppins(float tamano) { return POPPINS.deriveFont(Font.BOLD, tamano); }
    public static Font inter(float tamano) { return INTER.deriveFont(tamano); }

    public static Image iconoVentana() { return LOGO; }

    public static JLabel titulo(String texto, float tamano, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(poppins(tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    public static JLabel texto(String texto, float tamano, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(inter(tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    public static JLabel logo(int ancho, int alto) {
        JLabel etiqueta = new JLabel();
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiqueta.setPreferredSize(new java.awt.Dimension(ancho, alto));
        if (LOGO != null) {
            double escala = Math.min(ancho / (double) LOGO.getWidth(null), alto / (double) LOGO.getHeight(null));
            Image imagenEscalada = LOGO.getScaledInstance((int) (LOGO.getWidth(null) * escala),
                    (int) (LOGO.getHeight(null) * escala), Image.SCALE_SMOOTH);
            etiqueta.setIcon(new ImageIcon(imagenEscalada));
        }
        return etiqueta;
    }

    public static JTextField campo(String textoIndicador, int alto) {
        JTextField campo = new JTextField(textoIndicador);
        campo.setFont(inter(15));
        campo.setForeground(new Color(105, 112, 120));
        campo.setBackground(CREMA);
        campo.setBorder(new EmptyBorder(0, 15, 0, 12));
        campo.setPreferredSize(new java.awt.Dimension(430, alto));
        campo.setMaximumSize(new java.awt.Dimension(430, alto));
        return campo;
    }

    public static JButton botonRedondeado(String texto, Color color, int ancho, int alto, int tamano) {
        JButton boton = new JButton(texto) {
            @Override protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(new Color(0, 0, 0, 42));
                lienzo.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 24, 24);
                lienzo.setColor(getModel().isPressed() ? color.darker() : color);
                lienzo.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 8, 24, 24);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        boton.setFont(poppins(tamano));
        boton.setForeground(Color.WHITE);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new java.awt.Dimension(ancho, alto));
        boton.setMinimumSize(new java.awt.Dimension(ancho, alto));
        boton.setMaximumSize(new java.awt.Dimension(ancho, alto));
        return boton;
    }

    public static JButton botonCircular(String texto, int diametro) {
        JButton boton = new JButton(texto) {
            @Override protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(Color.BLACK);
                lienzo.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        boton.setFont(new Font("Dialog", Font.BOLD, Math.max(20, diametro / 2)));
        boton.setForeground(Color.WHITE);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new java.awt.Dimension(diametro, diametro));
        boton.setMinimumSize(new java.awt.Dimension(diametro, diametro));
        boton.setMaximumSize(new java.awt.Dimension(diametro, diametro));
        return boton;
    }

    public static JButton botonCircularConIcono(String recurso, int diametro, String descripcion) {
        JButton boton = botonCircular("", diametro);
        boton.setToolTipText(descripcion);
        boton.getAccessibleContext().setAccessibleName(descripcion);
        try (InputStream archivo = EstiloMockups.class.getResourceAsStream(recurso)) {
            if (archivo != null) {
                Image original = ImageIO.read(archivo);
                int tamanoIcono = Math.round(diametro * 0.58f);
                Image icono = original.getScaledInstance(tamanoIcono, tamanoIcono, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(icono));
            }
        } catch (Exception excepcion) {
            boton.setToolTipText(descripcion + " (icono no disponible)");
        }
        return boton;
    }
    private static Font cargarFuente(String recurso, int estilo) {
        try (InputStream archivo = EstiloMockups.class.getResourceAsStream(recurso)) {
            if (archivo == null) return new Font("Dialog", estilo, 14);
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, archivo);
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fuente);
            return fuente;
        } catch (Exception excepcion) {
            return new Font("Dialog", estilo, 14);
        }
    }

    private static Image cargarLogo() {
        try (InputStream archivo = EstiloMockups.class.getResourceAsStream("/referencias/logo-aplicacion.png")) {
            return archivo == null ? null : ImageIO.read(archivo);
        } catch (Exception excepcion) {
            return null;
        }
    }
    
}
