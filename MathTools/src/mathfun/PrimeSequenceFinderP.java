package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class PrimeSequenceFinderP {
	private static final BigInteger TWO = new BigInteger("2");
	public PrimeSequenceFinderP() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String... args) throws IOException
	{
		findPrimes();
	}
	
	private static void findPrimes() throws IOException
	{
		int[] primes = generateSieve(100000000);
		BigInteger N = BigInteger.ZERO;
		System.out.println("Seq: S = 2^p + 2^(p - 1) + 1. Find primes p such that p^2 divides S");
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Turner\\Desktop\\Computation_Results\\primes_p+1_divisible_100000000.txt"));
		for(int p : primes)
		{
			N = BigInteger.ONE.shiftLeft(p).add(BigInteger.ONE.shiftLeft(p - 1));
			if(N.mod((new BigInteger("" + (p + 1))).abs()).compareTo(BigInteger.ZERO) == 0)
			{
				out.write("p = " + p + ": " + N);
				out.newLine();
				out.flush();
			}
		}
		out.close();
	}
	
	private static int[] generateSieve(int n)
	{
		boolean[] sieve = new boolean[n + 1];
		for(int i = 2; i < sieve.length; i++)
			sieve[i] = true;
		for(int  i = 2; i < (int)Math.sqrt(n); i++)
			if(sieve[i])
				for(int j = i * i; j < sieve.length; j += i)
					sieve[j] = false;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 2; i < sieve.length; i++)
			if(sieve[i])
				list.add(i);
		int[] primes = new int[list.size()];
		Iterator<Integer> iter = list.iterator();
		for(int i = 0; i < primes.length; i++)
			primes[i] = iter.next().intValue();
		return primes;
	}
}
