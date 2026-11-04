package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/** Guarda las cuentas localmente en un archivo delimitado y legible entre sesiones. */
public class RepositorioUsuarios {
    private final Path archivoUsuarios;

    public RepositorioUsuarios() {
        this(Path.of("data", "usuarios.csv"));
    }

    public RepositorioUsuarios(Path archivoUsuarios) {
        this.archivoUsuarios = archivoUsuarios;
    }

    public synchronized List<Usuario> obtenerUsuarios() throws IOException {
        if (Files.notExists(archivoUsuarios)) return new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();
        for (String linea : Files.readAllLines(archivoUsuarios, StandardCharsets.UTF_8)) {
            String[] campos = linea.split(";", -1);
            if (campos.length != 5 && campos.length != 6) continue;
            try {
                usuarios.add(new Usuario(decodificar(campos[0]), decodificar(campos[1]), campos[2], campos[3],
                        campos[4].isBlank() ? null : campos[4], campos.length == 6 && !campos[5].isBlank() ? campos[5] : null));
            } catch (IllegalArgumentException excepcion) {
                throw new IOException("El archivo local de usuarios está dañado.", excepcion);
            }
        }
        return usuarios;
    }

    public synchronized Optional<Usuario> buscarPorCorreo(String correo) throws IOException {
        return obtenerUsuarios().stream()
                .filter(usuario -> usuario.obtenerCorreoElectronico().equalsIgnoreCase(correo.trim()))
                .findFirst();
    }

    public synchronized boolean existeCuenta(String nombreUsuario, String correo) throws IOException {
        return obtenerUsuarios().stream().anyMatch(usuario ->
                usuario.obtenerCorreoElectronico().equalsIgnoreCase(correo.trim())
                        || usuario.obtenerNombreUsuario().equalsIgnoreCase(nombreUsuario.trim()));
    }

    public synchronized void registrarUsuario(Usuario usuario) throws IOException {
        List<Usuario> usuarios = obtenerUsuarios();
        usuarios.add(usuario);
        guardarUsuarios(usuarios);
    }

    public synchronized void guardarPlantillaFacial(String correo, String plantilla) throws IOException {
        List<Usuario> usuarios = obtenerUsuarios();
        Usuario usuario = usuarios.stream()
                .filter(actual -> actual.obtenerCorreoElectronico().equalsIgnoreCase(correo))
                .findFirst().orElseThrow(() -> new IOException("No se encontró la cuenta para guardar el rostro."));
        usuario.establecerPlantillaFacial(plantilla);
        guardarUsuarios(usuarios);
    }

    public synchronized void guardarHashSesion(String correo, String hashSesion) throws IOException {
        List<Usuario> usuarios = obtenerUsuarios();
        Usuario usuario = usuarios.stream()
                .filter(actual -> actual.obtenerCorreoElectronico().equalsIgnoreCase(correo))
                .findFirst().orElseThrow(() -> new IOException("No se encontró la cuenta para recordar la sesión."));
        usuario.establecerHashSesionRecordada(hashSesion);
        guardarUsuarios(usuarios);
    }

    private void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        Path carpeta = archivoUsuarios.toAbsolutePath().getParent();
        if (carpeta != null) Files.createDirectories(carpeta);
        List<String> lineas = usuarios.stream().map(usuario ->
                codificar(usuario.obtenerNombreUsuario()) + ";" + codificar(usuario.obtenerCorreoElectronico()) + ";"
                        + usuario.obtenerSalContrasena() + ";" + usuario.obtenerHashContrasena() + ";"
                        + (usuario.obtenerPlantillaFacial() == null ? "" : usuario.obtenerPlantillaFacial()) + ";"
                        + (usuario.obtenerHashSesionRecordada() == null ? "" : usuario.obtenerHashSesionRecordada()))
                .toList();
        Files.write(archivoUsuarios, lineas, StandardCharsets.UTF_8);
    }

    private String codificar(String valor) {
        return Base64.getEncoder().encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    private String decodificar(String valor) {
        return new String(Base64.getDecoder().decode(valor), StandardCharsets.UTF_8);
    }
}
