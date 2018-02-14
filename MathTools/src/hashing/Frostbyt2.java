package hashing;

import java.util.Arrays;

public class Frostbyt2 
{
	private int s0 = 0xBEEFBEEF;
    private int s1 = 0xBEEFDEAD;
    private int s2 = 0xDEADBEEF;
    private int s3 = 0xDEADDEAD;
    
    private void round()
    {
    	s0 ^= (s0 << 5) ^ (s0 >> 2) ^ s1;
    	s1 ^= (s1 << 7) ^ (s1 >> 3) ^ s2;
    	s0 += s2;
    	s1 += s3;
    	s2 ^= (s2 << 11) ^ (s2 >> 5) ^ s3;
    	s3 ^= (s3 << 13) ^ (s3 >> 7) ^ s0;
    	s2 += s3;
    	s3 += s0;
    	s0 ^= (s0 << 17) ^ (s0 >> 11) ^ s2;
    	s1 ^= (s1 << 19) ^ (s1 >> 13) ^ s3;
    	s0 += s1;
    	s1 += s2;
    	s2 ^= (s2 << 23) ^ (s2 >> 17) ^ s0;
    	s3 ^= (s3 << 29) ^ (s3 >> 19) ^ s1;
    	s2 += s3;
    	s3 += s0;
    	s0 ^= (s0 << 31) ^ (s0 >> 23) ^ s3;
    	s1 ^= (s1 << 2) ^ (s1 >> 29) ^ s2;
    	s0 += s3;
    	s1 += s2;
    	s2 ^= (s2 << 3) ^ (s2 >> 31) ^ s3;
    	s3 ^= (s3 << 5) ^ (s3 >> 2) ^ s0;
    	s2 += s1;
    	s3 += s0;
    }
    
    public int hash(byte[] key, int c, int d)
    {
    	int h = 0;
    	for(byte b : key)
    		h ^= (h << 5) ^ (h >> 2) ^ b;
    	for(int i = 0; i < key.length; i++)
    	{
    		if(i % 2 == 0)
    		{
    			s0 ^= (s0 << 5) ^ (s0 >> 2) ^ key[i];
    			s1 += (s1 << 5) + (s1 >> 2) ^ key[i];
    		}
    		else
    		{
    			s2 ^= (s2 << 5) ^ (s2 >> 2) ^ key[i];
    			s3 += (s3 << 5) ^ (s3 >> 2) ^ key[i];
    		}
    	}
    	h ^= s0;
        h += s1;
        h ^= s2;
        h += s3;
        for(int i = 0; i < c; i++)
        	round();
        h ^= s3;
        h += s0;
        h ^= s1;
        h += s2;
        for(int i = 0; i < key.length; i++)
        {
        	if(i % 2 == 0)
        	{
        		s3 ^= (s3 << 5) ^ (s3 >> 2) ^ key[i];
        		s2 += (s2 << 5) + (s2 >> 2) ^ key[i];
        	}
        	else
        	{
        		s1 ^= (s1 << 5) ^ (s1 >> 2) ^ key[i];
        		s0 += (s0 << 5) + (s0 >> 2) ^ key[i];
        	}
        }
        h ^= s2;
        h += s3;
        h ^= s0;
        h += s1;
        for(int i = 0; i < d; i++)
        	round();
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
        Frostbyt2 F2 = new Frostbyt2();
        for(String S : students)
        {
        	int idx = F2.hash(S.getBytes(), 4, 8) % map.length;
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
    }
}
