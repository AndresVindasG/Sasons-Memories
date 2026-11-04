package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Protege las contraseñas con PBKDF2 y una sal aleatoria individual. */
public class ValidadorCredenciales {
    private static final int ITERACIONES = 160_000;
    private final SecureRandom generadorAleatorio = new SecureRandom();

    public boolean correoValido(String correo) {
        return correo != null && correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public String generarSal() {
        byte[] sal = new byte[16];
        generadorAleatorio.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }

    public String generarHash(String salCodificada, char[] contrasena) {
        byte[] sal = Base64.getDecoder().decode(salCodificada);
        PBEKeySpec especificacion = new PBEKeySpec(contrasena, sal, ITERACIONES, 256);
        try {
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception excepcion) {
            throw new IllegalStateException("No se pudo proteger la contraseña.", excepcion);
        } finally {
            especificacion.clearPassword();
        }
    }

    public boolean coincideContrasena(Usuario usuario, char[] contrasena) {
        byte[] hashGuardado = Base64.getDecoder().decode(usuario.obtenerHashContrasena());
        byte[] hashIngresado = Base64.getDecoder().decode(generarHash(usuario.obtenerSalContrasena(), contrasena));
        return MessageDigest.isEqual(hashGuardado, hashIngresado);
    }
}
