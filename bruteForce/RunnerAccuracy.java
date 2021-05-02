/*
 * Created on Sep 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package bruteForce;

import java.text.DecimalFormat;

import data.SVMTestResult;

import kernels.SimpleKernelParams;
import evaluators.AccuracyEvaluator;
import data.*;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class RunnerAccuracy {

    AccuracyEvaluator E;
    int type;
        
    public static void main(String[] args) {
		
		if (args.length != 2)
		{
		    throw new RuntimeException("run as java bruteForce/Runner command type (1=poly,2=rbf)");
		}
		RunnerAccuracy R = new RunnerAccuracy(args[0],Integer.parseInt(args[1]));
		TestResult result = R.search(  );
	}
	
	public RunnerAccuracy( String command, int type )
	{
		
		this.type = type;
		E = new AccuracyEvaluator(command);
	}

	public TestResult search( )
	{
	    if (this.type == 1)
	        return searchPolynomial();
	    else
	        return searchRBF( );
	}
	
	
	public TestResult searchPolynomial( )
	{
	    
	    SimpleKernelParams best = null;
	    SVMTestResult bestAccuracy = null;
	    int kernelEvals = 0;
	    int numBest = 0;
	    
	    for (int d = 1; d < 4; d++)
	    {
	        for (double w = 0.1; w < 3.0; w+=0.3)
	        {
	            for (double b = 0.1; b < 3.0; b+=0.3)
	            {
	                kernelEvals++;
	                
	                SimpleKernelParams K = new SimpleKernelParams(d,w,b);
	                
					SVMTestResult tmpAccuracy = (SVMTestResult) E.fitness(K);

					System.out.print(tmpAccuracy);
					
					
					if ((best==null) ||
					    (tmpAccuracy.compareTo(bestAccuracy) > 0))  {
					    best = K;
					    bestAccuracy = tmpAccuracy;
					    numBest = 1;
					    
					    System.out.print("*");
					}
					else if (tmpAccuracy.compareTo(bestAccuracy) == 0)
					    numBest++;
					
					System.out.println("\t" + K.toString()  );
	            }
	        }
	    }
	    
	    
	    System.out.println("Best kernel: " + best.toString() );
	    System.out.println("   accuracy: " + bestAccuracy.toString());
	    System.out.println("   num best kernels: " + numBest);
	    System.out.println("Kernel evals: " + kernelEvals);
	    
	    ResultWriter R = new ResultWriter( bestAccuracy.toString() );
	    R.writeResult();
	    
	    
	    return bestAccuracy;
	}
	
	public TestResult searchRBF( )
	{
	    SimpleKernelParams best = null;
	    data.TestResult bestAccuracy = null;
	    int kernelEvals = 0;
	    int numBest = 0;
	    
	    DecimalFormat D = new DecimalFormat("0.00000");
	    
        for (double gamma = 0.001; gamma <= 2; )
        {
            kernelEvals++;
            
            SimpleKernelParams K = new SimpleKernelParams(gamma);
            
            data.TestResult tmpAccuracy = E.fitness(K);

			//System.out.println(D.format(tmpAccuracy) + "\t" + D.format(gamma) + "\t" + K.toString()  );
			System.out.print(tmpAccuracy);
			
			if ((best==null) || (tmpAccuracy.compareTo(bestAccuracy) > 0)) {
			    best = K;
			    bestAccuracy = tmpAccuracy;
			    numBest = 1;
			    System.out.print("*");
			}
			else if (tmpAccuracy == bestAccuracy)
			    numBest++;
			
			System.out.println("\t" + K);
			
			if (gamma > 10)
			    gamma += 1;
			else if (gamma > 2.0) 
			    gamma += 0.5;
			else if (gamma > 0.2)
			    gamma += 0.01;
			else 
			    gamma += 0.001;
			
			    
        }
	    
	    
	    System.out.println("Best kernel: " + best.toString() );
	    System.out.println("   accuracy: " + bestAccuracy.toString());
	    System.out.println("   num best kernels: " + numBest);
	    System.out.println("Kernel evals: " + kernelEvals);

	    ResultWriter R = new ResultWriter( bestAccuracy.toString() );
	    R.writeResult();
	    
	    return bestAccuracy;
	}
	
	
}
