package crypto.cryptopals;

import java.math.BigInteger;

public class ByteXOR
{
    public String xor(byte b, String s1)
    {
        byte[] bytes = new BigInteger(s1,16).toByteArray();
        for(int i = 0; i < bytes.length; i++)
            bytes[i] ^= b;
        HexToBase64 encoder = new HexToBase64();
        return encoder.hexEncode(bytes);
    }
}
