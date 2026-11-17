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

/** Clasificación por victorias y herramientas monetarias independientes del juego. */
public class PanelPremios extends JPanel {
    private final JButton botonRegresar = EstiloInterfaz.botonCircularConIcono(
            "/recursos/iconos/icono_regresar.png", 60, "Volver al selector de modo");
    private final JButton botonMemoria = EstiloInterfaz.boton("Memoria", EstiloInterfaz.AZUL, 170, 44, 15);
    private final JButton botonPatrones = EstiloInterfaz.boton("Patrones", EstiloInterfaz.CARBON, 170, 44, 15);
    private final JButton botonActualizar = EstiloInterfaz.boton("Actualizar BCCR", EstiloInterfaz.CARBON, 160, 38, 13);
    private final JButton botonCalcularPremio = EstiloInterfaz.boton("Calcular premio", EstiloInterfaz.AZUL, 150, 38, 13);
    private final JButton botonConvertirDolares = EstiloInterfaz.boton("A colones", EstiloInterfaz.AZUL, 120, 38, 13);
    private final JButton botonConvertirColones = EstiloInterfaz.boton("A dólares", EstiloInterfaz.AZUL, 120, 38, 13);
    private final JTextField campoPuntaje = new JTextField("1", 5);
    private final JTextField campoDolares = new JTextField("10", 7);
    private final JTextField campoColones = new JTextField("5000", 7);
    private final JLabel etiquetaEstadoBCCR = EstiloInterfaz.texto(
            "La tasa y el cálculo monetario son independientes del ranking.", 13);
    private final JLabel etiquetaResultadoPremio = EstiloInterfaz.texto("Premio: —", 13);
    private final JLabel etiquetaResultadoColones = EstiloInterfaz.texto("Resultado: —", 13);
    private final JLabel etiquetaResultadoDolares = EstiloInterfaz.texto("Resultado: —", 13);
    private final JLabel etiquetaTituloRanking = EstiloInterfaz.titulo("Top 5 · Memoria", 25);
    private final JLabel etiquetaDetalleRanking = EstiloInterfaz.texto(
            "Clasificación ordenada por cantidad de victorias.", 12);
    private final JPanel listaRanking = new JPanel();
    private final NumberFormat formatoMoneda = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CR"));
    private Runnable accionRegresar = () -> { };
    private Runnable accionActualizarTipoCambio = () -> { };
    private Consumer<ModalidadRanking> accionCambiarModalidad = modalidad -> { };
    private DoubleConsumer accionCalcularPremio = puntaje -> { };
    private DoubleConsumer accionConvertirDolares = monto -> { };
    private DoubleConsumer accionConvertirColones = monto -> { };
    private ModalidadRanking modalidadSeleccionada = ModalidadRanking.MEMORIA;

    public PanelPremios() {
        super(new BorderLayout(16, 12));
        setBackground(EstiloInterfaz.FONDO);
        setBorder(new EmptyBorder(8, 18, 12, 18));
        formatoMoneda.setMinimumFractionDigits(2);
        formatoMoneda.setMaximumFractionDigits(2);

        add(construirCabecera(), BorderLayout.NORTH);
        add(construirClasificacion(), BorderLayout.CENTER);
        add(construirSeccionMonetaria(), BorderLayout.SOUTH);

        botonRegresar.addActionListener(evento -> accionRegresar.run());
        botonMemoria.addActionListener(evento -> accionCambiarModalidad.accept(ModalidadRanking.MEMORIA));
        botonPatrones.addActionListener(evento -> accionCambiarModalidad.accept(ModalidadRanking.PATRONES));
        botonActualizar.addActionListener(evento -> accionActualizarTipoCambio.run());
        botonCalcularPremio.addActionListener(evento -> procesarMonto(campoPuntaje, true));
        botonConvertirDolares.addActionListener(evento -> procesarMonto(campoDolares, false));
        botonConvertirColones.addActionListener(evento -> procesarMonto(campoColones, false));
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
    public void accionCalcularPremio(DoubleConsumer accion) {
        accionCalcularPremio = accion == null ? puntaje -> { } : accion;
    }
    public void accionConvertirDolares(DoubleConsumer accion) {
        accionConvertirDolares = accion == null ? monto -> { } : accion;
    }
    public void accionConvertirColones(DoubleConsumer accion) {
        accionConvertirColones = accion == null ? monto -> { } : accion;
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
                ? "En Patrones, cada secuencia completada correctamente suma una victoria."
                : "Jugadores ordenados por la cantidad de partidas ganadas.");
        revalidate();
    }

    public ModalidadRanking obtenerModalidadSeleccionada() { return modalidadSeleccionada; }

    public void establecerRanking(List<EntradaRanking> entradas) {
        listaRanking.removeAll();
        listaRanking.setLayout(new BoxLayout(listaRanking, BoxLayout.Y_AXIS));
        if (entradas == null || entradas.isEmpty()) {
            JLabel sinVictorias = EstiloInterfaz.texto(
                    "Aún no hay victorias registradas en esta modalidad.", 17);
            sinVictorias.setForeground(Color.WHITE);
            sinVictorias.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaRanking.add(Box.createVerticalGlue());
            listaRanking.add(sinVictorias);
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

    public void establecerCargaBCCR() { etiquetaEstadoBCCR.setText("Consultando tipo de cambio del BCCR…"); }
    public void establecerTipoCambio(double colonesPorDolar, String fecha) {
        etiquetaEstadoBCCR.setText("BCCR · Compra del dólar · " + fecha + " · $1 = ₡"
                + formatoMoneda.format(colonesPorDolar));
    }
    public void establecerErrorBCCR(String mensaje) { etiquetaEstadoBCCR.setText(mensaje); }
    public void establecerResultadoPremio(double monto) {
        etiquetaResultadoPremio.setText("Premio: ₡" + formatoMoneda.format(monto));
    }
    public void establecerResultadoColones(double monto) {
        etiquetaResultadoColones.setText("Resultado: ₡" + formatoMoneda.format(monto));
    }
    public void establecerResultadoDolares(double monto) {
        etiquetaResultadoDolares.setText("Resultado: $" + formatoMoneda.format(monto));
    }
    public void establecerErrorPremio(String mensaje) { etiquetaResultadoPremio.setText(mensaje); }
    public void establecerErrorConversion(String mensaje) {
        etiquetaResultadoColones.setText(mensaje);
        etiquetaResultadoDolares.setText(mensaje);
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(2, 8, 0, 8));
        cabecera.setPreferredSize(new Dimension(800, 68));
        cabecera.add(botonRegresar, BorderLayout.WEST);
        JLabel titulo = EstiloInterfaz.titulo("Premios", 28);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        cabecera.add(titulo, BorderLayout.CENTER);
        cabecera.add(Box.createHorizontalStrut(60), BorderLayout.EAST);
        return cabecera;
    }

    private JPanel construirClasificacion() {
        JPanel clasificacion = crearTarjeta(EstiloInterfaz.AZUL);
        clasificacion.setLayout(new BorderLayout(8, 10));
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

    private JPanel construirSeccionMonetaria() {
        JPanel seccion = new JPanel(new BorderLayout(8, 8));
        seccion.setOpaque(false);
        seccion.setBorder(new EmptyBorder(2, 4, 0, 4));

        JPanel estado = new JPanel(new BorderLayout(12, 0));
        estado.setOpaque(false);
        estado.add(etiquetaEstadoBCCR, BorderLayout.CENTER);
        estado.add(botonActualizar, BorderLayout.EAST);
        seccion.add(estado, BorderLayout.NORTH);

        JPanel herramientas = new JPanel(new GridLayout(1, 2, 16, 0));
        herramientas.setOpaque(false);
        herramientas.add(construirCalculadoraPremio());
        herramientas.add(construirConvertidor());
        seccion.add(herramientas, BorderLayout.CENTER);
        seccion.setPreferredSize(new Dimension(900, 190));
        return seccion;
    }

    private JPanel construirCalculadoraPremio() {
        JPanel calculadora = crearTarjeta(Color.WHITE);
        calculadora.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 6));
        calculadora.setBorder(new EmptyBorder(6, 8, 6, 8));
        JLabel titulo = EstiloInterfaz.titulo("Fórmula del premio", 15);
        JLabel formula = EstiloInterfaz.texto("(1 / puntaje) × 100 × tipo de cambio", 12);
        JLabel etiquetaPuntaje = EstiloInterfaz.texto("Puntaje:", 13);
        configurarCampo(campoPuntaje);
        calculadora.add(titulo);
        calculadora.add(formula);
        calculadora.add(etiquetaPuntaje);
        calculadora.add(campoPuntaje);
        calculadora.add(botonCalcularPremio);
        calculadora.add(etiquetaResultadoPremio);
        return calculadora;
    }

    private JPanel construirConvertidor() {
        JPanel convertidor = crearTarjeta(Color.WHITE);
        convertidor.setLayout(new GridLayout(2, 1, 4, 2));
        convertidor.setBorder(new EmptyBorder(6, 10, 6, 10));
        JLabel titulo = EstiloInterfaz.titulo("Conversor de moneda", 15);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        convertidor.add(titulo);
        JPanel conversiones = new JPanel(new GridLayout(1, 2, 12, 0));
        conversiones.setOpaque(false);
        conversiones.add(crearFilaConvertidor("USD", campoDolares, botonConvertirDolares, etiquetaResultadoColones));
        conversiones.add(crearFilaConvertidor("CRC", campoColones, botonConvertirColones, etiquetaResultadoDolares));
        convertidor.add(conversiones);
        return convertidor;
    }

    private JPanel crearFilaConvertidor(String moneda, JTextField campo, JButton boton, JLabel resultado) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        fila.setOpaque(false);
        configurarCampo(campo);
        JLabel etiquetaMoneda = EstiloInterfaz.texto(moneda, 12);
        resultado.setFont(EstiloInterfaz.inter(11));
        fila.add(etiquetaMoneda);
        fila.add(campo);
        fila.add(boton);
        fila.add(resultado);
        return fila;
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
        JLabel victorias = EstiloInterfaz.titulo(entrada.victorias() + " victorias", 14);
        victorias.setForeground(EstiloInterfaz.CARBON);
        fila.add(numero, BorderLayout.WEST);
        fila.add(nombre, BorderLayout.CENTER);
        fila.add(victorias, BorderLayout.EAST);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fila;
    }

    private JPanel crearTarjeta(Color color) {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics graficos) {
                Graphics2D lienzo = (Graphics2D) graficos.create();
                lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                lienzo.setColor(color);
                lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                lienzo.dispose();
                super.paintComponent(graficos);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private void configurarCampo(JTextField campo) {
        campo.setFont(EstiloInterfaz.inter(13));
        campo.setColumns(5);
        campo.setPreferredSize(new Dimension(76, 32));
    }

    private void procesarMonto(JTextField campo, boolean puntaje) {
        try {
            double monto = Double.parseDouble(campo.getText().trim().replace(',', '.'));
            if (!Double.isFinite(monto) || monto < 0 || (puntaje && monto <= 0)) {
                throw new NumberFormatException();
            }
            if (puntaje) accionCalcularPremio.accept(monto);
            else if (campo == campoDolares) accionConvertirDolares.accept(monto);
            else accionConvertirColones.accept(monto);
        } catch (NumberFormatException excepcion) {
            if (puntaje) establecerErrorPremio("El puntaje debe ser mayor que cero.");
            else establecerErrorConversion("Ingresa un monto válido.");
        }
    }
}
