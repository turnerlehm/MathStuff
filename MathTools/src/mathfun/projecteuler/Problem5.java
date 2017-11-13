package mathfun.projecteuler;

public class Problem5
{
    public static void main(String... args)
    {
        int i = 20;
        for(; i < Integer.MAX_VALUE; i++)
        {
            boolean div = true;
            for(int j = 1; j <= 20; j++)
                div = div && i % j == 0;
            if(div)
                break;
        }
        System.out.println(i);
    }
}
