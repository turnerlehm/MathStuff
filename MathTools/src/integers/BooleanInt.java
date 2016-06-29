package integers;

/**
 * @author Turner C.<!-- --> Lehmbecker
 * Translates a standard 32-bit signed integer into a boolean array for more control over bitwise operations
 * The underlying boolean array can of course be translated back into a standard 32-bit signed integer.
 * All basic arithmetic operations are supported as well as all standard bitwise operations.
 * Standard bitwise operations are also modified in this class to allow for more precise control and novel bitwise operations are also introduced.
 * 
 * For example, with this class someone may XOR the 1st, 2nd, and 3rd bits of an integer with the 30th, 31st, and 32nd bits of another. This allows for better bit mixing when 
 * performing hashing operations. Additionally, this allows for less management by the programmer.
 */
public class BooleanInt 
{
	private boolean[] bits;
	public static final BooleanInt ZERO = new BooleanInt();
	public static final BooleanInt ONE = new BooleanInt(1);
	public static final BooleanInt TEN = new BooleanInt(10);
	/**
	 * Default class constructor
	 * Characteristically constructs a boolean integer with initial value 0
	 */
	public BooleanInt() 
	{
		bits = new boolean[32];
	}
	
	/**
	 * Class constructor specifying integer to be converted to/represented as a boolean array
	 * @param n the 32-bit signed integer to be converted/represented
	 */
	public BooleanInt(int n)
	{
		translate(n);
	}
	
	/**
	 * Translates a 32-bit signed integer into a boolean array
	 * @param n
	 */
	private void translate(int n)
	{
		bits = new boolean[32];
		for(int i = 31; i >= 0; i--)
			bits[bits.length - i - 1] = (n & (1 << i)) != 0;
	}
	
	/**
	 * Returns the base-2 (binary) representation of <code>this</code>
	 * @return the base-2 representation of <code>this</code>
	 */
	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < 32; i++)
		{
			s += bits[i] ? 1 : 0;
			if(i != 0 && (i + 1) % 4 == 0)
				s += " ";
		}
		return s;
	}
	
	/**
	 * Returns the 32-bit signed integer representation of <code>this</code>
	 * @return the 32-bit signed integer representation of <code>this</code>
	 */
	public int intValue()
	{
		int n = 0;
		for(boolean b : bits)
			n = (n << 1) + (b ? 1 : 0);
		return n;
	}
	
	/**
	 * Multiplies <code>this</code> with the specified integer <code>n</code>
	 * @param n the integer to be multiplied by
	 * @return a BooleanInt representing the result of <code>this * n</code>
	 */
	public BooleanInt multiply(int n)
	{
		return new BooleanInt(this.intValue() * n);
	}
	
	/**
	 * Multiplies <code>this</code> with the BooleanInt <code>n</code>
	 * @param n the BooleanInt to be multiplied by 
	 * @return <code>this * n</code>
	 */
	public BooleanInt multiply(BooleanInt n)
	{
		return new BooleanInt(this.intValue() * n.intValue());
	}
	
	/**
	 * Returns a BooleanInt whose value is <code>this + n</code>
	 * @param n the value to be added
	 * @return <code>this + n</code>
	 */
	public BooleanInt add(int n)
	{
		return new BooleanInt(this.intValue() + n);
	}
	
	/**
	 * Returns a BooleanInt whose value is <code>this + n</code>
	 * @param n the BooleanInt to be added
	 * @return <code>this + n</code>
	 */
	public BooleanInt add(BooleanInt n)
	{
		return new BooleanInt(this.intValue() + n.intValue());
	}
	
	/**
	 * Returns a BooleanInt whose values is <code>this - n</code>
	 * @param n the value to be subtracted
	 * @return <code>this - n</code>
	 */
	public BooleanInt subtract(int n)
	{
		return new BooleanInt(this.intValue() - n);
	}
	
	/**
	 * Returns a BooleanInt whose value is <code>this - n</code>
	 * @param n the BooleanInt to be subtracted
	 * @return <code>this - n</code>
	 */
	public BooleanInt subtract(BooleanInt n)
	{
		return new BooleanInt(this.intValue() - n.intValue());
	}
	/*
	 * Sanity check
	 */
	public static void main(String... args)
	{
		BooleanInt five = new BooleanInt(3);
		System.out.println(five.multiply(3).toString());
	}
}
