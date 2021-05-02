/*
 * Created on Sep 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kernels;

import java.util.LinkedList;


/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SimpleKernelParams implements KernelParams {

    int type;
    int d;
    double w,b,gamma;
    double c;
    
    public SimpleKernelParams( int type )
    {
        this.type = type;
        this.d = 1;
        this.w = 0.1;
        this.b = 0.1;
        this.gamma = 0.1;
    }
    
    
    public SimpleKernelParams( )
    {
        this.type = 0;
        this.d = 0;
        this.w = this.b = this.gamma = 0;
    }
    
    public SimpleKernelParams(int d, double w, double b)
    {
        if ((d <= 0) || (w <= 0) || (b <= 0)) {
            throw new RuntimeException("Invalid parameters: " + d + " " + w + " " + b);
        }
        
        this.type = 1;
        this.d = d;
        this.w = w;
        this.b = b;
        this.gamma = 0;
    }
   
    public SimpleKernelParams(double gamma)
    {
       this(gamma,0.5);
    }

    public SimpleKernelParams(double gamma, double c) 
    {
        if (gamma < 0) {
            throw new RuntimeException("Invalid parameters");
        }
        this.type = 2;
        this.gamma = gamma;
        this.d = 0;
        this.w = this.b = 0;
	this.c = c;
    }
    
    public void setC(double c)
    { 
	this.c = c;
    }

    
    public Kernel getKernel() {
        
        switch(this.type) {
        	case 0:
        	    return new Linear();
        	case 1:
        	    return new Polynomial(w,b,d);
        	case 2:
        	    return new RBF(gamma);
        	default:
        	    throw new RuntimeException("Unknown kernel type");
        }
        
        
    }

    /* (non-Javadoc)
     * @see kernels.KernelParams#expand()
     */
    public LinkedList expand() {
     
        switch(this.type) {
    	case 0:
    	    return new LinkedList( );
    	case 1:
    	    return expandPolynomial( );
    	case 2:
    	    return expandRBF( );
    	default:
    	    throw new RuntimeException("Unknown kernel type");
    }
    }

    
    private LinkedList expandPolynomial( )
    {
    
        LinkedList L = new LinkedList( );
        
        if (d < 10)
            L.add( new SimpleKernelParams(d+1,w,b));
        
        if (w < 3.0)
            L.add( new SimpleKernelParams(d,w+0.01,b));
        
        if (b < 3.0) 
            L.add( new SimpleKernelParams(d,w,b+0.01));
        
        if (d >= 2)
            L.add( new SimpleKernelParams(d-1,w,b));
        
        if (w >= 0.2)
            L.add( new SimpleKernelParams(d,w-0.01,b));
        
        if (b >= 0.2)
            L.add( new SimpleKernelParams(d,w,b-0.01));
        
        return L;
    }
    
    private LinkedList expandRBF( )
    {
        LinkedList L = new LinkedList( );
        
        L.add( new SimpleKernelParams(gamma + 0.001));

	if (gamma > 0.02)
           L.add( new SimpleKernelParams(gamma - 0.001));

        L.add( new SimpleKernelParams(gamma,c - 0.01));

	if (gamma > 0.02)
           L.add( new SimpleKernelParams(gamma,c - 0.01));
        
        return L;
    }
    
    /* (non-Javadoc)
     * @see kernels.KernelParams#isEqual(kernels.CompositeKernelParams)
     */
    public boolean isEqual(KernelParams K2) {
        
        SimpleKernelParams K = (SimpleKernelParams) K2;
        
        if ( (this.type == K.type) && (this.d == K.d) && (this.w == K.w) &&
              (this.b == K.b) && (this.gamma == K.gamma))
            return true;
        else 
            return false;

    }

   public String toString( )
   {
       switch(type) {
       case 0:
           return toStringLinear( );
       case 1:
           return toStringPolynomial();
       case 2:
           return toStringRBF( );
       default:
           return "error";
       }
   }
    
   private String toStringLinear( )
   {
       return "K0()";
   }
   
   private String toStringPolynomial()
   {
       java.text.DecimalFormat D = new java.text.DecimalFormat("0.000");
       
       return "K1(" + d + "," +
       	D.format(w) + "," +
       	D.format(b) + ")";
   }
   
   private String toStringRBF( )
   {
       java.text.DecimalFormat D = new java.text.DecimalFormat("0.000");
       
       return "K2(" + D.format(gamma) + ")#" + D.format(c);
   }
    
   
   public void bottomStart( )
   {
       
       
       
   }
}
