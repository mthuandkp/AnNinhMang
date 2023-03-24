package src;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class FileEncryptor {

    public static void encrypt(File inputFile, File outputFile, String keyString) throws Exception {
        byte[] keyBytes = keyString.getBytes("UTF-8");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        try (FileInputStream in = new FileInputStream(inputFile);
             FileOutputStream out = new FileOutputStream(outputFile)) {

            out.write(ivBytes);

            byte[] inputBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(inputBuffer)) != -1) {
                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
                out.write(outputBuffer);
            }
            byte[] outputBuffer = cipher.doFinal();
            out.write(outputBuffer);
        }
    }

    public static void decrypt(File inputFile, File outputFile, String keyString) throws Exception {
        byte[] keyBytes = keyString.getBytes("UTF-8");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        try (FileInputStream in = new FileInputStream(inputFile);
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] ivBytes = new byte[cipher.getBlockSize()];
            in.read(ivBytes);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] inputBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(inputBuffer)) != -1) {
                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
                out.write(outputBuffer);
            }
            byte[] outputBuffer = cipher.doFinal();
            out.write(outputBuffer);
        }
    }
}

