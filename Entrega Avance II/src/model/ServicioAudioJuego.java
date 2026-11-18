package model;
// Arranca o corta la música de fondo y mantiene el estado de sonido elegido.
public final class ServicioAudioJuego implements AutoCloseable {
    private static final String RECURSO_MUSICA = "/recursos/audio/Meditation.wav";
    private Thread hiloMusica;
    private ThreadMusica reproduccion;
    private boolean sonidoActivado = true;
    // Evita abrir dos reproductores si el selector ya dejó la música en marcha.
    public synchronized void iniciarMusica() {
        if (!sonidoActivado) return;
        if (hiloMusica != null && hiloMusica.isAlive()) return;
        reproduccion = new ThreadMusica(RECURSO_MUSICA);
        hiloMusica = new Thread(reproduccion, "musica-memory-game");
        hiloMusica.setDaemon(true);
        hiloMusica.start();
    }
    // Silenciar corta la reproducción; activarlo vuelve a iniciar el bucle.
    public synchronized void establecerSonidoActivado(boolean activado) {
        sonidoActivado = activado;
        if (reproduccion != null && reproduccion.isEnEjecucion()) {
            reproduccion.establecerSonidoActivado(activado);
        } else if (activado) {
            iniciarMusica();
        }
    }
    // Cierra el hilo y el reproductor cuando la aplicación termina.
    public synchronized void detenerMusica() {
        if (reproduccion != null) reproduccion.detener();
        reproduccion = null;
        hiloMusica = null;
    }
    // Cierra el hilo y el reproductor cuando la aplicación termina.
    @Override public void close() { detenerMusica(); }
}
