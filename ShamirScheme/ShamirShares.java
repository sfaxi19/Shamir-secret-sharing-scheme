package ShamirScheme;

import java.math.BigInteger;

/**
 * Created by sfaxi19 on 06.05.17.
 */
public class ShamirShares {

    public ShamirKeys[] keys;
    public BigInteger p;

    public ShamirShares(ShamirKeys[] keys, BigInteger p) {
        this.keys = keys;
        this.p = p;
    }

    public ShamirKeys[] getKeys() {
        return keys;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }
}