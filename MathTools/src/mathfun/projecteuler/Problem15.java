package mathfun.projecteuler;

public class Problem15
{
    public static void main(String[] args)
    {
        long[][] grid = new long[20][20];
        for(int j = 0, val = 2; j < grid[0].length; j++, val++)
            grid[0][j] = val;
        for(int i = 0, val = 2; i < grid.length; i++, val++)
            grid[i][0] = val;
        for(int i = 1; i < grid.length; i++)
        {
            for(int j = 1; j < grid[i].length; j++)
            {
                grid[i][j] = grid[i-1][j] + grid[i][j-1];
                System.out.println("(" + i + "," + j + ") = " + grid[i][j]);
            }
        }
        System.out.println(grid[19][19]);
    }
}
