package crypto.cryptopals.ciphers.helpers;

public class PKCS7Padding
{
    public static byte[] pad(byte[] m, int blocksize)
    {
        int diff = blocksize - (m.length % blocksize);
        byte b = (byte)diff;
        byte[] bytes = new byte[m.length + diff];
        for(int i = 0; i < m.length; i++)
            bytes[i] = m[i];
        for(int i = m.length; i < bytes.length; i++)
            bytes[i] = b;
        return bytes;
    }

    public static String pad(String m, int blocksize)
    {
        int diff = blocksize - (m.length() % blocksize);
        byte b = (byte)diff;
        String s = m;
        for(int i = 0; i < diff; i++)
            s += (char)b;
        return s;
    }
}
