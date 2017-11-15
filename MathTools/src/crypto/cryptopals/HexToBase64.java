package crypto.cryptopals;

import java.math.BigInteger;
import java.util.Base64;

public class HexToBase64
{
    private static final byte[] ENCODING = {65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,
    97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,
    48,49,50,51,52,53,54,55,56,57,43,47};

    public static void main(String... args)
    {
        System.out.println(ENCODING.length);
        for(byte b : ENCODING)
            System.out.println((char)b);
        HexToBase64 encoder = new HexToBase64();
        System.out.println(encoder.cheatEncode("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
        boolean match = encoder.cheatEncode("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d").equals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t");
        System.out.println("matches: " + match);
    }

    //parts of implementation borrowed from https://rosettacode.org/wiki/Base64_encode_data
    public String toBase64(String hex)
    {
        byte[] hexbytes = hex.getBytes();
        StringBuilder sb = new StringBuilder();
        int blocks = 0;
        for(int i = hexbytes.length - 1; i >= 3; i -= 3)
        {
            int b1 = hexbytes[i];
            int b2 = hexbytes[i-1];
            int b3 = hexbytes[i-2];
            if(b1 == -1)
                break;
            int block = (b1 & 0xff) << 16 | (b2 & 0xff) << 8 | (b3 & 0xff);
            sb.append((char)ENCODING[block >> 18 & 63]); //get first 6 bits from 3 byte block
            sb.append((char)ENCODING[block >> 12 & 63]); //get next 6 bits from 3 byte block
            if(b2 == -1)
                sb.append('='); //pad is no more input, otherwise append next 6 bits from block
            else
                sb.append((char)ENCODING[block >> 6 & 63]);
            if(b3 == -1)
                sb.append('='); //pad if nor more input, otherwise append last 6 bits from block
            else
                sb.append((char)ENCODING[block & 63]);
        }
        return sb.toString();
    }

    //implementation borrowed from https://gist.github.com/EmilHernvall/953733
    public String hexToBase64(String hex)
    {
        byte[] data = hex.getBytes();
        StringBuilder sb = new StringBuilder();
        int pad = 0;
        for(int i = 0; i < data.length; i += 3)
        {
            int b = ((data[i] & 0xff) << 16) & 0xffffff;
            if(i + 1 < data.length)
                b |= (data[i+1] & 0xff) << 8;
            else
                pad++;
            if(i + 2 < data.length)
                b |= (data[i+2] & 0xff);
            else
                pad++;
            for(int j = 0; j < 4 - pad; j++)
            {
                int c = (b & 0xfc0000) >> 18;
                sb.append((char)ENCODING[c]);
                b <<= 6;
            }
            for(int j = 0; j < pad; j++)
                sb.append('=');
        }
        return sb.toString();
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
