/**
 *
 */
package it_sec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class implements the simple XOR-based (n,n) secret sharing.
 *
 * Secrets and shares are both represented as byte[] arrays.
 *
 * Randomness is taken from a {@link java.security.SecureRandom} object.
 *
 * @see SecureRandom
 *
 * @author elmar
 *
 */
public class XorSecretSharing {

    /**
     * Creates a XOR secret sharing object for n shares
     *
     * @param n
     *            number of shares to use. Needs to fulfill n >= 2.
     */
    public XorSecretSharing(int n) {
        assert (n >= 2);
        this.n = n;
        this.rng = new SecureRandom();
    }

    /**
     * Shares the secret into n parts.
     *
     * @param secret
     *            The secret to share.
     *
     * @return An array of the n shares.
     */
    public byte[][] share(final byte[] secret) {
        byte[][] result = new byte[n][secret.length];
        result[n - 1] = Arrays.copyOf(secret, secret.length);

        for (int i = 0; i < n - 1; i++) {
            rng.nextBytes(result[i]);
            for (int j = 0; j < result[i].length; j++) {
                result[n - 1][j] ^= result[i][j];
            }
        }

        return result;
    }

    /**
     * Recombines the given shares into the secret.
     *
     * @param shares
     *            The complete set of n shares for this secret.
     *
     * @return The reconstructed secret.
     */
    public byte[] combine(final byte[][] shares) {
        byte[] result = new byte[shares[0].length];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < shares[i].length; j++) {
                result[j] ^= shares[i][j];
            }
        }

        return result;
    }

    public int getN() {
        return n;
    }

    private final int n;
    private final Random rng;

    public static void main(String[] args) throws IOException {
        // read file, split into n share files and reconstruct file
        String file = "secret";
        int n = 4;

        // read bytes
        byte[] bytes = Files.readAllBytes(Path.of(file));

        // create shares
        XorSecretSharing xss = new XorSecretSharing(n);
        byte[][] shares = xss.share(bytes);

        // write to file
        for (int i = 0; i < shares.length; i++) {
            Files.write(Path.of("secret" + i), shares[i]);
        }

        // reconstruct
        byte[] reconstructed = xss.combine(shares);
        Files.write(Path.of("secret_reconstructed"), reconstructed);
    }
}
