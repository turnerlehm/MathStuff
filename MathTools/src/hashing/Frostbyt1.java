package hashing;

import java.util.Arrays;

public class Frostbyt1
{
    private int s0 = 0xBEEFBEEF;
    private int s1 = 0xBEEFDEAD;
    private int s2 = 0xDEADBEEF;
    private int s3 = 0xDEADDEAD;

    private void round1()
    {
        s0 = (s0 << 23) | (s0 >> 9);
        s1 = (s1 << 3) | (s1 >> 29);
        s0 += s3;
        s1 += s2;
        s0 ^= s2;
        s1 ^= s3;
        s0 = (s0 << 7) | (s0 >> 25);
        s2 = (s2 << 19) | (s2 >> 13);
        s3 = (s3 << 29) | (s3 >> 3);
        s2 += s1;
        s3 += s0;
        s3 ^= s0;
        s2 = (s2 << 11) | (s2 >> 21);
        s0 = (s0 << 5) | (s0 >> 27);
        s1 = (s1 << 17) | (s1 >> 15);
        s0 ^= s3;
        s1 ^= s2;
        s0 = (s0 << 23) | (s0 >> 9);
        s2 = (s2 << 13) | (s2 >> 19);
        s3 = (s3 << 31) | (s3 >> 1);
        s3 += s0;
        s2 += s0;
        s3 += s1;
        /*
        mix1();
        mix2();
        mix1();
        mix1();
        mix2();
        mix2();
        mix1();
        mix2();
        */
    }

    private void round2()
    {
        mix2();
        mix1();
        mix2();
        mix1();
        mix2();
        mix2();
        mix1();
        mix1();
        mix2();
        mix1();
        mix2();
        mix1();
    }

    private void mix1()
    {
        s2 += s1;
        s1 = (s1 << 13) | (s1 >> 19);
        s2 ^= s1;
        s3 = (s3 << 17) | (s3 >> 15);
        s0 ^= s3;
        s2 = (s2 << 23) | (s2 >> 9);
        s0 += s2;
        s1 += s3;
    }

    private void mix2()
    {
        s0 ^= (s0 << 5) ^ (s0 >> 2) ^ s1;
        s1 += (s1 << 3) + (s1 >> 7) ^ s2;
        s2 ^= (s2 << 7) ^ (s2 >> 13) ^ s3;
        s3 += (s3 << 11) + (s3 >> 19) ^ s0;
        s0 += (s0 << 2) ^ (s0 >> 7) ^ s2;
        s1 ^= (s1 << 19) + (s1 >> 23) + s3;
        s2 += (s2 << 13) ^ (s2 >> 29) ^ s0;
        s3 ^= (s3 << 17) + (s3 >> 19) + s1;
    }

    public int sax3(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 5) ^ (h >> 2) ^ b;
        return h & Integer.MAX_VALUE;
    }

    public int hash1(byte[] key, int c, int d)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 7) ^ (h >> 4) ^ b;
        for(int i = 0; i < key.length; i++)
        {
            if(i % 2 == 0)
            {
                s0 ^= (s0 << 5) ^ (s0 >> 2) ^ key[i];
                s2 += (s2 << 5) + (s2 >> 2) ^ key[i];
            }
            else
            {
                s1 ^= (s1 << 5) ^ (s1 >> 2) ^ key[i];
                s3 += (s3 << 5) + (s3 >> 2) ^ key[i];
            }
        }
        h ^= s0;
        h += s1;
        h ^= s2;
        h += s3;
        for(int i = 0; i < c; i++)
            round1();
        h ^= s3;
        h += s0;
        h ^= s1;
        h += s2;
        for(byte b : key)
            h ^= (h << 5) ^ (h >> 2) ^ b;
        for(int i = 0; i < key.length; i++)
        {
            if(i % 2 == 0)
            {
                s1 ^= (s1 << 5) ^ (s1 >> 2) ^ key[i];
                s3 += (s3 << 5) + (s3 >> 2) ^ key[i];
            }
            else
            {
                s0 ^= (s0 << 5) ^ (s0 >> 2) ^ key[i];
                s2 += (s2 << 5) + (s2 >> 2) ^ key[i];
            }
        }
        h ^= s2;
        h += s3;
        h ^= s0;
        h += s1;
        for(int i = 0; i < d; i++)
            round1();
        h ^= s1;
        h += s2;
        h ^= s3;
        h += s0;
        h ^= s0 ^ s1 ^ s2 ^ s3;
        return h & Integer.MAX_VALUE;
    }

    public static void main(String[] args)
    {
        String[] students = new String[5000];
        for(int i = 0; i < students.length; i++)
            students[i] = "Student" + i;
        int[] map = new int[5000];
        Frostbyt1 F1 = new Frostbyt1();
        for(String S : students)
        {
            int idx = F1.hash1(S.getBytes(),2,4) % map.length;
            map[idx]++;
        }
        int buckets = 0;
        for(int i : map)
            if(i < 1)
                buckets++;
        System.out.println("Empty buckets: " + buckets);
        buckets = 0;
        for(int i : map)
            if(i > 1)
                buckets++;
        System.out.println("Overfull buckets: " + buckets);
        System.out.println(Arrays.toString(map));
        for(String S : students)
        {
            int idx = F1.sax3(S.getBytes()) % map.length;
            map[idx]++;
        }
        buckets = 0;
        for(int i : map)
            if(i < 1)
                buckets++;
        System.out.println("Empty buckets: " + buckets);
        buckets = 0;
        for(int i : map)
            if(i > 1)
                buckets++;
        System.out.println("Overfull buckets: " + buckets);
        System.out.println(Arrays.toString(map));
    }
}
