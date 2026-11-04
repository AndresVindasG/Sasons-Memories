package Controlador;
import Modelo.ModeloTablero;
import Vista.VentanaMemoriaVisual;
public class ControladorTablero {

        private ModeloTablero modeloJugadorA;
        private ModeloTablero modeloJugadorB;

        private VentanaMemoriaVisual vista;

        public ControladorTablero() {

            modeloJugadorA = new ModeloTablero();
            modeloJugadorB = new ModeloTablero();

            vista = new VentanaMemoriaVisual();

            mostrarTableros();

            vista.setVisible(true);
        }

        public void mostrarTableros() {

            for (int fila = 0; fila < modeloJugadorA.getFilas(); fila++) {

                for (int columna = 0;
                     columna < modeloJugadorA.getColumnas();
                     columna++) {

                    int imagenA = modeloJugadorA.obtenerImagen(fila, columna);

                    int imagenB = modeloJugadorB.obtenerImagen(fila, columna);

                    vista.mostrarImagen(1, fila, columna, imagenA);

                    vista.mostrarImagen(2, fila, columna, imagenB);
                }
            }
        }
}
