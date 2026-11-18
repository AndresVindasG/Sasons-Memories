import controller.AutenticacionController;
import view.VentanaAutenticacion;

import javax.swing.SwingUtilities;
// Punto de entrada: prepara Swing y abre el flujo de autenticación.
public class Main {
    // Arranca Swing en su hilo de eventos para crear la interfaz con seguridad.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaAutenticacion ventana = new VentanaAutenticacion();
            new AutenticacionController(ventana).iniciarAplicacion();
        });
    }
}
