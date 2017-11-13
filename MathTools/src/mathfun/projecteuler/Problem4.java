package mathfun.projecteuler;

public class Problem4
{
    public static void main(String... args)
    {
        int max = 0;
        for(int i = 100; i <= 999; i++)
        {
            for(int j = i + 1; j <= 999; j++)
            {
                int prod = i * j;
                if(palindrome(prod) && prod > max)
                    max = prod;
            }
        }
        System.out.println(max);
    }

    private static boolean palindrome(int prod)
    {
        String p = "" + prod;
        boolean palindrome = true;
        for(int i = 0; i < p.length() / 2; i++)
            palindrome = palindrome && (p.charAt(i) == p.charAt(p.length() - 1 - i));
        return palindrome;
    }
}
