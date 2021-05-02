/*
 */
 /*
  * FILE:    HillClimber.java
  * PACKAGE: hillClimbing
  * PROJECT:	CSC110
  * AUTHOR:  Tom Briggs (c) 2004
  * DATE:    Sep 12, 2004
  */ 
package hillClimbing;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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
public class HillClimber {

	static double EPSILON =0.000000000000001; 
	VectorTable T;
	double bestAlignment = 0;
	KernelParams bestState = null;
	
	int kernelEvals = 0;
	int cacheHits = 0;
	
		
	public static void main(String[] args) {
		
	    if (args.length != 2)
	    {
	        System.err.println("Error: run as hillClibming/HillClimber file nRestarts");
	        return;
	    }
		
		HillClimber HC = new HillClimber(args[0]);
		double result = HC.climbHills( Integer.parseInt(args[1]));
	}
	
	public HillClimber( String fileName )
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
	public double climbHills( )
	{
	    return climbHills(10);
	}
	
	public double climbHills( int maxIterations )
	{
		java.text.DecimalFormat D = new java.text.DecimalFormat("0.0000");

		bestState = null;
		bestAlignment = 0;

		
		
		for (int i = 0; i < maxIterations; i++)
		{
			System.out.println("Starting iteration " + i +
					" evals so far: " + kernelEvals);
			if (i > 0) {
				System.out.println("The best kernel so far: " + bestState.toString());
				System.out.println("The best alignment so far: " + D.format(bestAlignment));
			}
			
			climbHillsIteration( );
		}
		
		System.out.println("The best kernel: " + bestState.toString());
		System.out.println("The best alignment: " + D.format(bestAlignment));
		return bestAlignment;
	}
	
	
	public double climbHillsIteration( )
	{
		boolean continueRunning = false;
		java.text.DecimalFormat D = new java.text.DecimalFormat("0.000000");
		GramMatrix YY = new GramMatrix(T);
		
		KernelParams state = randomStart( );
		
		GramMatrix K = new GramMatrix(state.getKernel(),T);
		double alignment = Alignment.targetAlignment(K,YY);
		double bestIterAlignment = alignment;
		
		
		KernelParams lastState = state;
		double lastAlign = alignment;
		
		boolean done = true;
		
		do {
			if (bestState != null)
				System.out.println("Curr best: " + lastState.toString() + " : " + D.format(lastAlign));
			done = true;
			
			LinkedList children = lastState.expand( );
			
			KernelParams tmpState = null;
			
			double tmpAlign = 0;
			GramMatrix tmpG = null;
			
			Iterator I = children.iterator();
			while (I.hasNext())
			{
				tmpState = (KernelParams) I.next();
				
				tmpG = new GramMatrix(tmpState.getKernel(),T);
				tmpAlign = Alignment.targetAlignment(tmpG, YY);
				
				kernelEvals++;
				
				double delta = tmpAlign - bestIterAlignment;
				
				if (tmpAlign > bestIterAlignment)
				{
					bestIterAlignment = tmpAlign;
					System.out.println("");
					System.out.println(tmpState);
					System.out.println("  best:" + D.format(bestAlignment) +
							" iter: " + D.format(bestIterAlignment) +
							" ta:" + D.format(tmpAlign) + 
							" delta:" + D.format(delta));
		
					if (tmpAlign > bestAlignment) {
						bestAlignment = tmpAlign;
						bestState = tmpState;
					}
					done = false;
				}
				else {
					System.out.print(".");
					System.out.flush();
				}
				
			}
			
		}
		while( ! done );
		
		
		System.out.println("Finished with HC loop " + kernelEvals + " evaluations.");
		System.out.println("Best kernel: " + bestState.toString() + " with alignment : " + bestAlignment);
		
		data.ResultWriter R = new data.ResultWriter("");
		R.writeResult();
		
		return bestAlignment;
	}
	
	public KernelParams randomStart( )
	{
		
		double alpha = Math.max(Math.random(),0.01);
		double s = Math.max(Math.random()*3,0.1);
		double b = Math.max(Math.random()*2,0.01);
		double d = Math.max((int) (Math.random() * 10),1);
		double g = Math.max(Math.random()*10,0.01);
		
		KernelParams state = new CompositeKernelParams(alpha,s,b,d,g);
		
		return state;
	}
}
