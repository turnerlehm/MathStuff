package integers;

/*
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

	public BooleanInt() 
	{
		// TODO Auto-generated constructor stub
	}

}
