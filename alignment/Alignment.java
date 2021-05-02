/*
/*
 * FILE:    Alignment.java
 * PACKAGE: alignment
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package alignment;

import data.GramMatrix;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Alignment {
	
	public static double kernelAlignment(GramMatrix K1, GramMatrix K2)
	{
		double fK1K2 = FrobeniusInnerProduct.product(K1,K2);
		double fK1K1 = FrobeniusInnerProduct.product(K1,K1);
		double fK2K2 = FrobeniusInnerProduct.product(K2,K2);
		
		return fK1K2 / Math.sqrt(fK1K1 * fK2K2);
	}

	public static double targetAlignment(GramMatrix K1, GramMatrix Y)
	{
		double fK1Y = FrobeniusInnerProduct.product(K1,Y);
		double fK1K1 = FrobeniusInnerProduct.product(K1,K1);
		
		double result = fK1Y / ( K1.getM( ) * Math.sqrt(fK1K1) );
		
		if (Double.isNaN(result)) 
		{
			System.err.println(fK1Y + "\t" + fK1K1 + "\t" + K1.getM() + "\t" + result);
			throw new RuntimeException("result is NaN");
		}
		return result;
	}
	
}
