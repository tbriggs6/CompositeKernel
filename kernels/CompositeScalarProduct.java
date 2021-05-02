/*
/*
 * FILE:    CompositeScalarProduct.java
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
public class CompositeScalarProduct extends Kernel {
	
	Kernel K;
	double alpha;
	
	public CompositeScalarProduct(double alpha, Kernel K)
	{
		this.K = K;
		this.alpha = alpha;
	}
	
	public double eval(SparseVector Xi, SparseVector Xj)
	{
		return alpha * K.eval(Xi,Xj);
	}
	
}
