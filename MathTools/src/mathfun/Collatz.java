package mathfun;

import java.math.BigInteger;

public class Collatz {
	private static final BigInteger THREE = new BigInteger("3");
	private static final BigInteger TWO = new BigInteger("2");
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger ZERO = BigInteger.ZERO;
	public Collatz() {
		// TODO Auto-generated constructor stub
	}

	private static void collatz(BigInteger n)
	{
		long steps = 1;
		while(n.compareTo(ONE) > 0)
		{
			if(n.and(ONE).compareTo(ZERO) != 0)
				n = n.multiply(THREE).add(ONE);
			else
				n = n.divide(TWO);
			System.out.println(steps + ": " + n);
			steps++;
		}
		System.out.print("# Steps: " + (steps - 1) + "\n");
	}
	
	public static void main(String... args)
	{
		long start = System.currentTimeMillis();
		collatz(new BigInteger("79886561233479456512354579855452355647856321354115474552568774563213321459879987984561231232323121478545111115387782376435786356519892398749872837634759876129634994081298734209782987087099734597897657861239138794567899872634907"));
		System.out.print("Time (ms): " + (System.currentTimeMillis() - start));
	}
}
