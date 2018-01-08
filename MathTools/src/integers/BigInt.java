package integers;

import java.util.Arrays;

public class BigInt extends Number implements Comparable<BigInt>
{
    private boolean[] mag;
    private int signum;
    private static final int MAX_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;
    static final long LONG_MASK = 0xffffffffL;
    private static long[] bitsPerDigit = {0,0,
    1,2,2,3,3,3,3,4,4,4,4,
    4,4,4,4,5,5,5,5,5,5,5,
    5,5,5,5,5,5,5,5,5,6,6,
    6,6};
    private static int digitsPerInt[] = {0, 0, 30, 19, 15, 13, 11,
            11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};
    private static int intRadix[] = {0, 0,
            0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800,
            0x75db9c97, 0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,
            0x19a10000, 0x309f1021, 0x57f6c100, 0xa2f1b6f,  0x10000000,
            0x18754571, 0x247dbc80, 0x3547667b, 0x4c4b4000, 0x6b5a6e1d,
            0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,  0x1269ae40,
            0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
            0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400
    };

    private static final int MAX_CONST = 16;
    private static BigInt[] posConst = new BigInt[MAX_CONST + 1];
    private static BigInt[] negConst = new BigInt[MAX_CONST + 1];
    static {
        for(int i = 1; i <= MAX_CONST; i++)
        {
            int[] mag = new int[i];
            mag[0] = i;
            posConst[i] = new BigInt(mag, 1);
            negConst[i] = new BigInt(mag, -1);
        }
    }

    public static final BigInt ZERO = new BigInt(new int[0], 0);
    private static final int SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 640;

    BigInt(int[] mag, int i)
    {
        this.signum = mag.length == 0 ? 0 : i;
        this.mag = translate(mag);
        this.mag = addOne(this.mag);
        this.mag = stripLeadingZeroBits(this.mag, 0, this.mag.length);
        if(this.mag.length >= MAX_LENGTH)
            checkRange();
    }

    private BigInt(long val)
    {
        if(val < 0)
        {
            val = -val;
            signum = -1;
        }
        else
            signum = 1;
        int hword = (int)(val >>> 32);
        int[] tmag = new int[2];
        if(hword == 0)
        {
            tmag = new int[1];
            tmag[0] = (int)val;
        }
        else
        {
            tmag[0] = hword;
            tmag[1] = (int)val;
        }
        this.mag = translate(tmag);
        this.mag = addOne(this.mag);
        this.mag = stripLeadingZeroBits(this.mag, 0, this.mag.length);
        if(this.mag.length >= MAX_LENGTH)
            checkRange();
    }

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

    public static BigInt valueOf(long val)
    {
        if(val == 0)
            return ZERO;
        if(val > 0 && val <= MAX_CONST)
            return posConst[(int)val];
        else if(val < 0 && val >= -MAX_CONST)
            return negConst[(int)-val];
        return new BigInt(val);
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
     * The array is assumed to <i>big-endian</i> bit order: The <i>MSB</i> is the zeroth element.
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
        mag = makePositive(val, off, len);
        mag = stripLeadingZeroBits(mag,off,len);
        if(val[off])
        {
            signum = -1;
        }
        else
        {
            signum = mag.length == 0 ? 0 : 1;
        }
        if(mag.length >= MAX_LENGTH)
        {
            checkRange();
        }
    }

    public BigInt(int signum, boolean[] mag, int off, int len)
    {
        if(signum < -1 || signum > 1)
            throw new NumberFormatException("Invalid signum value");
        else if(off < 0
                || len < 0
                || (len > 0 && (off >= mag.length
                || len > mag.length - off)))
            throw new IndexOutOfBoundsException();
        this.mag = stripLeadingZeroBits(mag, off, len);
        if(this.mag.length == 0)
            this.signum = 0;
        else
        {
            if(signum == 0)
                throw new NumberFormatException("sign-magnitude mismatch");
            this.signum = signum;
        }
        if(this.mag.length >= MAX_LENGTH)
            checkRange();
    }

    public BigInt(int signum, boolean[] mag)
    {
        this(signum,mag,0,mag.length);
    }

    public BigInt(String val, int radix)
    {
        int cursor = 0, digits;
        final int len = val.length();
        if(radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            throw new NumberFormatException("Radix out of range");
        if(len == 0)
            throw new NumberFormatException("Zero length BigInt");
        int sign = 1;
        int index1 = val.lastIndexOf('-');
        int index2 = val.lastIndexOf('+');
        if(index1 >= 0)
        {
            if(index1 != 0 || index2 >= 0)
                throw new NumberFormatException("Illegal embedded sign character");
            sign = -1;
            cursor = 1;
        }
        else if(index2 >= 0)
        {
            if(index2 != 0)
                throw new NumberFormatException("Illegal embedded sign character");
            cursor = 1;
        }
        if(cursor == len)
            throw new NumberFormatException("Zero length BigInt");
        while(cursor < len && Character.digit(val.charAt(cursor), radix) == 0)
            cursor++;
        if(cursor == len)
        {
            signum = 0;
            mag = ZERO.mag;
            return;
        }
        digits = len - cursor;
        signum = sign;
        long bits = digits * bitsPerDigit[radix] + 1;
        if(bits + 31 >= (1L << 32))
            reportOverflow();
        int words = (int)(bits + 31) >>> 5;
        if(32 * words > MAX_LENGTH)
            reportOverflow();
        int[] mag = new int[words];
        int fglen = digits % digitsPerInt[radix];
        if(fglen == 0)
            fglen = digitsPerInt[radix];
        String group = val.substring(cursor, cursor += fglen);
        mag[words - 1] = Integer.parseInt(group, radix);
        if(mag[words - 1] < 0)
            throw new NumberFormatException("Illegal digit");
        int sradix = intRadix[radix];
        int gval = 0;
        while(cursor < len)
        {
            group = val.substring(cursor, cursor += digitsPerInt[radix]);
            gval = Integer.parseInt(group, radix);
            if(gval < 0)
                throw new NumberFormatException("Illegal digit");
            destructiveMultAdd(mag, sradix, gval);
        }
        mag = trustedStripLeadingZeroInts(mag);
        this.mag = translate(mag);
        this.mag = addOne(this.mag);
        this.mag = stripLeadingZeroBits(this.mag, 0, this.mag.length);
        if(this.mag.length >= MAX_LENGTH)
            checkRange();
    }

    private BigInt(int signum, int[] mag)
    {
        mag = stripLeadingZeroInts(mag);
        this.mag = translate(mag);
        if(signum < -1 || signum > 1)
            throw new NumberFormatException("Invalid signum value");
        if(this.mag.length == 0)
            this.signum = 0;
        else
        {
            if(signum == 0)
                throw new NumberFormatException("Sign-magnitude mismatch");
            this.signum = signum;
        }
        if(this.mag.length >= MAX_LENGTH)
            checkRange();
    }

    public BigInt(String val)
    {
        this(val,10);
    }

    private static boolean[] translate(int[] mag)
    {
        boolean[] bmag = new boolean[32 * mag.length];
        int dp = bmag.length - 1;
        for(int i : mag)
        {
            boolean[] temp = translateInt(i);
            for(int j = 31; j >= 0; j--)
                bmag[dp--] = temp[j];
        }
        return bmag;
    }

    private static void destructiveMultAdd(int[] x, int y, int z)
    {
        long ylong = y & LONG_MASK;
        long zlong = z & LONG_MASK;
        int len = x.length;
        long prod = 0;
        long carry = 0;
        for(int i = len - 1; i >= 0; i--)
        {
            prod = ylong * (x[i] & LONG_MASK) + carry;
            x[i] = (int)prod;
            carry = prod >>> 32;
        }
        long sum = (x[len - 1] & LONG_MASK) + zlong;
        x[len - 1] = (int)sum;
        carry = sum >>> 32;
        for(int i = len - 2; i >= 0; i--)
        {
            sum = (x[i] & LONG_MASK) + carry;
            x[i] = (int)sum;
            carry = sum >>> 32;
        }
    }

    private static int[] trustedStripLeadingZeroInts(int[] val)
    {
        int vlen = val.length;
        int keep;
        for(keep = 0; keep < vlen && val[keep] == 0; keep++);
        return keep == 0 ? val : Arrays.copyOfRange(val, keep, vlen);
    }

    private static int[] stripLeadingZeroInts(int[] val)
    {
        int vlen = val.length;
        int keep;
        for(keep = 0; keep < vlen && val[keep] == 0; keep++);
        return Arrays.copyOfRange(val, keep, vlen);
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
            res[i] = val[b--];
        }
        //res = addOne(res);
        return res;
    }

    private boolean[] makePositive(boolean[] val, int off, int len)
    {
        int keep, k;
        int bound = off + len;
        for(keep = off; keep < bound && val[keep] && keep < 1; keep++);
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



    public static void main(String[] args)
    {
        boolean[] b1 = BigInt.translateInt(3);
        System.out.println(Arrays.toString(b1));
        //boolean[] b2 = BigInt.makePositive(b1, 0, b1.length);
        //System.out.println(Arrays.toString(b2));
        BigInt N = new BigInt("123456789123456789123456789123456789");
        System.out.println("Hello");
    }
}
