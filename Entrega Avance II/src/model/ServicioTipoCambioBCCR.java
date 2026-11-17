package model;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Consulta el indicador de compra del dólar (317) en la API SDDE del BCCR. */
public final class ServicioTipoCambioBCCR {
    private static final String URL_SERIES = "https://apim.bccr.fi.cr/SDDE/api/"
            + "Bccr.GE.SDDE.Publico.Indicadores.API/indicadoresEconomicos/317/series";
    private static final Pattern VALOR_SERIE = Pattern.compile(
            "\\\"fecha\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"[^{}]*?"
                    + "\\\"valorDatoPorPeriodo\\\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
    private static final DateTimeFormatter FORMATO_FECHA_API = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter FORMATO_FECHA_URL = DateTimeFormatter.ofPattern("yyyy%2FMM%2Fdd");
    private final HttpClient clienteHttp = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(10)).build();
    private final String token;

    public ServicioTipoCambioBCCR() {
        token = System.getenv("MEMORYGAME_BCCR_TOKEN");
    }

    /** Retorna la última tasa válida del rango de diez días, ignorando fines de semana y feriados. */
    public TipoCambio consultarCompra() throws Exception {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Configura MEMORYGAME_BCCR_TOKEN con tu token de suscripción del BCCR.");
        }
        LocalDate fechaFinal = LocalDate.now();
        LocalDate fechaInicial = fechaFinal.minusDays(10);
        String url = URL_SERIES + "?fechaInicio=" + codificarFecha(fechaInicial)
                + "&fechaFin=" + codificarFecha(fechaFinal) + "&idioma=ES";
        HttpRequest solicitud = HttpRequest.newBuilder(URI.create(url))
                .timeout(java.time.Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + token.trim())
                .header("Accept", "application/json")
                .GET().build();
        HttpResponse<String> respuesta = clienteHttp.send(solicitud,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (respuesta.statusCode() != 200) {
            throw new IllegalStateException("El BCCR respondió HTTP " + respuesta.statusCode()
                    + ". Revisa el token y vuelve a intentar.");
        }
        Matcher coincidencias = VALOR_SERIE.matcher(respuesta.body());
        TipoCambio ultimoTipoCambio = null;
        while (coincidencias.find()) {
            double valor = Double.parseDouble(coincidencias.group(2));
            if (valor > 0) ultimoTipoCambio = new TipoCambio(valor, coincidencias.group(1));
        }
        if (ultimoTipoCambio == null) {
            throw new IllegalStateException("La respuesta del BCCR no contiene una tasa de compra válida.");
        }
        return ultimoTipoCambio;
    }

    private String codificarFecha(LocalDate fecha) {
        return URLEncoder.encode(FORMATO_FECHA_API.format(fecha), StandardCharsets.UTF_8).replace("+", "%20");
    }

    public record TipoCambio(double colonesPorDolar, String fecha) { }
}
