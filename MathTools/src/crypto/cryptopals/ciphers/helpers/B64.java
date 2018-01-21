package crypto.cryptopals.ciphers.helpers;

import java.util.Arrays;
import java.util.Base64;

public class B64
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
        return new String(dst);
    }

    public byte[] decode(String enc)
    {
        return Base64.getDecoder().decode(enc);
    }
}
