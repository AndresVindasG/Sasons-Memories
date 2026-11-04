package view;

import javax.swing.*;
import java.awt.*;

/** Matriz 6×6 que conserva cartas cuadradas de 128 px cuando hay espacio. */
public final class TableroCartas extends JPanel {
    public static final int CANTIDAD_CARTAS = 36;
    public static final int TAMANO_CARTA_PREFERIDO = 128;
    private static final int SEPARACION = 8;
    private static final int MARGEN = 16;
    private final FichaJuego[] cartas = new FichaJuego[CANTIDAD_CARTAS];

    public TableroCartas() {
        setOpaque(false);
        setLayout(null);
        for (int indice = 0; indice < cartas.length; indice++) {
            cartas[indice] = new FichaJuego();
            cartas[indice].setPreferredSize(new Dimension(TAMANO_CARTA_PREFERIDO, TAMANO_CARTA_PREFERIDO));
            cartas[indice].setMinimumSize(new Dimension(44, 44));
            cartas[indice].getAccessibleContext().setAccessibleName("Carta " + (indice + 1));
            add(cartas[indice]);
        }
    }

    public FichaJuego obtenerCarta(int indice) { return cartas[indice]; }

    @Override public Dimension getPreferredSize() {
        int dimensionMatriz = TAMANO_CARTA_PREFERIDO * 6 + SEPARACION * 5 + MARGEN * 2;
        return new Dimension(dimensionMatriz, dimensionMatriz);
    }

    @Override public void doLayout() {
        int espacioAncho = Math.max(1, getWidth() - MARGEN * 2 - SEPARACION * 5);
        int espacioAlto = Math.max(1, getHeight() - MARGEN * 2 - SEPARACION * 5);
        int lado = Math.max(1, Math.min(TAMANO_CARTA_PREFERIDO, Math.min(espacioAncho / 6, espacioAlto / 6)));
        int anchoMatriz = lado * 6 + SEPARACION * 5;
        int altoMatriz = anchoMatriz;
        int origenX = (getWidth() - anchoMatriz) / 2;
        int origenY = (getHeight() - altoMatriz) / 2;
        for (int indice = 0; indice < cartas.length; indice++) {
            int fila = indice / 6;
            int columna = indice % 6;
            cartas[indice].setBounds(origenX + columna * (lado + SEPARACION),
                    origenY + fila * (lado + SEPARACION), lado, lado);
        }
    }

    @Override protected void paintComponent(Graphics graficos) {
        Graphics2D lienzo = (Graphics2D) graficos.create();
        lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        lienzo.setColor(EstiloInterfaz.AZUL);
        lienzo.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
        lienzo.dispose();
        super.paintComponent(graficos);
    }
}
