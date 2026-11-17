package view;

// Importa la clase encargada de controlar la cámara y realizar la captura facial.
import model.CapturadorCamara;

// Importa las clases necesarias para crear la interfaz gráfica con Swing.
import javax.swing.*;

// Importa clases para manejar dimensiones, diseños y otros elementos gráficos.
import java.awt.*;

// Permite trabajar con imágenes que se encuentran almacenadas en memoria.
import java.awt.image.BufferedImage;

// Clase principal de la interfaz de autenticación.
// Hereda de JFrame porque representa una ventana de escritorio.
public class VentanaAutenticacion extends JFrame {

    // Constantes que identifican cada una de las pantallas de la aplicación.
    // Se utilizan para navegar entre las diferentes vistas mediante CardLayout.

    public static final String INICIO_SESION = "inicio";
    public static final String REGISTRO = "registro";
    public static final String CAPTURA_FACIAL = "captura";
    public static final String BIENVENIDA = "bienvenida";
    public static final String JUEGO = "juego";

    // CardLayout permite mostrar diferentes paneles dentro de un mismo contenedor.
    // Funciona como un conjunto de tarjetas donde solamente una está visible a la vez.
    private final CardLayout distribucionPantallas = new CardLayout();

    // Panel principal que contiene todas las pantallas de la aplicación.
    // Utiliza el CardLayout definido anteriormente para controlar la navegación.
    private final JPanel panelPantallas = new JPanel(distribucionPantallas);

    // Instancias de los diferentes paneles que componen la interfaz.
    // Cada objeto representa una pantalla independiente de la aplicación.

    // Panel donde el usuario ingresa sus credenciales para iniciar sesión.
    private final PanelInicioSesion panelInicioSesion = new PanelInicioSesion();

    // Panel que contiene el formulario para registrar una nueva cuenta.
    private final PanelRegistro panelRegistro = new PanelRegistro();

    // Panel encargado de mostrar la cámara y realizar la captura del rostro.
    private final PanelCapturaFacial panelCapturaFacial = new PanelCapturaFacial();

    // Panel que muestra la pantalla de bienvenida después de autenticarse.
    private final PanelBienvenida panelBienvenida = new PanelBienvenida();

    // Panel que contiene la navegación hacia el juego.
    private final PanelNavegacionJuego panelJuego = new PanelNavegacionJuego();


    // Constructor de la ventana principal.
    // Se ejecuta cuando se crea un objeto de VentanaAutenticacion.
    public VentanaAutenticacion() {

        // Establece el título que aparecerá en la barra superior de la ventana.
        super("Memory Game - Inicio de sesión");

        // Indica que la aplicación debe cerrarse cuando se cierre esta ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Establece el tamaño inicial de la ventana en píxeles.
        setSize(1100, 760);

        // Establece el tamaño mínimo que puede tener la ventana.
        setMinimumSize(new Dimension(760, 540));

        // Permite que el usuario cambie el tamaño de la ventana.
        setResizable(true);

        // Coloca la ventana en el centro de la pantalla.
        setLocationRelativeTo(null);

        // Hace que la ventana se abra maximizada.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Agrega el panel de inicio de sesión al contenedor principal.
        // El segundo parámetro es el identificador utilizado para mostrarlo.
        panelPantallas.add(panelInicioSesion, INICIO_SESION);

        // Agrega el panel de registro.
        panelPantallas.add(panelRegistro, REGISTRO);

        // Agrega el panel de captura facial.
        panelPantallas.add(panelCapturaFacial, CAPTURA_FACIAL);

        // Agrega el panel de bienvenida.
        panelPantallas.add(panelBienvenida, BIENVENIDA);

        // Agrega el panel de navegación del juego.
        panelPantallas.add(panelJuego, JUEGO);

        // Establece el contenedor principal como contenido de la ventana.
        // De esta manera, todas las pantallas estarán dentro del JFrame.
        setContentPane(panelPantallas);

        // Establece el icono personalizado de la aplicación.
        // La imagen se obtiene mediante el método imagenLogo de EstiloInterfaz.
        setIconImage(EstiloInterfaz.imagenLogo());
    }


    // Método encargado de cambiar entre las diferentes pantallas.
    // Recibe el nombre de la pantalla que se desea mostrar.
    public void mostrarPantalla(String pantalla) {

        // CardLayout busca la tarjeta correspondiente al nombre recibido
        // y la muestra dentro del panel principal.
        distribucionPantallas.show(panelPantallas, pantalla);
    }


    // Métodos para obtener los paneles de la aplicación.
    // Permiten que otras clases puedan acceder a cada pantalla
    // sin necesidad de crear nuevas instancias.

    // Devuelve el panel de inicio de sesión.
    public PanelInicioSesion obtenerPanelInicioSesion() {
        return panelInicioSesion;
    }

    // Devuelve el panel de registro.
    public PanelRegistro obtenerPanelRegistro() {
        return panelRegistro;
    }

    // Devuelve el panel de captura facial.
    public PanelCapturaFacial obtenerPanelCapturaFacial() {
        return panelCapturaFacial;
    }

    // Devuelve el panel de bienvenida.
    public PanelBienvenida obtenerPanelBienvenida() {
        return panelBienvenida;
    }

    // Devuelve el panel de navegación del juego.
    public PanelNavegacionJuego obtenerPanelNavegacionJuego() {
        return panelJuego;
    }


    // Método para mostrar mensajes emergentes al usuario.
    // Recibe el título, el contenido del mensaje y el tipo de aviso.
    public void mostrarAviso(String titulo, String mensaje, int tipo) {

        // JOptionPane muestra una ventana emergente con el mensaje indicado.
        // El tipo puede representar información, advertencia o error.
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }


    // ---------------------------------------------------------
    // MÉTODOS RELACIONADOS CON EL INICIO DE SESIÓN
    // ---------------------------------------------------------

    // Obtiene el correo electrónico que el usuario escribió en el formulario.
    public String obtenerCorreoIngresado() {
        return panelInicioSesion.obtenerCorreo();
    }

    // Obtiene la contraseña ingresada por el usuario.
    // Se utiliza char[] en lugar de String para manejar contraseñas.
    public char[] obtenerContrasenaIngresada() {
        return panelInicioSesion.obtenerContrasena();
    }

    // Consulta si el usuario seleccionó la opción de recordar su correo.
    public boolean recordarCorreo() {
        return panelInicioSesion.recordarCorreo();
    }

    // Establece un correo en el campo de inicio de sesión.
    // Puede utilizarse para recuperar un correo guardado anteriormente.
    public void establecerCorreoIngresado(String correo) {
        panelInicioSesion.establecerCorreo(correo);
    }

    // Limpia el campo de contraseña del formulario de inicio de sesión.
    public void limpiarContrasenaIngresada() {
        panelInicioSesion.limpiarContrasena();
    }

    // Asigna una acción al botón de ingresar.
    // Recibe un Runnable que contiene el código que se ejecutará al presionar el botón.
    public void accionIngresar(Runnable accion) {
        panelInicioSesion.accionIngresar(accion);
    }

    // Asigna una acción al botón de acceso facial.
    // Permite conectar el botón con la lógica de autenticación mediante el rostro.
    public void accionAccesoFacial(Runnable accion) {
        panelInicioSesion.accionAccesoFacial(accion);
    }

    // Asigna una acción al botón para crear una cuenta nueva.
    public void accionCrearCuenta(Runnable accion) {
        panelInicioSesion.accionCrearCuenta(accion);
    }


    // ---------------------------------------------------------
    // MÉTODOS RELACIONADOS CON EL REGISTRO DE USUARIOS
    // ---------------------------------------------------------

    // Obtiene el nombre de usuario escrito en el formulario de registro.
    public String obtenerNombreDeRegistro() {
        return panelRegistro.obtenerNombreUsuario();
    }

    // Obtiene el correo electrónico ingresado durante el registro.
    public String obtenerCorreoDeRegistro() {
        return panelRegistro.obtenerCorreo();
    }

    // Obtiene la contraseña ingresada en el formulario de registro.
    public char[] obtenerContrasenaDeRegistro() {
        return panelRegistro.obtenerContrasena();
    }

    // Obtiene la contraseña que el usuario ingresó para confirmar su registro.
    public char[] obtenerConfirmacionDeRegistro() {
        return panelRegistro.obtenerConfirmacion();
    }

    // Limpia todos los campos del formulario de registro.
    public void limpiarFormularioRegistro() {
        panelRegistro.limpiarCampos();
    }

    // Asigna una acción al botón de continuar o registrar.
    // La acción recibida se ejecuta cuando el usuario presiona el botón.
    public void accionRegistrar(Runnable accion) {
        panelRegistro.accionContinuar(accion);
    }

    // Asigna una acción al botón para regresar desde el registro.
    public void accionVolverDesdeRegistro(Runnable accion) {
        panelRegistro.accionVolver(accion);
    }


    // ---------------------------------------------------------
    // MÉTODOS RELACIONADOS CON LA CAPTURA FACIAL
    // ---------------------------------------------------------

    // Establece el modo en el que funcionará la captura facial.
    // El parámetro registro permite diferenciar si se captura el rostro
    // para crear una cuenta o para iniciar sesión.
    public void establecerModoCapturaFacial(boolean registro) {
        panelCapturaFacial.establecerModo(registro);
    }

    // Inicia la visualización de la cámara en el panel de captura facial.
    // Recibe un objeto CapturadorCamara encargado de gestionar la cámara.
    public void iniciarPrevisualizacionFacial(CapturadorCamara camara) {
        panelCapturaFacial.iniciarCamara(camara);
    }

    // Permite mostrar un mensaje cuando ocurre algún problema con la cámara.
    public void informarErrorDeCamara(String mensaje) {
        panelCapturaFacial.informarErrorCamara(mensaje);
    }

    // Obtiene la imagen del rostro que fue capturado.
    // BufferedImage permite representar y manipular imágenes en memoria.
    public BufferedImage obtenerRostroCapturado() {
        return panelCapturaFacial.obtenerRostroCapturado();
    }

    // Detiene la previsualización de la cámara.
    // Se utiliza cuando ya no es necesario mostrar la imagen en pantalla.
    public void detenerPrevisualizacionFacial() {
        panelCapturaFacial.detenerPrevisualizacion();
    }

    // Asigna una acción al botón que permite capturar el rostro.
    public void accionCapturarRostro(Runnable accion) {
        panelCapturaFacial.accionCapturar(accion);
    }

    // Asigna una acción al botón para regresar desde la captura facial.
    public void accionVolverDesdeCaptura(Runnable accion) {
        panelCapturaFacial.accionVolver(accion);
    }

    // Asigna una acción al botón para cancelar el proceso de captura facial.
    public void accionCancelarCaptura(Runnable accion) {
        panelCapturaFacial.accionCancelar(accion);
    }


    // ---------------------------------------------------------
    // MÉTODOS RELACIONADOS CON LA PANTALLA DE BIENVENIDA
    // ---------------------------------------------------------

    // Establece el nombre del usuario que aparecerá en la bienvenida.
    public void establecerUsuarioBienvenida(String nombre) {
        panelBienvenida.establecerUsuario(nombre);
    }

    // Asigna una acción al botón para cerrar la sesión del usuario.
    public void accionCerrarSesion(Runnable accion) {
        panelBienvenida.accionCerrarSesion(accion);
    }

    // Asigna una acción al botón para iniciar el juego.
    public void accionIniciarJuego(Runnable accion) {
        panelBienvenida.accionIniciarJuego(accion);
    }


    // ---------------------------------------------------------
    // MÉTODOS RELACIONADOS CON LA NAVEGACIÓN DEL JUEGO
    // ---------------------------------------------------------

    // Asigna una acción al botón que permite regresar desde el juego
    // hacia la pantalla del perfil o bienvenida del usuario.
    public void accionVolverDesdeJuego(Runnable accion) {
        panelJuego.accionVolverAlPerfil(accion);
    }
}