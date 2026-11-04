package model;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.List;

/** Convierte un rostro detectado por OpenCV en un descriptor LBP local. */
public class ServicioHuellaFacial {
    private static final int TAMANO_NORMALIZADO = 128;
    private static final int CELDAS_POR_LADO = 8;
    private static final int BINS_POR_CELDA = 256;
    private static final double DISTANCIA_MAXIMA = 76.0;

    public String generarPlantilla(BufferedImage rostro) {
        BufferedImage normalizado = new BufferedImage(TAMANO_NORMALIZADO, TAMANO_NORMALIZADO,
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graficos = normalizado.createGraphics();
        graficos.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graficos.drawImage(rostro, 0, 0, TAMANO_NORMALIZADO, TAMANO_NORMALIZADO, null);
        graficos.dispose();

        int[][] tonos = ecualizarContraste(leerTonos(normalizado));
        int[][] histogramas = new int[CELDAS_POR_LADO * CELDAS_POR_LADO][BINS_POR_CELDA];
        for (int y = 1; y < TAMANO_NORMALIZADO - 1; y++) {
            for (int x = 1; x < TAMANO_NORMALIZADO - 1; x++) {
                int centro = tonos[y][x];
                int patron = 0;
                patron |= (tonos[y - 1][x - 1] >= centro ? 1 : 0) << 7;
                patron |= (tonos[y - 1][x] >= centro ? 1 : 0) << 6;
                patron |= (tonos[y - 1][x + 1] >= centro ? 1 : 0) << 5;
                patron |= (tonos[y][x + 1] >= centro ? 1 : 0) << 4;
                patron |= (tonos[y + 1][x + 1] >= centro ? 1 : 0) << 3;
                patron |= (tonos[y + 1][x] >= centro ? 1 : 0) << 2;
                patron |= (tonos[y + 1][x - 1] >= centro ? 1 : 0) << 1;
                patron |= tonos[y][x - 1] >= centro ? 1 : 0;
                int celdaX = Math.min(CELDAS_POR_LADO - 1, x / (TAMANO_NORMALIZADO / CELDAS_POR_LADO));
                int celdaY = Math.min(CELDAS_POR_LADO - 1, y / (TAMANO_NORMALIZADO / CELDAS_POR_LADO));
                histogramas[celdaY * CELDAS_POR_LADO + celdaX][patron]++;
            }
        }

        byte[] descriptor = new byte[CELDAS_POR_LADO * CELDAS_POR_LADO * BINS_POR_CELDA];
        int indice = 0;
        for (int[] histograma : histogramas) {
            int total = 0;
            for (int cuenta : histograma) total += cuenta;
            for (int cuenta : histograma) {
                descriptor[indice++] = (byte) (total == 0 ? 0 : Math.round(cuenta * 255.0 / total));
            }
        }
        return Base64.getEncoder().encodeToString(descriptor);
    }

    public List<String> generarPlantillasCompatibles(BufferedImage rostro) {
        if (rostro == null) return List.of();
        BufferedImage rostroConCanalesIntercambiados = intercambiarCanalesRojoAzul(rostro);
        return List.of(generarPlantilla(rostro), generarPlantilla(rostroConCanalesIntercambiados));
    }

    private BufferedImage intercambiarCanalesRojoAzul(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        int[] pixeles = imagenOriginal.getRGB(0, 0, ancho, alto, null, 0, ancho);
        for (int indice = 0; indice < pixeles.length; indice++) {
            int pixel = pixeles[indice];
            int rojo = (pixel >> 16) & 0xff;
            int verde = (pixel >> 8) & 0xff;
            int azul = pixel & 0xff;
            pixeles[indice] = (azul << 16) | (verde << 8) | rojo;
        }
        BufferedImage imagenCompatible = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        imagenCompatible.setRGB(0, 0, ancho, alto, pixeles, 0, ancho);
        return imagenCompatible;
    }
    public boolean coincide(String plantillaGuardada, String plantillaCapturada) {
        if (plantillaGuardada == null || plantillaCapturada == null) return false;
        byte[] guardada;
        byte[] capturada;
        try {
            guardada = Base64.getDecoder().decode(plantillaGuardada);
            capturada = Base64.getDecoder().decode(plantillaCapturada);
        } catch (IllegalArgumentException excepcion) {
            return false;
        }
        int longitudEsperada = CELDAS_POR_LADO * CELDAS_POR_LADO * BINS_POR_CELDA;
        if (guardada.length != longitudEsperada || capturada.length != longitudEsperada) return false;

        double distanciaFina = distanciaCuadricula(guardada, capturada, CELDAS_POR_LADO);
        double distanciaAmplia = distanciaCuadricula(guardada, capturada, CELDAS_POR_LADO / 2);
        double distanciaCombinada = distanciaFina * 0.70 + distanciaAmplia * 0.30;
        return distanciaCombinada <= DISTANCIA_MAXIMA;
    }

    private double distanciaCuadricula(byte[] guardada, byte[] capturada, int celdasPorLado) {
        int escalaAgrupacion = CELDAS_POR_LADO / celdasPorLado;
        double distanciaAcumulada = 0;
        for (int celdaY = 0; celdaY < celdasPorLado; celdaY++) {
            for (int celdaX = 0; celdaX < celdasPorLado; celdaX++) {
                int[] histogramaGuardado = new int[BINS_POR_CELDA];
                int[] histogramaCapturado = new int[BINS_POR_CELDA];
                int inicioX = celdaX * escalaAgrupacion;
                int inicioY = celdaY * escalaAgrupacion;
                for (int subY = 0; subY < escalaAgrupacion; subY++) {
                    for (int subX = 0; subX < escalaAgrupacion; subX++) {
                        int celdaOriginal = (inicioY + subY) * CELDAS_POR_LADO + inicioX + subX;
                        int inicioOriginal = celdaOriginal * BINS_POR_CELDA;
                        for (int bin = 0; bin < BINS_POR_CELDA; bin++) {
                            histogramaGuardado[bin] += guardada[inicioOriginal + bin] & 0xff;
                            histogramaCapturado[bin] += capturada[inicioOriginal + bin] & 0xff;
                        }
                    }
                }
                normalizarHistograma(histogramaGuardado);
                normalizarHistograma(histogramaCapturado);
                distanciaAcumulada += distanciaChiCuadrado(histogramaGuardado, histogramaCapturado);
            }
        }
        return distanciaAcumulada / (celdasPorLado * celdasPorLado);
    }

    private void normalizarHistograma(int[] histograma) {
        int total = 0;
        for (int cuenta : histograma) total += cuenta;
        if (total == 0) return;
        for (int bin = 0; bin < histograma.length; bin++) {
            histograma[bin] = (int) Math.round(histograma[bin] * 255.0 / total);
        }
    }

    private double distanciaChiCuadrado(int[] primero, int[] segundo) {
        double distancia = 0;
        for (int bin = 0; bin < primero.length; bin++) {
            double diferencia = primero[bin] - segundo[bin];
            distancia += diferencia * diferencia / (primero[bin] + segundo[bin] + 1.0);
        }
        return distancia;
    }
    private int[][] leerTonos(BufferedImage imagen) {
        int[][] tonos = new int[TAMANO_NORMALIZADO][TAMANO_NORMALIZADO];
        for (int y = 0; y < TAMANO_NORMALIZADO; y++) {
            for (int x = 0; x < TAMANO_NORMALIZADO; x++) {
                int rgb = imagen.getRGB(x, y);
                int rojo = (rgb >> 16) & 0xff;
                int verde = (rgb >> 8) & 0xff;
                int azul = rgb & 0xff;
                tonos[y][x] = (rojo * 299 + verde * 587 + azul * 114) / 1000;
            }
        }
        return tonos;
    }

    private int[][] ecualizarContraste(int[][] tonos) {
        int[] histograma = new int[256];
        for (int[] fila : tonos) for (int tono : fila) histograma[tono]++;
        int[] acumulado = new int[256];
        acumulado[0] = histograma[0];
        for (int tono = 1; tono < acumulado.length; tono++) acumulado[tono] = acumulado[tono - 1] + histograma[tono];
        int primerValor = 0;
        while (primerValor < 255 && acumulado[primerValor] == 0) primerValor++;
        int total = TAMANO_NORMALIZADO * TAMANO_NORMALIZADO;
        int denominador = Math.max(1, total - acumulado[primerValor]);
        int[][] ajustados = new int[TAMANO_NORMALIZADO][TAMANO_NORMALIZADO];
        for (int y = 0; y < TAMANO_NORMALIZADO; y++) {
            for (int x = 0; x < TAMANO_NORMALIZADO; x++) {
                ajustados[y][x] = Math.max(0, (acumulado[tonos[y][x]] - acumulado[primerValor]) * 255 / denominador);
            }
        }
        return ajustados;
    }
}
