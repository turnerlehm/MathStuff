package crypto.cryptopals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Challenge6
{
    private static final int[] KEYSIZE = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,
    30,31,32,33,34,35,36,37,38,39,40};
    public static int hammingDistance(String s1, String s2)
    {
        if(s1.length() != s2.length())
            throw new IllegalArgumentException("Strings must be the same length");
        char[] b1 = s1.toCharArray();
        char[] b2 = s2.toCharArray();
        int count = 0;
        for(int i = 0; i < b1.length; i++)
        {
            if(b1[i] != b2[i])
                count++;
        }
        return count;
    }

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        HexToBase64 encoder = new HexToBase64();
        System.out.println(convertToBits("this is a test"));
        System.out.println(convertToBits("wokka wokka!!!"));
        System.out.println(hammingDistance(convertToBits("this is a test"),convertToBits("wokka wokka!!!")));
    }

    public static String convertToBits(String s)
    {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = s.getBytes();
        for(byte b : bytes)
        {
            for(int i = 0; i < 8; i++)
            {
                sb.append((b & 128) == 0 ? 0 : 1);
                b <<= 1;
            }
        }
        return sb.toString();
    }

    public static String convertToBits(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes)
        {
            for(int i = 0; i < 8; i++)
            {
                sb.append((b & 128) == 0 ? 0 : 1);
                b <<= 1;
            }
        }
        return sb.toString();
    }

    public void decipher(byte[] bytes) throws UnsupportedEncodingException {
        double min = Double.MAX_VALUE;
        ArrayList<Integer> keys = new ArrayList<Integer>();
        String plain = "";
        for(int k : KEYSIZE) //Step 3
        {
            byte[] b1 = new byte[k];
            byte[] b2 = new byte[k];
            byte[] b3 = new byte[k];
            byte[] b4 = new byte[k];
            for(int i = 0; i < bytes.length - 4 && i < k; i++) //Step 3a
            {
                b1[i] = bytes[i];
                b2[i] = bytes[(i + 1) % bytes.length];
                b3[i] = bytes[(i + 2) % bytes.length];
                b4[i] = bytes[(i + 3) % bytes.length];
            }
            String s1 = convertToBits(b1);
            String s2 = convertToBits(b2);
            String s3 = convertToBits(b3);
            String s4 = convertToBits(b4);
            int h = hammingDistance(s1,s2) + hammingDistance(s2,s3) + hammingDistance(s3,s4);
            double norm = (double)h / (double)(k * 3); //Step 3b
            if(Double.compare(norm,min) < 0)
            {
                min = norm;
                keys.add(k);
            }
        }
        ArrayList<Integer> smallest = new ArrayList<Integer>();
        for(int i = keys.size() - 5; i < keys.size(); i++)
            smallest.add(keys.get(i));
        for(Integer i : smallest) // Step 4
        {
            int blocksize = i;
            ArrayList<ArrayList<Byte>> blocks = new ArrayList<ArrayList<Byte>>();
            for(int j = 0; j < blocksize; j++)
                blocks.add(j , new ArrayList<Byte>());
            for(int j = 0; j < bytes.length; j++)
                blocks.get(j%blocksize).add(bytes[j]);
            String key = "";
            byte[] b;
            for(ArrayList<Byte> blockbytes : blocks)
            {
                b = new byte[blockbytes.size()];
                for(int j = 0; j < b.length; j++)
                    b[j] = blockbytes.get(j);
                key += breakSingleXOR(b);
            }
            plain = xor(key,bytes);
            System.out.println("k = " + key);
            System.out.println("keysize = " + i);
            System.out.println("Plaintext: " + plain);
        }
    }

    public char breakSingleXOR(byte[] b) throws UnsupportedEncodingException {
        HexToBase64 encoder = new HexToBase64();
        String ct = encoder.hexEncode(b);
        ByteXOR bx = new ByteXOR();
        double maxfreq = 0.0;
        int key = 0;
        for(int i = 1; i < 256; i++)
        {
            String pt = bx.xor((byte)i,ct);
            double freq = Challenge3.maxCharFreq(pt,encoder);
            if(Double.compare(freq,maxfreq) > 0)
            {
                maxfreq = freq;
                key = i;
            }
        }
        return (char)key;
    }
    
    public String xor(String key, byte[] pt) throws UnsupportedEncodingException {
        byte[] k = key.getBytes();
        for(int i = 0; i < pt.length; i++)
            pt[i] = (byte)(pt[i] ^ k[i % k.length]);
        return new String(pt,"ASCII");
    }
}
