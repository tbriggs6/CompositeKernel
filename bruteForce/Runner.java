/*
 * Created on Sep 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package bruteForce;

import java.io.IOException;
import java.text.DecimalFormat;

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
public class Runner {

    VectorTable T;
    int type;
    boolean useAlignment;
    
    public static void main(String[] args) {
		
		if (args.length != 2)
		{
		    throw new RuntimeException("run as java bruteForce/Runner file type (1=poly,2=rbf)");
		}
		Runner R = new Runner(args[0],Integer.parseInt(args[1]));
		double result = R.search(  );
	}
	
	public Runner( String fileName, int type )
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

	public double search( )
	{
	    if (this.type == 1)
	        return searchPolynomial();
	    else
	        return searchRBF( );
	}
	
	
	public void setAlignment(boolean alignment)
	{
	    this.useAlignment = alignment;
	}
	
	public double searchPolynomial( )
	{
	    
	    SimpleKernelParams best = null;
	    double bestAlign = 0;
	    int kernelEvals = 0;
	    int numBest = 0;
	    
	    GramMatrix YY = new GramMatrix(T);
	    DecimalFormat D = new DecimalFormat("0.0000");
	    
	    for (int d = 1; d < 4; d++)
	    {
	        for (double w = 0.1; w < 12.0; w+=0.3)
	        {
	            for (double b = 0.1; b < 12.0; b+=0.3)
	            {
	                kernelEvals++;
	                
	                SimpleKernelParams K = new SimpleKernelParams(d,w,b);
	                
	                GramMatrix tmpG = new GramMatrix(K.getKernel(),T);
					double tmpAlign = Alignment.targetAlignment(tmpG, YY);

					System.out.println(D.format(tmpAlign) + "\t" + K.toString()  );
					
					if (tmpAlign > bestAlign) {
					    best = K;
					    bestAlign = tmpAlign;
					    numBest = 1;
					}
					else if (tmpAlign == bestAlign)
					    numBest++;
	            }
	        }
	    }
	    
	    
	    System.out.println("Best kernel: " + best.toString() );
	    System.out.println("   alignment: " + D.format(bestAlign));
	    System.out.println("   num best kernels: " + numBest);
	    System.out.println("Kernel evals: " + kernelEvals);
	    
	    return bestAlign;
	}
	
	public double searchRBF( )
	{
	    
	    SimpleKernelParams best = null;
	    double bestAlign = 0;
	    int kernelEvals = 0;
	    int numBest = 0;
	    
	    GramMatrix YY = new GramMatrix(T);
	    DecimalFormat D = new DecimalFormat("0.00000");
	    
        for (double gamma = 0.0001; gamma <= 2; )
        {
            kernelEvals++;
            
            SimpleKernelParams K = new SimpleKernelParams(gamma);
            
            GramMatrix tmpG = new GramMatrix(K.getKernel(),T);
			double tmpAlign = Alignment.targetAlignment(tmpG, YY);

			System.out.println(D.format(tmpAlign) + "\t" + D.format(gamma) + "\t" + K.toString()  );
			
			if (tmpAlign > bestAlign) {
			    best = K;
			    bestAlign = tmpAlign;
			    numBest = 1;
			}
			else if (tmpAlign == bestAlign)
			    numBest++;
			
			if (gamma > 10)
			    gamma += 1;
			else if (gamma > 2.0) 
			    gamma += 0.5;
			else if (gamma > 0.2)
			    gamma += 0.01;
			else 
			    gamma += 0.0001;
			
			    
        }
	    
	    
	    System.out.println("Best kernel: " + best.toString() );
	    System.out.println("   alignment: " + D.format(bestAlign));
	    System.out.println("   num best kernels: " + numBest);
	    System.out.println("Kernel evals: " + kernelEvals);
	    
	    return bestAlign;
	}
	
	
}
