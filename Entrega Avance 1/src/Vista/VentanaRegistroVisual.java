package Vista;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/** Vista Swing estática para el mockup de registro. */
public class VentanaRegistroVisual extends JFrame {
    public VentanaRegistroVisual() {
        super("Sason's Memories | Registro");
        setIconImage(EstiloMockups.iconoVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(construirVista());
        setSize(816, 638);
        setMinimumSize(new Dimension(816, 638));
        setLocationRelativeTo(null);
    }

    private JPanel construirVista() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setPreferredSize(new Dimension(800, 600));
        pantalla.add(construirPanelMarca(), BorderLayout.WEST);
        pantalla.add(construirPanelFormulario(), BorderLayout.CENTER);
        return pantalla;
    }

    private JPanel construirPanelMarca() {
        JPanel marca = new JPanel(new GridBagLayout());
        marca.setBackground(EstiloMockups.CREMA);
        marca.setPreferredSize(new Dimension(305, 600));
        marca.add(EstiloMockups.logo(250, 230));
        return marca;
    }

    private JPanel construirPanelFormulario() {
        JPanel panelAzul = new JPanel(new GridBagLayout());
        panelAzul.setBackground(EstiloMockups.AZUL);
        panelAzul.add(construirColumnaFormulario(), new GridBagConstraints());
        return panelAzul;
    }

    private JPanel construirColumnaFormulario() {
        JPanel columna = new JPanel(new GridBagLayout());
        columna.setOpaque(false);
        columna.setPreferredSize(new Dimension(430, 460));
        columna.setMinimumSize(new Dimension(430, 460));
        columna.setMaximumSize(new Dimension(430, 460));

        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = 0;
        posicion.gridy = 0;
        posicion.weightx = 1;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        posicion.insets = new java.awt.Insets(0, 0, 22, 0);
        JLabel titulo = EstiloMockups.titulo("Ingresa los datos solicitados", 23, Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        columna.add(titulo, posicion);

        agregarCampo(columna, posicion, "Nombre de usuario", "Ingresa tu nombre de usuario");
        agregarCampo(columna, posicion, "Correo electrónico", "Ingresa tu correo electrónico");
        agregarCampo(columna, posicion, "Contraseña", "Ingresa tu contraseña");

        JLabel terminos = new JLabel("<html><div style='text-align:center'>Al hacer clic en <b>Registrarse con correo</b><br><b>electrónico</b>, aceptas los <b>Términos de servicio</b><br>y la Política de privacidad</div></html>", SwingConstants.CENTER);
        terminos.setFont(EstiloMockups.inter(14));
        terminos.setForeground(Color.WHITE);
        posicion.gridy++;
        posicion.insets = new java.awt.Insets(2, 0, 18, 0);
        columna.add(terminos, posicion);

        JButton crearCuenta = EstiloMockups.botonRedondeado("Crear cuenta", EstiloMockups.CARBON, 400, 40, 14);
        posicion.gridy++;
        posicion.fill = GridBagConstraints.NONE;
        posicion.anchor = GridBagConstraints.CENTER;
        posicion.insets = new java.awt.Insets(0, 0, 0, 0);
        columna.add(crearCuenta, posicion);
        return columna;
    }

    private void agregarCampo(JPanel columna, GridBagConstraints posicion, String titulo, String textoIndicador) {
        JLabel etiqueta = EstiloMockups.texto(titulo, 15, new Color(30, 35, 39));
        etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        posicion.gridy++;
        posicion.insets = new java.awt.Insets(0, 0, 7, 0);
        columna.add(etiqueta, posicion);

        JTextField campo = EstiloMockups.campo(textoIndicador, 40);
        campo.setMinimumSize(new Dimension(430, 40));
        campo.setPreferredSize(new Dimension(430, 40));
        posicion.gridy++;
        posicion.insets = new java.awt.Insets(0, 0, 13, 0);
        columna.add(campo, posicion);
    }
    public static void main(String[] argumentos) {
        SwingUtilities.invokeLater(() -> new VentanaRegistroVisual().setVisible(true));
    }
}