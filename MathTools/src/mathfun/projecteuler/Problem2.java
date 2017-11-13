package mathfun.projecteuler;

public class Problem2
{
    public static void main(String... args)
    {
        int fib1 = 1;
        int fib2 = 2;
        int sum = 2;
        while(fib1 + fib2 < 4000000)
        {
            int temp = fib2;
            fib2 = fib1 + fib2;
            fib1 = temp;
            sum += fib2 % 2 == 0 ? fib2 : 0;
        }
        System.out.println(sum);
    }
}
