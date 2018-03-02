package ShamirScheme;

import SecureMethods.SecureMethods;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by sfaxi19 on 05.05.17.
 */
public class ShamirScheme {

    public ShamirShares distributionShares(BigInteger secret, int k, int n) {
        int bytes = (int) Math.floor(secret.bitLength() / 8) + 1;
        SecureRandom secureRandom = new SecureRandom();
        byte[] rand = secureRandom.generateSeed(bytes + 1);
        BigInteger bigRand = new BigInteger(1, rand);
        bigRand = bigRand.setBit(bytes * 8);
        BigInteger p = SecureMethods.getSimplesity(bigRand);
        BigInteger[] a = new BigInteger[k];
        ShamirKeys[] keys = new ShamirKeys[n];
        a[0] = secret;
        for (int i = 1; i < k; i++) {
            a[i] = new BigInteger(1, secureRandom.generateSeed(bytes));
            a[i] = a[i].mod(p);
        }
        for (int i = 0; i < n; i++) {
            BigInteger sum = BigInteger.ZERO;
            BigInteger id = BigInteger.valueOf(i + 1);
            for (int j = 0; j < k; j++) {
                BigInteger ai = a[j].multiply(id.pow(j));
                sum = sum.add(ai);
                sum = sum.mod(p);
            }
            keys[i] = new ShamirKeys(sum, id);
        }
        return new ShamirShares(keys, p);
    }

    public BigInteger randomBigInteger(int bytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] rand = secureRandom.generateSeed(bytes);
        return new BigInteger(1, rand);
    }

    public BigInteger associationShares(ShamirShares shamirShares) {
        int t = shamirShares.getKeys().length;
        ShamirKeys[] keys = shamirShares.getKeys();
        BigInteger p = shamirShares.getP();
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < t; i++) {
            BigDecimal c = BigDecimal.ONE;
            for (int j = 0; j < t; j++) {
                if (j == i) continue;
                BigDecimal down = new BigDecimal(keys[j].index.subtract(keys[i].index));
                BigDecimal dev = new BigDecimal(keys[j].index);
                dev = dev.divide(down, 10000, BigDecimal.ROUND_HALF_UP);
                c = c.multiply(dev);
            }
            BigInteger s = new BigInteger(c.multiply(new BigDecimal(keys[i].share)).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), 10);
            s = s.mod(p);
            sum = sum.add(s);
            sum = sum.mod(p);
        }
        return sum;
    }

    public static void main(String[] args) {
        ShamirScheme shamirScheme = new ShamirScheme();
        int k = 7;
        int n = 10;
        BigInteger secret = shamirScheme.randomBigInteger(16);//new BigInteger("12345678987654321", 10);
        System.out.println("Secret: " + secret.toString(16) + "\n");
        ShamirShares shamirShares = shamirScheme.distributionShares(secret, k, n);
        System.out.println("Distribution:");
        for (int i = 0; i < shamirShares.getKeys().length; i++) {
            System.out.println(shamirShares.getKeys()[i]);
        }
        int sharesCheckCount = k-1;
        ShamirKeys[] shamirKeys = new ShamirKeys[sharesCheckCount];
        System.out.println("\nAssociation:");
        for (int i = 0; i < sharesCheckCount; i++) {
            shamirKeys[i] = shamirShares.getKeys()[i];
            System.out.println("Share[" + shamirKeys[i].index + "]:" + shamirKeys[i].share);
        }
        ShamirShares shamirShares_test = new ShamirShares(shamirKeys, shamirShares.getP());
        BigInteger secretAssociation = shamirScheme.associationShares(shamirShares_test);
        System.out.println("\nAssociation key: " + secretAssociation.toString(16));
    }

}
