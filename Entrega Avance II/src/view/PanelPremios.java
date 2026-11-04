package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.DoubleConsumer;

/** Vista de premios con consulta del tipo de cambio y conversión USD/CRC. */
public class PanelPremios extends JPanel {
    private final JButton botonRegresar = EstiloInterfaz.botonCircularConIcono(
            "/recursos/iconos/icono_regresar.png", 60, "Volver al selector de modo");
    private final JButton botonActualizar = EstiloInterfaz.boton("Actualizar BCCR", EstiloInterfaz.CARBON, 180, 44, 15);
    private final JButton botonConvertirDolares = EstiloInterfaz.boton("Convertir a colones", EstiloInterfaz.AZUL, 190, 42, 14);
    private final JButton botonConvertirColones = EstiloInterfaz.boton("Convertir a dólares", EstiloInterfaz.AZUL, 190, 42, 14);
    private final JTextField campoDolares = new JTextField("10", 8);
    private final JTextField campoColones = new JTextField("5000", 8);
    private final JLabel etiquetaEstadoBCCR = EstiloInterfaz.texto(
            "El tipo de cambio se consulta en el BCCR al abrir esta pantalla.", 14);
    private final JLabel etiquetaResultadoColones = EstiloInterfaz.texto("Resultado: —", 14);
    private final JLabel etiquetaResultadoDolares = EstiloInterfaz.texto("Resultado: —", 14);
    private final NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CR"));
    private Runnable accionRegresar = () -> { };
    private Runnable accionActualizarTipoCambio = () -> { };
    private DoubleConsumer accionConvertirDolares = monto -> { };
    private DoubleConsumer accionConvertirColones = monto -> { };

    public PanelPremios() {
        super(new BorderLayout(0, 0));
        setBackground(EstiloInterfaz.FONDO);
        add(construirCabecera(), BorderLayout.NORTH);
        add(construirClasificacion(), BorderLayout.CENTER);
        add(construirPanelConversion(), BorderLayout.SOUTH);
        botonRegresar.addActionListener(evento -> accionRegresar.run());
        botonActualizar.addActionListener(evento -> accionActualizarTipoCambio.run());
        botonConvertirDolares.addActionListener(evento -> convertir(campoDolares, accionConvertirDolares));
        botonConvertirColones.addActionListener(evento -> convertir(campoColones, accionConvertirColones));
        formatoMoneda.setMinimumFractionDigits(2);
        formatoMoneda.setMaximumFractionDigits(2);
    }

    public void accionRegresar(Runnable accion) { accionRegresar = accion == null ? () -> { } : accion; }
    public void accionActualizarTipoCambio(Runnable accion) {
        accionActualizarTipoCambio = accion == null ? () -> { } : accion;
    }
    public void accionConvertirDolares(DoubleConsumer accion) {
        accionConvertirDolares = accion == null ? monto -> { } : accion;
    }
    public void accionConvertirColones(DoubleConsumer accion) {
        accionConvertirColones = accion == null ? monto -> { } : accion;
    }
    public void establecerCargaBCCR() { etiquetaEstadoBCCR.setText("Consultando el tipo de cambio del BCCR…"); }
    public void establecerTipoCambio(double colonesPorDolar, String fecha) {
        etiquetaEstadoBCCR.setText("BCCR · Compra del dólar · " + fecha + " · $1 = ₡"
                + formatoMoneda.format(colonesPorDolar));
    }
    public void establecerErrorBCCR(String mensaje) { etiquetaEstadoBCCR.setText(mensaje); }
    public void establecerResultadoColones(double monto) {
        etiquetaResultadoColones.setText("Resultado: ₡" + formatoMoneda.format(monto));
    }
    public void establecerResultadoDolares(double monto) {
        etiquetaResultadoDolares.setText("Resultado: $" + formatoMoneda.format(monto));
    }
    public void establecerErrorConversion(String mensaje) {
        etiquetaResultadoColones.setText(mensaje);
        etiquetaResultadoDolares.setText(mensaje);
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(12, 26, 10, 26));
        cabecera.setPreferredSize(new Dimension(800, 100));
        cabecera.add(botonRegresar, BorderLayout.WEST);
        JLabel titulo = EstiloInterfaz.titulo("Premios", 28);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(titulo, BorderLayout.CENTER);
        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        espacio.setPreferredSize(new Dimension(88, 60));
        cabecera.add(espacio, BorderLayout.EAST);
        return cabecera;
    }

    private JPanel construirClasificacion() {
        JPanel clasificacion = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(EstiloInterfaz.AZUL);
                lienzo.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 40, 40);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        clasificacion.setOpaque(false);
        clasificacion.setBorder(new EmptyBorder(20, 28, 12, 28));
        JLabel titulo = EstiloInterfaz.titulo("Top Nice de Sason’s Memories", 28);
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        clasificacion.add(titulo, BorderLayout.NORTH);
        clasificacion.add(construirPodio(), BorderLayout.CENTER);
        return clasificacion;
    }

    private JPanel construirPanelConversion() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(EstiloInterfaz.FONDO);
        panel.setBorder(new EmptyBorder(10, 24, 18, 24));
        JPanel barraBCCR = new JPanel(new BorderLayout(12, 0));
        barraBCCR.setOpaque(false);
        barraBCCR.add(etiquetaEstadoBCCR, BorderLayout.CENTER);
        barraBCCR.add(botonActualizar, BorderLayout.EAST);
        panel.add(barraBCCR, BorderLayout.NORTH);

        JPanel conversiones = new JPanel(new GridLayout(1, 2, 24, 0));
        conversiones.setOpaque(false);
        conversiones.add(construirConvertidor("Dólares a colones", campoDolares, botonConvertirDolares,
                etiquetaResultadoColones));
        conversiones.add(construirConvertidor("Colones a dólares", campoColones, botonConvertirColones,
                etiquetaResultadoDolares));
        panel.add(conversiones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirConvertidor(String titulo, JTextField campo, JButton boton, JLabel resultado) {
        JPanel convertidor = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        convertidor.setOpaque(false);
        JLabel etiqueta = EstiloInterfaz.texto(titulo + ":", 14);
        campo.setFont(EstiloInterfaz.inter(14));
        campo.setColumns(8);
        campo.setMaximumSize(new Dimension(100, 34));
        resultado.setForeground(EstiloInterfaz.CARBON);
        convertidor.add(etiqueta);
        convertidor.add(campo);
        convertidor.add(boton);
        convertidor.add(resultado);
        return convertidor;
    }

    private JPanel construirPodio() {
        JPanel podio = new JPanel(new GridBagLayout());
        podio.setOpaque(false);
        String[] puestos = {"4.º", "2.º", "1.º", "3.º", "5.º"};
        int[] alturasRelativas = {64, 82, 100, 82, 64};
        int altoReferencia = Math.min(350, Math.max(200,
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.34)));
        int anchoColumna = Math.min(172, Math.max(112,
                (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.10)));
        for (int indice = 0; indice < puestos.length; indice++) {
            int alto = altoReferencia * alturasRelativas[indice] / 100;
            GridBagConstraints posicion = new GridBagConstraints();
            posicion.gridx = indice;
            posicion.gridy = 0;
            posicion.weightx = 1;
            posicion.weighty = 1;
            posicion.anchor = GridBagConstraints.SOUTH;
            posicion.insets = new Insets(0, 7, 0, 7);
            podio.add(new ColumnaPodio(puestos[indice], "Jugador " + (indice + 1),
                    "Premio", anchoColumna, alto), posicion);
        }
        return podio;
    }

    private void convertir(JTextField campo, DoubleConsumer accion) {
        try {
            double monto = Double.parseDouble(campo.getText().trim().replace(',', '.'));
            if (!Double.isFinite(monto) || monto < 0) throw new NumberFormatException();
            accion.accept(monto);
        } catch (NumberFormatException excepcion) {
            establecerErrorConversion("Ingresa un monto válido.");
        }
    }

    private static final class ColumnaPodio extends JPanel {
        private final int ancho;
        private final int alto;

        ColumnaPodio(String posicion, String usuario, String premio, int ancho, int alto) {
            this.ancho = ancho;
            this.alto = alto;
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 8, 15, 8));
            JLabel puesto = EstiloInterfaz.titulo(posicion, 23);
            JLabel nombre = EstiloInterfaz.titulo(usuario, 15);
            JLabel recompensa = EstiloInterfaz.titulo(premio, 14);
            for (JLabel etiqueta : new JLabel[]{puesto, nombre, recompensa}) {
                etiqueta.setForeground(Color.WHITE);
                etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
            }
            add(puesto);
            add(Box.createVerticalStrut(3));
            add(nombre);
            add(Box.createVerticalGlue());
            add(recompensa);
        }

        @Override public Dimension getPreferredSize() { return new Dimension(ancho, alto); }

        @Override protected void paintComponent(Graphics graficos) {
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(EstiloInterfaz.CARBON);
            lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
            lienzo.dispose();
            super.paintComponent(graficos);
        }
    }
}
