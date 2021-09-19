package it_sec;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class implements Shamir's (t,n) secret sharing.
 * <p>
 * Secrets are represented as BigInteger objects, shares as ShamirShare objects.
 * <p>
 * Randomness is taken from a {@link java.security.SecureRandom} object.
 *
 * @author elmar
 * @see ShamirShare
 * @see BigInteger
 * @see SecureRandom
 */
public class ShamirSecretSharing {

    /**
     * Creates a (t,n) Shamir secret sharing object for n shares with threshold
     * t.
     *
     * @param t threshold: any subset of t <= i <= n shares can recover the
     *          secret.
     * @param n number of shares to use. Needs to fulfill n >= 2.
     */
    public ShamirSecretSharing(int t, int n) {
        assert (t >= 2);
        assert (n >= t);

        this.t = t;
        this.n = n;
        this.rng = new SecureRandom();

        // use p = 2^256 + 297
        this.p = BigInteger.ONE.shiftLeft(256).add(BigInteger.valueOf(297));
        assert (this.p.isProbablePrime(2));
    }

    /**
     * Shares the secret into n parts.
     *
     * @param secret The secret to share.
     * @return An array of the n shares.
     */
    public ShamirShare[] share(BigInteger secret) {
        // generate t polynomial coefficients (the first one being the secret itself)
        BigInteger[] coefficients = new BigInteger[t];
        coefficients[0] = secret;
        for (int i = 1; i < t; i++) {
            coefficients[i] = new BigInteger(p.bitLength(), rng);
        }

        ShamirShare[] shares = new ShamirShare[n];
        for (int x = 1; x <= n; x++) {
            // apply polynomial
            BigInteger s = horner(BigInteger.valueOf(x), coefficients);
            shares[x - 1] = new ShamirShare(BigInteger.valueOf(x), s.mod(p));
        }

        return shares;
    }

    public static void main(String[] args) {
        String lorem = "Lorem ipsum dolor sit amet.";
        int t = 5, n = 10;

        System.out.println("Text: " + lorem);
        ShamirSecretSharing sss = new ShamirSecretSharing(t, n);
        ShamirShare[] shares = sss.share(new BigInteger(lorem.getBytes()));
        ShamirShare[] subshares = new ShamirShare[t];
        System.arraycopy(shares, 0, subshares, 0, t);
        BigInteger reconstructed = sss.combine(subshares);
        System.out.println("Reconstructed BigInteger: " + reconstructed);
        System.out.println("Reconstructed text: " + new String(reconstructed.toByteArray()));
    }

    /**
     * Evaluates the polynomial a[0] + a[1]*x + ... + a[t-1]*x^(t-1) modulo p at
     * point x using Horner's rule.
     *
     * @param x point at which to evaluate the polynomial
     * @param a array of coefficients
     * @return value of the polynomial at point x
     */
    private BigInteger horner(BigInteger x, BigInteger[] a) {
        BigInteger s = a[t - 1];
        for (int i = t - 2; i >= 0; i--) {
            s = s.multiply(x).add(a[i]).mod(p);
        }

        return s;
    }

    /**
     * Recombines the given shares into the secret.
     *
     * @param shares A set of at least t out of the n shares for this secret.
     * @return The reconstructed secret.
     */
    public BigInteger combine(ShamirShare[] shares) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < t; i++) {
            BigInteger addend = shares[i].s;
            for (int j = 0; j < t; j++) {
                if (j == i) continue;
                BigInteger d = shares[j].x
                        .negate()
                        .multiply(shares[i].x.subtract(shares[j].x).modInverse(p))
                        .mod(p);
                addend = addend.multiply(d);
            }
            sum = sum.add(addend);
        }

        return sum.mod(p);
    }

    public int maxSecretLength() {
        return this.p.bitLength() / 8;
    }

    public int getT() {
        return t;
    }

    public int getN() {
        return n;
    }

    private int t;
    private int n;
    private SecureRandom rng;
    private BigInteger p;

}
