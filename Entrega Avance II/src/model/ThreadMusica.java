package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

/** Ejecuta una pista WAV en bucle en su propio hilo y permite silenciarla sin cerrar la app. */
public final class ThreadMusica implements Runnable {
    private final String rutaRecurso;
    private final Object bloqueoReproduccion = new Object();
    private volatile boolean enEjecucion = true;
    private volatile boolean sonidoActivado = true;
    private volatile Clip reproductorActual;

    public ThreadMusica(String rutaRecurso) { this.rutaRecurso = rutaRecurso; }

    public void establecerSonidoActivado(boolean activado) {
        sonidoActivado = activado;
        Clip reproductor = reproductorActual;
        if (reproductor != null) {
            synchronized (bloqueoReproduccion) {
                if (!activado) reproductor.stop();
                else if (enEjecucion && !reproductor.isRunning()) reproductor.loop(Clip.LOOP_CONTINUOUSLY);
                bloqueoReproduccion.notifyAll();
            }
        }
    }

    public boolean isEnEjecucion() { return enEjecucion; }

    public void detener() {
        enEjecucion = false;
        Clip reproductor = reproductorActual;
        if (reproductor != null) {
            reproductor.stop();
            reproductor.close();
        }
        synchronized (bloqueoReproduccion) { bloqueoReproduccion.notifyAll(); }
    }

    @Override public void run() {
        try (InputStream recurso = ThreadMusica.class.getResourceAsStream(rutaRecurso)) {
            if (recurso == null) {
                System.err.println("No se encontró la pista de música: " + rutaRecurso);
                return;
            }
            try (AudioInputStream flujoAudio = AudioSystem.getAudioInputStream(new BufferedInputStream(recurso))) {
                Clip reproductor = AudioSystem.getClip();
                reproductor.open(flujoAudio);
                reproductorActual = reproductor;
                synchronized (bloqueoReproduccion) {
                    if (sonidoActivado && enEjecucion) reproductor.loop(Clip.LOOP_CONTINUOUSLY);
                    while (enEjecucion) bloqueoReproduccion.wait();
                }
                reproductor.stop();
                reproductor.close();
            }
        } catch (InterruptedException excepcion) {
            Thread.currentThread().interrupt();
        } catch (Exception excepcion) {
            if (enEjecucion) System.err.println("No se pudo reproducir la música: " + excepcion.getMessage());
        } finally {
            reproductorActual = null;
            enEjecucion = false;
        }
    }
}
