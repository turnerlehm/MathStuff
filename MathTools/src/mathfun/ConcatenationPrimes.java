package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class ConcatenationPrimes {

	public ConcatenationPrimes() 
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
		int[] primes = generateSieve(100000000);
		BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\COMPUTATIONS\\concat_primes_100000000.txt"));
		BigInteger N = BigInteger.ZERO;
		for(int p : primes)
		{
			N = new BigInteger(translate(p));
			if(N.isProbablePrime(100000000))
			{
				out.write("p = " + p + ": " + N);
				out.flush();
				out.newLine();
				System.out.println("p = " + p + ": " + N);
			}
		}
		out.close();
	}
	
	private static String translate(int n)
	{
		String s = "";
		for(int i = 31; i >= 0; i--)
			s += (n & (1 << i)) != 0 ? (n & (1 << i)) : "";
		return s;
	}
	
	public static void main(String... args) throws IOException
	{
		findPrimes();
	}
}
