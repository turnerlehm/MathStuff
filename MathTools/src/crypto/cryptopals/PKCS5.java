package crypto.cryptopals;

public class PKCS5
{
    public static String pad(String block)
    {
        if(8 > block.length())
        {
            int diff = 8 - block.length();
            byte b = (byte)diff;
            String s = block;
            for(int i = 0; i < diff; i++)
                s += (char)b;
            return s;
        }
        return block;
    }

    public static byte[] pad(byte[] block)
    {
        int diff = 8 - (block.length % 8);
        byte b = (byte)diff;
        byte[] bytes = new byte[block.length + diff];
        for(int i = 0; i < block.length; i++)
            bytes[i] = block[i];
        for(int i = block.length; i < bytes.length; i++)
            bytes[i] = b;
        return bytes;
    }
}
