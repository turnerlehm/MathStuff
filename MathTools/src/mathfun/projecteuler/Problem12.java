package mathfun.projecteuler;

public class Problem12
{
    public static void main(String... args)
    {
        long tri = 0;
        for(int i = 2; i <= Integer.MAX_VALUE; i++)
        {
            tri = (i * (i-1))/2;
            System.out.print(tri);
            if(factors(tri) >= 500) {
                System.out.println(tri);
                break;
            }
        }
    }

    private static int factors(long tri)
    {
        int count = 0;
        for(int i = 1; i < Math.sqrt((double)tri); i++)
            if(tri % i == 0)
                count += 2;
        System.out.print(": " + count + "\n");
        return count;
    }
}
