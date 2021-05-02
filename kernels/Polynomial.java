/*
/*
 * FILE:    Linear.java
 * PACKAGE: kernels
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package kernels;

import data.SparseVector;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Polynomial extends Kernel {
	
	double s;
	double r;
	double d;
	
	public Polynomial(double s, double r, double d)
	{
		this.s = s;
		this.r = r;
		this.d = d;
	}
	
	public double eval(SparseVector Xi, SparseVector Xj)
	{
		double dot = Xi.dotProduct(Xj);		
		double tmp = (s * dot) + r;
		double pow = Math.pow(tmp,(int)d);
		
		if (Double.isNaN(dot) || Double.isInfinite(dot)) {
			throw new RuntimeException("Pkernel: dot is NaN: " + Xi + "\t" + Xj);
		}
		
		if (Double.isNaN(tmp) || Double.isInfinite(tmp)) {
			throw new RuntimeException("Pkernel: tmp is NaN: " + Xi + "\t" + Xj);
		}
		
		if (Double.isNaN(pow) || Double.isInfinite(pow)) {
			System.err.println("dot is: " + dot);
			System.err.println("tmp is: " + tmp);
			System.err.println("d: " + d + "\ts:" + s + "\tr:" + r);
			System.err.println("pow: " + pow);
			throw new RuntimeException("Pkernel: pow is NaN: " + Xi + "\t" + Xj);
		}
		
		return pow;
	}
	
}
