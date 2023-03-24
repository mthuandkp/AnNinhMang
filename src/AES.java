package src;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Base64;

public class AES {
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int DATA_LENGTH = 128;
    private Cipher encryptionCipher;

    public void init() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        key = keyGenerator.generateKey();
    }

    public void encrypt(String inputFile, String outputFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] dataInBytes = new byte[(int) inputFile.length()];
        inputStream.read(dataInBytes);

        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = encryptionCipher.doFinal(dataInBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(encryptedBytes);

        inputStream.close();
        outputStream.close();
    }


    public void decrypt(String inputFile, String outputFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] dataInBytes = new byte[(int) inputFile.length()];
        inputStream.read(dataInBytes);

        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, encryptionCipher.getIV());
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(decryptedBytes);

        inputStream.close();
        outputStream.close();
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
