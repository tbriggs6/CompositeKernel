/*
 */
 /*
  * FILE:    HillClimber.java
  * PACKAGE: hillClimbing
  * PROJECT:	CSC110
  * AUTHOR:  Tom Briggs (c) 2004
  * DATE:    Sep 12, 2004
  */ 
package runSVM;

import java.io.IOException;
import kernels.*;
import kernels.CompositeKernelParams;
import kernels.KernelParams;
import kernels.SimpleKernelParams;
import alignment.Alignment;
import data.FileReaderSVM;
import data.GramMatrix;
import data.SVMTestResult;
import data.VectorTable;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Runner {
	
	VectorTable T;
	double bestAlignment = 0;
	KernelParams bestState = null;
	int kernelEvals = 0;
	int nTests = 0;
	Executor GA,CV;
	
	public static void main(String[] args) {
		
	    if (args.length != 2) 
	    {
	        throw new RuntimeException("Run as: java runSVM/Runner ntests data-set");
	    }
		
	    String db = args[1];
	    String home = "/home/tbriggs/svm-work/";
	    String file = home + db + "/valid.txt";
	    String gaeval = home + db + "/valid-runner.sh";
	    String cveval = home + db + "/test-runner.sh";
	    
	    int n = Integer.parseInt(args[0]);
		Runner R = new Runner(n, file, gaeval, cveval);
		R.random(  );
		//R.maxK1();
	}
	
	public Runner( int numTests, String fileName , String command, String cvCommand )
	{
		
		try {
		     
		    this.nTests = numTests;
		    
			FileReaderSVM F = new FileReaderSVM(fileName);
			T = F.loadTable();
			GA = new Executor(command);
			CV = new Executor(cvCommand);
			
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
	public void random( )
	{
		
		java.text.DecimalFormat D = new java.text.DecimalFormat("#.######");
		bestState = null;
		bestAlignment = 0;
		
		GramMatrix YY = new GramMatrix(T);
		
		int i = 0;
		while (i < nTests)
		{
			KernelParams state = CompositeKernelParams.randomStart( );
			Kernel Kern = state.getKernel( );
	//		GramMatrix K = new GramMatrix(Kern,T);

	//		GramMatrix tmpG = new GramMatrix(state.getKernel(),T);
	//		double tmpAlign = Alignment.targetAlignment(tmpG, YY);
			double tmpAlign = 0;
			if (tmpAlign > bestAlignment) {
				bestAlignment = tmpAlign;
				bestState = state;
			}
			kernelEvals++;

			SVMTestResult testAcc = GA.doTest(state.toString());
			SVMTestResult cvAcc = CV.doTest(state.toString());
			 
			if ((testAcc.getAccuracy() > 0) && (cvAcc.getAccuracy() > 0))
			{
			    i++;
				System.out.println(i + "\t" + 
				        D.format(tmpAlign) + "\t" + 
				        D.format(testAcc.getAccuracy()) + "\t" +
				        D.format(testAcc.getSimpleFitness()) + "\t" +
				        D.format(cvAcc.getAccuracy()) + "\t" 
				        );
			}
		}
		
				
	}

	public void maxK1( )
	{
		
		java.text.DecimalFormat D = new java.text.DecimalFormat("0.00000");
		bestState = null;
		bestAlignment = 0;
		
		GramMatrix YY = new GramMatrix(T);
		
		for (int i = 2; i < Integer.MAX_VALUE; i=i * 2) {
			KernelParams state = new SimpleKernelParams(1,0.1,(double) i);
			GramMatrix K = new GramMatrix(state.getKernel(),T);
			
			double tmpAlign = Alignment.targetAlignment(K, YY);
			
			System.out.println(D.format(tmpAlign) + "\t" + state.toString());
		}
	

	}
}
