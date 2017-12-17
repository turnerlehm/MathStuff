package crypto.cryptopals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

public class HexToBase64
{
    private static final byte[] ENCODING = {65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,
    97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,
    48,49,50,51,52,53,54,55,56,57,43,47};
    private static final int[] DECODING = new int[256];
    static
    {
        Arrays.fill(DECODING, -1);
        for(int i = 0; i < ENCODING.length; i++)
            DECODING[ENCODING[i]] = i;
        DECODING['='] = -2;
    }
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    public static void main(String... args)
    {
        System.out.println(ENCODING.length);
        for(byte b : ENCODING)
            System.out.println((char)b);
        HexToBase64 encoder = new HexToBase64();
        System.out.println(encoder.encode(new BigInteger("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d", 16).toByteArray()));
        boolean match = encoder.encode(new BigInteger("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d", 16).toByteArray()).equals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t");
        System.out.println("matches: " + match);
    }

    /**
     * Based on the OpenJDK 9 implementation, but modified specifically for only RFC4648 encoding
     * @param src
     * @return
     */
    @SuppressWarnings("deprecation")
    public String encode(byte[] src)
    {
        int len = 4 * ((src.length + 2) / 3);
        byte[] dst = new byte[len];
        int slen = src.length;
        int i;
        int j;
        int pad = 0;
        for(i = 0, j = 0; i < slen; i += 3)
        {
            int block = (src[i] & 0xff) << 16;
            if(i + 1 < slen)
                block |= (src[i+1] & 0xff) << 8;
            else
                pad++;
            if(i + 2 < slen)
                block |= (src[i+2] & 0xff);
            else
                pad++;
            dst[j++] = ENCODING[(block >>> 18) & 0x3f];
            dst[j++] = ENCODING[(block >>> 12) & 0x3f];
            dst[j++] = ENCODING[(block >>> 6) & 0x3f];
            dst[j++] = ENCODING[block & 0x3f];
        }
        if(pad > 0) // 1 or 2 leftover bytes
        {
            int b0 = src[i++] & 0xff;
            dst[j++] = ENCODING[b0 >> 2];
            if(pad == 2)
            {
                dst[j++] = ENCODING[(b0 << 4) & 0x3f];
                dst[j++] = '=';
                dst[j++] = '=';
            }
            else
            {
                int b1 = src[i++] & 0xff;
                dst[j++] = ENCODING[(b0 << 4) & 0x3f | (b1 >> 4)];
                dst[j++] = ENCODING[(b1 << 2) & 0x3f];
                dst[j++] = '=';
            }
        }
        return new String(dst,0,0,dst.length);
    }

    public byte[] decode(String s)
    {
        return Base64.getDecoder().decode(s);
    }

    public String hexEncode2(byte[] src)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < src.length; i++)
        {
            sb.append(Character.forDigit((src[i] & 0xF) >> 4, 16));
            sb.append(Character.forDigit(src[i] & 0xF, 16));
        }
        return sb.toString();
    }

    public String hexEncode(byte[] src)
    {
        char[] chars = new char[src.length * 2];
        for(int i = 0; i < src.length; i++)
        {
            chars[2 * i] = HEX[(src[i] >> 4) & 0xF];
            chars[2 * i + 1] = HEX[src[i] & 0xF];
        }
        return new String(chars);
    }

    public byte[] hexDecode(String hex)
    {
        return new BigInteger(hex,16).toByteArray();
    }

    public byte[] hexDecode2(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < hex.length() - 1; i += 2)
            bytes[i / 2] = (byte)((Character.digit(hex.charAt(i),16) << 4) + Character.digit(hex.charAt(i + 1),16));
        return bytes;
    }

    public byte[] fromBase64(String b64)
    {
        return null;
    }

    public String cheatEncode(String hex)
    {
        return Base64.getEncoder().encodeToString(new BigInteger(hex,16).toByteArray());
    }

    public byte[] cheatDecode(String b64)
    {
        return Base64.getDecoder().decode(b64);
    }
}
