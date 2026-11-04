package controller;

import model.JuegoMemoria;
import model.JuegoPatrones;
import model.ServicioAudioJuego;
import model.ServicioTipoCambioBCCR;
import view.PanelMemoria;
import view.PanelNavegacionJuego;
import view.PanelPatrones;
import view.DialogoVictoria;
import view.PanelPremios;

import javax.swing.*;
import java.awt.Window;
import java.util.List;

/** Coordina la navegación y las reglas de los modos visuales del Memory Game. */
public final class JuegoController {
    private final PanelNavegacionJuego vista;
    private final Runnable accionVolverAlPerfil;
    private final JuegoPatrones juegoPatrones = new JuegoPatrones();
    private final JuegoMemoria juegoMemoria = new JuegoMemoria();
    private final ServicioAudioJuego servicioAudio = new ServicioAudioJuego();
    private final ServicioTipoCambioBCCR servicioTipoCambio = new ServicioTipoCambioBCCR();
    private final Timer temporizadorSecuencia;
    private final Timer temporizadorCambioNivel;
    private final Timer temporizadorCartas;
    private final Timer temporizadorPartida;
    private boolean sonidoActivado = true;
    private boolean aceptandoSeleccion;
    private boolean fichaIluminada;
    private int indiceSecuencia;
    private int segundosTranscurridos;
    private double tipoCambioCompra = Double.NaN;

    public JuegoController(PanelNavegacionJuego vista, Runnable accionVolverAlPerfil) {
        this.vista = vista;
        this.accionVolverAlPerfil = accionVolverAlPerfil == null ? () -> { } : accionVolverAlPerfil;
        temporizadorSecuencia = new Timer(350, evento -> avanzarSecuencia());
        temporizadorCambioNivel = new Timer(850, evento -> presentarSecuencia());
        temporizadorCartas = new Timer(850, evento -> ocultarCartasIncorrectas());
        temporizadorPartida = new Timer(1000, evento -> actualizarTiempoPartida());
        temporizadorSecuencia.setRepeats(true);
        temporizadorCambioNivel.setRepeats(false);
        temporizadorCartas.setRepeats(false);
        conectarVistas();
    }

    public void iniciar() {
        detenerTemporizadores();
        vista.mostrarBienvenidaJuego();
    }

    public void detener() {
        detenerTemporizadores();
        servicioAudio.close();
        aceptandoSeleccion = false;
    }

    private void conectarVistas() {
        vista.obtenerPanelBienvenida().accionContinuar(this::abrirSelectorModo);
        vista.obtenerPanelSeleccionModo().accionPatrones(this::iniciarModoPatrones);
        vista.obtenerPanelSeleccionModo().accionMemoria(this::iniciarModoMemoria);
        vista.obtenerPanelSeleccionModo().accionPremios(this::abrirPremios);
        vista.obtenerPanelPremios().accionRegresar(vista::mostrarSeleccionModo);
        vista.obtenerPanelPremios().accionActualizarTipoCambio(this::consultarTipoCambio);
        vista.obtenerPanelPremios().accionConvertirDolares(this::convertirDolaresAColones);
        vista.obtenerPanelPremios().accionConvertirColones(this::convertirColonesADolares);

        PanelPatrones panelPatrones = vista.obtenerPanelPatrones();
        panelPatrones.obtenerControles().accionRegresar(() -> {
            detenerTemporizadorPatrones();
            vista.mostrarSeleccionModo();
        });
        panelPatrones.obtenerControles().accionCambiarSonido(this::establecerSonidoActivado);
        panelPatrones.accionSeleccionarFicha(this::seleccionarFichaPatrones);

        PanelMemoria panelMemoria = vista.obtenerPanelMemoria();
        panelMemoria.obtenerControles().accionRegresar(() -> {
            detenerTemporizadoresMemoria();
            vista.mostrarSeleccionModo();
        });
        panelMemoria.obtenerControles().accionCambiarSonido(this::establecerSonidoActivado);
        panelMemoria.accionSeleccionarCarta(this::seleccionarCartaMemoria);

        vista.accionVolverAlPerfil(this::volverAlPerfil);
        sincronizarEstadoSonido();
    }

    private void abrirSelectorModo() {
        vista.mostrarSeleccionModo();
        servicioAudio.iniciarMusica();
    }

    private void abrirPremios() {
        vista.mostrarPremios();
        if (Double.isNaN(tipoCambioCompra)) consultarTipoCambio();
    }

    private void consultarTipoCambio() {
        PanelPremios panel = vista.obtenerPanelPremios();
        panel.establecerCargaBCCR();
        new SwingWorker<ServicioTipoCambioBCCR.TipoCambio, Void>() {
            @Override protected ServicioTipoCambioBCCR.TipoCambio doInBackground() throws Exception {
                return servicioTipoCambio.consultarCompra();
            }

            @Override protected void done() {
                try {
                    ServicioTipoCambioBCCR.TipoCambio tipoCambio = get();
                    tipoCambioCompra = tipoCambio.colonesPorDolar();
                    panel.establecerTipoCambio(tipoCambioCompra, tipoCambio.fecha());
                } catch (Exception excepcion) {
                    Throwable causa = excepcion.getCause() == null ? excepcion : excepcion.getCause();
                    panel.establecerErrorBCCR("No se pudo consultar el BCCR: " + causa.getMessage());
                }
            }
        }.execute();
    }

    private void convertirDolaresAColones(double dolares) {
        if (Double.isNaN(tipoCambioCompra)) {
            vista.obtenerPanelPremios().establecerErrorConversion("Actualiza primero el tipo de cambio BCCR.");
            return;
        }
        vista.obtenerPanelPremios().establecerResultadoColones(dolares * tipoCambioCompra);
    }

    private void convertirColonesADolares(double colones) {
        if (Double.isNaN(tipoCambioCompra) || tipoCambioCompra <= 0) {
            vista.obtenerPanelPremios().establecerErrorConversion("Actualiza primero el tipo de cambio BCCR.");
            return;
        }
        vista.obtenerPanelPremios().establecerResultadoDolares(colones / tipoCambioCompra);
    }

    private void iniciarModoPatrones() {
        detenerTemporizadores();
        juegoPatrones.iniciarPartida();
        PanelPatrones panel = vista.obtenerPanelPatrones();
        panel.establecerCaras(juegoPatrones.obtenerCarasFichas());
        panel.establecerNivel(juegoPatrones.obtenerNivel());
        panel.establecerEstado("Observa la secuencia y repítela.");
        panel.apagarFichas();
        panel.establecerEntradaHabilitada(false);
        vista.mostrarModoPatrones();
        presentarSecuencia();
    }

    private void presentarSecuencia() {
        PanelPatrones panel = vista.obtenerPanelPatrones();
        panel.establecerEntradaHabilitada(false);
        panel.apagarFichas();
        panel.establecerEstado("Observa la secuencia y repítela.");
        indiceSecuencia = 0;
        fichaIluminada = false;
        temporizadorSecuencia.setInitialDelay(600);
        temporizadorSecuencia.setDelay(700);
        temporizadorSecuencia.start();
    }

    private void avanzarSecuencia() {
        List<Integer> secuencia = juegoPatrones.obtenerSecuencia();
        PanelPatrones panel = vista.obtenerPanelPatrones();
        if (fichaIluminada) {
            panel.apagarFichas();
            fichaIluminada = false;
            indiceSecuencia++;
            if (indiceSecuencia >= secuencia.size()) {
                temporizadorSecuencia.stop();
                aceptandoSeleccion = true;
                panel.establecerEntradaHabilitada(true);
                panel.establecerEstado("Tu turno: repite la secuencia.");
            }
            return;
        }
        panel.iluminarFicha(secuencia.get(indiceSecuencia));
        fichaIluminada = true;
    }

    private void seleccionarFichaPatrones(int indiceFicha) {
        if (!aceptandoSeleccion) return;
        aceptandoSeleccion = false;
        JuegoPatrones.Resultado resultado = juegoPatrones.registrarSeleccion(indiceFicha);
        PanelPatrones panel = vista.obtenerPanelPatrones();
        panel.iluminarFicha(indiceFicha);
        Timer respuesta = new Timer(450, evento -> {
            ((Timer) evento.getSource()).stop();
            panel.apagarFichas();
            if (resultado == JuegoPatrones.Resultado.INCORRECTA) {
                panel.establecerEstado("La secuencia no coincide. Empezamos de nuevo.");
                temporizadorCambioNivel.setInitialDelay(950);
                temporizadorCambioNivel.restart();
                juegoPatrones.iniciarPartida();
                panel.establecerNivel(juegoPatrones.obtenerNivel());
            } else if (resultado == JuegoPatrones.Resultado.NIVEL_COMPLETADO) {
                juegoPatrones.comenzarSiguienteNivel();
                panel.establecerNivel(juegoPatrones.obtenerNivel());
                panel.establecerEstado("¡Nivel superado! Prepárate para la siguiente secuencia.");
                temporizadorCambioNivel.setInitialDelay(850);
                temporizadorCambioNivel.restart();
            } else {
                aceptandoSeleccion = true;
                panel.establecerEntradaHabilitada(true);
            }
        });
        respuesta.setRepeats(false);
        respuesta.start();
    }

    private void iniciarModoMemoria() {
        detenerTemporizadores();
        juegoMemoria.iniciarPartida();
        segundosTranscurridos = 0;
        aceptandoSeleccion = true;
        PanelMemoria panel = vista.obtenerPanelMemoria();
        panel.restablecerTableros();
        panel.establecerCaras(0, juegoMemoria.obtenerCaras(0));
        panel.establecerCaras(1, juegoMemoria.obtenerCaras(1));
        actualizarMarcadorMemoria();
        vista.mostrarModoMemoria();
        temporizadorPartida.start();
    }

    private void seleccionarCartaMemoria(int jugador, int indiceCarta) {
        if (!aceptandoSeleccion || juegoMemoria.esParejaEncontrada(jugador, indiceCarta)) return;
        int primeraCarta = juegoMemoria.obtenerPrimeraCarta();
        JuegoMemoria.Resultado resultado = juegoMemoria.seleccionarCarta(jugador, indiceCarta);
        if (resultado == JuegoMemoria.Resultado.IGNORADA) return;
        PanelMemoria panel = vista.obtenerPanelMemoria();
        int cara = juegoMemoria.obtenerCara(jugador, indiceCarta);
        panel.mostrarCarta(jugador, indiceCarta, cara);

        if (resultado == JuegoMemoria.Resultado.PRIMERA_CARTA) {
            return;
        }
        if (resultado == JuegoMemoria.Resultado.PAREJA_ENCONTRADA
                || resultado == JuegoMemoria.Resultado.PARTIDA_COMPLETADA) {
            panel.mostrarParejaEncontrada(jugador, primeraCarta,
                    juegoMemoria.obtenerCara(jugador, primeraCarta));
            panel.mostrarParejaEncontrada(jugador, indiceCarta, cara);
            actualizarMarcadorMemoria();
            if (resultado == JuegoMemoria.Resultado.PARTIDA_COMPLETADA) terminarPartidaMemoria(jugador);
            return;
        }

        aceptandoSeleccion = false;
        temporizadorCartas.restart();
    }

    private void ocultarCartasIncorrectas() {
        int jugador = juegoMemoria.obtenerJugadorActual();
        int primeraCarta = juegoMemoria.obtenerPrimeraCarta();
        int segundaCarta = juegoMemoria.obtenerSegundaCarta();
        PanelMemoria panel = vista.obtenerPanelMemoria();
        panel.ocultarCarta(jugador, primeraCarta);
        panel.ocultarCarta(jugador, segundaCarta);
        juegoMemoria.ocultarParejaIncorrecta();
        actualizarMarcadorMemoria();
        Timer habilitarSiguienteJugada = new Timer(300, evento -> {
            ((Timer) evento.getSource()).stop();
            aceptandoSeleccion = true;
        });
        habilitarSiguienteJugada.setRepeats(false);
        habilitarSiguienteJugada.start();
    }

    private void actualizarTiempoPartida() {
        segundosTranscurridos++;
        actualizarMarcadorMemoria();
    }

    private void actualizarMarcadorMemoria() {
        PanelMemoria panel = vista.obtenerPanelMemoria();
        panel.establecerMarcador(juegoMemoria.obtenerPuntosJugadorA(), juegoMemoria.obtenerPuntosJugadorB(),
                juegoMemoria.obtenerJugadorActual(), segundosTranscurridos);
    }

    private void terminarPartidaMemoria(int jugadorGanador) {
        temporizadorPartida.stop();
        aceptandoSeleccion = false;
        String ganador = jugadorGanador == 0 ? "Jugador A" : "Jugador B";
        String resultado = "¡Ganó " + ganador + "!";
        String subtitulo = ganador + " encontró todas las parejas de su tablero.";
        Window ventana = SwingUtilities.getWindowAncestor(vista);
        DialogoVictoria dialogo = new DialogoVictoria(ventana, resultado, subtitulo,
                () -> vista.mostrarSeleccionModo());
        dialogo.setVisible(true);
    }

    private void establecerSonidoActivado(boolean activado) {
        sonidoActivado = activado;
        servicioAudio.establecerSonidoActivado(activado);
        sincronizarEstadoSonido();
    }

    private void sincronizarEstadoSonido() {
        vista.obtenerPanelPatrones().obtenerControles().establecerSonidoActivado(sonidoActivado);
        vista.obtenerPanelMemoria().obtenerControles().establecerSonidoActivado(sonidoActivado);
    }

    private void volverAlPerfil() {
        detenerTemporizadores();
        servicioAudio.close();
        accionVolverAlPerfil.run();
    }

    private void detenerTemporizadorPatrones() {
        temporizadorSecuencia.stop();
        temporizadorCambioNivel.stop();
        aceptandoSeleccion = false;
        vista.obtenerPanelPatrones().apagarFichas();
    }

    private void detenerTemporizadoresMemoria() {
        temporizadorCartas.stop();
        temporizadorPartida.stop();
        aceptandoSeleccion = false;
    }

    private void detenerTemporizadores() {
        detenerTemporizadorPatrones();
        detenerTemporizadoresMemoria();
        temporizadorCambioNivel.stop();
    }
}
