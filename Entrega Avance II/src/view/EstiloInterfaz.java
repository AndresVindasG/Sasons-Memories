package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.InputStream;

public class EstiloInterfaz {
    private EstiloInterfaz() { }
    public static final Color FONDO = new Color(250, 239, 222);
    public static final Color AZUL = new Color(126, 160, 179);
    public static final Color CAMPO = new Color(53, 68, 78);
    public static final Color GRIS = new Color(105, 100, 94);
    public static final Color TEXTO = new Color(30, 31, 31);
    public static final Color CARBON = CAMPO;
    public static final Color GRIS_FICHA = new Color(217, 217, 217);
    private static final Font POPPINS_REGULAR = cargarFuente("/referencias/fonts/Poppins-Regular.ttf", Font.PLAIN);
    private static final Font POPPINS_SEMIBOLD = cargarFuente("/referencias/fonts/Poppins-SemiBold.ttf", Font.BOLD);
    private static final Font POPPINS_BOLD = cargarFuente("/referencias/fonts/Poppins-ExtraBold.ttf", Font.BOLD);
    private static final Font INTER_REGULAR = cargarFuente("/referencias/fonts/Inter.ttf", Font.PLAIN);
    private static final Image LOGO = cargarLogo();

    private static Font cargarFuente(String recurso, int estilo) {
        try (InputStream archivo = EstiloInterfaz.class.getResourceAsStream(recurso)) {
            if (archivo == null) return new Font("Dialog", estilo, 14);
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, archivo);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fuente);
            return fuente;
        } catch (Exception excepcion) {
            return new Font("Dialog", estilo, 14);
        }
    }

    private static Image cargarLogo() {
        try (InputStream archivo = EstiloInterfaz.class.getResourceAsStream("/referencias/logo-aplicacion.png")) {
            if (archivo == null) return null;
            java.awt.image.BufferedImage original = javax.imageio.ImageIO.read(archivo);
            if (original == null) return null;

            int izquierda = original.getWidth();
            int arriba = original.getHeight();
            int derecha = -1;
            int abajo = -1;
            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    if ((original.getRGB(x, y) >>> 24) > 8) {
                        izquierda = Math.min(izquierda, x);
                        arriba = Math.min(arriba, y);
                        derecha = Math.max(derecha, x);
                        abajo = Math.max(abajo, y);
                    }
                }
            }
            if (derecha < izquierda || abajo < arriba) return original;
            return original.getSubimage(izquierda, arriba, derecha - izquierda + 1, abajo - arriba + 1);
        } catch (Exception excepcion) {
            return null;
        }
    }

    public static Image imagenLogo() {
        return LOGO;
    }

    public static Font inter(float tamano) {
        return INTER_REGULAR.deriveFont(tamano);
    }

    public static JLabel logo(int anchoMaximo, int altoMaximo) {
        JLabel etiqueta = new JLabel();
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (LOGO == null) {
            etiqueta.setText("Memory Game");
            etiqueta.setFont(poppinsBold(22));
            etiqueta.setForeground(AZUL.darker());
            etiqueta.setPreferredSize(new Dimension(anchoMaximo, altoMaximo));
            return etiqueta;
        }
        double escala = Math.min((double) anchoMaximo / LOGO.getWidth(null),
                (double) altoMaximo / LOGO.getHeight(null));
        int ancho = (int) (LOGO.getWidth(null) * escala);
        int alto = (int) (LOGO.getHeight(null) * escala);
        etiqueta.setIcon(new ImageIcon(LOGO.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)));
        etiqueta.setPreferredSize(new Dimension(anchoMaximo, altoMaximo));
        return etiqueta;
    }

    private static Font poppinsBold(float tamano) {
        Font base = POPPINS_BOLD == null ? POPPINS_REGULAR : POPPINS_BOLD;
        return base.deriveFont(Font.BOLD, tamano);
    }

    public static Font poppins(float tamano) {
        return poppinsBold(tamano);
    }

    public static JLabel titulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(poppinsBold(tamano));
        etiqueta.setForeground(Color.BLACK);
        return etiqueta;
    }

    public static JLabel texto(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(inter(tamano));
        etiqueta.setForeground(TEXTO);
        return etiqueta;
    }

    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(INTER_REGULAR.deriveFont(Font.BOLD, 14f));
        etiqueta.setForeground(Color.BLACK);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    public static JButton botonCircularConIcono(String recurso, int diametro, String descripcion) {
        JButton boton = new JButton() {
            @Override
            protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(getModel().isPressed() ? Color.BLACK.darker() : Color.BLACK);
                lienzo.fillOval(1, 1, getWidth() - 3, getHeight() - 3);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        boton.setToolTipText(descripcion);
        boton.getAccessibleContext().setAccessibleName(descripcion);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(diametro, diametro));
        boton.setMinimumSize(new Dimension(diametro, diametro));
        boton.setMaximumSize(new Dimension(diametro, diametro));
        try (InputStream archivo = EstiloInterfaz.class.getResourceAsStream(recurso)) {
            if (archivo != null) {
                Image original = javax.imageio.ImageIO.read(archivo);
                if (original != null) {
                    int tamanoIcono = Math.round(diametro * 0.58f);
                    boton.setIcon(new ImageIcon(original.getScaledInstance(tamanoIcono, tamanoIcono, Image.SCALE_SMOOTH)));
                }
            }
        } catch (Exception excepcion) {
            boton.setToolTipText(descripcion + " (icono no disponible)");
        }
        return boton;
    }

    public static JButton boton(String texto, Color color, int ancho, int alto, int fuente) {
        JButton boton = new JButton(texto) {
            @Override protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(new Color(0, 0, 0, 38));
                lienzo.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                Color colorActivo = getModel().isPressed() ? color.darker()
                        : getModel().isRollover() ? color.brighter() : color;
                lienzo.setColor(colorActivo);
                lienzo.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 8, 20, 20);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        boton.setFont((POPPINS_SEMIBOLD == null ? POPPINS_REGULAR : POPPINS_SEMIBOLD)
                .deriveFont(Font.BOLD, (float) fuente));
        boton.setForeground(Color.WHITE);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(ancho, alto));
        boton.setMaximumSize(new Dimension(ancho, alto));
        boton.setMinimumSize(new Dimension(ancho, alto));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
