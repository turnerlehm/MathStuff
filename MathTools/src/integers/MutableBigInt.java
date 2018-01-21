package integers;

import static integers.BigInt.LONG_MASK;

import java.math.BigDecimal;
import java.util.Arrays;
import integers.BigInt;
class MutableBigInt
{
    boolean[] val;
    int bitLen;
    int offset = 0;

    static final int KNUTH_POW2_THRESH_LEN = 192;
    static final int KNUTH_POW2_THRESH_ZEROS = 96;

    MutableBigInt()
    {
        val = new boolean[1];
        bitLen = 0;
    }

    MutableBigInt(int val)
    {
        this.val = translate(val);
        bitLen = 32;
    }

    MutableBigInt(int[] val)
    {
        this.val = new boolean[32 * val.length];
        for(int i = 0; i < val.length; i++)
        {
            boolean[] temp = translate(val[i]);
            for (int j = 32 * i, k = 0; j < this.val.length && k < 32; j++, k++)
                this.val[i] = temp[i];
        }
        bitLen = 32 * val.length;
    }

    MutableBigInt(boolean[] val)
    {
        this.val = val;
        bitLen = val.length;
    }

    MutableBigInt(BigInt b)
    {
        bitLen = b.mag.length;
        val = Arrays.copyOf(b.mag, bitLen);
    }

    MutableBigInt(MutableBigInt m)
    {
        bitLen = m.bitLen;
        val = Arrays.copyOfRange(m.val, m.offset, m.offset + m.bitLen);
    }

    private void ones(int n)
    {
        if(n > val.length)
            val = new boolean[n];
        Arrays.fill(val, true);
        offset = 0;
        bitLen = n;
    }

    private boolean[] getMagnitudeArray()
    {
        if(offset > 0 || val.length != bitLen)
            return Arrays.copyOfRange(val, offset, offset + bitLen);
        return val;
    }

    private long toLong()
    {
        assert bitLen <= 64 : "this MutableBigInt exceeds the range of long";
        if(bitLen == 0)
            return 0;
        long n = 0;
        for(boolean b : val)
            n = (n << 1) + (b ? 1 : 0);
        return n;
    }

    BigInt toBigInt(int sign)
    {
        if(bitLen == 0 || sign == 0)
            return BigInt.ZERO;
        return new BigInt(getMagnitudeArray(), sign);
    }

    BigInt toBigInt()
    {
        normalize();
        return toBigInt(isZero() ? 0 : 1);
    }

    void clear()
    {
        offset = bitLen = 0;
        for(int i = 0; i < val.length; i++)
            val[i] = false;
    }

    void reset()
    {
        offset = bitLen = 0;
    }

    final int compare(MutableBigInt b)
    {
        int blen = b.bitLen;
        if(bitLen < blen)
            return -1;
        if(bitLen > blen)
            return 1;
        boolean[] bval = b.val;
        for(int i = offset, j = b.offset; i < bitLen + offset; i++, j++)
        {
            boolean b1 = val[i];
            boolean b2 = bval[j];
            if(!b1 && b2)
                return -1;
            if(b1 && !b2)
                return 1;
        }
        return 0;
    }

    private int compareShifted(MutableBigInt b, int bits)
    {
        int blen = b.bitLen;
        int alen = bitLen - bits;
        if(alen < blen)
            return -1;
        if(alen > blen)
            return 1;
        boolean[] bval = b.val;
        for(int i = offset, j = b.offset; i < alen + offset; i++, j++)
        {
            boolean b1 = val[i];
            boolean b2 = bval[j];
            if(!b1 && b2)
                return -1;
            if(b1 && !b2)
                return 1;
        }
        return 0;
    }

    final int compareHalf(MutableBigInt b)
    {
        int blen = b.bitLen;
        int len = bitLen;
        if(len <= 0)
            return blen <= 0 ? 0 : -1;
        if(len > blen)
            return 1;
        if(len < blen - 1)
            return -1;
        boolean[] bval = b.val;
        int bstart = 0;
        if(len != blen)
        {
            if(bval[bstart])
                ++bstart;
        }
        for(int i = offset, j = bstart; i < len + offset;)
        {
            boolean bv = bval[j++];
            boolean v = val[i++];
            if(v != bv)
                return !v && bv ? -1 : 1;
        }
        return 0;
    }

    public final int getLowestSetBit()
    {
        if(bitLen == 0)
            return -1;
        int j;
        boolean b;
        for(j = bitLen - 1; j >= 0 && !val[j + offset]; j--);
        b = val[j + offset];
        if(!b)
            return -1;
        return j;
    }

    private final boolean getBool(int idx)
    {
        return val[offset + idx];
    }

    private boolean isZero()
    {
        return bitLen == 0;
    }

    private void normalize()
    {
        if(bitLen == 0)
        {
            offset = 0;
            return;
        }
        int idx = offset;
        if(!val[idx])
            return;
        int idxBound = idx + bitLen;
        do
        {
            idx++;
        }while(idx < idxBound && !val[idx]);
        int zeros = idx - offset;
        bitLen -= zeros;
        offset = (bitLen == 0 ? 0 : offset + zeros);
    }

    private final void ensureCapacity(int len)
    {
        if(val.length < len)
        {
            val = new boolean[len];
            offset = 0;
            bitLen = len;
        }
    }

    boolean[] toBooleanArray()
    {
        boolean[] res = new boolean[bitLen];
        for(int i = 0; i < bitLen; i++)
            res[i] = val[offset + i];
        return res;
    }

    void setBool(int idx, boolean val)
    {
        this.val[idx + offset] = val;
    }

    void setVal(boolean[] val, int length)
    {
        this.val = val;
        bitLen = length;
        offset = 0;
    }

    void copyVal(MutableBigInt src)
    {
        int len = src.bitLen;
        if(val.length < len)
            val = new boolean[len];
        System.arraycopy(src.val, src.offset,val,0,len);
        bitLen = len;
        offset = 0;
    }

    void copyVal(boolean[] val)
    {
        int len = val.length;
        if(this.val.length < len)
            this.val = new boolean[len];
        System.arraycopy(val, 0, this.val,0,len);
        bitLen = len;
        offset = 0;
    }

    boolean isOne()
    {
        return bitLen == 1 && val[offset];
    }

    boolean isEven()
    {
        return bitLen == 0 || !val[offset + bitLen - 1];
    }

    boolean isOdd()
    {
        return isZero() ? false : val[offset + bitLen -1];
    }

    boolean isNormal()
    {
        if(bitLen + offset > val.length)
            return false;
        if(bitLen == 0)
            return true;
        return val[offset];
    }

    public String toString()
    {
        BigInt b = toBigInt(1);
        return b.toString();
    }

    void safeRightShift(int n)
    {
        if(n >= bitLen)
            reset();
        else
            rightShift(n);
    }

    public void rightShift(int n)
    {
        boolean[] result = new boolean[val.length];
        n = Math.min(n, val.length);
        System.arraycopy(val,0,result,n,val.length - n);
        val = result;
    }

    void safeLeftShift(int n)
    {
        if(n > 0)
            leftShift(n);
    }

    public void leftShift(int n)
    {
        boolean[] result = new boolean[val.length];
        n = Math.min(n, val.length);
        System.arraycopy(val, n, result, 0, val.length - n);
        val = result;
    }

    private boolean divadd(boolean[] a, boolean[] result, int offset)
    {
        boolean carry = false;
        for(int i = a.length - 1; i >= 0; i--)
        {
            boolean sum = a[i] || result[i + offset] || carry;
            result[i + offset] = sum;
            carry = a[i] && result[i + offset];
        }
        return carry;
    }

    private boolean mulsub(boolean[] q, boolean[] a, boolean x, int len, int offset)
    {
        boolean carry = false;
        offset += len;
        for(int i = len - 1; i >= 0; i--)
        {
            boolean prod = (a[i] && x) || carry;
            boolean diff = q[offset] && !prod;
            q[offset--] = diff;
            carry = prod || (diff && !prod);
        }
        return carry;
    }

    private boolean mulsubBorrow(boolean[] q, boolean[] a, boolean x, int len, int offset)
    {
        boolean carry = false;
        offset += len;
        for(int i = len - 1; i >= 0; i--)
        {
            boolean prod = (a[i] && x) || carry;
            boolean diff = q[offset--] && !prod;
            carry = prod || (diff && !prod);
        }
        return carry;
    }

    private BigInt getLower(int n)
    {
        if(isZero())
            return BigInt.ZERO;
        else if(bitLen < n)
            return toBigInt(1);
        else
        {
            int len = n;
            while(len > 0 && !val[offset + bitLen - len])
                len--;
            int sign = len > 0 ? 1 : 0;
            return new BigInt(Arrays.copyOfRange(val,offset+bitLen-len,offset+bitLen), sign);
        }
    }

    private void keepLower(int n)
    {
        if(bitLen >= n)
        {
            offset += bitLen - n;
            bitLen = n;
        }
    }

    int bitLengthForInt(int off)
    {
        int count = 0;
        for(int i = off; i < val.length && i < 32; i++)
            if(val[i])
                count++;
        return count;
    }

    private boolean[] translate(int n)
    {
        boolean[] bits = new boolean[32];
        for(int i = 31; i >= 0; i--)
            bits[bits.length - i - 1] = (n & (1 << i)) != 0;
        return bits;
    }

    public static void main(String[] args)
    {
        boolean[] bits = {false,false,false,true};
        MutableBigInt M = new MutableBigInt(bits);
        M.leftShift(2);
        System.out.println(M.getLowestSetBit());
    }
}
