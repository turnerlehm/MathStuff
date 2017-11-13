package integers;

import java.util.*;

public class BigBooleanInt extends Number implements Comparable<BigBooleanInt>
{
    private int sign;
    private boolean[] mag; //just so it will shut up until I actually build the constructors
    private int bitsPlusOne;
    private int lsbPplusTwo;
    private int firstNonzeroIntNumPlusTwo;
    static final long MASK = 0xffffffffL;
    //these are bitlengths, i.e. the size of the array
    private static final int PRIME_SEARCH_LENGTH_LIMIT = 500000000;
    private static final int K_THRESHOLD = 2560;
    private static final int TC_THRESHOLD = 7680;
    private static final int KS_THRESHOLD = 4096;
    private static final int TCS_THRESHOLD = 6912;
    private static final int BZ_THRESHOLD = 2560;
    private static final int BZ_OFFSET = 1280;
    private static final int SBC_THRESHOLD = 640;
    private static final int MS_THRESHOLD = 640;
    private static final int MI_THRESHOLD = 16384;
    //max supported array length by Java
    private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;

    private static int[] bitsPerDigit = {0, 0,
    1,2,2,3,3,3,3,4,4,4,4,
    4,4,4,4,5,5,5,5,5,5,5,
    5,5,5,5,5,5,5,5,5,6,6,
    6,6};

    private static int[] digitsPerInt = {0,0,30,19,15,13,11,11,10,9,9,8,8,8,8,7,7,7,7,7,7,7,6,6,6,6,6,6,6,6,6,6,6,6,6,6,5};

    private static int intRadix[] = {0, 0,
            0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800,
            0x75db9c97, 0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,
            0x19a10000, 0x309f1021, 0x57f6c100, 0xa2f1b6f,  0x10000000,
            0x18754571, 0x247dbc80, 0x3547667b, 0x4c4b4000, 0x6b5a6e1d,
            0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,  0x1269ae40,
            0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
            0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400
    };

    private static int[] intRadixMod2 = {0, 1,
    0, 1, 0, 1, 0,
    1, 0, 1, 0, 1,
    0, 1, 0, 1, 0,
    1, 0, 1, 0, 1,
    0, 1, 0, 1, 0,
    1, 0, 1, 0, 1,
    0, 1, 0, 1, 0};

    private static final int SMALL_PRIME_THRESH = 95;
    private static final int DEF_PRIME_CERTAINTY = 100;

    private static final int MAX_CONST = 16;
    private static BigBooleanInt[] posConst = new BigBooleanInt[MAX_CONST + 1];
    private static BigBooleanInt[] negConst = new BigBooleanInt[MAX_CONST + 1];

    static{
        for(int i = 1; i <= MAX_CONST; i++)
        {
            boolean[] magnitude = new boolean[1];
            magnitude[0] = (i & (1 << i)) != 0;
            posConst[i] = new BigBooleanInt(magnitude, 1);
            negConst[i] = new BigBooleanInt(magnitude, -1);
        }
    }

    public static final BigBooleanInt ZERO = new BigBooleanInt(new boolean[0], 0);

    public BigBooleanInt(boolean[] val, int off, int len)
    {
        if(val.length == 0)
            throw new NumberFormatException("Zero length BigBooleanInt");
        else if(off < 0 || off >= val.length | len < 0 || (len > val.length - off))
            throw new IndexOutOfBoundsException();
        if(!val[0])
        {
            mag = makePositive(val, off, len);
            sign = -1;
        }
        else
        {
            mag = stripLeadingZeroBits(val, off, len);
            sign = mag.length == 0 ? 0 : 1;
        }
        if(mag.length >= MAX_MAG_LENGTH)
            checkRange();
    }

    public BigBooleanInt(boolean[] val)
    {
        this(val, 0, val.length);
    }

    public BigBooleanInt(int sgn, boolean[] mag, int off, int len)
    {
        if(sgn < -1 || sgn > 1)
            throw new NumberFormatException("Invalid sign");
        else if((off < 0 || len < 0)
                || (len > 0 && ((off >= mag.length || len > mag.length - off))))
        {
            throw new IndexOutOfBoundsException();
        }
        this.mag = stripLeadingZeroBits(mag, off, len);
        if(this.mag.length == 0)
            this.sign = 0;
        else
        {
            if(sgn == 0)
                throw new NumberFormatException("sign-magnitude mismatch");
            sign = sgn;
        }
        if(mag.length >= MAX_MAG_LENGTH)
            checkRange();
    }

    public BigBooleanInt(int sgn, boolean[] mag)
    {
        this(sgn, mag, 0, mag.length);
    }

    public BigBooleanInt(String val, int radix)
    {
        int cur = 0, digits;
        final int len = val.length();
        if(radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            throw new NumberFormatException("Radix must be in range 2-36");
        if(len == 0)
            throw new NumberFormatException("Zero length BigBooleanInt");
        int sign = 1;
        int idx1 = val.lastIndexOf('-');
        int idx2 = val.lastIndexOf('+');
        if(idx1 >= 0)
        {
            if(idx1 != 0 || idx2 >= 0)
                throw new NumberFormatException("Illegal embedded sign character");
            sign = -1;
            cur = 1;
        }
        else if(idx2 >= 0)
        {
            if(idx2 != 0)
                throw new NumberFormatException("Illegal embedded sign character");
            cur = 1;
        }
        if(cur == len)
            throw new NumberFormatException("Zero length BigBooleanInt");
        while(cur < len && Character.digit(val.charAt(cur), radix) == 0)
            cur++;
        if(cur == len)
        {
            this.sign = 0;
            //TODO: add ZERO const
            mag = ZERO.mag;
            return;
        }
        digits = len - cur;
        this.sign = sign;
        int bits = Math.addExact(Math.multiplyExact(digits, bitsPerDigit[radix]),1);
        boolean[] magnitude = new boolean[bits];
        int firstGroupLen = digits % digitsPerInt[radix];
        if(firstGroupLen == 0)
            firstGroupLen = digitsPerInt[radix];
        String group = val.substring(cur, cur += firstGroupLen);
        int offset = 0;
        translateTo(magnitude,Integer.parseInt(group,radix), offset); //process first group

        /*int gval = 0;
        while(cur < len)
        {
            group = val.substring(cur, cur += digitsPerInt[radix]);
            gval = Integer.parseInt(group, radix);
            if(gval < 0)
                throw new NumberFormatException("Illegal digit");
            translateTo(magnitude, gval, offset += 31); //won't work because we need to add
        }*/
        int sradix = intRadix[radix];
        int gval = 0;
        while(cur < len)
        {
            group = val.substring(cur, cur += digitsPerInt[radix]);
            gval = Integer.parseInt(group, radix);
            if(gval < 0)
                throw new NumberFormatException("illegal digit");
            destructiveMulAdd(magnitude,sradix,gval);
        }
        mag = trustedStripLeadingZeroBits(magnitude);
        if(mag.length >= MAX_MAG_LENGTH)
            checkRange();
    }

    public BigBooleanInt(String val)
    {
        this(val, 10);
    }

    public BigBooleanInt(int bits, Random rng)
    {
        this(1, randBits(bits, rng));
    }

    private static boolean[] randBits(int nbits, Random rng)
    {
        if(nbits < 0)
            throw new IllegalArgumentException("Number of bits must be non-negative");
        boolean[] rbits = new boolean[nbits];
        for(int i = nbits; i >= 0; i--)
            rbits[i] = rng.nextBoolean();
        return rbits;
    }

    private static BigBooleanInt smallPrime(int bits, int certainty, Random rng)
    {
        boolean temp[] = new boolean[bits];
        while(true)
        {
            for(int i = 0; i < bits; i++)
                temp[i] = rng.nextBoolean();
            if(bits > 2)
                temp[bits - 1] |= true;
            BigBooleanInt p = new BigBooleanInt(temp, 1);
        }
    }

    BigBooleanInt(boolean[] magnitude, int sgn)
    {
        this.sign = magnitude.length == 0 ? 0 : sgn;
        this.mag = magnitude;
        if(mag.length >= MAX_MAG_LENGTH)
            checkRange();
    }

    public static BigBooleanInt valueOf(long val)
    {
        if(val == 0)
            return ZERO;
        return ZERO;
    }

    @Override
    public int compareTo(BigBooleanInt o) {
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

    private void checkRange()
    {
        if(mag.length > MAX_MAG_LENGTH || mag.length == MAX_MAG_LENGTH && !mag[0])
            reportOverflow();
    }

    private static boolean[] stripLeadingZeroBits(boolean[] val, int off, int len)
    {
        int bound = off + len;
        int keep;
        for(keep = off; keep < bound && val[keep]; keep++);
        int length = bound - keep;
        boolean[] res = new boolean[length];
        int b = bound - 1;
        for(int i = length - 1; i >=0; i--)
            res[i] = val[b--] & true;
        return res;
    }

    private static boolean[] trustedStripLeadingZeroBits(boolean[] val)
    {
        int len = val.length;
        int keep;
        for(keep = 0; keep < len && val[keep]; keep++);
        return keep == 0 ? val : Arrays.copyOfRange(val, keep, len);
    }

    private static boolean[] makePositive(boolean[] arr, int off, int len)
    {
        int keep, j = -1;
        int bound = off + len;
        //check if MSB is set, arr[0] == true, and unset it
        arr[0] = arr[0] ? !arr[0] : arr[0];
        //find first set bit after MSB
        for(keep = 0; keep < arr.length && !arr[j]; keep++);
        //make the output array
        //if all false, allocate one extra bit
        for(j = keep; j < arr.length && !arr[j]; j++);
        int extra = j == arr.length ? 1 : 0;
        boolean[] res = new boolean[arr.length - keep + extra];
        for(int i = keep; i < arr.length; i++)
            res[i - keep + extra] = !arr[i];
        return res;
    }

    private void translateTo(boolean[] mag, int n, int offset)
    {
        for(int i = 31; i >= 0; i--)
            mag[mag.length - offset - i - 1] = (n & (1 << i)) != 0;
    }

    private static void translateTo(boolean[] mag, int n)
    {
        for(int i = 31; i >= 0; i--)
            mag[mag.length - i - 1] = (n & (1 << i)) != 0;
    }

    private static void longTranslateTo(boolean[] x, long a)
    {
        for(int i = 63; i >= 0; i--)
            x[x.length - i - 1] = (a & (1L << i)) != 0;
    }

    private static int toBit(boolean val)
    {
        return val ? 1 : 0;
    }

    private static boolean toBool(int val)
    {
        if(val <= 1 && val >= 0)
            return val == 1;
        else
            throw new IllegalArgumentException("val must be 1 or 0");
    }

    //probably doesn't work
    private static void destructiveMulAdd(boolean[] x, int y, int z)
    {
        long ylong = y & MASK;
        long zlong = z & MASK;
        int len = x.length;
        long prod = 0;
        long carry = 0;
        for(int i = len - 1; i >= 0; i--)
        {
            prod = ylong * (toBit(x[i]) & MASK) + carry;
            x[i] = (prod & (1 << i)) != 0;
            carry = prod >>> 32;
        }

        long sum = (toBit(x[len - 1]) & MASK) + zlong;
        x[len - 1] = (sum & (1 << (len - 1))) != 0;
        carry = sum >>> 32;
        for(int i = len - 2; i >= 0; i--)
        {
            sum = (toBit(x[i]) & MASK) + carry;
            x[i] = (sum & (1 << i)) != 0;
            carry = sum >>> 32;
        }
    }

    //also probably doesn't work
    private static void destructiveMultAdd(boolean[] x, int y, int z)
    {
        boolean[] ybool = new boolean[x.length];
        boolean[] zbool = new boolean[x.length];
        translateTo(ybool, y);
        translateTo(zbool, z);
        int len = x.length;
        boolean[] prod = new boolean[len];
        boolean carry = false;
        int i = 0;
        while(!isZero(ybool))
        {
            if(bitwiseAnd(ybool, 0b01,2))
                prod = add(prod, x);
            x = boolLeftShit(x,1);
            ybool = boolRightShift(ybool,1);
        }
        x = prod;

        add(x,zbool);
    }

    //this also probably doesn't work
    private static void destructiveMulAddMod2(boolean[]x, int y, int z)
    {
        long ybit = y & 0b01;
        long zbit = z & 0b01;
        int len = x.length;
        boolean prod = false;
        boolean carry = false;
        for(int i = len - 1; i >= 0; i--)
        {
            prod = (ybit & (toBit(x[i]) & 0b01)) + toBit(carry) != 0;
            x[i] = prod;
            carry = (ybit & (toBit(x[i]) & 0b01)) + toBit(carry) > 1;
        }

        long sum = (toBit(x[len - 1]) & 0b01) + zbit;
        x[len - 1] = sum != 0;
        carry = sum > 1;
        for(int i = len - 2; i >= 0; i--)
        {
            sum = (toBit(x[i]) & 0b01) + toBit(carry);
            x[i] = sum != 0;
            carry = sum > 1;
        }
    }

    private static boolean[] add(boolean[] x, boolean[] y)
    {
        boolean[] result = new boolean[x.length];
        boolean carry = false;
        int i = 0;
        while(!carry && i < x.length)
        {
            carry = x[i] & y[i];
            result[i] = x[i] ^ y[i];
            if(i < x.length - 1)
                y[i+1] = toBool(toBit(carry) << 1);
            i++;
        }
        return result;
    }

    private static boolean bitwiseAnd(boolean[] x, int a, int bits)
    {
        boolean[] abool = new boolean[x.length];
        boolean[] result = new boolean[32];
        translateTo(abool, a);
        for(int i = 31; i >= x.length - bits; i--)
            result[i] = x[i] & abool[i];
        boolean andres = true;
        for(int i = 31; i >= result.length - bits; i--)
            andres = andres & result[i];
        return andres;
    }

    private static boolean[] boolLeftShit(boolean[] a, int shift)
    {
        for(int i = 0; i < shift; i++)
        {
            for (int j = a.length - 2; i >= 0; i--)
            {
                a[j] = a[j + 1];
            }
            a[a.length - i - 1] = false;
        }
        return a;
    }

    private static boolean[] boolRightShift(boolean[] a, int shift)
    {
        for(int i = 0; i < shift; i++)
        {
            for (int j = 0; i < a.length - 1; i++)
                a[j + 1] = a[j];
            a[i] = false;
        }
        return a;
    }

    private static boolean isZero(boolean[] a)
    {
        int i;
        for(i = a.length - 1; i > 0; i--)
        {
            if(a[i - 1])
                return false;
        }
        return true;
    }

    private static void reportOverflow()
    {
        throw new ArithmeticException("BigBooleanInt would overflow supported range");
    }
}
