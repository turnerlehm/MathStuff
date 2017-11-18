package crypto.cryptopals;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Challenge3
{
    private static final char[] ALPHA = {'A', 'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
    'U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w',
    'x','y','z'};
    private static final char[] MOST = "ETAOIN SHRDLUetaoin shrdlu".toCharArray();
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws UnsupportedEncodingException {
        String ct = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        HexToBase64 encoder = new HexToBase64();
        ByteXOR bx = new ByteXOR();
        double maxfreq = 0.0;
        for(char c : ALPHA)
        {
            String pt = bx.xor((byte)c,ct);
            double freq = maxCharFreq(pt, encoder);
            if(Double.compare(freq,maxfreq) > 0)
            {
                System.out.println("k = " + c + ": PT = " + pt + " max # chars = " + freq);
                maxfreq = freq;
                byte[] enc = new BigInteger(pt,16).toByteArray();
                String phrase = new String(enc,"ASCII");
                System.out.println("Phrase: " + phrase);
            }
        }
        maxfreq = 0.0;
        for(char c : MOST)
        {
            int key = c ^ ' ';
            String pt = bx.xor((byte)key,ct);
            double freq = maxCharFreq(pt, encoder);
            if(Double.compare(freq,maxfreq) > 0)
            {
                System.out.println("k = " + (char)key + ": PT = " + pt + " score = " + freq);
                maxfreq = freq;
            }
        }
    }

    private static int countNullBytes(String hex)
    {
        int count = 0;
        for(int i = 0; i < hex.length() - 1; i+=2)
        {
            if(hex.charAt(i) == '0' && hex.charAt(i + 1) == '0')
                count++;
        }
        return count;
    }

    public static double maxCharFreq(String hex, HexToBase64 enc) throws UnsupportedEncodingException {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        String pt = new String(enc.hexDecode(hex), "ASCII");
        for(int i = 0; i < pt.length(); i++)
        {
            char c = pt.charAt(i);
            if((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            {
                Integer count = map.get(c);
                if (count == null)
                    count = 0;
                map.put(c, count + 1);
            }
        }
        int maxfreq = 0;
        for(int i = 0; i < pt.length(); i++)
        {
            char c = pt.charAt(i);
            if((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            {
                Integer count = map.get(c);
                maxfreq += count;
            }
        }
        return (double)maxfreq / (double)pt.length();
    }
}
