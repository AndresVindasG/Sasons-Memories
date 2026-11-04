
package Vista;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

/** Ventana de inicio de sesión del juego. */
public class VentanaLoginVisual extends JFrame {

    private final JTextField campoCorreo =
            EstiloMockups.campo("nombre@correo.com", 46);

    private final JPasswordField campoContrasena =
            new JPasswordField("memorygame");

    private final JButton botonAccesoFacial =
            EstiloMockups.botonRedondeado(
                    "Utilizar reconocimiento facial",
                    EstiloMockups.AZUL, 284, 58, 15
            );

    private final JButton botonIngresar =
            EstiloMockups.botonRedondeado(
                    "Ingresar", EstiloMockups.AZUL, 170, 58, 16
            );

    private final JButton botonVolver =
            EstiloMockups.botonRedondeado(
                    "Volver", EstiloMockups.GRIS, 125, 36, 13
            );

    public VentanaLoginVisual() {
        super("Sason's Memories | Inicio de sesión");

        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(816, 638);
        setMinimumSize(new Dimension(816, 638));
        setLocationRelativeTo(null);
    }

    // Divide la ventana en el área del logo y el formulario.
    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setPreferredSize(new Dimension(800, 600));

        pantalla.add(construirPanelMarca(), BorderLayout.WEST);
        pantalla.add(construirPanelAcceso(), BorderLayout.CENTER);

        return pantalla;
    }

    // Muestra el logo en el lado izquierdo de la ventana.
    private JPanel construirPanelMarca() {
        JPanel marca = new JPanel(new GridBagLayout());
        marca.setBackground(EstiloMockups.CREMA);
        marca.setPreferredSize(new Dimension(305, 600));

        marca.add(EstiloMockups.logo(250, 230));

        return marca;
    }

    // Organiza los títulos, el formulario y los botones.
    private JPanel construirPanelAcceso() {
        JPanel acceso = new JPanel();
        acceso.setBackground(EstiloMockups.CREMA);
        acceso.setLayout(new BoxLayout(acceso, BoxLayout.Y_AXIS));
        acceso.setBorder(new EmptyBorder(43, 30, 24, 30));

        JLabel titulo = EstiloMockups.titulo(
                "¡Bienvenido de vuelta!", 25, Color.BLACK
        );
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = EstiloMockups.texto(
                "<html><div style='text-align:center'>"
                        + "Ingresa tu nombre de usuario o correo y<br>"
                        + "contraseña para acceder a tu cuenta."
                        + "</div></html>",
                15, Color.BLACK
        );
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        acceso.add(titulo);
        acceso.add(Box.createVerticalStrut(8));
        acceso.add(subtitulo);
        acceso.add(Box.createVerticalStrut(22));
        acceso.add(construirFormulario());
        acceso.add(Box.createVerticalStrut(10));

        // Separa los campos de acceso del botón de reconocimiento facial.
        JSeparator separador =
                new JSeparator(SwingConstants.HORIZONTAL);

        separador.setForeground(new Color(231, 221, 207));
        separador.setMaximumSize(new Dimension(368, 1));
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceso.add(separador);
        acceso.add(Box.createVerticalStrut(8));

        botonAccesoFacial.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceso.add(botonAccesoFacial);
        acceso.add(Box.createVerticalStrut(14));

        botonIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceso.add(botonIngresar);
        acceso.add(Box.createVerticalStrut(10));

        botonVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceso.add(botonVolver);

        return acceso;
    }

    // Crea los campos para ingresar el correo y la contraseña.
    private JPanel construirFormulario() {
        JPanel formulario = new JPanel();
        formulario.setOpaque(false);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setPreferredSize(new Dimension(368, 164));
        formulario.setMaximumSize(new Dimension(368, 164));
        formulario.setAlignmentX(Component.CENTER_ALIGNMENT);

        formulario.add(etiquetaCampo("Correo electrónico"));
        formulario.add(Box.createVerticalStrut(6));

        configurarCampoOscuro(campoCorreo);
        formulario.add(campoCorreo);
        formulario.add(Box.createVerticalStrut(13));

        formulario.add(etiquetaCampo("Contraseña"));
        formulario.add(Box.createVerticalStrut(6));

        campoContrasena.setText("memorygame");
        campoContrasena.setEchoChar('•');
        configurarCampoOscuro(campoContrasena);
        formulario.add(campoContrasena);

        return formulario;
    }

    // Crea las etiquetas que aparecen encima de cada campo.
    private JLabel etiquetaCampo(String texto) {
        JLabel etiqueta = EstiloMockups.texto(texto, 14, Color.BLACK);
        etiqueta.setFont(
                EstiloMockups.inter(14).deriveFont(java.awt.Font.BOLD)
        );
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        return etiqueta;
    }

    // Aplica el mismo estilo oscuro a ambos campos de texto.
    private void configurarCampoOscuro(JTextField campo) {
        campo.setFont(EstiloMockups.inter(14));
        campo.setForeground(new Color(218, 222, 226));
        campo.setCaretColor(Color.WHITE);
        campo.setBackground(EstiloMockups.CARBON);
        campo.setBorder(new EmptyBorder(0, 15, 0, 12));
        campo.setPreferredSize(new Dimension(368, 46));
        campo.setMaximumSize(new Dimension(368, 46));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // Inicia la ventana en el hilo de eventos de Swing.
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(
                () -> new VentanaLoginVisual().setVisible(true)
        );
    }
}
