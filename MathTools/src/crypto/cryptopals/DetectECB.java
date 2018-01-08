package crypto.cryptopals;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class DetectECB
{
    public static void main(String[] args)
    {
        detect("D:\\Projects\\COMPUTATIONS\\8.txt");
    }

    public static int repeatedBlocks(String msg, int blen)
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        if(blen <= 0)
            blen = 16;
        for(int i = 0; i < msg.length(); i += blen)
        {
            String block = msg.substring(i, i + blen);
            Integer count = map.get(block);
            if(count == null)
                count = 0;
            map.put(block, count + 1);
        }
        int sum = 0;
        for(int i = 0; i < msg.length(); i += blen)
        {
            String block = msg.substring(i, i + blen);
            Integer count = map.get(block);
            sum  += count;
        }
        return sum;
    }

    public static void detect(String fname)
    {
        try
        {
            Scanner fin = new Scanner(new File(fname));
            int max = 0;
            String ct = "";
            while(fin.hasNext())
            {
                String s = fin.nextLine();
                int score = repeatedBlocks(s, 16);
                if(score > max)
                {
                    max = score;
                    ct = s;
                }
            }
            System.out.println("Score: " + max + " Ciphertext: " + ct);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
