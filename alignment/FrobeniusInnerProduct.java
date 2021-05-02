/*
/*
 * FILE:    FrobeniusInnerProduct.java
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
public class FrobeniusInnerProduct {
	
	public static double product(GramMatrix K1, GramMatrix K2)
	{
		double sum = 0;
		
		int m = K1.getM();
		if (m != K2.getM()) {
			throw new RuntimeException("Dimensions do not agree.");
		}
		
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < m; j++)
			{
				
				double k1 = K1.get(i,j);
				double k2 = K2.get(i,j);
				
				if (Double.isNaN(k1) || Double.isNaN(k2))
				{
					throw new RuntimeException("frobenius was nan");
				}
				
				sum += k1 * k2;
				
				if (Double.isInfinite(sum)) {
					throw new RuntimeException("sum hit infinity");
				}
			}
		}
		
		return sum;
	}
	
}
