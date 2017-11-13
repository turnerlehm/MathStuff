package mathfun.projecteuler;

public class Problem14
{
    public static void main(String... args)
    {
        int maxlen = 0;
        int length = 1;
        int rec = -1;
        for(int i = 1; i < 1000000; i++)
        {
            length = collatz(i);
            System.out.println(i + ": " + length);
            if(length > maxlen)
            {
                maxlen = length;
                rec = i;
            }
        }
        System.out.println(rec);
    }

    private static int collatz(long n)
    {
        int length = 1;
        while(n != 1)
        {
            n = n % 2 == 0 ? n / 2 : 3*n + 1;
            length++;
        }
        return length;
    }
}
