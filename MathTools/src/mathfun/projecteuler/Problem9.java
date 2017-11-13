package mathfun.projecteuler;

public class Problem9
{
    public static void main(String... args)
    {
        outer:
        for(int m = 2; m < Integer.MAX_VALUE; m++)
        {
            for(int n = 1; n < Integer.MAX_VALUE && n < m; n++)
            {
                long a = m*m - n*n;
                long b = 2 * m * n;
                long c = m*m + n*n;
                if(a + b + c == 1000)
                {
                    System.out.println(a * b * c);
                    break outer;
                }
            }
        }
    }

    private static boolean coprime(int a, int b)
    {
        if(!(((a | b) & 1) == 1)) //even numbers
            return false;
        while(b != 0)
        {
            int t = a % b;
            a = b;
            b = t;
        }
        return a == 1;
    }
}
