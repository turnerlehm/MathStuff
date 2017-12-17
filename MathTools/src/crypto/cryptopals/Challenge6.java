package crypto.cryptopals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

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

    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        HexToBase64 encoder = new HexToBase64();
        System.out.println(convertToBits("this is a test"));
        System.out.println(convertToBits("wokka wokka!!!"));
        System.out.println(hammingDistance(convertToBits("this is a test"),convertToBits("wokka wokka!!!")));
        Scanner fin = new Scanner(new File("D:\\Projects\\COMPUTATIONS\\6.txt"));
        String b64 = "";
        while(fin.hasNext())
            b64 += fin.nextLine();
        byte[] b = encoder.decode(b64);
        decipher(b);
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

    public static void decipher(byte[] bytes) throws UnsupportedEncodingException {
        double min = Double.MAX_VALUE;
        //ArrayList<Double> norms = new ArrayList<Double>();
        //ArrayList<Integer> keysize = new ArrayList<Integer>();
        TreeMap<Double, Integer> norms = new TreeMap<Double, Integer>();
        String plain = "";
        for(int k : KEYSIZE) //Step 3
        {
            byte[] b1 = new byte[k];
            byte[] b2 = new byte[k];
            byte[] b3 = new byte[k];
            byte[] b4 = new byte[k];
            for(int i = 0;  i < k; i++) //Step 3a
            {
                b1[i] = bytes[i];
            }
            for(int i = k; i < k * 2; i++)
                b2[i % b2.length] = bytes[i];
            for(int i = k * 2; i < k * 3; i++)
                b3[i % b3.length] = bytes[i];
            for(int i = k * 3; i < k * 4; i++)
                b4[i % b4.length] = bytes[i];
            String s1 = convertToBits(b1);
            String s2 = convertToBits(b2);
            String s3 = convertToBits(b3);
            String s4 = convertToBits(b4);
            int h = hammingDistance(s1,s2) + hammingDistance(s2,s3) + hammingDistance(s3,s4);
            double norm = (double)h / (double)(k * 3); //Step 3b
            norms.put(norm, k);
        }
        ArrayList<Integer> smallest = new ArrayList<Integer>();
        Set es = norms.entrySet();
        Iterator iter = es.iterator();
        for(int i = 0; i < 5; i++)
        {
            Map.Entry m = (Map.Entry)iter.next();
            smallest.add((Integer)m.getValue());
        }
        for(Integer i : smallest)
        {
            ArrayList<ArrayList<Byte>> blocks = new ArrayList<ArrayList<Byte>>();
            String key = "";
            for(int j = 0; j < i; j++)
                blocks.add(j, new ArrayList<Byte>());
            for(int j = 0; j < bytes.length; j++)
                blocks.get(j % i).add(bytes[j]);
            for(int j = 0; j < i; j++)
            {
                ArrayList<Byte> b1 = blocks.get(j);
                byte[] b = new byte[b1.size()];
                for(int k = 0; k < b.length; k++)
                    b[k] = b1.get(k);
                System.out.println(Arrays.toString(b));
                key += (char)breakSingleXOR(b);
            }
            plain = xor(key,bytes);

            System.out.println("Key: " + key);
            System.out.println("Size: " + i);
            System.out.println("Plain: " + plain);
        }
    }

    public static int breakSingleXOR(byte[] b) throws UnsupportedEncodingException {
        HexToBase64 encoder = new HexToBase64();
        double maxfreq = 0.0;
        int key = 0;
        for(int i = 1; i < 256; i++)
        {
            byte[] b1 = xorSingle((byte)i, b);
            double freq = maxCharFreq(b1);
            if(Double.compare(freq,maxfreq) > 0)
            {
                maxfreq = freq;
                key = i;
            }
        }
        return key;
    }
    
    public static String xor(String key, byte[] pt) throws UnsupportedEncodingException {
        byte[] k = key.getBytes();
        for(int i = 0; i < pt.length; i++)
            pt[i] = (byte)(pt[i] ^ k[i % k.length]);
        return new String(pt,"ASCII");
    }

    public static byte[] xorSingle(byte b, byte[] bytes)
    {
        for(int i = 0; i < bytes.length; i++)
            bytes[i] ^= b;
        return bytes;
    }

    public static double maxCharFreq(byte[] b)
    {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for(int i = 0; i < b.length; i++)
        {
            char c = (char)b[i];
            if((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            {
                Integer count = map.get(c);
                if (count == null)
                    count = 0;
                map.put(c, count + 1);
            }
        }
        int max = 0;
        for(int i = 0; i < b.length; i++)
        {
            char c = (char)b[i];
            if((c >= 65 && c <= 90) || (c >= 97 && c <= 122))
            {
                Integer count = map.get(c);
                max += count;
            }
        }
        return (double)max / (double)b.length;
    }
}
