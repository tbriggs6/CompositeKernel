/*
/*
 * FILE:    GramMatrix.java
 * PACKAGE: data
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package data;

import kernels.Kernel;
import kernels.KernelParams;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GramMatrix {
	
	KernelParams K;
	double M[][];
	int m = 0;
	
	
	/**
	 * Create an empty gram matrix 
	 * @param m
	 */
	public GramMatrix(int m)
	{
		this.m = m;
		M = new double[m][m];
	}
	
	
	/**
	 * Compute the gram matrix from the given kernel and
	 * vector table T.
	 * @param K
	 * @param T
	 */
	public GramMatrix(Kernel K, VectorTable T)
	{
		this.m = T.getNumRows();
		M = new double[m][m];
		
		for (int i = 0; i < m; i++)
		{
			SparseVector I = T.getRow(i);
			for (int j = i; j < m; j++)
			{
				double result = K.eval(I,T.getRow(j));
				if (Double.isInfinite(result)) {
					throw new RuntimeException("Infinite matrix entry: " +
							i + "," + j + "\t" + I + "\t" + T.getRow(j));
				}
				
				// symmetrix matrix - don't need to compute the whole
				M[i][j] = result;
				M[j][i] = result;
			}
		}
		
	}
	
	/**
	 * Compute the gram matrix from the data labels in T
	 * @param T
	 */
	public GramMatrix(VectorTable T)
	{
		this.m = T.getNumRows( );
		M = new double[m][m];
		
		int Y[] = T.getY();
		for (int i = 0; i < m; i++)
			for (int  j = i; j < m; j++) {
				double result = Y[i] * Y[j];
				if (Double.isInfinite(result)) {
					throw new RuntimeException("Infinite class matrix entry: " +
							i + "," + j);
				}
				
				// symmetric positive matrix - don't need to compute the whole
				M[i][j] = result;
				M[j][i] = result;
			}
					
		
	}
	
	public double get(int i, int j)
	{
		return M[i][j];
	}
	
	public void set(int i, int j, double v)
	{
		M[i][j] = v;
		M[j][i] = v;
	}
	
	public int getM( ) 
	{
		return m;
	}
	
}
