package view;

import javax.swing.*;
import java.awt.*;

/** Contenedor CardLayout que agrupa las pantallas Swing del juego. */
public class PanelNavegacionJuego extends JPanel {
    public static final String BIENVENIDA = "bienvenida-juego";
    public static final String SELECCION = "seleccion-modo";
    public static final String PATRONES = "modo-patrones";
    public static final String MEMORIA = "modo-memoria";
    public static final String PREMIOS = "vista-premios";

    private final CardLayout distribucion = new CardLayout();
    private final PanelBienvenidaJuego panelBienvenida = new PanelBienvenidaJuego();
    private final PanelSeleccionModo panelSeleccionModo = new PanelSeleccionModo();
    private final PanelPatrones panelPatrones = new PanelPatrones();
    private final PanelMemoria panelMemoria = new PanelMemoria();
    private final PanelPremios panelPremios = new PanelPremios();
    private Runnable accionVolverAlPerfil = () -> { };
    private String pantallaActual = BIENVENIDA;

    public PanelNavegacionJuego() {
        super();
        setLayout(distribucion);
        setBackground(EstiloInterfaz.FONDO);
        add(panelBienvenida, BIENVENIDA);
        add(panelSeleccionModo, SELECCION);
        add(panelPatrones, PATRONES);
        add(panelMemoria, MEMORIA);
        add(panelPremios, PREMIOS);
        mostrarBienvenidaJuego();
        configurarTeclaEscape();
    }

    public PanelBienvenidaJuego obtenerPanelBienvenida() { return panelBienvenida; }
    public PanelSeleccionModo obtenerPanelSeleccionModo() { return panelSeleccionModo; }
    public PanelPatrones obtenerPanelPatrones() { return panelPatrones; }
    public PanelMemoria obtenerPanelMemoria() { return panelMemoria; }
    public PanelPremios obtenerPanelPremios() { return panelPremios; }

    public void mostrarBienvenidaJuego() { mostrarPantalla(BIENVENIDA); }
    public void mostrarSeleccionModo() { mostrarPantalla(SELECCION); }
    public void mostrarModoPatrones() { mostrarPantalla(PATRONES); }
    public void mostrarModoMemoria() { mostrarPantalla(MEMORIA); }
    public void mostrarPremios() { mostrarPantalla(PREMIOS); }

    public void accionVolverAlPerfil(Runnable accion) {
        accionVolverAlPerfil = accion == null ? () -> { } : accion;
    }

    public String obtenerPantallaActual() { return pantallaActual; }

    private void mostrarPantalla(String pantalla) {
        pantallaActual = pantalla;
        distribucion.show(this, pantalla);
        revalidate();
        repaint();
    }

    private void configurarTeclaEscape() {
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "volver-pantalla-anterior");
        getActionMap().put("volver-pantalla-anterior", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evento) {
                switch (pantallaActual) {
                    case PATRONES, MEMORIA, PREMIOS -> mostrarSeleccionModo();
                    case SELECCION -> mostrarBienvenidaJuego();
                    default -> accionVolverAlPerfil.run();
                }
            }
        });
    }
}