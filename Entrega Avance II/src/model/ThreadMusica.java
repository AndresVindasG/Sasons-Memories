package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
// Reproduce el audio en su propio hilo y vuelve al inicio mientras siga activado.
public final class ThreadMusica implements Runnable {
    private final String rutaRecurso;
    private final Object bloqueoReproduccion = new Object();
    private volatile boolean enEjecucion = true;
    private volatile boolean sonidoActivado = true;
    private volatile Clip reproductorActual;

    public ThreadMusica(String rutaRecurso) { this.rutaRecurso = rutaRecurso; }
    // Cambia el estado de audio y cierra el reproductor si se pidió silencio.
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
    // Marca el hilo para salir del bucle y despierta al reproductor cerrándolo.
    public void detener() {
        enEjecucion = false;
        Clip reproductor = reproductorActual;
        if (reproductor != null) {
            reproductor.stop();
            reproductor.close();
        }
        synchronized (bloqueoReproduccion) { bloqueoReproduccion.notifyAll(); }
    }
    // Reproduce el archivo de principio a fin; al acabar, vuelve a abrirlo mientras siga activo.
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
