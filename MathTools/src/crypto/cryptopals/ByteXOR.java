package crypto.cryptopals;

import java.math.BigInteger;

public class ByteXOR
{
    public String xor(byte b, String s1)
    {
        HexToBase64 encoder = new HexToBase64();
        byte[] bytes = encoder.hexDecode(s1);
        for(int i = 0; i < bytes.length; i++)
            bytes[i] ^= b;
        return encoder.hexEncode(bytes);
    }
}
