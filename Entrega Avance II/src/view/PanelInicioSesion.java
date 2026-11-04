package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelInicioSesion extends JPanel {
    private final CampoCorreo campoCorreo = new CampoCorreo();
    private final CampoContrasena campoContrasena = new CampoContrasena();
    private final JCheckBox casillaRecordarme = new JCheckBox("Recordarme en este equipo");
    private final JButton botonIngresar = EstiloInterfaz.boton("Ingresar", EstiloInterfaz.CAMPO, 430, 50, 16);
    private final JButton botonAccesoFacial = EstiloInterfaz.boton("Utilizar reconocimiento facial", EstiloInterfaz.AZUL, 430, 48, 15);
    private final JButton botonCrearCuenta = EstiloInterfaz.boton("Crear cuenta", EstiloInterfaz.GRIS, 190, 38, 14);

    public PanelInicioSesion() {
        setBackground(EstiloInterfaz.FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(8, 0, 8, 0));
        add(Box.createVerticalGlue());
        agregarCentrado(EstiloInterfaz.logo(250, 145));
        add(Box.createVerticalStrut(2));
        agregarCentrado(EstiloInterfaz.titulo("¡Bienvenido de vuelta!", 25));
        add(Box.createVerticalStrut(2));
        agregarCentrado(EstiloInterfaz.texto("Ingresa tu correo y contraseña para acceder a tu cuenta.", 14));
        add(Box.createVerticalStrut(9));
        add(construirFormulario());
        add(Box.createVerticalStrut(5));
        agregarCentrado(botonAccesoFacial);
        add(Box.createVerticalStrut(5));
        agregarCentrado(botonIngresar);
        add(Box.createVerticalStrut(2));
        casillaRecordarme.setOpaque(false);
        casillaRecordarme.setFont(EstiloInterfaz.inter(13));
        casillaRecordarme.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(casillaRecordarme);
        add(Box.createVerticalStrut(2));
        agregarCentrado(EstiloInterfaz.texto("¿Aún no tienes cuenta?", 13));
        add(Box.createVerticalStrut(2));
        agregarCentrado(botonCrearCuenta);
        add(Box.createVerticalGlue());
    }

    private JPanel construirFormulario() {
        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setPreferredSize(new Dimension(430, 136));
        formulario.setMaximumSize(new Dimension(430, 136));
        formulario.setAlignmentX(Component.CENTER_ALIGNMENT);
        configurarCampo(campoCorreo);
        formulario.add(crearCampoEtiquetado("Correo electrónico", campoCorreo));
        configurarCampo(campoContrasena);
        formulario.add(Box.createVerticalStrut(7));
        formulario.add(crearCampoEtiquetado("Contraseña", campoContrasena));
        return formulario;
    }

    private JPanel crearCampoEtiquetado(String titulo, JComponent campo) {
        JPanel filaCampo = new JPanel(new BorderLayout(0, 4));
        filaCampo.setOpaque(false);
        JLabel etiqueta = EstiloInterfaz.etiqueta(titulo);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        filaCampo.add(etiqueta, BorderLayout.NORTH);
        filaCampo.add(campo, BorderLayout.CENTER);
        filaCampo.setPreferredSize(new Dimension(430, 64));
        filaCampo.setMaximumSize(new Dimension(430, 64));
        filaCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return filaCampo;
    }

    private void configurarCampo(CampoContrasena campo) {
        campo.configurar(EstiloInterfaz.inter(14), EstiloInterfaz.TEXTO, Color.WHITE);
    }

    private void configurarCampo(CampoCorreo campo) {
        campo.configurar(EstiloInterfaz.inter(14), EstiloInterfaz.TEXTO, Color.WHITE);
    }

    private void agregarCentrado(Component componente) { if (componente instanceof JComponent componenteSwing) componenteSwing.setAlignmentX(Component.CENTER_ALIGNMENT); add(componente); }
    String obtenerCorreo() { return campoCorreo.getText().trim(); }
    char[] obtenerContrasena() { return campoContrasena.getPassword(); }
    boolean recordarCorreo() { return casillaRecordarme.isSelected(); }
    void establecerCorreo(String correo) { campoCorreo.setText(correo); }
    void limpiarContrasena() { campoContrasena.setText(""); }
    void accionIngresar(Runnable accion) { botonIngresar.addActionListener(evento -> accion.run()); }
    void accionAccesoFacial(Runnable accion) { botonAccesoFacial.addActionListener(evento -> accion.run()); }
    void accionCrearCuenta(Runnable accion) { botonCrearCuenta.addActionListener(evento -> accion.run()); }
}
