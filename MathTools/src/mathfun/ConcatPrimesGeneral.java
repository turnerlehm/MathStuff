package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class ConcatPrimesGeneral {

	public ConcatPrimesGeneral() {
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
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Turner\\Desktop\\Computation_Results\\concat_primes_general_100000000.txt"));
		BigInteger N = BigInteger.ZERO;
		for(int i = 1; i < 100000000; i++)
		{
			String[] S = translate(i);
			N = new BigInteger(S[0]);
			if(N.isProbablePrime(100000000))
			{
				out.write("i = " + i + ": " + N + "\t");
				out.flush();
				//out.write("Length: " + S[0].length() + "\tk = " + S[1] + "\tN mod p = " + N.mod(new BigInteger("" + p)));
				//out.flush();
				out.newLine();
				System.out.println("p = " + i + ": " + N + "\t");
				//System.out.print("Length: " + S[0].length() + "\tk = " + S[1] + "\tN mod p = " + N.mod(new BigInteger("" + p)) + "\n");
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
