package hashing;

import java.util.Arrays;

public class Linear
{
    private int s0 = 0xBEEFBEEF;
    private int s1 = 0xBEEFDEAD;
    private int s2 = 0xDEADBEEF;
    private int s3 = 0xDEADDEAD;

    private void round1()
    {
        s0 += s3;
        s1 += s2;
        s0 = (s0 << 7) | (s0 >> 25);
        s3 ^= s0;
        s2 = (s2 << 11) | (s2 >> 21);
        s1 ^= s2;
        s0 = (s0 << 23) | (s0 >> 9);
        s3 += s0;
        s2 += s1;
        s1 = (s1 << 13) | (s1 >> 19);
        s2 ^= s1;
        s3 = (s3 << 17) | (s3 >> 15);
        s0 ^= s3;
        s2 = (s2 << 23) | (s2 >> 9);
        s0 += s2;
        s1 += s3;
    }

    private void round2()
    {
        s0 ^= s3;
        s1 ^= s2;
        s0 = (s0 << 7) | (s0 >> 25);
        s3 += s0;
        s2 = (s2 << 11) | (s2 >> 21);
        s1 += s2;
        s0 = (s0 << 23) | (s0 >> 9);
        s3 ^= s0;
        s2 ^= s1;
        s1 = (s1 << 13) | (s1 >> 19);
        s2 += s1;
        s3 = (s3 << 17) | (s3 >> 15);
        s0 += s3;
        s3 = (s3 << 23) | (s2 >> 9);
        s0 ^= s2;
        s1 ^= s3;
    }


    public int hash(byte[] key, int c, int f)
    {
        int h = 0xB0B5BEEF;
        h ^= s0 ^ linear1(key);
        h += s1 + sax3(key);
        h ^= s2 ^ fnv(key);
        h *= s3 * bernstein2(key);
        s0 ^= linear1(key);
        s1 += sax3(key);
        s2 ^= fnv(key);
        s3 *= bernstein2(key);
        for(int i = 0; i < c; i++)
            round1();
        h ^= s0;
        h += s1;
        h ^= s2;
        h += s3;
        for(byte b : key)
            s0 ^= s3 ^ b;
        for(byte b : key)
            s1 += s2 ^ b;
        for(int i = 0; i < f; i++)
            round1();
        h ^= s0;
        h += s1;
        h ^= s2;
        h += s3;
        return (h * (s0 ^ s1 ^ s2 ^ s3)) & Integer.MAX_VALUE;
    }

    public int hash2(byte[] key, int c, int f)
    {
        int h = 0;
        h = s0 * linear1(key);
        h ^= s1 ^ sax3(key);
        h += s2 + fnv(key);
        h *= s3 ^ bernstein2(key);
        for(int i = 0; i < c; i++)
        {
            s0 ^= (s0 << 3) ^ (s0 >> 5) ^ sax3(key);
            s1 ^= (s1 << 5) ^ (s1 >> 2) ^ bernstein2(key);
            s2 ^= (s2 << 7) ^ (s2 >> 5) ^ sax3(key);
            s3 ^= (s3 << 11) ^ (s3 >> 4) ^ bernstein2(key);
        }
        h ^= s0 ^ s2;
        h += s1 + s3;
        h = ((h << 17) | (h >> 15)) ^ s0;
        h += s1 ^ s3;
        h ^= s0 + s2;
        h = ((h << 13) | (h >> 19)) ^ s1;
        h ^= s0 ^ s1;
        h += s2 + s3;
        h = ((h << 19) | (h >> 13)) ^ s2;
        h += s0 ^ s3;
        h ^= s1 + s2;
        h = ((h << 11) | (h >> 21)) ^ s3;
        for(int i = 0; i < f; i++)
        {
            s0 += (s0 << 23) ^ (s0 >> 19) + sax3(key);
            s1 ^= (s1 << 19) + (s1 >> 17) ^ bernstein2(key);
            s2 += (s2 << 17) ^ (s2 >> 13) + sax3(key);
            s3 ^= (s3 << 13) + (s3 >> 11) ^ bernstein2(key);
        }
        h ^= s0 ^ s2;
        h += s1 + s3;
        h = ((h << 5) | (h >> 27)) ^ s0;
        h += s1 ^ s3;
        h ^= s0 + s2;
        h = ((h << 3) | (h >> 29)) ^ s1;
        h ^= s0 ^ s1;
        h += s2 + s3;
        h = ((h << 7) | (h >> 25)) ^ s2;
        h += s0 ^ s3;
        h ^= s1 + s2;
        h = ((h << 2) | (h >> 30)) ^ s3;
        return h & Integer.MAX_VALUE;
    }

    private int linear1(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= 934646797 * b + 1806593049 + b;
        return h & Integer.MAX_VALUE;
    }

    private int sax3(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 5) ^ (h >> 2) ^ b;
        return h & Integer.MAX_VALUE;
    }

    private int fnv(byte[] key)
    {
        long h = 2166136261L;
        for(byte b : key)
            h = (h * 16777619) ^ b;
        return ((int)h)&Integer.MAX_VALUE;
    }

    private int bernstein2(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h = 33 * h ^ b;
        return h & Integer.MAX_VALUE;
    }

    public static void main(String[] args)
    {
        Linear L = new Linear();
        byte[] key = "YELLOW SUBMARINE".getBytes();
        String[] students = new String[5000];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student" + i;
        System.out.println(L.hash2(key,2,4));
        int[] map = new int[5000];
        for(String s : students)
        {
            int idx = L.hash2(s.getBytes(),2,4) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        int buckets = 0;
        for(int i : map)
            buckets += i > 1 ? 1 : 0;
        System.out.println("Buckets with more than 1 item: " + buckets);
    }
}
