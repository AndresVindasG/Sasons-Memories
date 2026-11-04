package model;

public class Usuario {
    private final String nombreUsuario;
    private final String correoElectronico;
    private final String salContrasena;
    private final String hashContrasena;
    private String plantillaFacial;
    private String hashSesionRecordada;

    public Usuario(String nombreUsuario, String correoElectronico, String salContrasena,
                   String hashContrasena, String plantillaFacial) {
        this(nombreUsuario, correoElectronico, salContrasena, hashContrasena, plantillaFacial, null);
    }

    public Usuario(String nombreUsuario, String correoElectronico, String salContrasena,
                   String hashContrasena, String plantillaFacial, String hashSesionRecordada) {
        this.nombreUsuario = nombreUsuario;
        this.correoElectronico = correoElectronico;
        this.salContrasena = salContrasena;
        this.hashContrasena = hashContrasena;
        this.plantillaFacial = plantillaFacial;
        this.hashSesionRecordada = hashSesionRecordada;
    }

    public String obtenerNombreUsuario() { return nombreUsuario; }
    public String obtenerCorreoElectronico() { return correoElectronico; }
    public String obtenerSalContrasena() { return salContrasena; }
    public String obtenerHashContrasena() { return hashContrasena; }
    public String obtenerPlantillaFacial() { return plantillaFacial; }
    public void establecerPlantillaFacial(String plantillaFacial) { this.plantillaFacial = plantillaFacial; }
    public String obtenerHashSesionRecordada() { return hashSesionRecordada; }
    public void establecerHashSesionRecordada(String hashSesionRecordada) { this.hashSesionRecordada = hashSesionRecordada; }
}