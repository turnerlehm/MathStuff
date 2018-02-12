package hashing;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HashFunctionTester
{
	static BigInteger combos = BigInteger.valueOf(127L).pow(64);
	static BigInteger p1 = combos.nextProbablePrime();
	static BigInteger p2 = BigInteger.valueOf(Integer.MAX_VALUE).nextProbablePrime();
	static Random rng = new SecureRandom();
	static int[] table = new int[128];
	static
    {
        for(int i = 0; i < table.length; i++)
            table[i] = rng.nextInt();
    }
	public HashFunctionTester() 
	{
		// TODO Auto-generated constructor stub
	}

	public static int additive(byte[] key)
    {
        int h = 0;
        for(int i = 0; i < key.length; i++)
            h += key[i];
        return h < 0 ? -h : h;
    }

    public static int xor(byte[] key)
    {
        int h = 0;
        for(int i = 0; i < key.length; i++)
            h ^= key[i];
        return h < 0 ? -h : h;
    }

    public static int primeRotate(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h = (h << 5) | (h >> 27) ^ b;
        return h & Integer.MAX_VALUE;
    }

    public static int bernstein1(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h = 33 * h + b;
        return h & Integer.MAX_VALUE;
    }

    public static int bernstein2(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h = 33 * h ^ b;
        return h & Integer.MAX_VALUE;
    }

    public static int sax1(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 5) + (h >> 2) + b;
        return h & Integer.MAX_VALUE;
    }

    public static int sax2(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 5) | (h >> 2) | b;
        return h & Integer.MAX_VALUE;
    }

    public static int sax3(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= (h << 5) ^ (h >> 2) ^ b;
        return h & Integer.MAX_VALUE;
    }

    public static int fnv(byte[] key)
    {
        long h = 2166136261L;
        for(byte b : key)
            h = (h * 16777619) ^ b;
        return ((int)h)&Integer.MAX_VALUE;
    }

    public static int oat(byte[] key)
    {
        int h = 0;
        for(byte b : key)
        {
            h += b;
            h += (h << 10);
            h ^= (h >> 6);
        }
        h += (h << 3);
        h ^= (h >> 11);
        h += (h << 15);
        return h & Integer.MAX_VALUE;
    }

    public static int jsw(byte[] key)
    {
        int h = 16777551;
        for(byte b : key)
            h = ((h << 1) | (h >> 31)) ^ table[b];
        return h & Integer.MAX_VALUE;
    }

    public static int elf(byte[] key)
    {
        int h = 0;
        long g;
        for(byte b : key)
        {
            h = (h << 4) + b;
            g = h & 0xF0000000L;
            if(g != 0)
                h ^= g >> 24;
            h &= ~g;
        }
        return h & Integer.MAX_VALUE;
    }

    public static int linear1(byte[] key)
    {
        int h = 0;
        for(byte b : key)
            h ^= 1778502657 * b ^ 1510417151 + b;
        return h & Integer.MAX_VALUE;
    }

	//worst
	public static int xorSum(String s)
	{
		int sum = 0;
		for(char c : s.toCharArray())
			sum = sum ^ (int)c;
		return sum;
	}

	//basic perfect scheme
	//max 64 bytes for strings
	public static int perfect_string(String s, int n)
	{
		assert s.length() <= 64 : "only operates on 64 character \"chunks\"";
		int x = s.hashCode();
		int k = rng.nextInt();
		BigInteger g = BigInteger.valueOf(k * x).mod(p1).mod(BigInteger.valueOf(n));
		return g.intValueExact();
	}

	public static int perfect_int(int x, int n)
	{
		int k = rng.nextInt();
		BigInteger g = BigInteger.valueOf(k * x).mod(p2).mod(BigInteger.valueOf(n));
		return g.intValueExact();
	}

	public static int murmur(byte[] key, int len, int seed)
	{
		assert len == key.length : "key and length mismatch";
		int c1 = 0xcc9e2d51;
		int c2 = 0x1b873593;
		int r1 = 15;
		int r2 = 13;
		int m = 5;
		int n = 0xe6546b64;
		int hash = seed;
		int res;
		for(int i = 0; i < key.length - 4; i += 4)
		{
			res = 0;
			res |= key[i] & 0xFF;
			res <<= 8;
			res |= key[i+1] & 0xFF;
			res <<= 8;
			res |= key[i+2] & 0xFF;
			res <<= 8;
			res |= key[i+3] & 0xFF;
			int k = res;
			k *= c1;
			k = (k << r1) | (k >> 17);
			k *= c2;
			hash ^= k;
			hash = (hash << r2) | (hash >> 19);
			hash = hash * m + n;
		}
		if(len % 4 != 0)
		{
			int off = len - (len % 4);
			int rem = 0;
			for(int i = off; i < len; i++)
			{
				rem <<= 8;
				rem |= key[i] & 0xFF;
			}
			rem *= c1;
			rem = (rem << r1) | (rem >> 17);
			rem *= c2;
			hash ^= rem;
		}
		hash ^= len;
		hash ^= (hash >> 16);
		hash *= 0x85ebca6b;
		hash ^= (hash >> 13);
		hash *= 0xc2b2ae35;
		hash ^= (hash >> 16);
		return hash;
	}

	public static long siphash128(byte[] key, byte[] msg, int crounds, int frounds)
	{
		assert key.length == 16 : "key length is not 128";
		//assert msg.length % 16 == 0 : "message length is not a multiple of 16";
		long k0 = translateToLong(key, 15);
		long k1 = translateToLong(key, 7);
		long v0 = k0 ^ 0x736f6d6570736575L;
		long v1 = k1 ^ 0x646f72616e646f6dL;
		long v2 = k0 ^ 0x6c7967656e657261L;
		long v3 = k1 ^ 0x7465646279746573L;

		int words = (int) Math.ceil((double)(msg.length + 1) / 8.0);
		long[] m = new long[words];
		for(int i = 0; i < words - 1; i++)
		{
			m[i] = translateToLong(msg, msg.length - 8*i - 1);
		}
		m[words - 1] = translateToLong(msg,7);
		m[words - 1] |= msg.length % 256;
		for(int i = 0; i < words; i++)
			v3 ^= m[i];
		for(int i = 0; i < crounds; i++)
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
		for(int i = 0; i < words; i++)
			v0 ^= m[i];
		v2 ^= 0xFF;
		for(int i = 0; i < frounds; i++)
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
		return v0 ^ v1 ^ v2 ^ v3;
	}

	private static int translateToInt(byte[] b, int off)
	{
		int res = 0;
		for(int i = off, c = 0; i >= 0 && c < 4; i--, c++)
		{
			res <<= 8;
			res |= b[i] & 0xFF;
		}
		return res;
	}

	private static long translateToLong(byte[] b, int off)
	{
		long res = 0;
		for(int i = off, c = 0; i >= 0 && c < 8; i--, c++)
		{
			res <<= 8;
			res |= b[i] & 0xFF;
		}
		return res;
	}

	public static void main(String[] args)
	{
		String[] students = new String[50000];
		for(int i = 0; i < students.length; i++)
			students[i] = "Student" + i;
		int[] map = new int[50000];
		System.out.println(HashFunctionTester.perfect_string("Student1", 50000));
		for(String s : students)
		{
			int idx = HashFunctionTester.perfect_string(s,50000);
			//System.out.println(idx);
			map[idx]++;
		}
		System.out.println(Arrays.toString(map));
		System.out.println(p1);
		byte[] key = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		byte[] message = "YELLOW SUBMARINE".getBytes();
		System.out.println(siphash128(key,message,100,57));
		System.out.println(murmur(key,16,0xDEADBEEF));
		Arrays.fill(map, 0);
		for(String s : students)
        {
            int idx = HashFunctionTester.primeRotate(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.bernstein1(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.bernstein2(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.sax1(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.sax2(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.sax3(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.fnv(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.oat(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.jsw(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
        Arrays.fill(map, 0);
        for(String s : students)
        {
            int idx = HashFunctionTester.elf(s.getBytes()) % map.length;
            map[idx]++;
        }
        System.out.println(Arrays.toString(map));
		//0xBEEFBEEF
		//0xBEEFDEAD
		//0xDEADBEEF
		//0xDEADDEAD
	}
}
