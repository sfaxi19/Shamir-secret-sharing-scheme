package ShamirScheme;

import java.math.BigInteger;

/**
 * Created by sfaxi19 on 12.05.17.
 */
public class ShamirKeys {
    public BigInteger share;
    public BigInteger index;

    public ShamirKeys(BigInteger share, BigInteger index) {
        this.share = share;
        this.index = index;
    }

    public BigInteger getShare() {
        return share;
    }

    public void setShare(BigInteger share) {
        this.share = share;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return ("Share[" + index.toString() + "]: " + share);
    }
}
