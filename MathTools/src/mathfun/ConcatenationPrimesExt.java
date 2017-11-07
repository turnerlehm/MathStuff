package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class ConcatenationPrimesExt {

	public ConcatenationPrimesExt() 
	{
		// TODO Auto-generated constructor stub
	}
	
	private static int[] generateSieve(int n)
	{
		boolean[] sieve = new boolean[n + 1];
		for(int i = 2; i < sieve.length; i++)
			sieve[i] = true;
		for(int i = 2; i < (int)Math.sqrt(n); i++)
			if(sieve[i])
				for(int j = i * i; j < sieve.length; j += i)
					sieve[j] = false;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 2; i < sieve.length; i++)
			if(sieve[i])
				list.add(i);
		Iterator<Integer> iter = list.iterator();
		int[] primes = new int[list.size()];
		for(int i = 0; i < primes.length; i++)
			primes[i] = iter.next().intValue();
		return primes;
	}
	
	private static void findPrimes() throws IOException
	{
		int[] primes = generateSieve(1000000000);
		BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\COMPUTATIONS\\PMODN_PRIME_1000000000.txt"));
		BigInteger N = BigInteger.ZERO;
		for(int p : primes)
		{
			String[] S = translate(p);
			N = new BigInteger(S[0]);
			BigInteger mod;
			if(N.isProbablePrime(100000000) && (mod = N.mod(new BigInteger("" + p))).isProbablePrime(100000000))
			{
				out.write("p = " + p + ": " + "N mod p = " + mod);
				out.newLine();
				out.flush();
				System.out.println("p = " + p + ": " + "N mod p = " + mod);
			}
		}
		out.close();
	}
	
	private static String[] translate(int n)
	{
		String[] S = new String[2];
		S[0] = "";
		S[1] = "";
		for(int i = 31; i >= 0; i--)
		{
			if((n & (1 << i)) != 0)
			{
				S[0] += (n & (1 << i));
				S[1] += i + ", ";
			}
		}
		return S;
	}
	
	public static void main(String... args) throws IOException
	{
		findPrimes();
	}
}
