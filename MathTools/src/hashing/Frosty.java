package hashing;

import java.math.BigInteger;
import java.util.Arrays;

public class Frosty
{
    private BigInteger s0 = BigInteger.valueOf(0xBEEFBEEF);
    private BigInteger s1 = BigInteger.valueOf(0xBEEFDEAD);
    private BigInteger s2 = BigInteger.valueOf(0xDEADBEEF);
    private BigInteger s3 = BigInteger.valueOf(0xDEADDEAD);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    private void round()
    {
        s0 = s0.shiftLeft(23).or(s0.shiftRight(9));
        s1 = s1.shiftLeft(3).or(s1.shiftRight(29));
        s0 = s0.add(s3);
        s1 = s1.add(s2);
        s0 = s0.xor(s2);
        s1 = s1.xor(s3);
        s0 = s0.shiftLeft(7).or(s0.shiftRight(25));
        s2 = s2.shiftLeft(19).or(s2.shiftRight(13));
        s3 = s3.shiftLeft(29).or(s3.shiftRight(3));
        s2 = s2.add(s1);
        s3 = s3.add(s0);
        s3 = s3.xor(s0);
        s2 = s2.shiftLeft(11).or(s2.shiftRight(21));
        s0 = s0.shiftLeft(5).or(s0.shiftRight(27));
        s1 = s1.shiftLeft(17).or(s1.shiftRight(15));
        s0 = s0.xor(s3);
        s1 = s1.xor(s2);
        s0 = s0.shiftLeft(23).or(s0.shiftRight(9));
        s2 = s2.shiftLeft(13).or(s2.shiftRight(19));
        s3 = s3.shiftLeft(31).or(s3.shiftRight(1));
        s3 = s3.add(s0);
        s2 = s2.add(s0);
        s3 = s3.add(s1);
        half1();
        half2();
        half1();
        half1();
        half2();
        half2();
        half1();
        half2();
    }

    private void half1()
    {
        s2 = s2.add(s1);
        s1 = s1.shiftLeft(13).or(s1.shiftRight(19));
        s2 = s2.xor(s1);
        s3 = s3.shiftLeft(17).or(s3.shiftRight(15));
        s0 = s0.xor(s3);
        s2 = s2.shiftLeft(23).or(s2.shiftRight(9));
        s0 = s0.add(s2);
        s1 = s1.add(s3);
    }

    private void half2()
    {
        s0 = s0.xor(s0.shiftLeft(5).xor(s0.shiftRight(2)).xor(s1));
        s1 = s1.add(s1.shiftLeft(3).add(s1.shiftRight(7)).xor(s2));
        s2 = s2.xor(s2.shiftLeft(7).xor(s2.shiftRight(13)).xor(s3));
        s3 = s3.add(s3.shiftLeft(11).add(s3.shiftRight(19)).xor(s0));
        s0 = s0.add(s0.shiftLeft(2).xor(s0.shiftRight(7)).xor(s2));
        s1 = s1.xor(s1.shiftLeft(19).add(s1.shiftRight(23)).add(s3));
        s2 = s2.add(s2.shiftLeft(13).xor(s2.shiftRight(29)).xor(s0));
        s3 = s3.xor(s3.shiftLeft(13).add(s3.shiftRight(19)).add(s1));
    }

    private BigInteger hash(byte[] msg, int c, int d, int mod)
    {
        BigInteger h = BigInteger.ZERO;
        for(byte b : msg)
            h = h.xor(h.shiftLeft(7).xor(h.shiftRight(4)).xor(BigInteger.valueOf(b)));
        for(int i = 0; i < msg.length; i++)
        {
            if(i % 2 == 0)
            {
                s0 = s0.xor(BigInteger.valueOf(msg[i]));
                s2 = s2.add(BigInteger.valueOf(msg[i]));
            }
            else
            {
                s1 = s1.xor(BigInteger.valueOf(msg[i]));
                s3 = s3.add(BigInteger.valueOf(msg[i]));
            }
        }
        h = h.xor(s0);
        h = h.add(s1);
        h = h.xor(s2);
        h = h.add(s3);
        for(int i = 0; i < c; i++)
            round();
        h = h.xor(s3);
        h = h.add(s0);
        h = h.xor(s1);
        h = h.xor(s2);
        for(byte b : msg)
            h = h.xor(h.shiftLeft(5).xor(h.shiftRight(2)).xor(BigInteger.valueOf(b)));
        for(int i = 0; i < msg.length; i++)
        {
            if(i % 2 == 0)
            {
                s1 = s1.xor(BigInteger.valueOf(msg[i]));
                s3 = s3.add(BigInteger.valueOf(msg[i]));
            }
            else
            {
                s0 = s0.xor(BigInteger.valueOf(msg[i]));
                s2 = s2.add(BigInteger.valueOf(msg[i]));
            }
        }
        h = h.xor(s2);
        h = h.add(s3);
        h = h.xor(s0);
        h = h.add(s1);
        for(int i = 0; i < d; i++)
            round();
        h = h.xor(s1);
        h = h.add(s2);
        h = h.xor(s3);
        h = h.add(s0);
        h = h.xor(s0.xor(s1).xor(s2).xor(s3)).mod(TWO.pow(mod).subtract(BigInteger.ONE));
        return h;
    }

    private long sax3(byte[] key)
    {
        long h = 0;
        for(byte b : key)
            h ^= (h << 5) ^ (h >> 2) ^ b;
        return h & Long.MAX_VALUE;
    }

    private long bernstein2(byte[] key)
    {
        long h = 0;
        for(byte b : key)
            h = 33 * h ^ b;
        return h & Long.MAX_VALUE;
    }

    public static void main(String[] args)
    {
        String[] students = new String[50];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student" + i;
        Frosty F = new Frosty();
        int[] map = new int[50];
        System.out.println("Modulus: " + TWO.pow(512).subtract(BigInteger.ONE));
        for(String S : students)
            System.out.println(F.hash(S.getBytes(),2,4,512));
        /*
        for(String S : students)
        {
            int idx = F.hash(S.getBytes(),2,4,31).intValueExact() % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        */
    }
}
