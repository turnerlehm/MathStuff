package hashing;

import java.util.Arrays;

public class Prime
{
    private int s0 = 0xBEEFBEEF;
    private int s1 = 0xBEEFDEAD;
    private int s2 = 0xDEADBEEF;
    private int s3 = 0xDEADDEAD;

    private void round1()
    {
        s0 = (s0 << 5) | (s0 >> 27);
        s1 = (s1 << 3) | (s1 >> 29);
        s0 ^= s2;
        s1 ^= s3;
        s2 = (s2 << 11) | (s2 >> 21);
        s3 = (s3 << 7) | (s3 >> 25);
        s2 += s1;
        s3 += s0;
        s0 = (s0 << 23) | (s0 >> 9);
        s1 = (s1 << 17) | (s1 >> 15);
        s0 ^= s3;
        s1 ^= s2;
        s2 = (s2 << 13) | (s2 >> 19);
        s3 = (s3 << 19) | (s3 >> 13);
        s2 += s0;
        s3 += s1;
    }

    private void round2()
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

    public int hash1(byte[] msg, int c, int d)
    {
        int h = 0;
        for(byte b : msg)
            h ^= (h << 7) ^ (h >> 4) ^ b; //modified sax3 to init h
        for(int i = 0; i < msg.length; i++)
        {
            if(i % 2 == 0)
            {
                s0 ^= msg[i];
                s2 += msg[i];
            }
            else
            {
                s1 ^= msg[i];
                s3 += msg[i];
            }
        }
        h ^= s0;
        h += s1;
        h ^= s2;
        h += s3;
        for(int i = 0; i < c; i++)
            round2();
        h ^= s3;
        h += s0;
        h ^= s1;
        h += s2;
        for(byte b : msg)
            h ^= (h << 5) ^ (h >> 2) ^ b; //sax3
        for(int i = 0; i < msg.length; i++)
        {
            if(i % 2 == 0)
            {
                s1 ^= msg[i];
                s3 += msg[i];
            }
            else
            {
                s0 ^= msg[i];
                s2 += msg[i];
            }
        }
        h ^= s2;
        h += s3;
        h ^= s0;
        h += s1;
        for(int i = 0; i < d; i++)
            round2();
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
        Prime P = new Prime();
        for(String S : students)
        {
            int idx = P.hash1(S.getBytes(),2,4) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        int buckets = 0;
        for(int i : map)
            buckets += i > 1 ? 1 : 0;
        System.out.println("Buckets with more than 1 item: " + buckets);
    }
}
