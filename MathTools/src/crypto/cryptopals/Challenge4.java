package crypto.cryptopals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class Challenge4
{

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
    {
        String file = "D:\\Projects\\COMPUTATIONS\\challenge4.txt";
        HexToBase64 encoder = new HexToBase64();
        ByteXOR bx = new ByteXOR();
        Scanner fin = new Scanner(new File(file));
        double max = 0.0;
        String dec = "";
        while(fin.hasNext())
        {
            String ct = fin.nextLine();
            double maxfreq = 0.0;
            String plain = "";
            for(int i = 1; i < 256; i++)
            {
                String pt = bx.xor((byte)i, ct);
                double freq = Challenge3.maxCharFreq(pt, encoder);
                if(Double.compare(freq,maxfreq) > 0)
                {
                    maxfreq = freq;
                    plain = new String(new BigInteger(pt,16).toByteArray(), "ASCII");
                }
            }
            System.out.println("PT = " + plain + ": " + maxfreq);
            if(Double.compare(maxfreq, max) > 0)
            {
                max = maxfreq;
                dec = plain;
            }
        }
        System.out.println("Plaintext: " + dec + " score = " + max);
    }
}
