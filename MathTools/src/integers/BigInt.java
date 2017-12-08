package integers;

import java.util.Arrays;

public class BigInt extends Number implements Comparable<BigInt>
{
    private boolean[] mag;
    private int signum;
    private static final int MAX_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;
    static final long LONG_MASK = 0xffffffffL;
    /**
     * Translates a standard 32-bit Java integer into a <code>boolean</code> array. Big-endian.
     * @param n the integer to be translated.
     * @return <code>boolean</code> array representing the integer
     */
    public static boolean[] translateInt(int n)
    {
        boolean[] res = new boolean[32];
        for(int i = 31; i >= 0; i--)
            res[res.length - i - 1] = (n & (1 << i)) != 0;
        return res;
    }

    /**
     * The <code>long</code> analogue of <code>translateInt()</code>
     * @param n long to be translated
     * @return boolean array representing <code>n</code>
     * @see #translateInt(int)
     */
    public static boolean[] translateLong(long n)
    {
        boolean[] res = new boolean[64];
        for(int i = 63; i >= 0; i--)
            res[res.length - i - 1] = (n & (1 << i)) != 0;
        return res;
    }

    /**
     * Constructor that takes a boolean array representing an integer in one's-complement and converts it to two's-complement.
     * The array is assumed to <i>big-endian</i> bit order: The <i>MSB</i> is the zeroeth element.
     * @param val big-endian one's-complement binary representation of a BigInt
     * @param off the starting offset in the array, if necessary
     * @param len len of bits to use
     * @throws NumberFormatException if {@code val == 0}
     * @throws IndexOutOfBoundsException if {@code off < 0} or {@code off >= val.length} or {@code len < 0} or {@code len > val.length - off}
     */
    public BigInt(boolean[] val, int off, int len)
    {
        if(val.length == 0)
            throw new NumberFormatException("Zero length BigInt");
        else if(off < 0
                || off >= val.length
                || len < 0
                || len > val.length - off)
            throw new IndexOutOfBoundsException();
        if(val[off])
        {
            mag = makePositive(val, off, len);
            signum = -1;
        }
        else
        {
            mag = makePositive(val, off, len);
            mag = stripLeadingZeroBits(val, off, len);
            signum = mag.length == 0 ? 0 : 1;
        }
        if(mag.length >= MAX_LENGTH)
        {
            checkRange();
        }
    }

    /**
     * Constructor that takes a boolean array representing an integer in one's-complement and converts it to two's-complement.
     * The array is assumed to <i>big-endian</i> bit order: The <i>MSB</i> is the zeroeth element.
     * @param val big-endian one's-complement binary representation of a BigInt
     */
    public BigInt(boolean[] val)
    {
        this(val, 0, val.length);
    }
    private void checkRange()
    {
        if(mag.length > MAX_LENGTH || mag.length == MAX_LENGTH && !mag[0])
            reportOverflow();
    }

    private void reportOverflow()
    {
        throw new ArithmeticException("BigInt would overflow supported range");
    }

    /**
     * Strips any leading zero bits (false values) starting from {@code off} and ending after {@code len}
     *
     */
    private boolean[] stripLeadingZeroBits(boolean[] val, int off, int len)
    {
        int bound = off + len;
        int keep;
        for(keep = off; keep < bound && !val[keep]; keep++);
        int length = bound - keep;
        boolean[] res = new boolean[length];
        int b = bound - 1;
        for(int i = length - 1; i >= 0; i--)
        {
            res[i] = val[i--];
        }
        //res = addOne(res);
        return res;
    }

    private boolean[] makePositive(boolean[] val, int off, int len)
    {
        int keep, k;
        int bound = off + len;
        for(keep = off; keep < bound && val[keep]; keep++);
        for(k = keep; k < bound && !val[k]; k++);
        int extra = k == bound ? 1 : 0;
        int length = (bound - keep + extra);
        boolean[] res = new boolean[length];
        int b = bound - 1;
        for(int i = length - 1; i >= 0; i--)
        {
            res[i] = !val[b--];
        }
        res = addOne(res);
        return res;
    }

    /**
     * Internal method for adding one to boolean array in one's-complement representation to make it two's-complement
     * @param b array to be complemented
     * @return the two's-complement of <code>b</code>
     */
    private static boolean[] addOne(boolean[] b)
    {
        boolean[] one = new boolean[b.length];
        one[one.length - 1] = true;
        boolean carry = false;
        for(int i = one.length - 1; i > 0; i--)
        {
            carry = b[i] && one[i];
            b[i] ^= one[i];
            one[i - 1] = carry;
        }
        if(carry)
            b[0] ^= carry;
        return b;
    }

    @Override
    public int compareTo(BigInt o) {
        return 0;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }


    /*
    public static void main(String[] args)
    {
        boolean[] b1 = BigInt.translateInt(3);
        System.out.println(Arrays.toString(b1));
        boolean[] b2 = BigInt.makePositive2(b1, 0, b1.length);
        System.out.println(Arrays.toString(b2));
    }*/
}
