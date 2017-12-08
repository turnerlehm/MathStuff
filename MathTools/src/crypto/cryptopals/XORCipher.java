package crypto.cryptopals;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class XORCipher
{

    public byte[] encrypt(String key, String plain)
    {
        byte[] k = key.getBytes();
        byte[] pt = plain.getBytes();
        for(int i = 0; i < pt.length; i++)
            pt[i] = (byte)(pt[i] ^ k[i % k.length]);
        return pt;
    }

    private static String buildKey(String key, int n)
    {
        String k = key;
        for(int i = 0; i < n; i++)
            k += key;
        return k;
    }

    public char[] encrypt2(String key, String text)
    {
        String s = "";
        char[] plain = text.toUpperCase().toCharArray();
        char[] k = key.toCharArray();
        for(int i =0, j = 0; i < plain.length; i++)
        {
            char c = plain[i];
            //if((c < 'A' || c > 'Z') && (c < 'a' || c > 'z'))
               // continue;
            s += (char)(c ^ k[j]);
            j = ++j % k.length;
        }
        return s.toCharArray();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String plain = "Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal";
        String ct = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
        //BigInteger temp = new BigInteger(ct,16);
        //ct = temp.toString(16);
        String key =  "ICE";
        XORCipher xc = new XORCipher();
        HexToBase64 encoder = new HexToBase64();
        byte[] cipher = xc.encrypt(key,plain);
        String enc = encoder.hexEncode(cipher);
        System.out.println(enc);
        System.out.println("matches: " + enc.equals(ct));
    }
}
