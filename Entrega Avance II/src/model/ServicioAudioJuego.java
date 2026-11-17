package model;

//**Gestor del hilo de la música**//

public final class ServicioAudioJuego implements AutoCloseable {
    private static final String RECURSO_MUSICA = "/recursos/audio/Meditation.wav";
    private Thread hiloMusica; //contiene la lógica de la lectura del audio
    private ThreadMusica reproduccion;
    private boolean sonidoActivado = true;

    //synchronized evita condiciones de carrera, o sea asegura operaciones sobre el hilo y el estado del sonido se ejecuten de forma ordenada.

    public synchronized void iniciarMusica() {
        if (!sonidoActivado) return;
        if (hiloMusica != null && hiloMusica.isAlive()) return; //is Alive es como decir "si está activo"
        reproduccion = new ThreadMusica(RECURSO_MUSICA);
        hiloMusica = new Thread(reproduccion, "musica-memory-game");
        hiloMusica.setDaemon(true); //si la app principal se cierra, el hilo de música morirá con ella en lugar de dejar el proceso colgado en segundo plano
        hiloMusica.start();
    }

    public synchronized void establecerSonidoActivado(boolean activado) { //cambia el estado del sonido
        sonidoActivado = activado;
        if (reproduccion != null && reproduccion.isEnEjecucion()) {
            reproduccion.establecerSonidoActivado(activado);
        } else if (activado) {
            iniciarMusica();
        }
    }

    public synchronized void detenerMusica() { //Detiene la reproducción a través del objeto ThreadMusica
        if (reproduccion != null) reproduccion.detener();
        reproduccion = null;
        hiloMusica = null;
    }

    @Override public void close() { detenerMusica(); }
}
