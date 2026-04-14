/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restauranteutil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncriptadorTelefono {

    private static final String ALGORITMO = "AES";
    private static final String CLAVE = "PROYECTO2BD12345";

    public static String encriptar(String telefono) throws Exception {
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(telefono.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String desencriptar(String telefonoEncriptado) throws Exception {
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(telefonoEncriptado));
        return new String(decrypted);
    }
}
