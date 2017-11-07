package mathfun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Turner Lehmbecker
 * Calculates the base-2 concatenation for the natural numbers. For now, we only consider 32- and 64-bit integers, though in theory we can consider arbitrarily large integers.
 * Also, no known formula exists to directly calculate the base-2 concatenation since it is not a standard arithmetic operation.
 * In fact, concatenation is more related to automata and formal languages than mathematics.
 * 
 * The general formula is as follows where <code>a</code> is some number, <code>||</code> denotes concatenation, and <code>x_i</code> is the <code>ith</code> digit of the base-2 representation of <code>a</code>.
 * <p><code>a = x_0 || 2 * x_1 || (2^2) * x_2 || ... || (2^n) * x_n.</code>
 */

public class Base2Concatenation {

	public Base2Concatenation() {
		// TODO Auto-generated constructor stub
	}
	
	//32-bit signed integer
	private static void concatenate_int()throws IOException
	{
		BigInteger N_2 = null;
		BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\COMPUTATIONS\\numbers_base-2_concatention_Integer.MAX_VALUE.txt"));
		for(int n = 1; n < Integer.MAX_VALUE; n++)
		{
			N_2 = new BigInteger(translate(n));
			out.write("n = " + n + ": " + N_2);
			out.newLine();
			out.flush();
			System.out.println("n = " + n + ": " + N_2);
		}
		out.close();
	}

	private static String translate(int n)
	{
		String s = "";
		for(int i = 31; i >= 0; i--)
			if((n & (1 << i)) != 0)
				s += (n & (1 << i));
		return s;
	}
	
	//64-bit signed integer
	private static void concatenate_long()throws IOException
	{
		BigInteger N_2 = null;
		BufferedWriter out = new BufferedWriter(new FileWriter("D:\\Projects\\COMPUTATIONS\\numbers_base-2_concatention_Long.MAX_VALUE.txt"));
		for(long n = 1; n < Long.MAX_VALUE; n++)
		{
			System.out.println("n = " + n + ": " + N_2);
			N_2 = new BigInteger(translate(n));
			out.write("n = " + n + ": " + N_2);
			out.newLine();
			out.flush();
		}
		out.close();
	}
	
	private static String translate(long n)
	{
		String s = "";
		for(int i = 63; i >= 0; i--)
			if((n & (1 << i)) != 0)
				s += (n & (1 << i));
		return s;
	}
	
	public static void main(String... args) throws IOException
	{
		System.out.println("TEST");
		concatenate_int();
	}
}
