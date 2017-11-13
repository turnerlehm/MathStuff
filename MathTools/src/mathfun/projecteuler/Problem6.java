package mathfun.projecteuler;

public class Problem6
{
    public static void main(String... args)
    {
        long squaresum = ((100 * 101) / 2) * ((100 * 101) / 2);
        long sumsquare = (100 * 101 * 201) / 6;
        System.out.println(sumsquare - squaresum);
    }
}
