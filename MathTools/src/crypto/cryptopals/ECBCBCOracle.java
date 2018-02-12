package crypto.cryptopals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class ECBCBCOracle
{
    private static Random RNG = new SecureRandom();

    private static byte[] genKey()
    {
        byte[] key = new byte[16];
        RNG.nextBytes(key);
        return key;
    }

    public static byte[] encryptionOracle(byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        int before = RNG.nextInt(6) + 5;
        int after = RNG.nextInt(6) + 5;
        byte[] b4 = new byte[before];
        byte[] aft = new byte[after];
        byte[] res = new byte[before + input.length + after];
        RNG.nextBytes(b4);
        RNG.nextBytes(aft);
        for(int i = 0; i < before; i++)
            res[i] = b4[i];
        int j = 0;
        for(int i = before; i < input.length + before; i++)
            res[i] = input[j++];
        for(int i = 0; i < after; i++)
            res[i + input.length + before] = aft[i];
        int coin = RNG.nextInt(2);
        byte[] key = genKey();
        res = PKCS7.pad(res, 16);
        byte[] enc;
        if(coin == 0) //ECB
        {
            Cipher C = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec K = new SecretKeySpec(key, "AES");
            C.init(Cipher.ENCRYPT_MODE, K);
            enc = C.doFinal(res);
        }
        else //CBC
        {
            byte[] IV = new byte[16];
            RNG.nextBytes(IV);
            enc = AESCBC.encryptCBC(key,IV,res);
        }
        //encrypt once more under known ECB
        //XOR the results to detect ECB
        Cipher C = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec K = new SecretKeySpec(key, "AES");
        C.init(Cipher.ENCRYPT_MODE, K);
        byte[] temp = C.doFinal(res);
        if(temp.length == enc.length)
        {
            int sum = 0;
            for(int i = 0; i < enc.length; i++)
                sum |= temp[i] ^ enc[i];
            System.out.println(sum == 0 ? "Running in ECB" : "Running in CBC");
        }
        else
            System.out.println("Running in CBC");
        return enc;
    }

    public static int repeatedBlocks(String msg, int blen)
    {
        HashMap<String,Integer> map = new HashMap<>();
        for(int i = 0; i < msg.length(); i += blen)
        {
            String block = msg.substring(i, i + blen);
            Integer count = map.get(block);
            if(count == null)
                count = 0;
            map.put(block, count + 1);
        }
        int sum = 0;
        for(int i = 0; i < msg.length(); i += blen)
        {
            String block = msg.substring(i, i + blen);
            Integer count = map.get(block);
            sum += count;
        }
        return sum;
    }

    public static boolean detection(byte[] msg) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] b1 = encryptionOracle(msg);
        byte[] b2 = encryptionOracle(msg);
        int sum = 0;
        int len = Math.max(b1.length, b2.length);
        for(int i = 0; i < len; i++)
            sum += (b1[i % b1.length] ^ b2[i % b2.length]) == 0 ? 1 : 0;
        return sum >= msg.length;
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        byte[] E = ECBCBCOracle.encryptionOracle("YELLOW SUBMARINE".getBytes());
        HexToBase64 htb = new HexToBase64();
        String hex = htb.hexEncode(E);
        //System.out.println(detection("YELLOW SUBMARINE".getBytes()));
        System.out.println(hex);
        //System.out.println(repeatedBlocks(hex,4));
        //System.out.println(hex.length() / 4);
        //System.out.println(repeatedBlocks(hex,4) > hex.length() / 4 ? "ECB" : "CBC");
    }
}
