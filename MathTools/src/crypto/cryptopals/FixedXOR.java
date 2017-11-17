package crypto.cryptopals;

import java.math.BigInteger;

public class FixedXOR
{

    /**
     * XORs two hex encoded strings of the same length
     * @param s1 the first hex encoded string
     * @param s2 the second hex encoded string
     * @return the hex encoded result of <code>s1 ^ s2</code>
     * @throws IllegalArgumentException if <code>s1.length() != s2.length()</code>
     */
    public String xor(String s1, String s2)
    {
        if(s1.length() != s2.length())
            throw new IllegalArgumentException("Strings must be the same length");
        byte[] b1 = new BigInteger(s1,16).toByteArray();
        byte[] b2 = new BigInteger(s2,16).toByteArray();
        int len = Math.max(b1.length, b2.length);
        byte[] res = new byte[len];
        for(int i = 0; i < len; i++)
            res[i] = (byte)(b1[i % b1.length] ^ b2[i % b2.length]);
        HexToBase64 encoder = new HexToBase64();
        return encoder.hexEncode(res);
    }

    public static void main(String[] args)
    {
        FixedXOR fx = new FixedXOR();
        System.out.println(fx.xor("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"));
        System.out.println("matches: " + fx.xor("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965").equals("746865206b696420646f6e277420706c6179"));
    }
}
