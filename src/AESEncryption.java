package src;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public class AESEncryption {
    private SecretKey secretKey;

    public AESEncryption() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        secretKey = keyGenerator.generateKey();
    }

//    public static void main(String[] args) throws Exception {
//        String inputFile = "src/file_demo/test.pdf";
//        String encryptedFile = "src/file_demo/encrypted.enc";
//        String decryptedFile = "src/file_demo/test.pdf";
//
//        AESEncryption aesEncryption = new AESEncryption();
//
//
//        // Encrypt the input file
//        encrypt(inputFile, encryptedFile, aesEncryption.secretKey);
//
//        // Decrypt the encrypted file
//        decrypt(encryptedFile, decryptedFile, aesEncryption.secretKey);
//    }

    public void encrypt(String inputFile, String outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

        byte[] buffer = new byte[8192];
        int count;
        while ((count = inputStream.read(buffer)) > 0) {
            cipherOutputStream.write(buffer, 0, count);
        }

        inputStream.close();
        cipherOutputStream.flush();
        cipherOutputStream.close();
    }

    public void decrypt(String inputFile, String outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

        byte[] buffer = new byte[8192];
        int count;
        while ((count = inputStream.read(buffer)) > 0) {
            cipherOutputStream.write(buffer, 0, count);
        }

        inputStream.close();
        cipherOutputStream.flush();
        cipherOutputStream.close();
    }
}