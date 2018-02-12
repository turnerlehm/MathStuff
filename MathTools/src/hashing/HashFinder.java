package hashing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class HashFinder
{
    private static Random RNG = new SecureRandom();

    //find a linear function f = ax + b with the best score
    public static int findLinear(String[] keys, int[] map) throws IOException
    {
        int max = Integer.MAX_VALUE;
        int A = -1, B = -1, count = 0;
        BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\MathStuff\\MathStuff\\MathTools\\src\\hashing\\found_values_linear_50000.txt"));
        out.write("Sum,a,b");
        out.flush();
        out.newLine();
        out.flush();
        //only generate as many RNs as there are slots
        for(int i = 0; i < map.length; i++)
        {
            int a = RNG.nextInt(Integer.MAX_VALUE);
            int b = RNG.nextInt(Integer.MAX_VALUE);
            Arrays.fill(map, 0);
            for(String s : keys)
            {
                int idx = linear(a,b,s.getBytes()) % map.length;
                map[idx]++;
            }
            int sum = 0;
            for(int j : map)
                if(j > 1)
                    sum += j;
            if(sum <= max)
            {
                max = sum;
                A = a;
                B = b;
                count++;
                out.write( max + "," + A + "," + B);
                out.flush();
                out.newLine();
                out.flush();
            }
        }
        out.write(""+count);
        out.close();
        System.out.println("Max: " + max + " a = " + A + ", b = " + B);
        return max;
    }

    private static int linear(int a, int b, byte[] key)
    {
        int f = 0;
        for(byte x : key)
            f += a * x + b;
        return f & Integer.MAX_VALUE;
    }

    public static void main(String[] args) throws IOException {
        String[] students = new String[50000];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student" + i;
        int[] map = new int[50000];
        HashFinder.findLinear(students,map);
    }
}
