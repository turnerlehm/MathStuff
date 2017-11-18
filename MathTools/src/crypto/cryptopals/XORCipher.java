package crypto.cryptopals;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class XORCipher
{

    public char[] encrypt(String key, String plain)
    {
        char[] k = key.toCharArray();
        char[] pt = plain.toCharArray();
        for(int i = 0; i < pt.length; i++)
            pt[i] = (char)(pt[i] ^ k[i % k.length]);
        return pt;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String plain = "Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal";
        String ct = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
        //BigInteger temp = new BigInteger(ct,16);
        //ct = temp.toString(16);
        String key =  "ICE";
        XORCipher xc = new XORCipher();
        char[] enc = xc.encrypt(key,plain);

    }
}
