package Modelo;
import java.util.Random;
public class ModeloTablero {
        private final int filas = 6;
        private final int columnas = 6;

        // Matriz que contiene las imágenes
        private final int[][] tablero = new int[filas][columnas];

        // Constructor
        public ModeloTablero() {
            crearTablero();
        }

        // Coloca las 18 parejas de imágenes aleatoriamente
        public void crearTablero() {

            int[] imagenes = new int[36];

            // Colocar cada imagen dos veces
            for (int i = 0; i < 18; i++) {
                imagenes[i * 2] = i + 1;
                imagenes[i * 2 + 1] = i + 1;
            }

            // Mezclar las imágenes aleatoriamente
            Random aleatorio = new Random();

            for (int i = 0; i < imagenes.length; i++) {

                int posicion = aleatorio.nextInt(36);

                int temporal = imagenes[i];
                imagenes[i] = imagenes[posicion];
                imagenes[posicion] = temporal;
            }

            // Colocar las imágenes en la matriz
            int posicion = 0;

            for (int fila = 0; fila < filas; fila++) {
                for (int columna = 0; columna < columnas; columna++) {

                    tablero[fila][columna] = imagenes[posicion];
                    posicion++;
                }
            }
        }

        // Obtener la imagen que contiene una casilla
        public int obtenerImagen(int fila, int columna) {

            if (fila < 0 || fila >= filas ||
                    columna < 0 || columna >= columnas) {
                return 0;
            }

            return tablero[fila][columna];
        }

        // Verificar si dos casillas forman una pareja
        public boolean comprobarPareja(int fila1, int columna1,
                                       int fila2, int columna2) {

            return tablero[fila1][columna1] ==
                    tablero[fila2][columna2];
        }

        // Devuelve la cantidad de filas
        public int getFilas() {
            return filas;
        }

        // Devuelve la cantidad de columnas
        public int getColumnas() {
            return columnas;
        }

        // Imprimir el tablero para probar el modelo
        public String imprimirTablero() {

            StringBuilder sb = new StringBuilder();

            for (int fila = 0; fila < filas; fila++) {

                for (int columna = 0; columna < columnas; columna++) {

                    sb.append(tablero[fila][columna]).append("\t");
                }

                sb.append("\n");
            }

            return sb.toString();
        }

        // Prueba del modelo desde consola
        public static void main(String[] args) {

            ModeloTablero modelo = new ModeloTablero();

            System.out.println(modelo.imprimirTablero());

            System.out.println("Filas: " + modelo.getFilas());
            System.out.println("Columnas: " + modelo.getColumnas());

            System.out.println("Imagen en posición (0,0): "
                    + modelo.obtenerImagen(0, 0));
        }
}
