package primality;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AKS
{
    public boolean step1(BigInteger n)
    {

        return true;
    }

    public int step2(BigInteger n)
    {
        int maxk = (int)Math.pow(n.bitLength(),2);
        boolean next = true;
        int r;
        for(r = 2; next; r++)
        {
            next = false;
            for(int k = 1; !next && k <= maxk; k++)
            {
                next = n.pow(k).mod(BigInteger.valueOf(r)).equals(BigInteger.ONE) || n.pow(k).mod(BigInteger.valueOf(r)).equals(BigInteger.ZERO);
            }
        }
        r--;
        return r;
    }

    public boolean step3(BigInteger n, int r)
    {
        for(int a = r; a > 1; a--)
        {
            BigInteger gcd = n.gcd(BigInteger.valueOf(a));
            if(gcd.compareTo(BigInteger.ONE) > 0 && gcd.compareTo(n) < 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        AKS A = new AKS();
        BigInteger N = BigInteger.valueOf(31);
        System.out.println(A.step1(N));
        int r = A.step2(N);
        System.out.println(r);
        System.out.println(A.step3(N,r));
    }
}
