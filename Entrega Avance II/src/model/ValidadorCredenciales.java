package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
// Revisa correos y deriva hashes de contraseña para verificar credenciales.
public class ValidadorCredenciales {
    private static final int ITERACIONES = 160_000;
    private final SecureRandom generadorAleatorio = new SecureRandom();
    // Comprueba un formato básico de correo antes de intentar crear la cuenta.
    public boolean correoValido(String correo) {
        return correo != null && correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
    // Aporta bytes aleatorios para que dos claves iguales no compartan el mismo hash.
    public String generarSal() {
        byte[] sal = new byte[16];
        generadorAleatorio.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }
    // Deriva la clave con PBKDF2; el archivo local nunca guarda la contraseña original.
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
    // Recalcula el hash con la sal guardada y compara los bytes en tiempo constante.
    public boolean coincideContrasena(Usuario usuario, char[] contrasena) {
        byte[] hashGuardado = Base64.getDecoder().decode(usuario.obtenerHashContrasena());
        byte[] hashIngresado = Base64.getDecoder().decode(generarHash(usuario.obtenerSalContrasena(), contrasena));
        return MessageDigest.isEqual(hashGuardado, hashIngresado);
    }
}
