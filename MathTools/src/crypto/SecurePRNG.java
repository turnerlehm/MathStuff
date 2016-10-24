package crypto;

public class SecurePRNG 
{
	public SecurePRNG() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public SecurePRNG(int seed)
	{
		x1 = (seed << 11) ^ 0x7fffffff;
		y1 = (seed >> 8) & 0x7fffffff;
		z1 = (seed << 13) ^ 0x7fffffff;
		w1 = (seed >> 4) & 0x7fffffff;
		x2 = (seed << 31) ^ 0x7fffffff;
	}
	
	//used for example version of XORshift
	private int x1 = (int)(System.nanoTime() ^ 0x7fffffff) + 1; 
	private int y1 = (int)(System.nanoTime() ^ 0x7fffffff) + 2; 
	private int z1 = (int)(System.nanoTime() ^ 0x7fffffff) + 3; 
	private int w1 = (int)(System.nanoTime() ^ 0x7fffffff) - 1;
	
	/**
	 * Based on the example XORshift implementation mentioned <a href="https://en.wikipedia.org/wiki/Xorshift#Example_implementation">here</a>
	 * Generates a moderately secure PR 32-bit signed integer. If no seed is specified, <code>System.nanoTime()</code> is used as the seed.
	 * This generator should not be used when a very high degree of cryptographically secure randomness is needed.
	 * This function has a maximal period of <code>2^128 - 1</code>.
	 * @return a moderately secure PRN
	 */
	public int nextInt_128()
	{
		int t = x1;
		t ^= t << 11;
		t ^= t >> 8;
		x1 = y1; y1 = z1; z1 = w1;
		w1 ^= w1 >> 19;
		w1 ^= t;
		return w1;
	}
	/**
	 * The same as <code>nextInt_128()</code> but positive integers only.
	 * @return a nonnegative moderately secure PRN
	 * @see nextInt_128()
	 */
	public int nextInt_128_non()
	{
		int t = x1;
		t ^= t << 11;
		t ^= t >> 8;
		x1 = y1; y1 = z1; z1 = w1;
		w1 ^= w1 >> 19;
		w1 ^= t;
		return w1 & 0x7fffffff;
	}
	
	//used for xorshift+
	private int x2 = (int)(System.nanoTime() ^ 0x7fffffff) + 257;
	/**
	 * An implementation of the example xorshift+ PRNG mentioned here <a href="https://en.wikipedia.org/wiki/Xorshift#xorshift.2A">here</a>
	 * Generates a moderately secure 32-bit signed integer. If no seed is specified at instantiation <code>System.nanoTime()</code> is used as the seed.
	 * This generator is an example and should not be used if a high degree of cryptographically secure randomness is desired.
	 * This function has a maximal period of  <code>2^64 - 1</code> 
	 * @return a moderately secure PRN
	 */
	public int nextInt_64()
	{
		x2 ^= x2 >> 21;
		x2 ^= x2 << 25;
		x2 ^= x2 >> 27;
		return x2 * 0x7fffffff;
	}
	/**
	 * The same as <code>nextInt_64()</code>, but with positive integers only.
	 * @return a moderately secure PRN
	 * @see nextInt_64()
	 */
	public int nextInt_64_non()
	{
		x2 ^= x2 >> 21;
		x2 ^= x2 << 25;
		x2 ^= x2 >> 27;
		return (x2 * 0x7fffffff) & 0x7fffffff;
	}
}
