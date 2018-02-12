package crypto.cryptopals;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Random;

/**
 * Java version of the code found here: https://github.com/tungpun/tp-cryptopals-solutions/blob/master/set%202/Challenge%2012%20-%20Byte-at-a-time%20ECB%20decryption%20(Simple)/main.py
 */

public class ECBDecrypt
{
    private static Random RNG = new SecureRandom();
    private static byte[] KEY;
    private static final String UNKNOWN = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg" +
            "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq" +
            "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" +
            "YnkK";
    private static String[] dictionary;
    private static final String KNOWN = "AAAAAAAAAAAAAAAA";
    private static Hashtable<String,String> DICT = new Hashtable<>();
    private static Hashtable<byte[],String> DICT2 = new Hashtable<>();

    public static void makeRandom(int keylength)
    {
        KEY = new byte[keylength];
        RNG.nextBytes(KEY);
    }

    public static byte[] encrypt(byte[] msg)
    {
        try {
            msg = concat(msg,UNKNOWN.getBytes());
            msg = PKCS7.pad(msg,16);
            Cipher C = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec K = new SecretKeySpec(KEY, "AES");
            C.init(Cipher.ENCRYPT_MODE, K);
            byte[] enc = C.doFinal(msg);
            return enc;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concat(byte[] b1, byte[] b2)
    {
        byte[] res = new byte[b1.length + b2.length];
        int i;
        for(i = 0; i < b1.length; i++)
            res[i] = b1[i];
        for(int j = 0; j < b2.length; j++)
            res[i++] = b2[j];
        return res;
    }

    public static boolean detectECB(byte[] msg)
    {
        byte[] b1 = encrypt(msg);
        byte[] b2 = encrypt(msg);
        int sum = 0;
        for(int i = 0; i < b1.length; i++)
            sum |= b1[i] ^ b2[i];
        return sum == 0;
    }

    public static String append(String msg, int blocksize)
    {
        int need = blocksize - msg.length() % blocksize - 1;
        return buildString('A',need);
    }

    public static Hashtable<byte[],String> makeDictionary(String msg, int blocksize)
    {
        Hashtable<byte[], String> dict = new Hashtable<>();
        for(int i = 0; i < 128; i++)
        {
            byte[] block = encrypt((msg + (char)i).getBytes());
            dict.put(slice(block,msg.length()), "" + (char)i);
        }
        return dict;
    }

    public static String getUnknown(int blocksize)
    {
        String known = "";
        while(true)
        {
            String append = append(known,blocksize);
            String input = append + known;
            Hashtable<byte[],String> dict = makeDictionary(input,blocksize);
            byte[] sample = slice(encrypt(append.getBytes()),input.length());
            if(dict.get(sample) != null)
            {
                known += dict.get(sample);
            }
            else
                return known;
        }
    }

    public static byte[] slice(byte[] input, int size)
    {
        assert size <= input.length : "size out of bounds";
        byte[] ret = new byte[size];
        for(int i = 0; i < ret.length; i++)
            ret[i] = input[i];
        return ret;
    }

    public static int getBlocksize()
    {
        byte[] sample = encrypt("".getBytes());
        int len = 1;
        int first = 0;
        int second = 0;
        while(true)
        {
            byte[] cipher = encrypt(buildString('A',len).getBytes());
            if(sample.length != cipher.length)
            {
                if(first == 0)
                    first = len;
                else
                {
                    second = len;
                    System.out.println("[+] Found blocksize: " + (second - first));
                    return second - first;
                }
                sample = cipher;
            }
            len++;
        }
    }

    public static String buildString(char c, int size)
    {
        String S = "";
        for(int i = 0; i < size; i++)
            S += c;
        return S;
    }

    public static void main(String[] args)
    {
        makeRandom(16);
        int blocksize = getBlocksize();
        detectECB("".getBytes());
        System.out.println("[+] Unknown string: " + getUnknown(blocksize));
    }
}
