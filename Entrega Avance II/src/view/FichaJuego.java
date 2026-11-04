package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Carta Swing compartida por Memoria y Patrones, con imágenes y giro animado. */
public class FichaJuego extends JButton {
    private static final String RUTA_REVERSO = "/recursos/cartas/1.png";
    private static final Map<Integer, BufferedImage> IMAGENES_CARAS = new HashMap<>();
    private static BufferedImage imagenReverso;
    private static final int DURACION_GIRO_MS = 280;

    private Color colorFicha = EstiloInterfaz.GRIS_FICHA;
    private boolean encontrada;
    private boolean caraVisible;
    private int indiceCara = -1;
    private float progresoGiro = 1f;
    private Timer animacionGiro;

    public FichaJuego() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));
    }

    /** Asigna una de las caras 0-17 del mazo, sin alterar las dimensiones de la carta. */
    public void establecerCara(int indice) {
        indiceCara = Math.floorMod(indice, 18);
        caraVisible = false;
        progresoGiro = 1f;
        repaint();
    }

    public void mostrarCara(int indice) {
        indiceCara = Math.floorMod(indice, 18);
        girarA(true);
    }

    public void mostrarReverso() { girarA(false); }

    public void establecerColor(Color color) {
        colorFicha = color;
        repaint();
    }

    public void establecerEncontrada(boolean estaEncontrada) {
        encontrada = estaEncontrada;
        setEnabled(!estaEncontrada);
        repaint();
    }

    private void girarA(boolean mostrarCara) {
        if (animacionGiro != null) animacionGiro.stop();
        if (caraVisible == mostrarCara) {
            progresoGiro = 1f;
            repaint();
            return;
        }
        final boolean mostrarAlCerrar = mostrarCara;
        final long inicio = System.nanoTime();
        animacionGiro = new Timer(15, evento -> {
            float progreso = Math.min(1f, (System.nanoTime() - inicio) / (DURACION_GIRO_MS * 1_000_000f));
            progresoGiro = progreso;
            if (progreso >= 0.5f && caraVisible != mostrarAlCerrar) caraVisible = mostrarAlCerrar;
            repaint();
            if (progreso >= 1f) {
                ((Timer) evento.getSource()).stop();
                progresoGiro = 1f;
                caraVisible = mostrarAlCerrar;
                repaint();
            }
        });
        animacionGiro.start();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        Graphics2D lienzo = (Graphics2D) graficos.create();
        lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        lienzo.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        lienzo.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int margenInferior = encontrada ? 2 : 5;
        lienzo.setColor(new Color(0, 0, 0, 32));
        lienzo.fillRoundRect(2, 4, getWidth() - 5, getHeight() - 5, 20, 20);
        Color colorActivo = getModel().isPressed() ? colorFicha.darker()
                : getModel().isRollover() && isEnabled() ? colorFicha.brighter() : colorFicha;
        lienzo.setColor(colorActivo);
        lienzo.fillRoundRect(0, 0, getWidth() - 4, getHeight() - margenInferior, 20, 20);

        int contenidoAncho = Math.max(1, getWidth());
        int contenidoAlto = Math.max(1, getHeight() - margenInferior);
        int anchoDibujado = Math.max(2, Math.round(contenidoAncho * Math.abs(1f - 2f * progresoGiro)));
        int x = (getWidth() - anchoDibujado) / 2;
        Shape recorte = new RoundRectangle2D.Double(x, 0, anchoDibujado, contenidoAlto, 18, 18);
        lienzo.setClip(recorte);
        BufferedImage imagen = cargarImagen(caraVisible ? indiceCara + 2 : -1);
        if (imagen != null) lienzo.drawImage(imagen, x, 0, anchoDibujado, contenidoAlto, null);
        else {
            lienzo.setColor(caraVisible ? EstiloInterfaz.FONDO : EstiloInterfaz.AZUL);
            lienzo.fillRoundRect(x, 0, anchoDibujado, contenidoAlto, 18, 18);
            lienzo.setColor(Color.WHITE);
            lienzo.setFont(EstiloInterfaz.poppins(Math.max(20, getHeight() / 3f)));
            String marca = caraVisible ? String.valueOf(indiceCara + 1) : "?";
            FontMetrics medidas = lienzo.getFontMetrics();
            lienzo.drawString(marca, getWidth() / 2 - medidas.stringWidth(marca) / 2,
                    getHeight() / 2 + medidas.getAscent() / 3);
        }
        lienzo.dispose();
    }

    private static BufferedImage cargarImagen(int numeroCara) {
        if (numeroCara < 0) {
            if (imagenReverso == null) imagenReverso = leerImagen(RUTA_REVERSO);
            return imagenReverso;
        }
        return IMAGENES_CARAS.computeIfAbsent(numeroCara,
                numero -> leerImagen("/recursos/cartas/" + numero + ".png"));
    }

    private static BufferedImage leerImagen(String ruta) {
        try {
            return ImageIO.read(FichaJuego.class.getResource(ruta));
        } catch (IOException | IllegalArgumentException | NullPointerException excepcion) {
            return null;
        }
    }
}
