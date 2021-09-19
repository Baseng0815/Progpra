package it_sec;

import com.macasaet.fernet.Key;
import com.macasaet.fernet.Token;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;

public class Encryption {
    public static void main(String[] args) throws IOException {
        int n = 5, t = 5;
        ShamirSecretSharing sss = new ShamirSecretSharing(t, n);

        // encryption
        {
            // generate key and save shares (generate bytes externally for key serialization)
            byte[] keyBytes = new byte[32];
            new SecureRandom().nextBytes(keyBytes);

            Key key = new Key(keyBytes);
            System.out.println("Generated key.");
            BigInteger keyInteger = BigIntegerUtils.fromUnsignedByteArray(keyBytes);
            System.out.println("Key as big integer: " + keyInteger);
            ShamirShare[] shares = sss.share(keyInteger);
            for (int i = 0; i < shares.length; i++) {
                String file = "F.key." + i;
                shares[i].writeTo(new FileOutputStream(file));
                System.out.printf("%s: x=%s s=%s\n", file, shares[i].x, shares[i].s);
            }

            // read F and encrypt
            byte[] fileBytes = Files.readAllBytes(Path.of("F"));
            Token token = Token.generate(key, fileBytes);
            token.writeTo(new FileOutputStream("F.enc"));
            System.out.println("File read, encrypted and written.");
        }

        // decryption
        {
            // recreate key from shares
            ShamirShare[] shares = new ShamirShare[t];
            for (int i = 0; i < t; i++) {
                String file = "F.key." + i;
                shares[i] = ShamirShare.fromStream(new FileInputStream(file));
                System.out.printf("%s: x=%s s=%s\n", file, shares[i].x, shares[i].s);
            }

            BigInteger keyInteger = sss.combine(shares);
            System.out.println("Key as big integer: " + keyInteger);
            Key key = new Key(BigIntegerUtils.toUnsignedByteArray(keyInteger));

            // read F.enc and decrypt
            byte[] fileBytes = Files.readAllBytes(Path.of("F.enc"));
            Token encrypted = Token.fromBytes(fileBytes);
            byte[] decrypted = encrypted.validateAndDecrypt(key, new BytesValidator());
            Files.write(Path.of("F.dec"), decrypted);
            System.out.println("File read, decrypted and written.");
        }
    }
}