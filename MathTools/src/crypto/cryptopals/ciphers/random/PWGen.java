package crypto.cryptopals.ciphers.random;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class PWGen
{
    private Random rng = new SecureRandom();
    private static final String DEFAULT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,./;'[]\\<>?:\"{}|`~!@#$%^&*()-=_+";

    private String generate(int len, int num, String alpha)
    {
        if(alpha == null)
            alpha = DEFAULT;
        if(len <= 0)
            throw new IllegalArgumentException("length <= 0");
        if(num <= 0)
            throw new IllegalArgumentException("num <= 0");
        if(num > 1)
        {
            String[] passes = new String[num];
            for(int i = 0; i < passes.length; i++)
                passes[i] = "";
            char[] pchar = alpha.toCharArray();
            shuffle(pchar);
            for(int i = 0; i < passes.length; i++)
            {
                for(int j = 0; j < len; j++)
                {
                    int k = rng.nextInt(pchar.length);
                    passes[i] += pchar[k];
                }
            }
            String ret = "Passwords:\n";
            for(String S : passes)
                ret += S + "\n";
            ret += "Appx. bits of entropy: " + entropy(pchar.length,len);
            return ret;
        }
        else
        {
            char[] pchar = alpha.toCharArray();
            shuffle(pchar);
            String pass = "";
            for(int i = 0; i < len; i++)
            {
                int j = rng.nextInt(pchar.length);
                pass += pchar[j];
            }
            String ret = "Password: " + pass + "\nAppx. bits of entropy: " + entropy(pchar.length,len);
            return ret;
        }
    }

    private int entropy(int alen, int plen)
    {
        return (int)Math.floor(Math.log(Math.pow((double)alen,(double)plen)) / Math.log(2.0));
    }

    private void shuffle(char[] pchar)
    {
        for(int i = 0; i < pchar.length - 1; i++)
        {
            int j = rng.nextInt((pchar.length - 1 - i) + 1) + i;
            char temp = pchar[i];
            pchar[i] = pchar[j];
            pchar[j] = temp;
        }
    }

    public static void main(String[] args)
    {
        PWGen P = new PWGen();
        System.out.println(Double.MAX_VALUE);
        System.out.println(Math.pow((double)DEFAULT.length(),64));
        System.out.println(P.generate(64, 5, null));
        System.out.println(P.generate(64,5,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
    }
}
