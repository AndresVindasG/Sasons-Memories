package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Mantiene la secuencia y valida las elecciones del modo Patrones. */
public final class JuegoPatrones {
    public enum Resultado { CORRECTA, NIVEL_COMPLETADO, INCORRECTA }

    private static final int CANTIDAD_FICHAS = 36;
    private static final int LONGITUD_INICIAL = 3;
    private final Random generadorAleatorio = new Random();
    private final List<Integer> secuencia = new ArrayList<>();
    private final int[] carasFichas = new int[CANTIDAD_FICHAS];
    private int indiceEsperado;

    public void iniciarPartida() {
        for (int indice = 0; indice < CANTIDAD_FICHAS; indice++) carasFichas[indice] = indice / 2;
        for (int indice = CANTIDAD_FICHAS - 1; indice > 0; indice--) {
            int otraFicha = generadorAleatorio.nextInt(indice + 1);
            int temporal = carasFichas[indice];
            carasFichas[indice] = carasFichas[otraFicha];
            carasFichas[otraFicha] = temporal;
        }
        secuencia.clear();
        indiceEsperado = 0;
        for (int indice = 0; indice < LONGITUD_INICIAL; indice++) agregarPaso();
    }

    public List<Integer> obtenerSecuencia() {
        return List.copyOf(secuencia);
    }

    public int[] obtenerCarasFichas() { return carasFichas.clone(); }

    public int obtenerNivel() {
        return secuencia.size();
    }

    public Resultado registrarSeleccion(int fichaSeleccionada) {
        if (fichaSeleccionada != secuencia.get(indiceEsperado)) {
            return Resultado.INCORRECTA;
        }
        indiceEsperado++;
        if (indiceEsperado == secuencia.size()) {
            return Resultado.NIVEL_COMPLETADO;
        }
        return Resultado.CORRECTA;
    }

    public void comenzarSiguienteNivel() {
        agregarPaso();
        indiceEsperado = 0;
    }

    private void agregarPaso() {
        secuencia.add(generadorAleatorio.nextInt(CANTIDAD_FICHAS));
    }
}
