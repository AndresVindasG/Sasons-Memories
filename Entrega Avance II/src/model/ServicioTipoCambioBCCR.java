package model;

// Permite construir y manejar direcciones de internet.
import java.net.URI;

// Permite codificar parámetros para utilizarlos en una URL.
import java.net.URLEncoder;

// Permite realizar solicitudes HTTP a servicios externos.
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Permite trabajar con caracteres UTF-8.
import java.nio.charset.StandardCharsets;

// Permite trabajar con fechas sin incluir horas.
import java.time.LocalDate;

// Permite definir formatos para representar fechas.
import java.time.format.DateTimeFormatter;

// Permite buscar patrones dentro de una cadena de texto.
import java.util.regex.Matcher;
import java.util.regex.Pattern;

<<<<<<< Updated upstream
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
=======
/**
 * Servicio encargado de consultar el indicador de compra del dólar (317)
 * mediante la API del Sistema de Divulgación de Datos Económicos (SDDE)
 * del Banco Central de Costa Rica (BCCR).
 *
 * Obtiene el valor más reciente disponible dentro de un rango de diez días.
 */
public final class ServicioTipoCambioBCCR {

    // Dirección principal de la API del BCCR.
    // El indicador 317 corresponde a la compra del dólar.
    private static final String URL_SERIES =
            "https://apim.bccr.fi.cr/SDDE/api/"
                    + "Bccr.GE.SDDE.Publico.Indicadores.API/indicadoresEconomicos/317/series";

    // Expresión regular utilizada para localizar dentro del JSON
    // la fecha y el valor correspondiente a cada registro.
    private static final Pattern VALOR_SERIE = Pattern.compile(
            "\\\"fecha\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"[^{}]*?"
                    + "\\\"valorDatoPorPeriodo\\\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)"
    );

    // Define el formato de fecha que espera la API.
    private static final DateTimeFormatter FORMATO_FECHA_API =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // Define otro formato de fecha codificado para una URL.
    // Actualmente no se utiliza en esta implementación.
    private static final DateTimeFormatter FORMATO_FECHA_URL =
            DateTimeFormatter.ofPattern("yyyy%2FMM%2Fdd");

    // Cliente HTTP encargado de realizar las conexiones con el BCCR.
    private final HttpClient clienteHttp = HttpClient.newBuilder()

            // Establece un tiempo máximo de 10 segundos para establecer
            // la conexión con el servidor.
            .connectTimeout(java.time.Duration.ofSeconds(10))

            // Construye el cliente HTTP con la configuración anterior.
            .build();

    // Almacena el token de autenticación necesario para consultar la API.
>>>>>>> Stashed changes
    private final String token;


    /**
     * Constructor de la clase.
     *
     * Obtiene el token de autenticación desde una variable de entorno
     * del sistema operativo.
     */
    public ServicioTipoCambioBCCR() {
<<<<<<< Updated upstream
        token = System.getenv("MEMORYGAME_BCCR_TOKEN"); //esto se pasó por la UI del IDE
    }

    //Retorna la última tasa válida del rango de diez días, ignorando fines de semana y feriados.
    public TipoCambio consultarCompra() throws Exception {
        if (token == null || token.isBlank()) { //VERIFICA SI EL TOKEN ESTÁ Y CONFIGURADO
            throw new IllegalStateException("Configura MEMORYGAME_BCCR_TOKEN con tu token de suscripción del BCCR.");
=======

        // Lee el valor de la variable de entorno MEMORYGAME_BCCR_TOKEN.
        // De esta manera no es necesario escribir el token directamente
        // dentro del código fuente.
        token = System.getenv("MEMORYGAME_BCCR_TOKEN");
    }


    /**
     * Consulta el indicador de compra del dólar en el BCCR.
     *
     * Busca los registros de los últimos diez días para encontrar
     * el valor más reciente disponible.
     *
     * @return objeto TipoCambio con el valor y la fecha consultada.
     * @throws Exception si ocurre un error durante la solicitud.
     */
    public TipoCambio consultarCompra() throws Exception {

        // Comprueba que exista un token de autenticación.
        // También verifica que no esté vacío.
        if (token == null || token.isBlank()) {

            // Si no existe el token, detiene la consulta y muestra
            // un mensaje indicando cómo configurarlo.
            throw new IllegalStateException(
                    "Configura MEMORYGAME_BCCR_TOKEN con tu token de suscripción del BCCR."
            );
>>>>>>> Stashed changes
        }

        // Obtiene la fecha actual del equipo.
        LocalDate fechaFinal = LocalDate.now();

        // Calcula una fecha inicial diez días antes de la fecha actual.
        // Esto permite encontrar el último registro disponible,
        // incluso si existen fines de semana o feriados.
        LocalDate fechaInicial = fechaFinal.minusDays(10);
<<<<<<< Updated upstream
        String url = URL_SERIES + "?fechaInicio=" + codificarFecha(fechaInicial) //Toma la fecha de hoy (LocalDate.now()) y calcula una fecha inicial de 10 días atrás.
                + "&fechaFin=" + codificarFecha(fechaFinal) + "&idioma=ES";
=======

        // Construye la dirección completa de consulta.
        // Incluye la fecha inicial, la fecha final y el idioma español.
        String url = URL_SERIES
                + "?fechaInicio=" + codificarFecha(fechaInicial)
                + "&fechaFin=" + codificarFecha(fechaFinal)
                + "&idioma=ES";

        // Construye la solicitud HTTP que se enviará al BCCR.
>>>>>>> Stashed changes
        HttpRequest solicitud = HttpRequest.newBuilder(URI.create(url))

                // Establece un tiempo máximo de 20 segundos
                // para recibir la respuesta del servidor.
                .timeout(java.time.Duration.ofSeconds(20))

                // Envía el token para autenticar la solicitud.
                // Bearer es el esquema de autenticación utilizado.
                .header("Authorization", "Bearer " + token.trim())

                // Indica que esperamos recibir información en formato JSON.
                .header("Accept", "application/json")
<<<<<<< Updated upstream
                .GET().build();

        //Si el código de estado HTTP no es 200 OK tira una excepcion
        HttpResponse<String> respuesta = clienteHttp.send(solicitud,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
=======

                // Define que la solicitud será de tipo GET.
                // GET se utiliza para consultar información.
                .GET()

                // Construye la solicitud.
                .build();

        // Envía la solicitud al servidor del BCCR.
        // La respuesta se recibe como una cadena de texto utilizando UTF-8.
        HttpResponse<String> respuesta = clienteHttp.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        // Comprueba el código HTTP recibido.
        // El código 200 indica que la solicitud fue exitosa.
>>>>>>> Stashed changes
        if (respuesta.statusCode() != 200) {

            // Si el servidor devuelve otro código,
            // genera una excepción indicando el problema.
            throw new IllegalStateException(
                    "El BCCR respondió HTTP " + respuesta.statusCode()
                            + ". Revisa el token y vuelve a intentar."
            );
        }

<<<<<<< Updated upstream
        //Recorre el cuerpo de la respuesta buscando todas las coincidencias del patrón definido en VALOR SERIE
=======
        // Obtiene el contenido de la respuesta JSON
        // y crea un buscador basado en la expresión regular definida.
>>>>>>> Stashed changes
        Matcher coincidencias = VALOR_SERIE.matcher(respuesta.body());

        // Inicializa la variable donde se almacenará
        // el último tipo de cambio válido encontrado.
        TipoCambio ultimoTipoCambio = null;

        // Recorre todas las coincidencias encontradas en la respuesta.
        while (coincidencias.find()) {

            // Obtiene el segundo grupo de la expresión regular,
            // que contiene el valor del tipo de cambio.
            // Luego convierte ese texto a un número decimal.
            double valor = Double.parseDouble(coincidencias.group(2));

            // Comprueba que el valor sea mayor que cero.
            // Si es válido, guarda el tipo de cambio y su fecha.
            // Si aparecen más registros válidos, reemplaza el anterior.
            if (valor > 0) {
                ultimoTipoCambio = new TipoCambio(
                        valor,
                        coincidencias.group(1)
                );
            }
        }

        // Comprueba si se encontró algún tipo de cambio válido.
        if (ultimoTipoCambio == null) {

            // Si no se encontró información válida,
            // genera una excepción indicando el problema.
            throw new IllegalStateException(
                    "La respuesta del BCCR no contiene una tasa de compra válida."
            );
        }
<<<<<<< Updated upstream
        //se quedará guardado el registro más reciente (el último día hábil encontrado)
        return ultimoTipoCambio;
    }

    //Convierte un objeto de fecha al formato de texto esperado por la API (yyyy/MM/dd) y lo codifica de manera segura para URLs DONDE %20 son espacios
=======

        // Devuelve el último tipo de cambio válido encontrado.
        return ultimoTipoCambio;
    }


    /**
     * Codifica una fecha para poder utilizarla como parámetro
     * dentro de una dirección URL.
     *
     * @param fecha fecha que se desea codificar.
     * @return fecha convertida a un formato válido para una URL.
     */
>>>>>>> Stashed changes
    private String codificarFecha(LocalDate fecha) {

        // Convierte la fecha al formato año/mes/día.
        // Después la codifica utilizando UTF-8 para incluirla
        // correctamente dentro de la dirección de la API.
        return URLEncoder.encode(
                FORMATO_FECHA_API.format(fecha),
                StandardCharsets.UTF_8
        ).replace("+", "%20");
    }


    /**
     * Record que representa el resultado de la consulta del BCCR.
     *
     * Almacena el valor del tipo de cambio y la fecha correspondiente.
     *
     * @param colonesPorDolar cantidad de colones por cada dólar.
     * @param fecha fecha del registro obtenido.
     */
    public record TipoCambio(
            double colonesPorDolar,
            String fecha
    ) {
    }
}