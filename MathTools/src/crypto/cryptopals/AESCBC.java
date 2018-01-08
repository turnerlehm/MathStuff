package crypto.cryptopals;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESCBC
{
    /**
     * Do one round of AES CBC encryption
     * @param key
     * @param block
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] key, byte[] block) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        if(block.length % 16 != 0)
            block = PKCS7.pad(block, 16);
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] enc = c.doFinal(block);
        return enc;
    }

    public static byte[] decrypt(byte[] key, byte[] block) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
        //block = PKCS7.pad(block, 16);
        c.init(Cipher.DECRYPT_MODE, k);
        //byte[] bytes = Base64.getDecoder().decode(block);
        byte[] b = c.doFinal(block);
        return b;
    }

    public static String cheatEncrypt(String key, String IV, String msg)
    {
        try
        {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, k, iv);
            byte[] enc = c.doFinal(msg.getBytes());
            return Base64.getEncoder().encodeToString(enc);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String cheatDecrypt(String key, String IV, byte[] ct)
    {
        try
        {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, k, iv);
            byte[] p = c.doFinal(ct);
            return new String(p);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptCBC(String key, String IV, String msg) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        if(msg.length() % 16 != 0) //make sure correct length
            msg = PKCS7.pad(msg, 16);
        String first = msg.substring(0,16);
        byte[] input = xor(first.getBytes(), IV.getBytes());
        byte[] iv = encrypt(key.getBytes(), input);
        for(int i = 16; i < msg.length(); i += 16)
        {
            first = msg.substring(i, i + 16);
            input = xor(first.getBytes(), iv);
            iv = encrypt(key.getBytes(), input);
        }
        return iv;
    }

    public static String decryptCBC(String key, String IV, byte[] enc) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] b = new byte[16];
        int i;
        for(i = 0; i < 16; i++)
            b[i] = enc[i];
        byte[] dec = decrypt(key.getBytes(), b);
        String plain = new String(xor(IV.getBytes(),dec));
        for(int k = 1; k < enc.length / 16; k++)
        {
            byte[] temp = new byte[16];
            for(int j = 0; j < temp.length; j++)
                temp[j] = enc[i++];
            dec = decrypt(key.getBytes(), temp);
            plain += new String(xor(b,dec));
            b = temp;
        }
        return plain;
    }

    private static byte[] xor(byte[] b1, byte[] b2)
    {
        int len = Math.max(b1.length, b2.length);
        byte[] res = new byte[len];
        for(int i = 0; i < len; i++)
            res[i] = (byte)(b1[i % b1.length] ^ b2[i % b2.length]);
        return res;
    }

    public static void main(String[] args) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException
    {
        byte b = 0;
        String IV = "";
        for(int i = 0; i < 16; i++)
            IV += (char)b;
        byte[] enc = encryptCBC("YELLOW SUBMARINE", IV,"YELLOW SUBMARINEYELLOW SUBMARINE");
        System.out.println(Base64.getEncoder().encodeToString(enc) + " Length: " + enc.length);
        String e = cheatEncrypt("YELLOW SUBMARINE", IV, "YELLOW SUBMARINEYELLOW SUBMARINE");
        System.out.println(e + " Length: " + e.length());
        String dec = decryptCBC("YELLOW SUBMARINE", IV,enc);
        System.out.println(dec);
        //String dec = decrypt("YELLOW SUBMARINE", enc);
        //System.out.println(dec);
    }
}
