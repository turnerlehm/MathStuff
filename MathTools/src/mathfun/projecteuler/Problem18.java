package mathfun.projecteuler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Problem18
{
    public static void main(String[] args) throws FileNotFoundException
    {
        int[][] tri = new int[15][];
        Scanner fin = new Scanner(new File("D:\\Projects\\COMPUTATIONS\\p18_triangle.txt"));
        for(int i = 0; i < tri.length; i++)
            tri[i] = new int[i+1];
        for(int i = 0; i < tri.length; i++)
            for(int j = 0; j < tri[i].length; j++)
                tri[i][j] = fin.nextInt();
        for(int i = tri.length - 2; i >= 0; i--)
        {
            for(int j = 0; j < tri[i].length; j++)
            {
                tri[i][j] = Math.max(tri[i][j] + tri[i+1][j], tri[i][j] + tri[i+1][j+1]);
            }
        }
        System.out.println(tri[0][0]);
    }
}
