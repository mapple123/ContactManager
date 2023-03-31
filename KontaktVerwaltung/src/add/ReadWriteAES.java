package add;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Hilfsklasse für die Verschlüsselung der sensiblen Kontaktdaten mittels AES-Verschlüsselung
 *
 * HINWEIS: Methoden sind übernommen worden und nach Bedarf angepasst
 *
 * Entwickler: Jan Schwenger
 */
public class ReadWriteAES {

    // Methode zum Verschlüsseln der Daten
    static void encode(byte[] bytes, OutputStream out, String pass) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), "5500".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Key k = new SecretKeySpec(secret.getEncoded(), "AES");
        System.out.println(new String(k.getEncoded()));
        c.init(Cipher.ENCRYPT_MODE, k);
        OutputStream cos = new CipherOutputStream(out, c);
        cos.write(bytes);
        cos.close();
    }

    // Methode zum Entschlüsseln der Daten
    static byte[] decode(InputStream is, String pass) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), "5500".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Key k = new SecretKeySpec(secret.getEncoded(), "AES");
        System.out.println(new String(k.getEncoded()));
        c.init(Cipher.DECRYPT_MODE, k);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(is, c);
        for (int b; (b = cis.read()) != -1; )
            bos.write(b);
        cis.close();
        return bos.toByteArray();
    }


    // TODO: Gucken, ob die Methoden gebraucht werden
    static void encode(byte[] bytes, FileOutputStream out, byte[] secKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        Cipher c = Cipher.getInstance("AES");

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(new String(secKey).toCharArray(), "5500".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Key k = new SecretKeySpec(secret.getEncoded(), "AES");
        c.init(Cipher.ENCRYPT_MODE, k);

        OutputStream cos = new CipherOutputStream(out, c);
        cos.write(bytes);
        cos.close();

    }

    static byte[] decode(InputStream is, byte[] secKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeySpecException {
        Cipher c = Cipher.getInstance("AES");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(new String(secKey).toCharArray(), "5500".getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Key k = new SecretKeySpec(secret.getEncoded(), "AES");
        c.init(Cipher.DECRYPT_MODE, k);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(is, c);

        for (int b; (b = cis.read()) != -1; ) {
            bos.write(b);
        }

        cis.close();
        return bos.toByteArray();
    }
}
