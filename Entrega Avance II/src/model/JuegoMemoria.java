package model;

import java.util.Arrays;
import java.util.Random;
// Lleva dos tableros 6x6 y decide qué pasa cuando cada jugador voltea sus cartas.
public final class JuegoMemoria {
    public enum Resultado { PRIMERA_CARTA, PAREJA_ENCONTRADA, PAREJA_INCORRECTA, PARTIDA_COMPLETADA, IGNORADA }

    private static final int CANTIDAD_JUGADORES = 2;
    private static final int CANTIDAD_PAREJAS_POR_JUGADOR = 18;
    private static final int CANTIDAD_CARTAS = CANTIDAD_PAREJAS_POR_JUGADOR * 2;
    private final Random generadorAleatorio = new Random();
    private final int[][] mazos = new int[CANTIDAD_JUGADORES][CANTIDAD_CARTAS];
    private final boolean[][] parejasEncontradas = new boolean[CANTIDAD_JUGADORES][CANTIDAD_CARTAS];
    private final int[] parejasCompletadas = new int[CANTIDAD_JUGADORES];
    private int primeraCarta = -1;
    private int segundaCarta = -1;
    private int jugadorActual;
    private int puntosJugadorA;
    private int puntosJugadorB;
    // Crea 18 parejas para cada jugador y mezcla cada tablero por separado.
    public void iniciarPartida() {
        for (int jugador = 0; jugador < CANTIDAD_JUGADORES; jugador++) {
            for (int indice = 0; indice < CANTIDAD_CARTAS; indice++) mazos[jugador][indice] = indice / 2;
            barajar(mazos[jugador]);
            Arrays.fill(parejasEncontradas[jugador], false);
            parejasCompletadas[jugador] = 0;
        }
        primeraCarta = -1;
        segundaCarta = -1;
        jugadorActual = 0;
        puntosJugadorA = 0;
        puntosJugadorB = 0;
    }

    public int[] obtenerCaras(int jugador) { return mazos[jugador].clone(); }

    public int obtenerCara(int jugador, int indiceCarta) { return mazos[jugador][indiceCarta]; }

    public int obtenerPrimeraCarta() { return primeraCarta; }

    public int obtenerSegundaCarta() { return segundaCarta; }

    public int obtenerJugadorActual() { return jugadorActual; }

    public int obtenerPuntosJugadorA() { return puntosJugadorA; }

    public int obtenerPuntosJugadorB() { return puntosJugadorB; }

    public boolean esParejaEncontrada(int jugador, int indiceCarta) {
        return parejasEncontradas[jugador][indiceCarta];
    }
    // Rechaza elecciones inválidas y decide si el turno sigue, cambia o acaba.
    public Resultado seleccionarCarta(int jugador, int indiceCarta) {
        if (jugador != jugadorActual || jugador < 0 || jugador >= CANTIDAD_JUGADORES
                || indiceCarta < 0 || indiceCarta >= CANTIDAD_CARTAS
                || parejasEncontradas[jugador][indiceCarta] || indiceCarta == primeraCarta) {
            return Resultado.IGNORADA;
        }
        if (primeraCarta < 0) {
            primeraCarta = indiceCarta;
            return Resultado.PRIMERA_CARTA;
        }

        segundaCarta = indiceCarta;
        if (mazos[jugador][primeraCarta] == mazos[jugador][segundaCarta]) {
            parejasEncontradas[jugador][primeraCarta] = true;
            parejasEncontradas[jugador][segundaCarta] = true;
            if (jugadorActual == 0) puntosJugadorA++;
            else puntosJugadorB++;
            parejasCompletadas[jugador]++;
            primeraCarta = -1;
            segundaCarta = -1;
            return parejasCompletadas[jugador] == CANTIDAD_PAREJAS_POR_JUGADOR
                    ? Resultado.PARTIDA_COMPLETADA : Resultado.PAREJA_ENCONTRADA;
        }
        return Resultado.PAREJA_INCORRECTA;
    }
    // Después de mostrar el error un momento, deja ambas cartas disponibles otra vez.
    public void ocultarParejaIncorrecta() {
        primeraCarta = -1;
        segundaCarta = -1;
        jugadorActual = 1 - jugadorActual;
    }
    // Mezcla el mazo en sitio con Fisher–Yates, sin sesgar las posiciones.
    private void barajar(int[] mazo) {
        for (int indice = mazo.length - 1; indice > 0; indice--) {
            int otroIndice = generadorAleatorio.nextInt(indice + 1);
            int temporal = mazo[indice];
            mazo[indice] = mazo[otroIndice];
            mazo[otroIndice] = temporal;
        }
    }
}
