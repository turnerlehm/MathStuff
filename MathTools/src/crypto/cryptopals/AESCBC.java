package crypto.cryptopals;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

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

    public static byte[] encrypt(byte[] key, byte[] IV, byte[] msg)
    {
        try
        {
            IvParameterSpec iv = new IvParameterSpec(IV);
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, k, iv);
            byte[] enc = c.update(msg);
            return enc;
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

    public static byte[] encryptCBC(byte[] key, byte[] IV, byte[] msg) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        if(msg.length % 16 != 0)
            msg = PKCS7.pad(msg, 16);
        byte[] first = xor(IV, Arrays.copyOfRange(msg,0,16));
        byte[] enc = encrypt(key, IV, first);
        ByteArrayOutputStream B = new ByteArrayOutputStream();
        B.write(enc);
        for(int i = 16; i < msg.length; i += 16)
        {
            byte[] pt = Arrays.copyOfRange(msg,i,i+16);
            first = xor(enc,pt);
            enc = encrypt(key, IV, first);
            B.write(enc);
        }
        return B.toByteArray();
    }

    public static String decryptCBC(byte[] key, byte[] IV, byte[] enc) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        byte[] b = new byte[16];
        int i;
        for(i = 0; i < 16; i++)
            b[i] = enc[i];
        byte[] dec = decrypt(key, b);
        String plain = new String(xor(IV,dec));
        for(int k = 1; k < enc.length / 16; k++)
        {
            byte[] temp = new byte[16];
            for(int j = 0; j < temp.length; j++)
                temp[j] = enc[i++];
            dec = decrypt(key, temp);
            plain += new String(xor(b,dec));
            b = temp;
        }
        return plain;
    }

    private static byte[] xor(byte[] b1, byte[] b2)
    {
        int len = Math.max(b1.length, b2.length);
        if(b1.length % len != 0)
            b1 = PKCS7.pad(b1,len);
        else if(b2.length % len != 0)
            b2 = PKCS7.pad(b2,len);
        byte[] res = new byte[len];
        for(int i = 0; i < len; i++)
            res[i] = (byte)(b1[i % b1.length] ^ b2[i % b2.length]);
        return res;
    }

    public static void main(String[] args) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, IOException {
        byte b = 0;
        byte[] IV = new byte[16];
        for(int i = 0; i < 16; i++)
            IV[i] = 0;
        byte[] enc = encryptCBC("YELLOW SUBMARINE".getBytes(), IV,"YELLOW SUBMARINEYELLOW SUBMARINE".getBytes());
        System.out.println(Base64.getEncoder().encodeToString(enc) + " Length: " + enc.length);
        String e = cheatEncrypt("YELLOW SUBMARINE", new String(IV), "YELLOW SUBMARINEYELLOW SUBMARINE");
        System.out.println(e + " Length: " + e.length());
        String dec = decryptCBC("YELLOW SUBMARINE".getBytes(), IV,enc);
        System.out.println(dec);
        Scanner fin = new Scanner(new File("D:\\Projects\\COMPUTATIONS\\10.txt"));
        String pt = "";
        String ct = "";
        while(fin.hasNext())
        {
            ct += fin.nextLine();
        }
        pt = decryptCBC("YELLOW SUBMARINE".getBytes(),IV,Base64.getDecoder().decode(ct));
        System.out.println(pt);
        //String dec = decrypt("YELLOW SUBMARINE", enc);
        //System.out.println(dec);
    }
}
