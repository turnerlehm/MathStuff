package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Collatz {
	private static final BigInteger THREE = new BigInteger("3");
	private static final BigInteger TWO = new BigInteger("2");
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger ZERO = BigInteger.ZERO;
	public Collatz() {
		// TODO Auto-generated constructor stub
	}

	private static void collatz(BigInteger n)throws IOException
	{
		BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\COMPUTATIONS\\collatz_RAND_PRIME_8192.txt"));
		long steps = 1;
		long start = System.currentTimeMillis();
		while(n.compareTo(ONE) > 0)
		{
			if(n.and(ONE).compareTo(ZERO) != 0)
				n = n.multiply(THREE).add(ONE);
			else
				n = n.divide(TWO);
			out.write(steps + ": " + n);
			out.newLine();
			out.flush();
			System.out.println(steps + ": " + n);
			steps++;
		}
		long stop = System.currentTimeMillis();
		System.out.print("# Steps: " + steps + "\n");
		out.write("# Steps: " + steps);
		out.newLine();
		out.flush();
		System.out.print("Time (ms): " + (stop - start));
		out.write("Time (ms): " + (stop - start));
		out.flush();
		out.close();
	}
	
	public static void main(String... args)throws IOException
	{
		Random rng = new SecureRandom();
		collatz(new BigInteger(8192, 10000, rng));
	}
}
