package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.function.Consumer;

/** Controles redondos de regreso y estado de sonido para los modos de juego. */
public class PanelControlesJuego extends JPanel {
    private static final String RECURSO_REGRESAR = "/recursos/iconos/icono_regresar.png";
    private static final String RECURSO_SONIDO = "/recursos/iconos/icono_sonido.png";
    private static final String RECURSO_SILENCIO = "/recursos/iconos/icono_sonido_silenciado.png";

    private final JButton botonRegresar = EstiloInterfaz.botonCircularConIcono(
            RECURSO_REGRESAR, 60, "Volver al selector de modo");
    private final JButton botonSonido = EstiloInterfaz.botonCircularConIcono(
            RECURSO_SONIDO, 60, "Silenciar música");
    private boolean sonidoActivado = true;
    private Consumer<Boolean> accionCambiarSonido = activado -> { };

    public PanelControlesJuego() {
        super(new FlowLayout(FlowLayout.LEFT, 14, 0));
        setOpaque(false);
        botonSonido.addActionListener(evento -> alternarSonido());
        add(botonRegresar);
        add(botonSonido);
    }

    public void accionRegresar(Runnable accion) {
        for (var oyente : botonRegresar.getActionListeners()) botonRegresar.removeActionListener(oyente);
        botonRegresar.addActionListener(evento -> accion.run());
    }

    public void accionCambiarSonido(Consumer<Boolean> accion) {
        accionCambiarSonido = accion == null ? activado -> { } : accion;
    }

    public void establecerSonidoActivado(boolean activado) {
        sonidoActivado = activado;
        String descripcion = activado ? "Silenciar música" : "Activar sonido";
        botonSonido.setToolTipText(descripcion);
        botonSonido.getAccessibleContext().setAccessibleName(descripcion);
        String recurso = activado ? RECURSO_SONIDO : RECURSO_SILENCIO;
        botonSonido.setIcon(cargarIcono(recurso, 35));
    }

    private void alternarSonido() {
        establecerSonidoActivado(!sonidoActivado);
        accionCambiarSonido.accept(sonidoActivado);
    }

    private ImageIcon cargarIcono(String recurso, int tamano) {
        try (InputStream archivo = PanelControlesJuego.class.getResourceAsStream(recurso)) {
            if (archivo == null) return null;
            Image imagen = ImageIO.read(archivo);
            return imagen == null ? null : new ImageIcon(imagen.getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH));
        } catch (Exception excepcion) {
            return null;
        }
    }
}
