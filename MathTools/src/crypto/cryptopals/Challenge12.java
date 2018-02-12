package crypto.cryptopals;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;

public class Challenge12
{
    private static Random RNG = new SecureRandom();
    private static byte[] KEY = new byte[16];
    static{RNG.nextBytes(KEY);}
    private static final String UNKNOWN = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg" +
            "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq" +
            "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" +
            "YnkK";
    private static String[] dictionary;
    private static final String KNOWN = "AAAAAAAAAAAAAAAA";
    private static Hashtable<String,String> DICT = new Hashtable<>();
    private static Hashtable<byte[],String> DICT2 = new Hashtable<>();


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

    public static int step1a(String msg)
    {
        int maxlen = 0;
        int len = 1;
        int first = 0;
        int second = 0;
        byte[] E = encrypt(msg.getBytes());
        while(true)
        {
            String res = "";
            for(int i = 0; i < len; i++)
            {
                if(msg.length() >= len)
                    res = msg.substring(0,len);
                else
                    res += msg.charAt(0);
            }
            byte[] enc = encrypt(res.getBytes());
            if(E.length != enc.length)
            {
                if(first == 0)
                    first = E.length;
                else
                {
                    second = enc.length;
                    return second - first;
                }
                E = enc;
            }
            len++;
        }
    }

    public static int step1(String msg)
    {
        int maxlen = 0;
        for(int i = 1; i <= msg.length(); i++)
        {
            String sub = msg.substring(0,i);
            byte[] enc = encrypt(sub.getBytes());
            String encoded = Base64.getEncoder().encodeToString(enc);
            if(encoded.length() > maxlen && encoded.charAt(encoded.length() - 2) != '=')
            {
                maxlen = sub.length();
            }
        }
        return maxlen;
    }

    public static void buildDictionary(String msg,int blocksize)
    {
        String oneshort = msg.substring(0,blocksize - 1);
        System.out.println("One short known: " + oneshort);
        char[] alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`~!@#$%^&*()-_=+[]{}\\|;:'\",./<>? 0123456789".toCharArray();
        dictionary = new String[alpha.length];
        for(int i = 0; i < alpha.length; i++)
        {
            dictionary[i] = Base64.getEncoder().encodeToString(encrypt((oneshort + alpha[i]).getBytes()));
            DICT.put(Base64.getEncoder().encodeToString(encrypt((oneshort + alpha[i]).getBytes())).substring(0,16), "" + alpha[i]);
            DICT2.put(slice(encrypt((oneshort + alpha[i]).getBytes()),16), "" + alpha[i]);
        }
        System.out.println(Arrays.toString(dictionary));
        String b64 = Base64.getEncoder().encodeToString(encrypt(oneshort.getBytes()));
        System.out.println(b64);
        //step5(b64);
    }

    public static Hashtable<String,String> buildDict(String msg, int blocksize)
    {
        Hashtable<String,String> dict = new Hashtable<>();
        String oneshort = msg.substring(0,blocksize - 1);
        String b64 = Base64.getEncoder().encodeToString(encrypt(oneshort.getBytes()));
        String b64short = b64.substring(0,b64.length() - 3);
        for(int i = 0; i < 128; i++)
        {
            dict.put(Base64.getEncoder().encodeToString(encrypt((oneshort + (char)i).getBytes())).substring(0,b64short.length()),"" + (char)i);
        }
        b64 = Base64.getEncoder().encodeToString(encrypt(oneshort.getBytes()));
        System.out.println(b64);
        System.out.println(b64.length());
        return dict;
        //step5(b64);
    }

    public static byte[] slice(byte[] input, int size)
    {
        assert size <= input.length : "size out of bounds";
        byte[] ret = new byte[size];
        for(int i = 0; i < ret.length; i++)
            ret[i] = input[i];
        return ret;
    }

    public static String step5(String msg, int blocksize)
    {
        String known = "";
        while(true)
        {
            Hashtable<String,String> dict = buildDict(msg,blocksize);
            String b64 = Base64.getEncoder().encodeToString(encrypt(msg.substring(0,blocksize - 1).getBytes()));
            String b64short = b64.substring(0, b64.length() - 3);
            if(dict.get(b64short) != null)
            {
                known += dict.get(b64short);
                if(known.length() < blocksize)
                {
                    msg = "";
                    for(int i = 0; i < blocksize; i++)
                        msg += dict.get(b64short);
                }
                else
                    msg = known.substring(known.length() - blocksize, known.length() + blocksize);
            }
            else
                return known;
        }
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

    public static void main(String[] args)
    {
        HexToBase64 htb = new HexToBase64();
        byte[] con = concat(KNOWN.getBytes(), Base64.getDecoder().decode(UNKNOWN));
        byte[] enc = encrypt(con);
        System.out.println(Base64.getEncoder().encodeToString(enc));
        System.out.println("Blocksize: " + Challenge12.step1a(KNOWN));
        String b64 = Base64.getEncoder().encodeToString(encrypt(KNOWN.substring(0,15).getBytes()));
        System.out.println("Running in ECB? " + detectECB(con));
        buildDict(KNOWN,16);
        step5(KNOWN,16);
    }
}
