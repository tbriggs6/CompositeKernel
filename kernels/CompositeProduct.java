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
public class CompositeProduct extends Kernel {
	
	Kernel K1,K2;
	
	
	public CompositeProduct(Kernel K1,Kernel K2)
	{
		this.K1 = K1;
		this.K2 = K2;
	}
	
	public double eval(SparseVector Xi, SparseVector Xj)
	{
		return K1.eval(Xi,Xj) * K2.eval(Xi,Xj);
	}
	
}
