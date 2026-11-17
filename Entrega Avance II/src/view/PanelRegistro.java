package view;

// Importa los componentes necesarios para construir interfaces gráficas.
import javax.swing.*;

// Permite utilizar bordes personalizados y márgenes internos.
import javax.swing.border.EmptyBorder;

// Importa herramientas para organizar y personalizar los componentes gráficos.
import java.awt.*;

// Clase encargada de construir la pantalla de registro de usuarios.
// Hereda de JPanel porque representa un panel dentro de la ventana principal.
public class PanelRegistro extends JPanel {

    // Campo donde el usuario escribe su nombre de usuario.
    private final JTextField campoNombreUsuario = new JTextField();

    // Campo personalizado para ingresar el correo electrónico.
    private final CampoCorreo campoCorreo = new CampoCorreo();

    // Campo personalizado para ingresar la contraseña.
    private final CampoContrasena campoContrasena = new CampoContrasena();

    // Campo donde el usuario debe repetir su contraseña para confirmarla.
    private final CampoContrasena campoConfirmacion = new CampoContrasena();

    // Botón que permite continuar con el proceso de registro facial.
    // Se configura mediante los métodos de la clase EstiloInterfaz.
    private final JButton botonContinuar = EstiloInterfaz.boton(
            "Continuar con registro facial", EstiloInterfaz.CAMPO, 430, 50, 15);

    // Botón que permite regresar a la pantalla de inicio de sesión.
    private final JButton botonVolver = EstiloInterfaz.boton(
            "Volver al inicio", EstiloInterfaz.GRIS, 190, 40, 14);


    // Constructor de la clase.
    // Se encarga de crear y organizar todos los elementos de la pantalla.
    public PanelRegistro() {

        // Establece BorderLayout como administrador de distribución principal.
        // Permite dividir el panel en regiones como WEST y CENTER.
        setLayout(new BorderLayout());

        // Establece el color de fondo de la pantalla.
        setBackground(EstiloInterfaz.AZUL);

        // Agrega el panel de marca en el lado izquierdo de la pantalla.
        // Este panel contiene el logo y el nombre de la aplicación.
        add(construirPanelMarca(), BorderLayout.WEST);


        // ---------------------------------------------------------
        // CONSTRUCCIÓN DEL ÁREA DEL FORMULARIO
        // ---------------------------------------------------------

        // Crea un panel que permitirá centrar el formulario dentro
        // del espacio disponible utilizando GridBagLayout.
        JPanel areaFormulario = new JPanel(new GridBagLayout());

        // Establece el color de fondo del área del formulario.
        areaFormulario.setBackground(EstiloInterfaz.AZUL);

        // Crea el panel que contendrá todos los elementos del formulario.
        JPanel panelFormulario = new JPanel();

        // Establece el mismo color de fondo que el área principal.
        panelFormulario.setBackground(EstiloInterfaz.AZUL);

        // BoxLayout permite organizar los componentes uno debajo del otro.
        // El eje Y_AXIS indica que la distribución será vertical.
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));

        // Establece los márgenes internos del formulario.
        // Los valores representan: arriba, izquierda, abajo y derecha.
        panelFormulario.setBorder(new EmptyBorder(18, 44, 14, 44));


        // ---------------------------------------------------------
        // TÍTULO Y SUBTÍTULO
        // ---------------------------------------------------------

        // Crea el título principal utilizando el estilo personalizado.
        JLabel titulo = EstiloInterfaz.titulo("Crea tu cuenta", 27);

        // Establece el color blanco para el texto del título.
        titulo.setForeground(Color.WHITE);

        // Alinea el título hacia el lado izquierdo del formulario.
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega el título al panel del formulario.
        panelFormulario.add(titulo);

        // Agrega un espacio vertical de 4 píxeles aproximadamente.
        // Sirve para separar el título del subtítulo.
        panelFormulario.add(Box.createVerticalStrut(4));


        // Crea el texto descriptivo que aparece debajo del título.
        JLabel subtitulo = EstiloInterfaz.texto(
                "Ingresa los datos solicitados para registrarte.", 14);

        // Establece el color blanco para el subtítulo.
        subtitulo.setForeground(Color.WHITE);

        // Alinea el subtítulo hacia la izquierda.
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega el subtítulo al formulario.
        panelFormulario.add(subtitulo);

        // Agrega un espacio vertical antes de comenzar los campos.
        panelFormulario.add(Box.createVerticalStrut(10));


        // ---------------------------------------------------------
        // CONSTRUCCIÓN DE LOS CAMPOS DE REGISTRO
        // ---------------------------------------------------------

        // Crea un panel que contendrá todos los campos de entrada.
        JPanel formulario = new JPanel();

        // Hace que el panel sea transparente y permita visualizar
        // el color de fondo del panel que lo contiene.
        formulario.setOpaque(false);

        // Organiza los campos verticalmente.
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        // Alinea el formulario hacia el lado izquierdo.
        formulario.setAlignmentX(Component.LEFT_ALIGNMENT);


        // Agrega el campo para ingresar el nombre de usuario.
        // Recibe el panel, el nombre de la etiqueta, el componente
        // donde se escribe y un texto indicador.
        agregarCampo(formulario, "Nombre de usuario",
                campoNombreUsuario, "Elige un nombre de usuario");

        // Agrega el campo para ingresar el correo electrónico.
        agregarCampo(formulario, "Correo electrónico",
                campoCorreo, "nombre@correo.com");

        // Agrega el campo para ingresar la contraseña.
        agregarCampo(formulario, "Contraseña",
                campoContrasena, "Mínimo 8 caracteres");

        // Agrega el campo para confirmar la contraseña.
        agregarCampo(formulario, "Confirmar contraseña",
                campoConfirmacion, "Repite la contraseña");

        // Incorpora todos los campos al panel principal del formulario.
        panelFormulario.add(formulario);

        // Agrega una pequeña separación debajo de los campos.
        panelFormulario.add(Box.createVerticalStrut(7));


        // ---------------------------------------------------------
        // AVISO Y BOTONES
        // ---------------------------------------------------------

        // Crea un mensaje que informa al usuario que el registro
        // continuará mediante el reconocimiento facial.
        JLabel aviso = EstiloInterfaz.texto(
                "El registro continúa con la cámara en tiempo real.", 13);

        // Establece el color blanco del mensaje.
        aviso.setForeground(Color.WHITE);

        // Alinea el mensaje hacia la izquierda.
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega el mensaje informativo al formulario.
        panelFormulario.add(aviso);

        // Agrega una separación antes del botón de continuar.
        panelFormulario.add(Box.createVerticalStrut(12));


        // Establece la alineación del botón de continuar.
        botonContinuar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega el botón al panel del formulario.
        panelFormulario.add(botonContinuar);

        // Agrega una separación entre los dos botones.
        panelFormulario.add(Box.createVerticalStrut(8));


        // Establece la alineación del botón para volver.
        botonVolver.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega el botón de volver al formulario.
        panelFormulario.add(botonVolver);


        // Agrega el formulario completo al área central.
        // GridBagLayout permite mantenerlo centrado en ese espacio.
        areaFormulario.add(panelFormulario);

        // Incorpora el área del formulario en el centro de la pantalla.
        add(areaFormulario, BorderLayout.CENTER);
    }


    // ---------------------------------------------------------
    // CONSTRUCCIÓN DEL PANEL DE MARCA
    // ---------------------------------------------------------

    // Método privado que construye el panel izquierdo de la interfaz.
    // Contiene el logo, el nombre del juego y un mensaje representativo.
    private JPanel construirPanelMarca() {

        // Crea el panel que contendrá los elementos de marca.
        JPanel marca = new JPanel();

        // Organiza los elementos verticalmente.
        marca.setLayout(new BoxLayout(marca, BoxLayout.Y_AXIS));

        // Establece el color de fondo del panel.
        marca.setBackground(EstiloInterfaz.FONDO);

        // Define los márgenes internos del panel.
        marca.setBorder(new EmptyBorder(0, 24, 0, 24));

        // Establece las dimensiones preferidas del panel.
        // Su ancho es de 330 píxeles y su alto de 550 píxeles.
        marca.setPreferredSize(new Dimension(330, 550));


        // Agrega un espacio flexible en la parte superior.
        // Permite distribuir los elementos verticalmente.
        marca.add(Box.createVerticalGlue());

        // Agrega el logo de la aplicación con un tamaño de 270 x 205.
        marca.add(EstiloInterfaz.logo(270, 205));

        // Agrega una separación debajo del logo.
        marca.add(Box.createVerticalStrut(18));


        // Crea el texto con el nombre de la aplicación.
        JLabel nombreAplicacion = EstiloInterfaz.titulo("Memory Game", 22);

        // Centra horizontalmente el nombre de la aplicación.
        nombreAplicacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el nombre al panel de marca.
        marca.add(nombreAplicacion);

        // Agrega una pequeña separación antes del mensaje.
        marca.add(Box.createVerticalStrut(5));


        // Crea el mensaje representativo de la aplicación.
        JLabel mensaje = EstiloInterfaz.texto("Tu memoria, a prueba.", 14);

        // Centra horizontalmente el mensaje.
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el mensaje al panel.
        marca.add(mensaje);

        // Agrega otro espacio flexible para mantener los elementos centrados.
        marca.add(Box.createVerticalGlue());

        // Devuelve el panel construido para poder agregarlo a la ventana.
        return marca;
    }


    // ---------------------------------------------------------
    // MÉTODO PARA AGREGAR Y CONFIGURAR LOS CAMPOS
    // ---------------------------------------------------------

    // Método encargado de construir cada fila del formulario.
    //
    // Recibe:
    // panel: panel donde se agregará el campo.
    // titulo: texto que aparecerá como etiqueta.
    // campo: componente donde el usuario ingresará la información.
    // indicador: texto de ayuda que se mostrará en el campo.
    private void agregarCampo(JPanel panel, String titulo,
                              JComponent campo, String indicador) {

        // Crea una etiqueta utilizando el estilo personalizado.
        JLabel etiqueta = EstiloInterfaz.etiqueta(titulo);

        // Establece el color blanco para la etiqueta.
        etiqueta.setForeground(Color.WHITE);

        // Centra horizontalmente el texto de la etiqueta.
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);


        // Comprueba si el componente recibido es un campo de contraseña.
        // instanceof permite verificar el tipo de objeto.
        if (campo instanceof CampoContrasena contrasena) {

            // Configura la fuente, el color del texto y el color de fondo.
            // Los estilos se obtienen de la clase EstiloInterfaz.
            contrasena.configurar(
                    EstiloInterfaz.inter(14),
                    EstiloInterfaz.TEXTO,
                    EstiloInterfaz.FONDO
            );

            // Establece el texto indicador del campo de contraseña.
            contrasena.establecerIndicador(indicador);

            // Comprueba si el componente es un campo personalizado de correo.
        } else if (campo instanceof CampoCorreo correo) {

            // Configura la fuente y los colores del campo de correo.
            correo.configurar(
                    EstiloInterfaz.inter(14),
                    EstiloInterfaz.TEXTO,
                    EstiloInterfaz.FONDO
            );

            // Establece el texto indicador del campo de correo.
            correo.establecerIndicador(indicador);

            // Comprueba si el componente es un campo de texto convencional.
        } else if (campo instanceof JTextField campoTexto) {

            // Establece el tipo y tamaño de letra.
            campoTexto.setFont(EstiloInterfaz.inter(14));

            // Establece el color de fondo del campo.
            campoTexto.setBackground(EstiloInterfaz.FONDO);

            // Establece el color del texto escrito por el usuario.
            campoTexto.setForeground(EstiloInterfaz.TEXTO);

            // Establece el color del cursor que aparece al escribir.
            campoTexto.setCaretColor(EstiloInterfaz.TEXTO);

            // Establece un texto de ayuda que aparece al pasar
            // el cursor del mouse sobre el campo.
            campoTexto.setToolTipText(indicador);

            // Crea un borde compuesto.
            // Combina un borde exterior de línea con un margen interno.
            campoTexto.setBorder(BorderFactory.createCompoundBorder(

                    // Crea el borde exterior con un color beige y grosor de 1.
                    BorderFactory.createLineBorder(
                            new Color(231, 221, 207), 1),

                    // Establece los márgenes internos del texto.
                    new EmptyBorder(0, 13, 0, 12)
            ));

            // Establece el tamaño preferido del campo de texto.
            campoTexto.setPreferredSize(new Dimension(430, 40));

            // Establece el tamaño máximo para evitar que se expanda.
            campoTexto.setMaximumSize(new Dimension(430, 40));
        }


        // ---------------------------------------------------------
        // ORGANIZACIÓN DE LA FILA DEL CAMPO
        // ---------------------------------------------------------

        // Crea un panel para organizar la etiqueta y el campo.
        // BorderLayout permite colocar la etiqueta arriba y el campo abajo.
        // El valor 3 representa la separación vertical entre ambos.
        JPanel filaCampo = new JPanel(new BorderLayout(0, 3));

        // Hace que el panel sea transparente.
        filaCampo.setOpaque(false);

        // Coloca la etiqueta en la parte superior.
        filaCampo.add(etiqueta, BorderLayout.NORTH);

        // Coloca el campo de entrada en el centro.
        filaCampo.add(campo, BorderLayout.CENTER);

        // Establece el tamaño preferido de la fila.
        filaCampo.setPreferredSize(new Dimension(430, 63));

        // Establece el tamaño máximo de la fila.
        filaCampo.setMaximumSize(new Dimension(430, 63));

        // Alinea la fila hacia el lado izquierdo.
        filaCampo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agrega la fila completa al panel del formulario.
        panel.add(filaCampo);

        // Agrega una separación vertical antes del siguiente campo.
        panel.add(Box.createVerticalStrut(5));
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA OBTENER LOS DATOS DEL FORMULARIO
    // ---------------------------------------------------------

    // Obtiene el nombre de usuario ingresado.
    // trim() elimina los espacios en blanco al inicio y al final.
    String obtenerNombreUsuario() {
        return campoNombreUsuario.getText().trim();
    }

    // Obtiene el correo electrónico ingresado.
    // trim() elimina los espacios innecesarios al inicio y al final.
    String obtenerCorreo() {
        return campoCorreo.getText().trim();
    }

    // Obtiene la contraseña ingresada como un arreglo de caracteres.
    char[] obtenerContrasena() {
        return campoContrasena.getPassword();
    }

    // Obtiene la contraseña que se escribió para confirmarla.
    char[] obtenerConfirmacion() {
        return campoConfirmacion.getPassword();
    }


    // ---------------------------------------------------------
    // MÉTODO PARA LIMPIAR EL FORMULARIO
    // ---------------------------------------------------------

    // Limpia todos los campos de entrada del formulario.
    void limpiarCampos() {

        // Limpia el campo del nombre de usuario.
        campoNombreUsuario.setText("");

        // Limpia el campo del correo electrónico.
        campoCorreo.setText("");

        // Limpia el campo de contraseña.
        campoContrasena.setText("");

        // Limpia el campo de confirmación de contraseña.
        campoConfirmacion.setText("");
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA ASIGNAR ACCIONES A LOS BOTONES
    // ---------------------------------------------------------

    // Recibe una acción que se ejecutará cuando el usuario
    // presione el botón de continuar con el registro.
    void accionContinuar(Runnable accion) {

        // addActionListener permite detectar cuando se presiona el botón.
        // El evento recibido se utiliza para ejecutar la acción mediante run().
        botonContinuar.addActionListener(evento -> accion.run());
    }

    // Recibe una acción que se ejecutará cuando el usuario
    // presione el botón para volver al inicio de sesión.
    void accionVolver(Runnable accion) {

        // Agrega un listener al botón de volver.
        // Cuando ocurre el evento, se ejecuta la acción recibida.
        botonVolver.addActionListener(evento -> accion.run());
    }
}