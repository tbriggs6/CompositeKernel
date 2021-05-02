/*
/*
 * FILE:    Gene.java
 * PACKAGE: evolutionaryAlgorithm
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 14, 2004
 */ 
package evolutionaryAlgorithm;

import kernels.CompositeKernelParams;
import data.*;


/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Gene implements Comparable {
	
	CompositeKernelParams params;
	TestResult fitness;
	
	public Gene(CompositeKernelParams P)
	{
		this.params = P;
	}
	
	public Gene( )
	{
		this.params = CompositeKernelParams.randomStart();
	}
	
	public CompositeKernelParams getParams( )
	{
		return params;
	}
	
	public TestResult getFitness( )
	{
		return fitness;
	}

	public double getSimpleFitness( )
	{
	    return fitness.getSimpleFitness();
	        
	}
	
	public void setFitness(TestResult fitness)
	{
		this.fitness = fitness;
	}
	
	public int compareTo(Object O)
	{
		Gene G = (Gene) O;
		
		return fitness.compareTo(G.fitness);
	}
	
	public Gene crossOver(Gene mother)
	{
		
		CompositeKernelParams f = this.getParams();
		CompositeKernelParams m = mother.getParams();
		
		boolean addition;
		if (f.isAddition()) addition = true;
		else addition = false;
		
		double fV[] = f.getValues();
		double mV[] = m.getValues();
		double cV[] = new double[ fV.length ];
		
		for (int i = 0; i < fV.length; i++) 
			cV[i] = geometric(fV[i], mV[i]);
		
		cV[3] = (int) cV[3];
		
		CompositeKernelParams c = new CompositeKernelParams(addition, cV);
		return new Gene(c);
		
	}
	
	public void mutate( )
	{
		double maxs[] = { 1.0, 3.0, 3.0, 10.0, 6.0 };
		
		int field = (int)(Math.random() * 6.0);
		if (field == 0) 
			params.setAddition( ! params.isAddition());
		else if (field == 1)
			params.setAlpha(Math.random() * maxs[0]);
		else if (field == 2)
			params.setB(Math.random() * maxs[1]);
		else if (field == 3)
			params.setD((int) Math.random() * maxs[2]);
		else if (field == 4)
			params.setG(Math.random() * maxs[3]);
		else if (field == 5)
			params.setS(Math.random() * maxs[4]);
		
	}
	
	
	private double midPoint(double x, double y)
	{
		return (x + y)/2;
	}
	
	private double geometric(double x, double y)
	{
		return Math.sqrt(x * y);
	}
	
	public String toString( )
	{
	    if (fitness == null)
	        return "(null)";
	    else 
	        return fitness.toString();
	}
}
