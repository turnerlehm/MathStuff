package mathfun.projecteuler;

import java.util.ArrayList;
import java.util.Arrays;

public class Problem3
{
    public static void main(String... args)
    {
        long num = 600851475143L;
        ArrayList<Integer> primes = sieve((int)Math.sqrt((double)num));
        int max = 0;
        for(int i = 0; i < primes.size(); i++)
        {
            if(num % primes.get(i) == 0)
                max = primes.get(i);
        }
        System.out.println(max);
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
