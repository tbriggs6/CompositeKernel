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

import kernels.KernelParams;
import kernels.SimpleKernelParams;
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
public class HillClimberSimple {

	static double EPSILON =0.000000000000001; 
	VectorTable T;
	double bestAlignment = 0;
	KernelParams bestState = null;
	
	int kernelEvals = 0;
	int cacheHits = 0;
	int type = 0;
		
	public static void main(String[] args) {
		
		if (args.length != 2)
		{
		    throw new RuntimeException("run as java HillClimberSimple file type (1=poly,2=rbf)");
		}
		HillClimberSimple HC = new HillClimberSimple(args[0],Integer.parseInt(args[1]));
		double result = HC.climbHills(  );
	}
	
	public HillClimberSimple( String fileName, int type )
	{
		
		try {
			FileReaderSVM F = new FileReaderSVM(fileName);
			T = F.loadTable();
			this.type = type;
			
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
		java.text.DecimalFormat D = new java.text.DecimalFormat("0.0000");
		int maxIterations = 10;

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
		
		return bestAlignment;
	}
	
	public KernelParams randomStart( )
	{
		
	    if (type == 1) {
	        int d = (int) ( Math.random() * 10);
	        double w = Math.max(Math.random() * 3,0.1);
	        double b = Math.max(Math.random() * 2,0.1);
	        
	        return new SimpleKernelParams(d,w,b);
	    }
	    else if (type == 2) {
	        double gamma = Math.max(Math.random() * 10, 0.1);
	        
	        return new SimpleKernelParams(gamma);
	    }
	        
		else {
		    throw new RuntimeException("Unknown type");
		}
	}
}
