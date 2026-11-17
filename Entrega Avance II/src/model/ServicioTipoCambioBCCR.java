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

//**Consulta la compra del dolar 317 con el API del BCCR**//

public final class ServicioTipoCambioBCCR {
    //endpoint oficial del BCCR para consultar la serie histórica del indicador 317 (Compra de dólares).
    private static final String URL_SERIES = "https://apim.bccr.fi.cr/SDDE/api/"
            + "Bccr.GE.SDDE.Publico.Indicadores.API/indicadoresEconomicos/317/series";

    //busca directamente dentro del texto de la respuesta HTTP los campos de fecha y tasa de cambio
    private static final Pattern VALOR_SERIE = Pattern.compile(
            "\\\"fecha\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"[^{}]*?"
                    + "\\\"valorDatoPorPeriodo\\\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");

    private static final DateTimeFormatter FORMATO_FECHA_API = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter FORMATO_FECHA_URL = DateTimeFormatter.ofPattern("yyyy%2FMM%2Fdd");
    private final HttpClient clienteHttp = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(10)).build(); //10 segs de conexión entre lo del api del bccr por si se cae la página
    private final String token;

    public ServicioTipoCambioBCCR() {
        token = System.getenv("MEMORYGAME_BCCR_TOKEN"); //esto se pasó por la UI del IDE
    }

    //Retorna la última tasa válida del rango de diez días, ignorando fines de semana y feriados.
    public TipoCambio consultarCompra() throws Exception {
        if (token == null || token.isBlank()) { //VERIFICA SI EL TOKEN ESTÁ Y CONFIGURADO
            throw new IllegalStateException("Configura MEMORYGAME_BCCR_TOKEN con tu token de suscripción del BCCR.");
        }
        LocalDate fechaFinal = LocalDate.now();
        LocalDate fechaInicial = fechaFinal.minusDays(10);
        String url = URL_SERIES + "?fechaInicio=" + codificarFecha(fechaInicial) //Toma la fecha de hoy (LocalDate.now()) y calcula una fecha inicial de 10 días atrás.
                + "&fechaFin=" + codificarFecha(fechaFinal) + "&idioma=ES";
        HttpRequest solicitud = HttpRequest.newBuilder(URI.create(url))
                .timeout(java.time.Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + token.trim())
                .header("Accept", "application/json")
                .GET().build();

        //Si el código de estado HTTP no es 200 OK tira una excepcion
        HttpResponse<String> respuesta = clienteHttp.send(solicitud,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (respuesta.statusCode() != 200) {
            throw new IllegalStateException("El BCCR respondió HTTP " + respuesta.statusCode()
                    + ". Revisa el token y vuelve a intentar.");
        }

        //Recorre el cuerpo de la respuesta buscando todas las coincidencias del patrón definido en VALOR SERIE
        Matcher coincidencias = VALOR_SERIE.matcher(respuesta.body());
        TipoCambio ultimoTipoCambio = null;
        while (coincidencias.find()) {
            double valor = Double.parseDouble(coincidencias.group(2));
            if (valor > 0) ultimoTipoCambio = new TipoCambio(valor, coincidencias.group(1));
        }
        if (ultimoTipoCambio == null) {
            throw new IllegalStateException("La respuesta del BCCR no contiene una tasa de compra válida.");
        }
        //se quedará guardado el registro más reciente (el último día hábil encontrado)
        return ultimoTipoCambio;
    }

    //Convierte un objeto de fecha al formato de texto esperado por la API (yyyy/MM/dd) y lo codifica de manera segura para URLs DONDE %20 son espacios
    private String codificarFecha(LocalDate fecha) {
        return URLEncoder.encode(FORMATO_FECHA_API.format(fecha), StandardCharsets.UTF_8).replace("+", "%20");
    }

    public record TipoCambio(double colonesPorDolar, String fecha) { }
}
