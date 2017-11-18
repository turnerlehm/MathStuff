package mathfun.projecteuler;

import java.math.BigInteger;

public class Problem16
{
    public static void main(String[] args)
    {
        BigInteger num = new BigInteger("2").pow(1000);
        String nums = num.toString();
        long sum = sumDigits(nums);
        System.out.println(sum);
    }

    private static long sumDigits(String num)
    {
        long sum = 0;
        for(int i = 0; i < num.length(); i++)
        {
            int digit = Integer.parseInt(num.substring(i,i+1));
            sum += digit;
        }
        return sum;
    }
}
