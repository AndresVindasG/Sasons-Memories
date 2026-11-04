import controller.AutenticacionController;
import view.VentanaAutenticacion;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaAutenticacion ventana = new VentanaAutenticacion();
            new AutenticacionController(ventana).iniciarAplicacion();
        });
    }
}