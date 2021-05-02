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
public class RBF extends Kernel {
	
	double gamma;
	
	public RBF(double gamma)
	{
		this.gamma = gamma;
		
		if (gamma == 0) { throw new RuntimeException("yawp."); }
	}
	
	
	public double eval(SparseVector Xi, SparseVector Xj)
	{
		double norm1 = Xi.norm2();
		double norm2 = Xj.norm2();
		double prod = Xi.dotProduct(Xj);
		
		double norm1sq = Math.pow(norm1,2);
		double norm2sq = Math.pow(norm2,2);
		
		double tmp = norm1sq - 2 * prod + norm2sq;
		
		double f = Math.exp(-1 * gamma * tmp ); 
		
		if ((Double.isNaN(f)) || Double.isInfinite(f)) {
			throw new RuntimeException("RBF: f is NaN: " + 
					"f:" + f + " n2: " + norm2 + " g: " + gamma +  
					"\t" + Xi + "\t" + Xj);
		}
		
		return f;
	}
	
}
