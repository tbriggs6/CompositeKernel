/*
/*
 * FILE:    Kernel.java
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
public abstract class Kernel {
	
	public abstract double eval(SparseVector Xi, SparseVector Xj);
	

	

	
	
}
