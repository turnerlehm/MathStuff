package crypto.cryptopals;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class AESEcb
{
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, FileNotFoundException, BadPaddingException, IllegalBlockSizeException {
        Cipher c2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec("YELLOW SUBMARINE".getBytes(), "AES");
        c2.init(Cipher.DECRYPT_MODE, key);
        Scanner fin = new Scanner(new File("D:\\Projects\\COMPUTATIONS\\7.txt"));
        String enc = "";
        while(fin.hasNext())
            enc += fin.nextLine();
        byte[] pt = c2.doFinal(Base64.getDecoder().decode(enc));
        for(byte b : pt)
            System.out.print((char)b);
    }

    public static String decrypt(String key, String enc)
    {
        try
        {
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, k);
            byte[] b = c.doFinal(Base64.getDecoder().decode(enc));
            return new String(b);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String key, String msg)
    {
        try
        {
            SecretKeySpec k = new SecretKeySpec(key.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, k);
            byte[] e = c.doFinal(msg.getBytes());
            return Base64.getEncoder().encodeToString(e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
