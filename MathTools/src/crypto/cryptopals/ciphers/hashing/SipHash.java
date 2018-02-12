package crypto.cryptopals.ciphers.hashing;

public class SipHash
{
    private long v0, v1, v2, v3;

    public long hash(int c, int d, byte[] key, byte[] msg)
    {
        long k0 = toLong(key, 0);
        long k1 = toLong(key, 8);
        //init
        long v0 = k0 ^ 0x736f6d6570736575L;
        long v1 = k1 ^ 0x646f72616e646f6dL;
        long v2 = k0 ^ 0x6c7967656e657261L;
        long v3 = k1 ^ 0x7465646279746573L;
        //compression
        int words = (int)Math.ceil((double)(msg.length + 1) / 8.0);
        long[] m = new long[words];
        for(int i = 0; i < words - 1; i++)
            m[i] = toLong(msg, 8*i);
        m[words - 1] = toLong(msg,0);
        m[words - 1] |= msg.length % 256;
        for(long l : m)
            v3 ^= l;
        for(int i = 0; i < c; i++)
            sipround();
        for(long l : m)
            v0 ^= l;
        v2 ^= 0xFF;
        for(int i = 0; i < d; i++)
            sipround();
        return v0 ^ v1 ^ v2 ^ v3;
    }

    private long toLong(byte[] key, int off)
    {
        long res = 0;
        for(int i = off, c = 0; i < key.length && c < 8; i++, c++)
        {
            res <<= 8;
            res |= key[i] & 0xFF;
        }
        return res;
    }

    private void sipround()
    {
        v0 += v1;
        v2 += v3;
        v1 = (v1 << 13) | (v1 >> 19);
        v3 = (v3 << 16) | (v3 >> 16);
        v1 ^= v0;
        v3 ^= v2;
        v0 = (v0 << 32) | v0;
        v2 += v1;
        v0 += v3;
        v1 = (v1 << 17) | (v1 >> 15);
        v3 = (v3 << 21) | (v3 >> 11);
        v1 ^= v2;
        v3 ^= v0;
        v2 = (v2 << 32) | v2;
    }

    public static void main(String[] args)
    {
        SipHash S = new SipHash();
        byte[] M = "YELLOW SUBMARINE".getBytes();
        byte[] K = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        System.out.println(S.hash(100,57,K,M));

    }
}
