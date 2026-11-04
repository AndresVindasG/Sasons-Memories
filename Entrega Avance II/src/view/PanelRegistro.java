package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistro extends JPanel {
    private final JTextField campoNombreUsuario = new JTextField();
    private final CampoCorreo campoCorreo = new CampoCorreo();
    private final CampoContrasena campoContrasena = new CampoContrasena();
    private final CampoContrasena campoConfirmacion = new CampoContrasena();
    private final JButton botonContinuar = EstiloInterfaz.boton("Continuar con registro facial", EstiloInterfaz.CAMPO, 430, 50, 15);
    private final JButton botonVolver = EstiloInterfaz.boton("Volver al inicio", EstiloInterfaz.GRIS, 190, 40, 14);

    public PanelRegistro() {
        setLayout(new BorderLayout());
        setBackground(EstiloInterfaz.AZUL);
        add(construirPanelMarca(), BorderLayout.WEST);

        JPanel areaFormulario = new JPanel(new GridBagLayout());
        areaFormulario.setBackground(EstiloInterfaz.AZUL);
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(EstiloInterfaz.AZUL);
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(new EmptyBorder(18, 44, 14, 44));

        JLabel titulo = EstiloInterfaz.titulo("Crea tu cuenta", 27);
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(titulo);
        panelFormulario.add(Box.createVerticalStrut(4));
        JLabel subtitulo = EstiloInterfaz.texto("Ingresa los datos solicitados para registrarte.", 14);
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(subtitulo);
        panelFormulario.add(Box.createVerticalStrut(10));

        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        agregarCampo(formulario, "Nombre de usuario", campoNombreUsuario, "Elige un nombre de usuario");
        agregarCampo(formulario, "Correo electrónico", campoCorreo, "nombre@correo.com");
        agregarCampo(formulario, "Contraseña", campoContrasena, "Mínimo 8 caracteres");
        agregarCampo(formulario, "Confirmar contraseña", campoConfirmacion, "Repite la contraseña");
        panelFormulario.add(formulario);
        panelFormulario.add(Box.createVerticalStrut(7));

        JLabel aviso = EstiloInterfaz.texto("El registro continúa con la cámara en tiempo real.", 13);
        aviso.setForeground(Color.WHITE);
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(aviso);
        panelFormulario.add(Box.createVerticalStrut(12));
        botonContinuar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(botonContinuar);
        panelFormulario.add(Box.createVerticalStrut(8));
        botonVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormulario.add(botonVolver);
        areaFormulario.add(panelFormulario);
        add(areaFormulario, BorderLayout.CENTER);
    }

    private JPanel construirPanelMarca() {
        JPanel marca = new JPanel();
        marca.setLayout(new BoxLayout(marca, BoxLayout.Y_AXIS));
        marca.setBackground(EstiloInterfaz.FONDO);
        marca.setBorder(new EmptyBorder(0, 24, 0, 24));
        marca.setPreferredSize(new Dimension(330, 550));
        marca.add(Box.createVerticalGlue());
        marca.add(EstiloInterfaz.logo(270, 205));
        marca.add(Box.createVerticalStrut(18));
        JLabel nombreAplicacion = EstiloInterfaz.titulo("Memory Game", 22);
        nombreAplicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        marca.add(nombreAplicacion);
        marca.add(Box.createVerticalStrut(5));
        JLabel mensaje = EstiloInterfaz.texto("Tu memoria, a prueba.", 14);
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        marca.add(mensaje);
        marca.add(Box.createVerticalGlue());
        return marca;
    }

    private void agregarCampo(JPanel panel, String titulo, JComponent campo, String indicador) {
        JLabel etiqueta = EstiloInterfaz.etiqueta(titulo);
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        if (campo instanceof CampoContrasena contrasena) {
            contrasena.configurar(EstiloInterfaz.inter(14), EstiloInterfaz.TEXTO, EstiloInterfaz.FONDO);
            contrasena.establecerIndicador(indicador);
        } else if (campo instanceof CampoCorreo correo) {
            correo.configurar(EstiloInterfaz.inter(14), EstiloInterfaz.TEXTO, EstiloInterfaz.FONDO);
            correo.establecerIndicador(indicador);
        } else if (campo instanceof JTextField campoTexto) {
            campoTexto.setFont(EstiloInterfaz.inter(14));
            campoTexto.setBackground(EstiloInterfaz.FONDO);
            campoTexto.setForeground(EstiloInterfaz.TEXTO);
            campoTexto.setCaretColor(EstiloInterfaz.TEXTO);
            campoTexto.setToolTipText(indicador);
            campoTexto.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(231, 221, 207), 1), new EmptyBorder(0, 13, 0, 12)));
            campoTexto.setPreferredSize(new Dimension(430, 40));
            campoTexto.setMaximumSize(new Dimension(430, 40));
        }
        JPanel filaCampo = new JPanel(new BorderLayout(0, 3));
        filaCampo.setOpaque(false);
        filaCampo.add(etiqueta, BorderLayout.NORTH);
        filaCampo.add(campo, BorderLayout.CENTER);
        filaCampo.setPreferredSize(new Dimension(430, 63));
        filaCampo.setMaximumSize(new Dimension(430, 63));
        filaCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(filaCampo);
        panel.add(Box.createVerticalStrut(5));
    }

    String obtenerNombreUsuario() { return campoNombreUsuario.getText().trim(); }
    String obtenerCorreo() { return campoCorreo.getText().trim(); }
    char[] obtenerContrasena() { return campoContrasena.getPassword(); }
    char[] obtenerConfirmacion() { return campoConfirmacion.getPassword(); }
    void limpiarCampos() { campoNombreUsuario.setText(""); campoCorreo.setText(""); campoContrasena.setText(""); campoConfirmacion.setText(""); }
    void accionContinuar(Runnable accion) { botonContinuar.addActionListener(evento -> accion.run()); }
    void accionVolver(Runnable accion) { botonVolver.addActionListener(evento -> accion.run()); }
}
