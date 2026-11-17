package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Guarda y consulta victorias por cuenta y modalidad, separadas de los datos de autenticación. */
public final class RepositorioVictorias {
    private static final int CANTIDAD_MOSTRADA = 5;
    private final Path archivoVictorias;

    public RepositorioVictorias() {
        this(Path.of("data", "ranking_victorias.csv"));
    }

    public RepositorioVictorias(Path archivoVictorias) {
        this.archivoVictorias = archivoVictorias;
    }

    public synchronized void registrarVictoria(String nombreUsuario, String correoElectronico,
                                                ModalidadRanking modalidad) throws IOException {
        if (nombreUsuario == null || nombreUsuario.isBlank()
                || correoElectronico == null || correoElectronico.isBlank() || modalidad == null) {
            throw new IllegalArgumentException("Se necesita una cuenta y una modalidad para registrar la victoria.");
        }

        Map<String, EntradaAlmacenada> victorias = leerVictorias();
        String clave = crearClave(correoElectronico, modalidad);
        EntradaAlmacenada existente = victorias.get(clave);
        int cantidadActual = existente == null ? 0 : existente.victorias();
        victorias.put(clave, new EntradaAlmacenada(nombreUsuario.trim(), correoElectronico.trim(),
                modalidad, cantidadActual + 1));
        guardarVictorias(victorias);
    }

    public synchronized List<EntradaRanking> obtenerTopCinco(ModalidadRanking modalidad) throws IOException {
        if (modalidad == null) throw new IllegalArgumentException("Selecciona una modalidad.");
        return leerVictorias().values().stream()
                .filter(entrada -> entrada.modalidad() == modalidad)
                .map(entrada -> new EntradaRanking(entrada.nombreUsuario(), entrada.correoElectronico(),
                        entrada.victorias()))
                .sorted(Comparator.comparingInt(EntradaRanking::victorias).reversed()
                        .thenComparing(EntradaRanking::nombreUsuario, String.CASE_INSENSITIVE_ORDER))
                .limit(CANTIDAD_MOSTRADA)
                .toList();
    }

    private Map<String, EntradaAlmacenada> leerVictorias() throws IOException {
        Map<String, EntradaAlmacenada> victorias = new LinkedHashMap<>();
        if (Files.notExists(archivoVictorias)) return victorias;
        for (String linea : Files.readAllLines(archivoVictorias, StandardCharsets.UTF_8)) {
            if (linea.isBlank()) continue;
            String[] campos = linea.split(";", -1);
            if (campos.length != 4) throw new IOException("El archivo de victorias tiene un formato inválido.");
            try {
                String nombre = decodificar(campos[0]);
                String correo = decodificar(campos[1]);
                ModalidadRanking modalidad = ModalidadRanking.valueOf(campos[2]);
                int victoriasUsuario = Integer.parseInt(campos[3]);
                if (correo.isBlank() || victoriasUsuario < 1) continue;
                victorias.put(crearClave(correo, modalidad),
                        new EntradaAlmacenada(nombre, correo, modalidad, victoriasUsuario));
            } catch (IllegalArgumentException excepcion) {
                throw new IOException("El archivo de victorias está dañado.", excepcion);
            }
        }
        return victorias;
    }

    private void guardarVictorias(Map<String, EntradaAlmacenada> victorias) throws IOException {
        Path carpeta = archivoVictorias.toAbsolutePath().getParent();
        if (carpeta != null) Files.createDirectories(carpeta);
        List<String> lineas = new ArrayList<>();
        for (EntradaAlmacenada entrada : victorias.values()) {
            lineas.add(codificar(entrada.nombreUsuario()) + ";" + codificar(entrada.correoElectronico())
                    + ";" + entrada.modalidad().name() + ";" + entrada.victorias());
        }
        Files.write(archivoVictorias, lineas, StandardCharsets.UTF_8);
    }

    private String crearClave(String correo, ModalidadRanking modalidad) {
        return correo.trim().toLowerCase(java.util.Locale.ROOT) + "|" + modalidad.name();
    }

    private String codificar(String valor) {
        return Base64.getEncoder().encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    private String decodificar(String valor) {
        return new String(Base64.getDecoder().decode(valor), StandardCharsets.UTF_8);
    }

    private record EntradaAlmacenada(String nombreUsuario, String correoElectronico,
                                     ModalidadRanking modalidad, int victorias) { }
}
