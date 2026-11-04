package view;

import model.CapturadorCamara;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VentanaAutenticacion extends JFrame {
    public static final String INICIO_SESION = "inicio";
    public static final String REGISTRO = "registro";
    public static final String CAPTURA_FACIAL = "captura";
    public static final String BIENVENIDA = "bienvenida";
    public static final String JUEGO = "juego";

    private final CardLayout distribucionPantallas = new CardLayout();
    private final JPanel panelPantallas = new JPanel(distribucionPantallas);
    private final PanelInicioSesion panelInicioSesion = new PanelInicioSesion();
    private final PanelRegistro panelRegistro = new PanelRegistro();
    private final PanelCapturaFacial panelCapturaFacial = new PanelCapturaFacial();
    private final PanelBienvenida panelBienvenida = new PanelBienvenida();
    private final PanelNavegacionJuego panelJuego = new PanelNavegacionJuego();

    public VentanaAutenticacion() {
        super("Memory Game - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(760, 540));
        setResizable(true);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        panelPantallas.add(panelInicioSesion, INICIO_SESION);
        panelPantallas.add(panelRegistro, REGISTRO);
        panelPantallas.add(panelCapturaFacial, CAPTURA_FACIAL);
        panelPantallas.add(panelBienvenida, BIENVENIDA);
        panelPantallas.add(panelJuego, JUEGO);
        setContentPane(panelPantallas);
        setIconImage(EstiloInterfaz.imagenLogo());
    }

    public void mostrarPantalla(String pantalla) { distribucionPantallas.show(panelPantallas, pantalla); }
    public PanelInicioSesion obtenerPanelInicioSesion() { return panelInicioSesion; }
    public PanelRegistro obtenerPanelRegistro() { return panelRegistro; }
    public PanelCapturaFacial obtenerPanelCapturaFacial() { return panelCapturaFacial; }
    public PanelBienvenida obtenerPanelBienvenida() { return panelBienvenida; }
    public PanelNavegacionJuego obtenerPanelNavegacionJuego() { return panelJuego; }

    public void mostrarAviso(String titulo, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public String obtenerCorreoIngresado() { return panelInicioSesion.obtenerCorreo(); }
    public char[] obtenerContrasenaIngresada() { return panelInicioSesion.obtenerContrasena(); }
    public boolean recordarCorreo() { return panelInicioSesion.recordarCorreo(); }
    public void establecerCorreoIngresado(String correo) { panelInicioSesion.establecerCorreo(correo); }
    public void limpiarContrasenaIngresada() { panelInicioSesion.limpiarContrasena(); }
    public void accionIngresar(Runnable accion) { panelInicioSesion.accionIngresar(accion); }
    public void accionAccesoFacial(Runnable accion) { panelInicioSesion.accionAccesoFacial(accion); }
    public void accionCrearCuenta(Runnable accion) { panelInicioSesion.accionCrearCuenta(accion); }

    public String obtenerNombreDeRegistro() { return panelRegistro.obtenerNombreUsuario(); }
    public String obtenerCorreoDeRegistro() { return panelRegistro.obtenerCorreo(); }
    public char[] obtenerContrasenaDeRegistro() { return panelRegistro.obtenerContrasena(); }
    public char[] obtenerConfirmacionDeRegistro() { return panelRegistro.obtenerConfirmacion(); }
    public void limpiarFormularioRegistro() { panelRegistro.limpiarCampos(); }
    public void accionRegistrar(Runnable accion) { panelRegistro.accionContinuar(accion); }
    public void accionVolverDesdeRegistro(Runnable accion) { panelRegistro.accionVolver(accion); }

    public void establecerModoCapturaFacial(boolean registro) { panelCapturaFacial.establecerModo(registro); }
    public void iniciarPrevisualizacionFacial(CapturadorCamara camara) { panelCapturaFacial.iniciarCamara(camara); }
    public void informarErrorDeCamara(String mensaje) { panelCapturaFacial.informarErrorCamara(mensaje); }
    public BufferedImage obtenerRostroCapturado() { return panelCapturaFacial.obtenerRostroCapturado(); }
    public void detenerPrevisualizacionFacial() { panelCapturaFacial.detenerPrevisualizacion(); }
    public void accionCapturarRostro(Runnable accion) { panelCapturaFacial.accionCapturar(accion); }
    public void accionVolverDesdeCaptura(Runnable accion) { panelCapturaFacial.accionVolver(accion); }
    public void accionCancelarCaptura(Runnable accion) { panelCapturaFacial.accionCancelar(accion); }

    public void establecerUsuarioBienvenida(String nombre) { panelBienvenida.establecerUsuario(nombre); }
    public void accionCerrarSesion(Runnable accion) { panelBienvenida.accionCerrarSesion(accion); }
    public void accionIniciarJuego(Runnable accion) { panelBienvenida.accionIniciarJuego(accion); }
    public void accionVolverDesdeJuego(Runnable accion) { panelJuego.accionVolverAlPerfil(accion); }
}
