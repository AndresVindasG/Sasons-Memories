package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelBienvenida extends JPanel {
    private final JLabel etiquetaNombre = EstiloInterfaz.titulo("", 30);
    private final JButton botonIniciarJuego = EstiloInterfaz.boton("Continuar al juego", EstiloInterfaz.AZUL, 220, 54, 16);
    private final JButton botonCerrarSesion = EstiloInterfaz.boton("Cerrar sesión", EstiloInterfaz.GRIS, 190, 45, 14);
    public PanelBienvenida() {
        setBackground(EstiloInterfaz.FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 0, 24, 0));
        add(Box.createVerticalGlue());
        JLabel titulo = EstiloInterfaz.titulo("¡Inicio de sesión exitoso!", 28);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonIniciarJuego.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(18));
        add(etiquetaNombre);
        add(Box.createVerticalStrut(30));
        add(botonIniciarJuego);
        add(Box.createVerticalStrut(10));
        add(botonCerrarSesion);
        add(Box.createVerticalGlue());
    }
    void establecerUsuario(String nombreUsuario) { etiquetaNombre.setText("Bienvenido, " + nombreUsuario); }
    void accionCerrarSesion(Runnable accion) { botonCerrarSesion.addActionListener(evento -> accion.run()); }
    void accionIniciarJuego(Runnable accion) { botonIniciarJuego.addActionListener(evento -> accion.run()); }
}
