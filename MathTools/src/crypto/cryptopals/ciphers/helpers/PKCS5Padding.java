package crypto.cryptopals.ciphers.helpers;

public class PKCS5Padding
{
    public static String pad(String src)
    {
        int diff = 8 - (src.length() % 8);
        byte b = (byte)diff;
        String S = src;
        for(int i = 0; i < diff; i++)
            S += (char)b;
        return S;
    }

    public static byte[] pad(byte[] m)
    {
        int diff = 8 - (m.length % 8);
        byte b = (byte)diff;
        byte[] bytes = new byte[m.length + diff];
        for(int i = 0; i < m.length; i++)
            bytes[i] = m[i];
        for(int i = m.length; i < bytes.length; i++)
            bytes[i] = b;
        return bytes;
    }
}
