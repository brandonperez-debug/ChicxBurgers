package Funcionalidades;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PasswordUtil {
    
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 120_000;
    private static final int LONGITUD_SALT_BYTES = 16;
    private static final int LONGITUD_CLAVE_BITS = 256;

    private PasswordUtil() {
    }

    public static String hash(String passwordPlano) {
        if (passwordPlano == null) {
            throw new IllegalArgumentException("La contraseña no puede ser null.");
        }
        byte[] salt = new byte[LONGITUD_SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] clave = derivarClave(passwordPlano.toCharArray(), salt, ITERACIONES);
        return ITERACIONES + ":" + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(clave);
    }

    public static boolean verify(String passwordPlano, String valorAlmacenado) {
        if (passwordPlano == null || valorAlmacenado == null) {
            return false;
        }
        String[] partes = valorAlmacenado.split(":");
        if (partes.length != 3) {
            // Compatibilidad con datos antiguos guardados en texto plano antes de este cambio.
            return valorAlmacenado.equals(passwordPlano);
        }
        try {
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[2]);
            byte[] hashCalculado = derivarClave(passwordPlano.toCharArray(), salt, iteraciones);
            return constantTimeEquals(hashEsperado, hashCalculado);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] derivarClave(char[] passwordChars, byte[] salt, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, iteraciones, LONGITUD_CLAVE_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("No se pudo derivar la clave de la contraseña.", ex);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int resultado = 0;
        for (int i = 0; i < a.length; i++) {
            resultado |= a[i] ^ b[i];
        }
        return resultado == 0;
    }
}
