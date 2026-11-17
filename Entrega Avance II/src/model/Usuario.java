package model;

// Clase que representa a un usuario registrado en el sistema.
// Pertenece al modelo de la arquitectura MVC.
// Se encarga de almacenar la información personal y de autenticación.
public class Usuario {

    // Nombre de usuario registrado.
    // Es final porque no debe cambiar después de crear el objeto.
    private final String nombreUsuario;

    // Correo electrónico asociado a la cuenta del usuario.
    // También es inmutable después de crear el objeto.
    private final String correoElectronico;

    // Sal utilizada durante el proceso de protección de la contraseña.
    // Se emplea para generar un hash diferente para cada usuario.
    private final String salContrasena;

    // Hash de la contraseña del usuario.
    // Almacena el resultado del proceso de hash y no la contraseña original.
    private final String hashContrasena;

    // Almacena la plantilla facial asociada al usuario.
    // No es final porque puede establecerse o actualizarse posteriormente.
    private String plantillaFacial;

    // Almacena el hash de la sesión recordada.
    // Permite identificar una sesión que el sistema debe recordar.
    private String hashSesionRecordada;


    // ---------------------------------------------------------
    // PRIMER CONSTRUCTOR
    // ---------------------------------------------------------

    // Constructor que recibe los datos principales del usuario.
    // No recibe el hash de sesión recordada, por lo que se establece en null.
    public Usuario(String nombreUsuario, String correoElectronico,
                   String salContrasena, String hashContrasena,
                   String plantillaFacial) {

        // Llama al segundo constructor de esta misma clase.
        // Se utiliza this() para reutilizar la inicialización de los atributos.
        //
        // Se envían todos los datos recibidos y null como valor
        // inicial para el hash de sesión recordada.
        this(nombreUsuario, correoElectronico, salContrasena,
                hashContrasena, plantillaFacial, null);
    }


    // ---------------------------------------------------------
    // SEGUNDO CONSTRUCTOR
    // ---------------------------------------------------------

    // Constructor que recibe todos los datos del usuario,
    // incluyendo el hash de la sesión recordada.
    public Usuario(String nombreUsuario, String correoElectronico,
                   String salContrasena, String hashContrasena,
                   String plantillaFacial, String hashSesionRecordada) {

        // Asigna el nombre recibido al atributo de la clase.
        // this permite diferenciar el atributo del parámetro.
        this.nombreUsuario = nombreUsuario;

        // Guarda el correo electrónico del usuario.
        this.correoElectronico = correoElectronico;

        // Guarda la sal utilizada para proteger la contraseña.
        this.salContrasena = salContrasena;

        // Guarda el hash de la contraseña.
        this.hashContrasena = hashContrasena;

        // Guarda la plantilla facial del usuario.
        this.plantillaFacial = plantillaFacial;

        // Guarda el hash correspondiente a la sesión recordada.
        this.hashSesionRecordada = hashSesionRecordada;
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA OBTENER LOS DATOS DEL USUARIO
    // ---------------------------------------------------------

    // Devuelve el nombre de usuario.
    // Permite consultar el atributo sin acceder directamente a él.
    public String obtenerNombreUsuario() {
        return nombreUsuario;
    }

    // Devuelve el correo electrónico registrado.
    public String obtenerCorreoElectronico() {
        return correoElectronico;
    }

    // Devuelve la sal utilizada en el proceso de protección
    // de la contraseña.
    public String obtenerSalContrasena() {
        return salContrasena;
    }

    // Devuelve el hash de la contraseña almacenada.
    public String obtenerHashContrasena() {
        return hashContrasena;
    }

    // Devuelve la plantilla facial asociada al usuario.
    public String obtenerPlantillaFacial() {
        return plantillaFacial;
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA MODIFICAR LA INFORMACIÓN FACIAL
    // ---------------------------------------------------------

    // Permite establecer o actualizar la plantilla facial del usuario.
    // Recibe una nueva plantilla y la almacena en el atributo correspondiente.
    public void establecerPlantillaFacial(String plantillaFacial) {

        // this.plantillaFacial representa el atributo de la clase.
        // plantillaFacial representa el valor recibido como parámetro.
        this.plantillaFacial = plantillaFacial;
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA ADMINISTRAR LA SESIÓN RECORDADA
    // ---------------------------------------------------------

    // Devuelve el hash de la sesión recordada.
    public String obtenerHashSesionRecordada() {
        return hashSesionRecordada;
    }

    // Permite establecer o actualizar el hash de la sesión recordada.
    public void establecerHashSesionRecordada(String hashSesionRecordada) {

        // Asigna el nuevo valor al atributo de la clase.
        this.hashSesionRecordada = hashSesionRecordada;
    }
}