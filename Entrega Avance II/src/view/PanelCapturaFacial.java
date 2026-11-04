package view;

import model.CapturadorCamara;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelCapturaFacial extends JPanel {
    private final JLabel etiquetaTitulo = EstiloInterfaz.titulo("Mira a la cámara", 28);
    private final JLabel etiquetaEstado = EstiloInterfaz.texto("Iniciando cámara...", 15);
    private final Dimension dimensionPrevisualizacion = calcularDimensionPrevisualizacion();
    private final JLabel etiquetaVideo = new JLabel("◉", SwingConstants.CENTER);
    private final JButton botonCapturar = EstiloInterfaz.boton("Capturar rostro", EstiloInterfaz.AZUL, 190, 53, 16);
    private final JButton botonVolver = EstiloInterfaz.boton("Volver", EstiloInterfaz.GRIS, 145, 48, 15);
    private final JButton botonCancelar = EstiloInterfaz.boton("Cancelar", EstiloInterfaz.GRIS, 145, 48, 15);
    private final Timer refrescoCamara;
    private CapturadorCamara capturador;
    private BufferedImage rostroCapturado;

    public PanelCapturaFacial() {
        setBackground(EstiloInterfaz.FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 0, 24, 0));
        add(Box.createVerticalGlue());
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaEstado.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(etiquetaTitulo);
        add(Box.createVerticalStrut(5));
        JLabel indicacion = EstiloInterfaz.texto("Centra el rostro, mira al frente y mantén una iluminación uniforme.", 14);
        indicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(indicacion);
        add(Box.createVerticalStrut(14));
        etiquetaVideo.setOpaque(true);
        etiquetaVideo.setBackground(Color.BLACK);
        etiquetaVideo.setForeground(Color.WHITE);
        etiquetaVideo.setFont(new Font("SansSerif", Font.PLAIN, 115));
        etiquetaVideo.setPreferredSize(dimensionPrevisualizacion);
        etiquetaVideo.setMinimumSize(new Dimension(360, 248));
        etiquetaVideo.setMaximumSize(dimensionPrevisualizacion);
        etiquetaVideo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(etiquetaVideo);
        add(Box.createVerticalStrut(9));
        add(etiquetaEstado);
        add(Box.createVerticalStrut(10));
        botonCapturar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCapturar.setEnabled(false);
        add(botonCapturar);
        add(Box.createVerticalStrut(7));
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        acciones.setOpaque(false);
        acciones.add(botonVolver);
        acciones.add(botonCancelar);
        add(acciones);
        add(Box.createVerticalGlue());
        refrescoCamara = new Timer(40, evento -> actualizarPrevisualizacion());
    }

    void establecerModo(boolean esRegistro) {
        etiquetaTitulo.setText(esRegistro ? "Registra tu rostro" : "Mira a la cámara");
        botonVolver.setVisible(esRegistro);
        botonCancelar.setVisible(!esRegistro);
        etiquetaEstado.setText("Iniciando cámara...");
        etiquetaVideo.setIcon(null);
        etiquetaVideo.setText("◉");
        botonCapturar.setEnabled(false);
        rostroCapturado = null;
    }

    void iniciarCamara(CapturadorCamara capturador) {
        this.capturador = capturador;
        refrescoCamara.start();
    }

    void informarErrorCamara(String mensaje) {
        etiquetaEstado.setText(mensaje);
        botonCapturar.setEnabled(false);
    }

    BufferedImage obtenerRostroCapturado() { return rostroCapturado; }
    void accionCapturar(Runnable accion) { botonCapturar.addActionListener(evento -> { guardarRostroDeCamara(); if (rostroCapturado != null) accion.run(); }); }
    void accionVolver(Runnable accion) { botonVolver.addActionListener(evento -> accion.run()); }
    void accionCancelar(Runnable accion) { botonCancelar.addActionListener(evento -> accion.run()); }

    void detenerPrevisualizacion() {
        refrescoCamara.stop();
        capturador = null;
        etiquetaVideo.setIcon(null);
        etiquetaVideo.setText("◉");
        botonCapturar.setEnabled(false);
    }

    private void actualizarPrevisualizacion() {
        if (capturador == null) return;
        CapturadorCamara.FotogramaActual fotograma = capturador.obtenerFotogramaActual();
        if (fotograma == null) {
            if (capturador.obtenerErrorCamara() != null) informarErrorCamara(capturador.obtenerErrorCamara());
            return;
        }
        BufferedImage imagen = fotograma.obtenerImagenPrevia();
        Image escalada = imagen.getScaledInstance(dimensionPrevisualizacion.width,
                dimensionPrevisualizacion.height, Image.SCALE_SMOOTH);
        etiquetaVideo.setText("");
        etiquetaVideo.setIcon(new ImageIcon(escalada));
        if (fotograma.tieneRostro()) {
            etiquetaEstado.setText("Rostro detectado. Mantén la posición y captura.");
            botonCapturar.setEnabled(true);
        } else {
            etiquetaEstado.setText("Buscando un rostro... centra tu cara en la imagen.");
            botonCapturar.setEnabled(false);
        }
    }

    private void guardarRostroDeCamara() { rostroCapturado = capturador == null ? null : capturador.capturarRostroActual(); }

    private static Dimension calcularDimensionPrevisualizacion() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int ancho = Math.max(420, Math.min(680, (int) (pantalla.width * 0.40)));
        if (pantalla.height < 700) ancho = Math.min(ancho, 420);
        return new Dimension(ancho, (int) Math.round(ancho * 330.0 / 480.0));
    }
}
