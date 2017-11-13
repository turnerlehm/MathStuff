package mathfun.projecteuler;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Problem13
{
    public static void main(String... args) throws FileNotFoundException {
        BigInteger sum = BigInteger.ZERO;
        Scanner fin = new Scanner(new File("D:\\Projects\\COMPUTATIONS\\p13_nums.txt"));
        while(fin.hasNext())
            sum = sum.add(new BigInteger(fin.nextLine()));
        String s = sum.toString();
        System.out.println(s);
        for(int i = 0; i < 10; i++)
            System.out.print(s.charAt(i));
    }
}
