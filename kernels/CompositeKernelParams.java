/*
/*
 * FILE:    KernelParams.java
 * PACKAGE: hillClimbing
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package kernels;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CompositeKernelParams implements KernelParams {
	
	boolean addition = true;
	double alpha, s, b, d, g;
	double c = 0;
	
	public static double deltas[] = { 0.05, 0.01, 0.01, 1.0, 0.002 ,0.001};
	public static double mins[] = { 0.01, 0.01, 0.01, 1.0, 0.001, 0.001};
	public static double maxs[] = { 1.0, 1.51, 1.51, 5.0, 6.0001 , 1.0};
	public static int numParams = mins.length;
	
	/*
	 * 
	 * (alpha * K1(s,b,d)) + ((1-alpha) * K2(g))
	 */
	public CompositeKernelParams(double alpha, double s, double b, double d, double g)
	{
	    this.addition = true; 
		this.alpha = alpha;
		this.s = s;
		this.b = b;
		this.g = g;
		this.d = d;
		this.c = 0.0;
		
		checkValues();
	}
	
	public CompositeKernelParams(boolean addition, double alpha, double s, double b, double d, double g)
	{
	    this(alpha,s,b,d,g);
		this.addition = addition;
		this.c = 0.0;
		
		checkValues();
	}
	
	public CompositeKernelParams(boolean addition, double alpha, double s, double b, double d, double g, double c)
	{
	    this(alpha,s,b,d,g);
	    this.addition = addition;
	    this.c = c;
	    
	    checkValues();
	}
	
	public CompositeKernelParams(double params[])
	{
		int i = 0;
		this.alpha = params[i++];
		this.s = params[i++];
		this.b = params[i++];
		this.d = params[i++];
		this.g = params[i++];
		
		if (i < params.length) 
		    this.c = params[i++];
		
		this.addition = true;
		
		checkValues();
	}
	
	public CompositeKernelParams(boolean addition, double params[])
	{
		this(params);
		this.addition = addition;
		
		checkValues();
	}
	
	public CompositeKernelParams(CompositeKernelParams K)
	{
	    this(K.getValues());
	    this.addition = K.addition;
	}
	
	
	public Kernel getKernel( )
	{
		Polynomial K1 = new Polynomial(s,b,d);
		RBF K2 = new RBF(g);
		
		CompositeScalarProduct T1 = new CompositeScalarProduct(alpha, K1);
		CompositeScalarProduct T2 = new CompositeScalarProduct((1-alpha), K2);

		if (addition)
			return new CompositeAddition(T1,T2);
		else
			return new CompositeProduct(T1,T2);
		
		
	}
	
	public String toString( )
	{
	    DecimalFormat D = new DecimalFormat("0.0000");
	    String S;
		if (addition)
			S = toStringAddition();
		else
			S = toStringProduct();
		
		if (c != 0.0) 
		{
		    S = S + "#" + D.format(c);
		}
		
		return S;
	}
	
	 
	private String toStringAddition( )
	{
		
		DecimalFormat F = new DecimalFormat("##0.000");
		DecimalFormat D = new DecimalFormat("#");
		
		return "(" + F.format(alpha) + " K1(" +
			D.format(d) + "," +
			F.format(s) + "," + 
			F.format(b) +  
			 ")+" +
			F.format((1-alpha)) + " K2(" + 
			F.format(g) + "))";
	}
	
	private String toStringProduct( )
	{
		
		DecimalFormat F = new DecimalFormat("##0.000");
		DecimalFormat D = new DecimalFormat("#");
		
		String krn1 = "K1(" + D.format(d) + "," + F.format(s) + 
		"," + F.format(s) + ")";
		
		String krn2 = "K2(" + F.format(g) + ")";
		
		String alpha1 = "( " + F.format(alpha) + " * " + krn1 + ")";
		String alpha2 = "( " + F.format(1-alpha) + " * " + krn2 + " ) ";
		String kernel = "( " + alpha1 + " * " + alpha2 + ")";

		return kernel;
	}
	
	public LinkedList expand( )
	{
		LinkedList children = new LinkedList( );
		
		double values[] = { alpha, s, b, d, g , c};
		
		for (int i = 0; i < values.length; i++)
		{
		    
			if ((values[i] - deltas[i]) > mins[i]) {
				double v2[] = copy(values);
				v2[i]-=deltas[i];
				KernelParams child = new CompositeKernelParams(addition, v2);
				children.add(child);
			}
			
			if ((values[i] + deltas[i]) < maxs[i]) {
				double v2[] = copy(values);
				v2[i]+=deltas[i];
				KernelParams child = new CompositeKernelParams(addition, v2);
				children.add(child);
			}
		}

		
		
		CompositeKernelParams child = new CompositeKernelParams(values);
		child.addition = ! child.addition;
		children.add(child);
		
		
		return children;
	}

	private double[] copy(double values[])
	{
		double v2[] = new double[values.length];
		for (int i = 0; i < values.length; i++)
			v2[i] = values[i];
		
		return v2;
	}
	
	
	public static CompositeKernelParams randomStart( )
	{
		
		boolean addition;
		if (Math.random() < 0.5) addition = true; 
		else addition = false;
		
		double newValues[] = new double[6];
		
		for (int i = 0; i < newValues.length; i++) {
		    do {
		        newValues[i] = Math.random( ) * maxs[i];

		    } while ((newValues[i] <= mins[i]) || (newValues[i] >= maxs[i]));
		}
		
	
		CompositeKernelParams state = new CompositeKernelParams(addition,newValues);
		
		return state;
	}
	/**
	 * @return Returns the addition.
	 */
	public boolean isAddition() {
		return addition;
	}
	/**
	 * @param addition The addition to set.
	 */
	public void setAddition(boolean addition) {
		this.addition = addition;
	}
	/**
	 * @return Returns the alpha.
	 */
	public double getAlpha() {
		return alpha;
	}
	/**
	 * @param alpha The alpha to set.
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	/**
	 * @return Returns the b.
	 */
	public double getB() {
		return b;
	}
	/**
	 * @param b The b to set.
	 */
	public void setB(double b) {
		this.b = b;
	}
	/**
	 * @return Returns the d.
	 */
	public double getD() {
		return d;
	}
	/**
	 * @param d The d to set.
	 */
	public void setD(double d) {
		this.d = d;
	}
	/**
	 * @return Returns the g.
	 */
	public double getG() {
		return g;
	}
	/**
	 * @param g The g to set.
	 */
	public void setG(double g) {
		this.g = g;
	}
	/**
	 * @return Returns the s.
	 */
	public double getS() {
		return s;
	}
	/**
	 * @param s The s to set.
	 */
	public void setS(double s) {
		this.s = s;
	}
	
	public double[] getValues( )
	{
		double D[] = { alpha, s, b, d, g , c};
		
		return D;
	}
	
	public boolean isEqual(KernelParams K2)
	{
	    CompositeKernelParams K = (CompositeKernelParams) K2;
	    
		if ((this.addition == K.addition) && (this.alpha == K.alpha) &&
			(this.b == K.b) && (this.d == K.d) &&
			(this.g == K.g) && (this.s == K.s) &&
			(this.c == K.c))
			return true;
		else
			return false;
		
	}
	
	
	private void checkValues( )
	{
	    double values[] = getValues( );
	    for (int i = 0; i < values.length; i++)
	    {
	        if ((values[i] < mins[i]) || values[i] > maxs[i]) {
	            showTable();
	            throw new RuntimeException("value " + i + " out of range");
	        }
	    }
	    
	    if (!(c != 0) && (c >= mins[5]) && (c <=maxs[5])) 
	            throw new RuntimeException("c out of range");
	}
    /**
     * @return Returns the c.
     */
    public static int getNumParams() {
        return numParams;
    }
    /**
     * @param c The c to set.
     */
    public static void setNumParams(int c) {
        numParams = c;
    }
    /**
     * @param c The c to set.
     */
    public void setC(double c) {
        if (c < mins[5]) c = mins[5];
        if (c > maxs[5]) c = maxs[5];
        
        this.c = c;
    }
    
    public double getC( ) { return this.c; }
    
    public void showTable( )
    {
        double values[] = getValues( );
        for (int i = 0; i < maxs.length; i++)
        {
            System.out.println(i + "\t" + mins[i] + "\t" + 
                    values[i] + "\t" +  maxs[i]);
        }
    }
}
