package mathfun.projecteuler;

import java.util.ArrayList;
import java.util.Arrays;

public class Problem7
{
    public static void main(String... args)
    {
        ArrayList<Integer> primes = sieve(10001);
        int i = 10001;
        while(primes.size() < 10001)
            primes = sieve(++i);
        System.out.println(primes.get(10000));
    }
    private static ArrayList<Integer> sieve(int n)
    {
        boolean[] a = new boolean[n + 1];
        Arrays.fill(a, true);
        for(int i = 2; i < Math.sqrt((double)n); i++)
        {
            if(a[i])
            {
                for(int j = i * i; j < a.length; j += i)
                    a[j] = false;
            }
        }
        ArrayList<Integer> primes = new ArrayList<Integer>();
        for(int i = 2; i < a.length; i++)
            if(a[i])
                primes.add(i);
        return primes;
    }
}
