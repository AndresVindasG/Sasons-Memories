package model;

// Importa las clases necesarias para generar claves criptográficas.
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Permite comparar hashes de manera segura.
import java.security.MessageDigest;

// Genera números aleatorios criptográficamente seguros.
import java.security.SecureRandom;

// Permite convertir datos binarios a texto y viceversa.
import java.util.Base64;

/**
 * Protege las contraseñas utilizando PBKDF2
 * y una sal aleatoria individual para cada usuario.
 */
public class ValidadorCredenciales {

    // Cantidad de iteraciones utilizadas por el algoritmo PBKDF2.
    // Un mayor número de iteraciones aumenta el costo de calcular el hash.
    private static final int ITERACIONES = 160_000;

    // Generador de números aleatorios seguros.
    // Se utiliza para crear una sal diferente para cada contraseña.
    private final SecureRandom generadorAleatorio = new SecureRandom();


    /**
     * Comprueba si un correo electrónico tiene un formato válido.
     *
     * @param correo correo que se desea validar.
     * @return true si cumple el formato establecido, false en caso contrario.
     */
    public boolean correoValido(String correo) {

        // Comprueba que el correo no sea null y que cumpla
        // con una estructura básica de dirección electrónica.
        return correo != null
                && correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }


    /**
     * Genera una sal aleatoria para proteger una contraseña.
     *
     * @return sal codificada en formato Base64.
     */
    public String generarSal() {

        // Crea un arreglo de 16 bytes para almacenar la sal.
        byte[] sal = new byte[16];

        // Llena el arreglo con valores aleatorios seguros.
        generadorAleatorio.nextBytes(sal);

        // Convierte los bytes a texto utilizando Base64
        // para facilitar su almacenamiento.
        return Base64.getEncoder().encodeToString(sal);
    }


    /**
     * Genera un hash de una contraseña utilizando PBKDF2.
     *
     * @param salCodificada sal del usuario codificada en Base64.
     * @param contrasena contraseña que se desea proteger.
     * @return hash de la contraseña codificado en Base64.
     */
    public String generarHash(String salCodificada, char[] contrasena) {

        // Decodifica la sal para recuperar sus bytes originales.
        byte[] sal = Base64.getDecoder().decode(salCodificada);

        // Crea una especificación para el algoritmo PBKDF2.
        // Recibe la contraseña, la sal, las iteraciones y el tamaño
        // del resultado en bits (256).
        PBEKeySpec especificacion =
                new PBEKeySpec(contrasena, sal, ITERACIONES, 256);

        try {

            // Obtiene una instancia del algoritmo PBKDF2
            // utilizando HMAC con SHA-256.
            byte[] hash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion)
                    .getEncoded();

            // Convierte el resultado binario del hash a Base64.
            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception excepcion) {

            // Si ocurre un error durante el proceso criptográfico,
            // genera una excepción indicando que no se pudo proteger
            // la contraseña.
            throw new IllegalStateException(
                    "No se pudo proteger la contraseña.",
                    excepcion
            );

        } finally {

            // Limpia la contraseña almacenada en la especificación
            // para reducir el tiempo que permanece en memoria.
            especificacion.clearPassword();
        }
    }


    /**
     * Comprueba si una contraseña ingresada coincide con la
     * contraseña protegida que tiene almacenada un usuario.
     *
     * @param usuario usuario cuya contraseña se desea verificar.
     * @param contrasena contraseña ingresada durante el inicio de sesión.
     * @return true si las contraseñas coinciden, false en caso contrario.
     */
    public boolean coincideContrasena(Usuario usuario, char[] contrasena) {

        // Obtiene el hash que se encuentra guardado en el usuario
        // y lo convierte nuevamente de Base64 a bytes.
        byte[] hashGuardado = Base64.getDecoder().decode(
                usuario.obtenerHashContrasena()
        );

        // Genera un nuevo hash utilizando la sal original del usuario
        // y la contraseña que acaba de ingresar.
        byte[] hashIngresado = Base64.getDecoder().decode(
                generarHash(usuario.obtenerSalContrasena(), contrasena)
        );

        // Compara ambos hashes de manera segura.
        // Devuelve true si son iguales y false si son diferentes.
        return MessageDigest.isEqual(hashGuardado, hashIngresado);
    }
}