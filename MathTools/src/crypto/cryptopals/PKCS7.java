package crypto.cryptopals;

public class PKCS7
{
    public static String pad(String block, int blocksize)
    {
        int diff = blocksize - (block.length() % blocksize);
        byte b = (byte)diff;
        String s = block;
        for(int i = 0; i < diff; i++)
            s += (char)b;
        return s;
    }

    public static byte[] pad(byte[] block, int blocksize)
    {
        int diff = blocksize - (block.length % blocksize);
        byte b = (byte)diff;
        byte[] bytes = new byte[block.length + diff];
        for(int i = 0; i < block.length; i++)
            bytes[i] = block[i];
        for(int i = block.length; i < bytes.length; i++)
            bytes[i] = b;
        return bytes;
    }

    public static String pad16(byte[] block)
    {
        if(16 > block.length)
        {
            int diff = 16 - block.length;
            byte b = (byte)diff;
            String s = "";
            for(int i = 0; i < block.length; i++)
                s += (char)block[i];
            for(int i = 0; i < diff; i++)
                s += (char)b;
            return s;
        }
        return new String(block);
    }

    public static void main(String[] args)
    {
        System.out.println(pad("YELLOW SUBMARINE", 20));
    }
}
