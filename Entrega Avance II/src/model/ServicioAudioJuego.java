package model;

/** Gestiona el hilo de música desde el menú selector hasta cerrar o silenciar la aplicación. */
public final class ServicioAudioJuego implements AutoCloseable {
    private static final String RECURSO_MUSICA = "/recursos/audio/Meditation.wav";
    private Thread hiloMusica;
    private ThreadMusica reproduccion;
    private boolean sonidoActivado = true;

    public synchronized void iniciarMusica() {
        if (!sonidoActivado) return;
        if (hiloMusica != null && hiloMusica.isAlive()) return;
        reproduccion = new ThreadMusica(RECURSO_MUSICA);
        hiloMusica = new Thread(reproduccion, "musica-memory-game");
        hiloMusica.setDaemon(true);
        hiloMusica.start();
    }

    public synchronized void establecerSonidoActivado(boolean activado) {
        sonidoActivado = activado;
        if (reproduccion != null && reproduccion.isEnEjecucion()) {
            reproduccion.establecerSonidoActivado(activado);
        } else if (activado) {
            iniciarMusica();
        }
    }

    public synchronized void detenerMusica() {
        if (reproduccion != null) reproduccion.detener();
        reproduccion = null;
        hiloMusica = null;
    }

    @Override public void close() { detenerMusica(); }
}
