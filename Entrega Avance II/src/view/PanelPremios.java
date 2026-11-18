package view;

import model.EntradaRanking;
import model.ModalidadRanking;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
// Presenta el Top 5 separado por modalidad y el convertidor del BCCR.
public class PanelPremios extends JPanel {
    private final JButton botonRegresar = EstiloInterfaz.botonCircularConIcono(
            "/recursos/iconos/icono_regresar.png", 60, "Volver al selector de modo");
    private final JButton botonMemoria = EstiloInterfaz.boton("Memoria", EstiloInterfaz.AZUL, 170, 44, 15);
    private final JButton botonPatrones = EstiloInterfaz.boton("Patrones", EstiloInterfaz.CARBON, 170, 44, 15);
    private final JButton botonActualizar = EstiloInterfaz.boton(
            "Actualizar BCCR", EstiloInterfaz.CARBON, 180, 44, 15);
    private final JButton botonConvertirDolares = EstiloInterfaz.boton(
            "Convertir a colones", EstiloInterfaz.AZUL, 190, 42, 14);
    private final JButton botonConvertirColones = EstiloInterfaz.boton(
            "Convertir a dólares", EstiloInterfaz.AZUL, 190, 42, 14);
    private final JButton botonCalcularPremio = EstiloInterfaz.boton(
            "Calcular premio", EstiloInterfaz.AZUL, 190, 42, 14);
    private final JTextField campoDolares = new JTextField("10", 8);
    private final JTextField campoColones = new JTextField("5000", 8);
    private final JTextField campoMontoPremio = new JTextField("0", 8);
    private final JLabel etiquetaEstadoBCCR = EstiloInterfaz.texto(
            "El tipo de cambio se consulta en el BCCR al abrir esta pantalla.", 14);
    private final JLabel etiquetaResultadoColones = EstiloInterfaz.texto("Resultado: —", 14);
    private final JLabel etiquetaResultadoDolares = EstiloInterfaz.texto("Resultado: —", 14);
    private final JLabel etiquetaResultadoPremio = EstiloInterfaz.texto("Premio calculado: —", 14);

    private final JLabel etiquetaTituloRanking = EstiloInterfaz.titulo("Top 5 · Memoria", 25);
    private final JLabel etiquetaDetalleRanking = EstiloInterfaz.texto(
            "Clasificación ordenada por cantidad de partidas ganadas.", 12);
    private final JPanel listaRanking = new JPanel();
    private final NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CR"));
    private Runnable accionRegresar = () -> { };
    private Runnable accionActualizarTipoCambio = () -> { };
    private Consumer<ModalidadRanking> accionCambiarModalidad = modalidad -> { };
    private DoubleConsumer accionConvertirDolares = monto -> { };
    private DoubleConsumer accionConvertirColones = monto -> { };
    private DoubleConsumer accionCalcularPremio = monto -> { };
    private ModalidadRanking modalidadSeleccionada = ModalidadRanking.MEMORIA;

    public PanelPremios() {
        super(new BorderLayout(0, 0));
        setBackground(EstiloInterfaz.FONDO);
        add(construirCabecera(), BorderLayout.NORTH);
        add(construirClasificacion(), BorderLayout.CENTER);
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);
        panelInferior.add(construirPanelConversion());
        panelInferior.add(construirPanelPremio());
        add(panelInferior, BorderLayout.SOUTH);
        botonRegresar.addActionListener(evento -> accionRegresar.run());
        botonMemoria.addActionListener(evento -> accionCambiarModalidad.accept(ModalidadRanking.MEMORIA));
        botonPatrones.addActionListener(evento -> accionCambiarModalidad.accept(ModalidadRanking.PATRONES));
        botonActualizar.addActionListener(evento -> accionActualizarTipoCambio.run());
        botonConvertirDolares.addActionListener(evento -> convertir(campoDolares, accionConvertirDolares));
        botonConvertirColones.addActionListener(evento -> convertir(campoColones, accionConvertirColones));
        botonCalcularPremio.addActionListener(evento -> calcularPremioDesdeCampo());
        formatoMoneda.setMinimumFractionDigits(2);
        formatoMoneda.setMaximumFractionDigits(2);
        establecerModalidad(ModalidadRanking.MEMORIA);
        establecerRanking(List.of());
    }

    public void accionRegresar(Runnable accion) { accionRegresar = accion == null ? () -> { } : accion; }
    public void accionActualizarTipoCambio(Runnable accion) {
        accionActualizarTipoCambio = accion == null ? () -> { } : accion;
    }
    public void accionCambiarModalidad(Consumer<ModalidadRanking> accion) {
        accionCambiarModalidad = accion == null ? modalidad -> { } : accion;
    }
    public void accionConvertirDolares(DoubleConsumer accion) {
        accionConvertirDolares = accion == null ? monto -> { } : accion;
    }
    public void accionConvertirColones(DoubleConsumer accion) {
        accionConvertirColones = accion == null ? monto -> { } : accion;
    }
    // El controlador aporta la fórmula; la vista solo dispara el cálculo al tocar el botón.
    public void accionCalcularPremio(DoubleConsumer accion) {
        accionCalcularPremio = accion == null ? monto -> { } : accion;
    }

    public void establecerModalidad(ModalidadRanking modalidad) {
        modalidadSeleccionada = modalidad == null ? ModalidadRanking.MEMORIA : modalidad;
        botonMemoria.setBackground(modalidadSeleccionada == ModalidadRanking.MEMORIA
                ? EstiloInterfaz.AZUL : EstiloInterfaz.CARBON);
        botonPatrones.setBackground(modalidadSeleccionada == ModalidadRanking.PATRONES
                ? EstiloInterfaz.AZUL : EstiloInterfaz.CARBON);
        etiquetaTituloRanking.setText("Top 5 · " + (modalidadSeleccionada == ModalidadRanking.MEMORIA
                ? "Memoria" : "Patrones"));
        etiquetaDetalleRanking.setText(modalidadSeleccionada == ModalidadRanking.PATRONES
                ? "Clasificación ordenada por cantidad de secuencias completadas correctamente."
                : "Clasificación ordenada por cantidad de partidas ganadas.");
        revalidate();
    }

    public ModalidadRanking obtenerModalidadSeleccionada() { return modalidadSeleccionada; }
    // Actualiza las filas del Top 5 que corresponde al modo seleccionado.
    public void establecerRanking(List<EntradaRanking> entradas) {
        listaRanking.removeAll();
        listaRanking.setLayout(new BoxLayout(listaRanking, BoxLayout.Y_AXIS));
        if (entradas == null || entradas.isEmpty()) {
            JLabel sinResultados = EstiloInterfaz.texto("Aún no hay resultados registrados.", 17);
            sinResultados.setForeground(Color.WHITE);
            sinResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaRanking.add(Box.createVerticalGlue());
            listaRanking.add(sinResultados);
            listaRanking.add(Box.createVerticalGlue());
        } else {
            int puesto = 1;
            for (EntradaRanking entrada : entradas) listaRanking.add(crearFilaRanking(puesto++, entrada));
            listaRanking.add(Box.createVerticalGlue());
        }
        listaRanking.revalidate();
        listaRanking.repaint();
    }

    public void establecerErrorRanking(String mensaje) {
        listaRanking.removeAll();
        JLabel error = EstiloInterfaz.texto(mensaje, 15);
        error.setForeground(Color.WHITE);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        listaRanking.add(Box.createVerticalGlue());
        listaRanking.add(error);
        listaRanking.add(Box.createVerticalGlue());
        listaRanking.revalidate();
        listaRanking.repaint();
    }

    public void establecerCargaBCCR() { etiquetaEstadoBCCR.setText("Consultando el tipo de cambio del BCCR…"); }
    // Muestra el valor y fecha que devolvió el servicio del BCCR.
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
    // El resultado del premio va en su propio control, separado de las conversiones monetarias.
    public void establecerErrorPremio(String mensaje) {
        etiquetaResultadoPremio.setText(mensaje);
    }
    public void establecerResultadoPremio(double monto) {
        etiquetaResultadoPremio.setText("Premio calculado: " + formatoMoneda.format(monto));
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
    // Reserva controles para cambiar entre los dos rankings independientes.
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
        clasificacion.setBorder(new EmptyBorder(16, 24, 18, 24));

        JPanel cabeceraRanking = new JPanel(new GridLayout(2, 1, 0, 2));
        cabeceraRanking.setOpaque(false);
        etiquetaTituloRanking.setForeground(Color.WHITE);
        etiquetaTituloRanking.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaDetalleRanking.setForeground(Color.WHITE);
        etiquetaDetalleRanking.setHorizontalAlignment(SwingConstants.CENTER);
        cabeceraRanking.add(etiquetaTituloRanking);
        cabeceraRanking.add(etiquetaDetalleRanking);
        clasificacion.add(cabeceraRanking, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setOpaque(false);
        JPanel selectores = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        selectores.setOpaque(false);
        selectores.add(botonMemoria);
        selectores.add(botonPatrones);
        contenido.add(selectores, BorderLayout.NORTH);
        listaRanking.setOpaque(false);
        contenido.add(listaRanking, BorderLayout.CENTER);
        clasificacion.add(contenido, BorderLayout.CENTER);
        return clasificacion;
    }
    // Mantiene la conversión monetaria aparte del conteo del Top 5.
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

    // Mantiene el cálculo del premio en una fila aparte; no comparte campos con el convertidor.
    private JPanel construirPanelPremio() {
        JPanel panelPremio = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        panelPremio.setBackground(EstiloInterfaz.FONDO);
        panelPremio.add(EstiloInterfaz.texto("Monto:", 14));
        campoMontoPremio.setFont(EstiloInterfaz.inter(14));
        campoMontoPremio.setColumns(8);
        panelPremio.add(campoMontoPremio);
        panelPremio.add(botonCalcularPremio);
        panelPremio.add(etiquetaResultadoPremio);
        return panelPremio;
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

    private JPanel crearFilaRanking(int puesto, EntradaRanking entrada) {
        JPanel fila = new JPanel(new BorderLayout(16, 0));
        fila.setOpaque(true);
        fila.setBackground(new Color(255, 255, 255, 220));
        fila.setBorder(new EmptyBorder(7, 16, 7, 16));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        JLabel numero = EstiloInterfaz.titulo(puesto + ".", 17);
        numero.setForeground(EstiloInterfaz.CARBON);
        JLabel nombre = EstiloInterfaz.texto(entrada.nombreUsuario(), 16);
        String textoResultado = modalidadSeleccionada == ModalidadRanking.PATRONES
                ? entrada.victorias() + " secuencias" : entrada.victorias() + " victorias";
        JLabel resultado = EstiloInterfaz.titulo(textoResultado, 14);
        resultado.setForeground(EstiloInterfaz.CARBON);
        fila.add(numero, BorderLayout.WEST);
        fila.add(nombre, BorderLayout.CENTER);
        fila.add(resultado, BorderLayout.EAST);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fila;
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

    // Lee el monto del premio y solo lo envía al controlador si tiene un valor numérico válido.
    private void calcularPremioDesdeCampo() {
        try {
            double monto = Double.parseDouble(campoMontoPremio.getText().trim().replace(',', '.'));
            if (!Double.isFinite(monto) || monto < 0) throw new NumberFormatException();
            accionCalcularPremio.accept(monto);
        } catch (NumberFormatException excepcion) {
            establecerErrorPremio("Ingresa un monto válido.");
        }
    }
}
