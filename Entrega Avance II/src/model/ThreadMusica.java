package model;

// Importa las clases necesarias para trabajar con archivos de audio.
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// Importa clases para leer los recursos de audio.
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Ejecuta una pista WAV en bucle en su propio hilo.
 * Permite activar, silenciar y detener la música
 * sin cerrar la aplicación.
 */
public final class ThreadMusica implements Runnable {

    // Ruta del archivo de música dentro de los recursos del proyecto.
    private final String rutaRecurso;

    // Objeto utilizado para sincronizar el acceso a la reproducción.
    private final Object bloqueoReproduccion = new Object();

    // Indica si el hilo todavía debe continuar ejecutándose.
    // volatile permite que los cambios sean visibles entre hilos.
    private volatile boolean enEjecucion = true;

    // Indica si el sonido está activado o silenciado.
    private volatile boolean sonidoActivado = true;

    // Guarda el reproductor de audio que se está utilizando actualmente.
    // volatile permite que otros hilos vean su valor actualizado.
    private volatile Clip reproductorActual;


    /**
     * Constructor de la clase.
     *
     * @param rutaRecurso ubicación del archivo WAV que se reproducirá.
     */
    public ThreadMusica(String rutaRecurso) {
        this.rutaRecurso = rutaRecurso;
    }


    /**
     * Activa o desactiva la música.
     *
     * @param activado true para activar el sonido,
     *                 false para silenciarlo.
     */
    public void establecerSonidoActivado(boolean activado) {

        // Actualiza el estado del sonido.
        sonidoActivado = activado;

        // Obtiene el reproductor que se encuentra activo.
        Clip reproductor = reproductorActual;

        // Comprueba que exista un reproductor disponible.
        if (reproductor != null) {

            // Sincroniza el acceso al reproductor para evitar conflictos.
            synchronized (bloqueoReproduccion) {

                // Si el usuario desactiva el sonido, detiene la reproducción.
                if (!activado) {
                    reproductor.stop();
                }

                // Si activa el sonido y el hilo sigue funcionando,
                // vuelve a reproducir la música en bucle.
                else if (enEjecucion && !reproductor.isRunning()) {
                    reproductor.loop(Clip.LOOP_CONTINUOUSLY);
                }

                // Notifica a los hilos que estén esperando este bloqueo.
                bloqueoReproduccion.notifyAll();
            }
        }
    }


    /**
     * Consulta si el hilo de música continúa ejecutándose.
     *
     * @return true si sigue activo, false si ya se detuvo.
     */
    public boolean isEnEjecucion() {
        return enEjecucion;
    }


    /**
     * Detiene completamente la reproducción de música.
     */
    public void detener() {

        // Cambia el estado para indicar que el hilo debe finalizar.
        enEjecucion = false;

        // Obtiene el reproductor actual.
        Clip reproductor = reproductorActual;

        // Si existe un reproductor, detiene y libera el audio.
        if (reproductor != null) {
            reproductor.stop();
            reproductor.close();
        }

        // Despierta al hilo si se encuentra esperando.
        synchronized (bloqueoReproduccion) {
            bloqueoReproduccion.notifyAll();
        }
    }


    /**
     * Método principal que ejecuta el hilo.
     * Se encarga de cargar y reproducir el archivo WAV.
     */
    @Override
    public void run() {

        // Obtiene el archivo de audio desde los recursos del proyecto.
        // try-with-resources permite cerrar automáticamente el recurso.
        try (InputStream recurso =
                     ThreadMusica.class.getResourceAsStream(rutaRecurso)) {

            // Comprueba si el archivo existe.
            if (recurso == null) {

                // Muestra un mensaje si no encuentra el archivo.
                System.err.println(
                        "No se encontró la pista de música: " + rutaRecurso
                );

                // Finaliza el método si no existe el recurso.
                return;
            }

            // Convierte el recurso en un flujo de audio.
            // BufferedInputStream mejora la lectura del archivo.
            try (AudioInputStream flujoAudio =
                         AudioSystem.getAudioInputStream(
                                 new BufferedInputStream(recurso))) {

                // Obtiene un reproductor de audio.
                Clip reproductor = AudioSystem.getClip();

                // Carga el contenido del archivo WAV en el reproductor.
                reproductor.open(flujoAudio);

                // Guarda el reproductor para poder controlarlo desde otros métodos.
                reproductorActual = reproductor;

                // Sincroniza las operaciones relacionadas con la reproducción.
                synchronized (bloqueoReproduccion) {

                    // Si el sonido está activado y el hilo sigue funcionando,
                    // reproduce la música continuamente.
                    if (sonidoActivado && enEjecucion) {
                        reproductor.loop(Clip.LOOP_CONTINUOUSLY);
                    }

                    // Mantiene el hilo esperando mientras la música
                    // deba continuar ejecutándose.
                    while (enEjecucion) {
                        bloqueoReproduccion.wait();
                    }
                }

                // Cuando termina el hilo, detiene la reproducción.
                reproductor.stop();

                // Libera los recursos utilizados por el reproductor.
                reproductor.close();
            }

        } catch (InterruptedException excepcion) {

            // Restaura el estado de interrupción del hilo.
            Thread.currentThread().interrupt();

        } catch (Exception excepcion) {

            // Muestra el error solamente si el hilo seguía activo.
            if (enEjecucion) {
                System.err.println(
                        "No se pudo reproducir la música: "
                                + excepcion.getMessage()
                );
            }

        } finally {

            // Elimina la referencia al reproductor.
            reproductorActual = null;

            // Indica que el hilo ya no está ejecutándose.
            enEjecucion = false;
        }
    }
}