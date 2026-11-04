package controller;

import model.CapturadorCamara;
import model.PersistenciaSesion;
import model.RepositorioUsuarios;
import model.ServicioHuellaFacial;
import model.Usuario;
import model.ValidadorCredenciales;
import view.VentanaAutenticacion;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AutenticacionController {
    private final VentanaAutenticacion vista;
    private final RepositorioUsuarios repositorioUsuarios;
    private final ValidadorCredenciales validadorCredenciales;
    private final ServicioHuellaFacial servicioHuellaFacial;
    private final PersistenciaSesion persistenciaSesion;
    private final JuegoController juegoController;
    private CapturadorCamara capturadorCamara;
    private long solicitudCamara;
    private Usuario usuarioActual;

    private String nombrePendiente;
    private String correoPendiente;
    private String salPendiente;
    private String hashPendiente;

    public AutenticacionController(VentanaAutenticacion vista) {
        this(vista, new RepositorioUsuarios(), new ValidadorCredenciales(),
                new ServicioHuellaFacial(), new PersistenciaSesion());
    }

    public AutenticacionController(VentanaAutenticacion vista, RepositorioUsuarios repositorioUsuarios,
                                   ValidadorCredenciales validadorCredenciales,
                                   ServicioHuellaFacial servicioHuellaFacial,
                                   PersistenciaSesion persistenciaSesion) {
        this.vista = vista;
        this.repositorioUsuarios = repositorioUsuarios;
        this.validadorCredenciales = validadorCredenciales;
        this.servicioHuellaFacial = servicioHuellaFacial;
        this.persistenciaSesion = persistenciaSesion;
        this.juegoController = new JuegoController(vista.obtenerPanelNavegacionJuego(),
                () -> vista.mostrarPantalla(VentanaAutenticacion.BIENVENIDA));
        conectarAcciones();
    }

    public void iniciarAplicacion() {
        vista.establecerCorreoIngresado(persistenciaSesion.obtenerCorreoRecordado());
        vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
        vista.setVisible(true);
        iniciarSesionRecordada();
    }

    private void conectarAcciones() {
        vista.accionIngresar(this::validarInicioSesion);
        vista.accionAccesoFacial(() -> iniciarCapturaFacial(false));
        vista.accionCrearCuenta(() -> {
            vista.limpiarFormularioRegistro();
            vista.mostrarPantalla(VentanaAutenticacion.REGISTRO);
        });
        vista.accionRegistrar(this::validarRegistro);
        vista.accionVolverDesdeRegistro(() -> vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION));
        vista.accionCapturarRostro(this::procesarRostroCapturado);
        vista.accionVolverDesdeCaptura(this::volverAlFormularioRegistro);
        vista.accionCancelarCaptura(this::cancelarAccesoFacial);
        vista.accionCerrarSesion(this::cerrarSesion);
        vista.accionIniciarJuego(() -> {
            vista.mostrarPantalla(VentanaAutenticacion.JUEGO);
            juegoController.iniciar();
        });
        vista.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent evento) {
                cerrarCapturadorCamara();
                juegoController.detener();
            }
            @Override public void windowClosed(WindowEvent evento) {
                cerrarCapturadorCamara();
                juegoController.detener();
            }
        });
    }

    private void validarInicioSesion() {
        String correo = vista.obtenerCorreoIngresado();
        char[] contrasena = vista.obtenerContrasenaIngresada();
        if (correo.isBlank() || contrasena.length == 0) {
            mostrarError("Ingresa tu correo y contraseña.");
            borrar(contrasena);
            return;
        }
        try {
            Optional<Usuario> cuenta = repositorioUsuarios.buscarPorCorreo(correo);
            if (cuenta.isEmpty() || !validadorCredenciales.coincideContrasena(cuenta.get(), contrasena)) {
                mostrarError("El correo o la contraseña no son correctos.");
                return;
            }
            actualizarSesionRecordada(cuenta.get(), vista.recordarCorreo());
            mostrarBienvenida(cuenta.get());
        } catch (IOException excepcion) {
            mostrarError("No se pudo leer el archivo local de cuentas: " + excepcion.getMessage());
        } finally {
            borrar(contrasena);
        }
    }

    private void validarRegistro() {
        String nombre = vista.obtenerNombreDeRegistro();
        String correo = vista.obtenerCorreoDeRegistro();
        char[] contrasena = vista.obtenerContrasenaDeRegistro();
        char[] confirmacion = vista.obtenerConfirmacionDeRegistro();

        if (!nombre.matches("[\\p{L}\\p{N}._-]{3,32}")) {
            mostrarError("El nombre de usuario debe tener entre 3 y 32 letras, números, puntos, guiones o guiones bajos.");
            borrar(contrasena, confirmacion);
            return;
        }
        if (!validadorCredenciales.correoValido(correo)) {
            mostrarError("Ingresa un correo electrónico válido.");
            borrar(contrasena, confirmacion);
            return;
        }
        if (contrasena.length < 8) {
            mostrarError("La contraseña debe tener al menos 8 caracteres.");
            borrar(contrasena, confirmacion);
            return;
        }
        if (!Arrays.equals(contrasena, confirmacion)) {
            mostrarError("Las contraseñas no coinciden.");
            borrar(contrasena, confirmacion);
            return;
        }
        try {
            if (repositorioUsuarios.existeCuenta(nombre, correo)) {
                mostrarError("Ya existe una cuenta con ese nombre de usuario o correo.");
                borrar(contrasena, confirmacion);
                return;
            }
            nombrePendiente = nombre;
            correoPendiente = correo;
            salPendiente = validadorCredenciales.generarSal();
            hashPendiente = validadorCredenciales.generarHash(salPendiente, contrasena);
            vista.limpiarFormularioRegistro();
            borrar(contrasena, confirmacion);
            iniciarCapturaFacial(true);
        } catch (IOException excepcion) {
            mostrarError("No se pudo comprobar el archivo local de cuentas: " + excepcion.getMessage());
            borrar(contrasena, confirmacion);
        }
    }

    private void iniciarCapturaFacial(boolean esRegistro) {
        cerrarCapturadorCamara();
        vista.establecerModoCapturaFacial(esRegistro);
        vista.mostrarPantalla(VentanaAutenticacion.CAPTURA_FACIAL);
        long numeroSolicitud = solicitudCamara;
        SwingWorker<CapturadorCamara, Void> apertura = new SwingWorker<>() {
            @Override protected CapturadorCamara doInBackground() throws Exception { return new CapturadorCamara(); }
            @Override protected void done() {
                try {
                    CapturadorCamara nuevoCapturador = get();
                    if (numeroSolicitud != solicitudCamara || !vista.isDisplayable()) {
                        nuevoCapturador.close();
                        return;
                    }
                    capturadorCamara = nuevoCapturador;
                    vista.iniciarPrevisualizacionFacial(nuevoCapturador);
                } catch (Exception excepcion) {
                    if (numeroSolicitud != solicitudCamara) return;
                    Throwable causa = excepcion.getCause() == null ? excepcion : excepcion.getCause();
                    vista.informarErrorDeCamara(causa.getMessage());
                }
            }
        };
        apertura.execute();
    }

    private void procesarRostroCapturado() {
        BufferedImage rostro = vista.obtenerRostroCapturado();
        if (rostro == null) {
            mostrarError("No se pudo capturar el rostro. Colócate frente a la cámara e inténtalo de nuevo.");
            return;
        }
        if (correoPendiente != null) {
            String plantilla = servicioHuellaFacial.generarPlantilla(rostro);
            guardarCuentaConRostro(plantilla);
        } else {
            validarRostroDeAcceso(rostro);
        }
    }

    private void guardarCuentaConRostro(String plantilla) {
        try {
            Usuario nuevoUsuario = new Usuario(nombrePendiente, correoPendiente, salPendiente, hashPendiente, plantilla);
            repositorioUsuarios.registrarUsuario(nuevoUsuario);
            String correoRegistrado = correoPendiente;
            limpiarRegistroPendiente();
            cerrarCapturadorCamara();
            vista.establecerCorreoIngresado(correoRegistrado);
            vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
            vista.mostrarAviso("Cuenta creada", "Tu cuenta quedó registrada. Ya puedes iniciar sesión con tu contraseña o con la cámara.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException excepcion) {
            mostrarError("No se pudo guardar la cuenta: " + excepcion.getMessage());
        }
    }

    private void validarRostroDeAcceso(BufferedImage rostroCapturado) {
        try {
            List<String> plantillasCapturadas = servicioHuellaFacial.generarPlantillasCompatibles(rostroCapturado);
            List<Usuario> usuarios = repositorioUsuarios.obtenerUsuarios();
            Usuario coincidencia = usuarios.stream()
                    .filter(usuario -> plantillasCapturadas.stream().anyMatch(plantillaCapturada ->
                            servicioHuellaFacial.coincide(usuario.obtenerPlantillaFacial(), plantillaCapturada)))
                    .findFirst().orElse(null);
            if (coincidencia == null) {
                cerrarCapturadorCamara();
                vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
                mostrarError("No se reconoció el rostro. Comprueba que registraste este rostro y vuelve a intentarlo.");
                return;
            }
            cerrarCapturadorCamara();
            actualizarSesionRecordada(coincidencia, vista.recordarCorreo());
            mostrarBienvenida(coincidencia);
        } catch (IOException excepcion) {
            cerrarCapturadorCamara();
            vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
            mostrarError("No se pudo consultar el registro local: " + excepcion.getMessage());
        }
    }
    private void mostrarBienvenida(Usuario usuario) {
        usuarioActual = usuario;
        vista.establecerUsuarioBienvenida(usuario.obtenerNombreUsuario());
        vista.mostrarPantalla(VentanaAutenticacion.BIENVENIDA);
    }

    private void actualizarSesionRecordada(Usuario usuario, boolean recordar) {
        try {
            if (recordar) {
                String token = persistenciaSesion.generarToken();
                repositorioUsuarios.guardarHashSesion(usuario.obtenerCorreoElectronico(),
                        persistenciaSesion.calcularHashToken(token));
                persistenciaSesion.guardarSesion(usuario.obtenerCorreoElectronico(), token);
                vista.establecerCorreoIngresado(usuario.obtenerCorreoElectronico());
            } else {
                repositorioUsuarios.guardarHashSesion(usuario.obtenerCorreoElectronico(), null);
                persistenciaSesion.borrarSesion();
            }
        } catch (IOException excepcion) {
            vista.mostrarAviso("Sesión", "La cuenta se autenticó, pero no se pudo guardar la opción Recordarme.", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void iniciarSesionRecordada() {
        String correo = persistenciaSesion.obtenerCorreoRecordado();
        String token = persistenciaSesion.obtenerTokenRecordado();
        if (correo.isBlank() || token.isBlank()) return;
        try {
            Optional<Usuario> cuenta = repositorioUsuarios.buscarPorCorreo(correo);
            if (cuenta.isPresent() && cuenta.get().obtenerHashSesionRecordada() != null
                    && cuenta.get().obtenerHashSesionRecordada().equals(persistenciaSesion.calcularHashToken(token))) {
                mostrarBienvenida(cuenta.get());
            } else {
                persistenciaSesion.borrarSesion();
                vista.establecerCorreoIngresado(correo);
            }
        } catch (IOException excepcion) {
            vista.establecerCorreoIngresado(correo);
        }
    }

    private void cerrarSesion() {
        try {
            if (usuarioActual != null) {
                repositorioUsuarios.guardarHashSesion(usuarioActual.obtenerCorreoElectronico(), null);
            }
            persistenciaSesion.borrarSesion();
        } catch (IOException excepcion) {
            vista.mostrarAviso("Sesión", "No se pudo borrar la sesión recordada del almacenamiento local.", JOptionPane.WARNING_MESSAGE);
        }
        usuarioActual = null;
        vista.limpiarContrasenaIngresada();
        vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
    }

    private void volverAlFormularioRegistro() {
        cerrarCapturadorCamara();
        limpiarRegistroPendiente();
        vista.mostrarPantalla(VentanaAutenticacion.REGISTRO);
    }

    private void cancelarAccesoFacial() {
        cerrarCapturadorCamara();
        vista.mostrarPantalla(VentanaAutenticacion.INICIO_SESION);
    }

    private void cerrarCapturadorCamara() {
        solicitudCamara++;
        vista.detenerPrevisualizacionFacial();
        if (capturadorCamara != null) {
            capturadorCamara.close();
            capturadorCamara = null;
        }
    }

    private void limpiarRegistroPendiente() {
        nombrePendiente = null;
        correoPendiente = null;
        salPendiente = null;
        hashPendiente = null;
    }

    private void mostrarError(String mensaje) {
        vista.mostrarAviso("No se pudo completar la operación", mensaje, JOptionPane.ERROR_MESSAGE);
    }

    private void borrar(char[]... textos) {
        for (char[] texto : textos) if (texto != null) Arrays.fill(texto, '\0');
    }
}
