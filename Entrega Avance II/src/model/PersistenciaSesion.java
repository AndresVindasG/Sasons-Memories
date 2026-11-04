package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Administra un token de sesión recordada; nunca guarda la contraseña. */
public class PersistenciaSesion {
    private final Path archivoSesion;
    private final SecureRandom aleatorio = new SecureRandom();

    public PersistenciaSesion() {
        String carpetaLocal = System.getenv("LOCALAPPDATA");
        Path carpetaBase = carpetaLocal == null || carpetaLocal.isBlank()
                ? Path.of(System.getProperty("user.home"), ".memory-game")
                : Path.of(carpetaLocal, "MemoryGame");
        archivoSesion = carpetaBase.resolve("sesion_recordada.dat");
    }

    public String generarToken() {
        byte[] token = new byte[32];
        aleatorio.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    public String calcularHashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception excepcion) {
            throw new IllegalStateException("No se pudo proteger la sesión recordada.", excepcion);
        }
    }

    public void guardarSesion(String correo, String token) throws IOException {
        Files.createDirectories(archivoSesion.toAbsolutePath().getParent());
        String correoCodificado = Base64.getEncoder().encodeToString(correo.getBytes(StandardCharsets.UTF_8));
        Files.writeString(archivoSesion, correoCodificado + System.lineSeparator() + token, StandardCharsets.UTF_8);
    }

    public String obtenerCorreoRecordado() {
        String[] datos = leerDatos();
        if (datos.length != 2) return "";
        try {
            return new String(Base64.getDecoder().decode(datos[0]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException excepcion) {
            return "";
        }
    }

    public String obtenerTokenRecordado() {
        String[] datos = leerDatos();
        return datos.length == 2 ? datos[1] : "";
    }

    public void borrarSesion() throws IOException {
        Files.deleteIfExists(archivoSesion);
    }

    private String[] leerDatos() {
        try {
            if (!Files.exists(archivoSesion)) return new String[0];
            return Files.readString(archivoSesion, StandardCharsets.UTF_8).split("\\R", -1);
        } catch (IOException excepcion) {
            return new String[0];
        }
    }
}