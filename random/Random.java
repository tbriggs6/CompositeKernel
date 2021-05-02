/*
 */
 /*
  * FILE:    HillClimber.java
  * PACKAGE: hillClimbing
  * PROJECT:	CSC110
  * AUTHOR:  Tom Briggs (c) 2004
  * DATE:    Sep 12, 2004
  */ 
package random;

import java.io.IOException;

import kernels.CompositeKernelParams;
import kernels.KernelParams;
import alignment.Alignment;
import data.FileReaderSVM;
import data.GramMatrix;
import data.VectorTable;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Random {
	
	VectorTable T;
	double bestAlignment = 0;
	KernelParams bestState = null;
	int kernelEvals = 0;
	
	public static void main(String[] args) {
		
		if (args.length != 2)
		{
			System.err.println("Must run as java random/Random file num_iterations");
		}
		else {
			Random R = new Random(args[0]);
			double result = R.random( Integer.parseInt(args[1]));
		}
	}
	
	public Random( String fileName )
	{
		
		try {
			FileReaderSVM F = new FileReaderSVM(fileName);
			T = F.loadTable();
			
		}
		catch(IOException E)
		{
			E.printStackTrace( );
		}
		
		
	}
	
	/* 
	 * Hill Climbing agent:
	 * (alpha1 * K1(s,b,d)) + (1-alpha1) * K2(g))
	 */
	public double random(int maxIterations )
	{
		
	    java.text.DecimalFormat D = new java.text.DecimalFormat("#.######");
		bestState = null;
		bestAlignment = 0;
		
		GramMatrix YY = new GramMatrix(T);
		
		for (int i = 0; i < maxIterations; i++)
		{
			KernelParams state = CompositeKernelParams.randomStart( );
			GramMatrix K = new GramMatrix(state.getKernel(),T);

			GramMatrix tmpG = new GramMatrix(state.getKernel(),T);
			double tmpAlign = Alignment.targetAlignment(tmpG, YY);
			
			if (tmpAlign > bestAlignment) {
				bestAlignment = tmpAlign;
				bestState = state;
			}
			kernelEvals++;

			System.out.println(kernelEvals + "\t" + state.toString() + "\t" + tmpAlign);
		}
		
				
		System.out.println("Best kernel: " + bestState.toString() + " with alignment : " + D.format(bestAlignment));
		
		return bestAlignment;
	}
	
}
