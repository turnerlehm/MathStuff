package mathfun;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.*;
/**
 * 
 * @author Turner C.<!-- --> Lehmbecker
 * This simple class finds all primes of the form <code>2^n + 67</code> for <code>0 <= n <= 50000</code>.
 */
public class PrimeSequenceFinder {
	private static final BigInteger SIXTY_SEVEN = new BigInteger("67");
	private static final BigInteger TWO = new BigInteger("2");
	public PrimeSequenceFinder() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String... args) throws IOException
	{
		findPrimes();
	}
	
	private static void findPrimes() throws IOException
	{
		BufferedWriter out = new BufferedWriter(new PrintWriter("C:\\Users\\Turner\\Desktop\\Computation_Results\\primes_2^n+67_50000.txt"));//change this to the directory you want
		BigInteger p = BigInteger.ZERO;
		for(int n = 0; n <= 50000; n++)//change this to what you want as well
		{
			if((n & 1) != 0)
				continue;
			p = BigInteger.ONE.shiftLeft(n).add(BigInteger.ONE.shiftLeft(6)).add(TWO).add(BigInteger.ONE);
			if(p.isProbablePrime(10000000))
			{
				out.write("n = " + n + ": " + p);
				out.newLine();
			}
		}
		out.close();
	}
}
