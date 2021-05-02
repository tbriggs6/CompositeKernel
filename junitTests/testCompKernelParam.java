/*
 * Created on Sep 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package junitTests;

import java.util.LinkedList;

import junit.framework.TestCase;
import kernels.CompositeKernelParams;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class testCompKernelParam extends TestCase {

    boolean addition = false;
    double alpha = 0.01;
    double s = 0.02;
    double b = 0.03;
    double d = 1.0;
    double gamma = 0.04;
    double c = 0.0;
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(testCompKernelParam.class);
    }

    private void checkVars(CompositeKernelParams KP)
    {
        if (KP.isAddition() != addition) fail("addition");
        if (KP.getAlpha() != alpha) fail("alpha");
        if (KP.getS() != s) fail("s");
        if (KP.getB() != b) fail("b");
        if (KP.getC() != c) fail("c");
        if (KP.getD() != d) fail("d");
        if (KP.getG() != gamma) fail("g");
        
    }
    
    /*
     * Class under test for void CompositeKernelParams(double, double, double, double, double)
     */
    public void testCompositeKernelParamsdoubledoubledoubledoubledouble() {

        c = 0.0;
        addition = true;
        
        CompositeKernelParams KP = new 
        	CompositeKernelParams(alpha,s,b,d,gamma);
        
        checkVars(KP);
        
    }

    /*
     * Class under test for void CompositeKernelParams(boolean, double, double, double, double, double)
     */
    public void testCompositeKernelParamsbooleandoubledoubledoubledoubledouble() {
        
        c = 0.00;
        addition = false;
        
        CompositeKernelParams KP = new 
        	CompositeKernelParams(addition,alpha,s,b,d,gamma);
        
        checkVars(KP);
    }

    /*
     * Class under test for void CompositeKernelParams(boolean, double, double, double, double, double, double)
     */
    public void testCompositeKernelParamsbooleandoubledoubledoubledoubledoubledouble() {
        
        c= 0.5;
        addition = false;
        
        CompositeKernelParams KP = new 
        	CompositeKernelParams(addition,alpha,s,b,d,gamma,c);
        
        checkVars(KP);
        
    }

    /*
     * Class under test for void CompositeKernelParams(double[])
     */
    public void testCompositeKernelParamsdoubleArray() {

        double vals[] = { alpha, s, b, d, gamma, c };
        addition = true;
        
        CompositeKernelParams KP = new
        	CompositeKernelParams(vals);
        
        checkVars(KP);
    }

    /*
     * Class under test for void CompositeKernelParams(boolean, double[])
     */
    public void testCompositeKernelParamsbooleandoubleArray() {
        double vals[] = { alpha, s, b, d, gamma, c };
        
        addition = false;
        
        CompositeKernelParams KP = new
        	CompositeKernelParams(addition,vals);

        checkVars(KP);
    }

    /*
     * Class under test for String toString()
     */
    public void testToString() {

        addition = false;
        c = 0.80;
        double vals[] = { alpha, s, b, d, gamma, c };
        
        addition = false;
        
        CompositeKernelParams KP = new
        	CompositeKernelParams(addition,vals);
        
        checkVars( KP );
        
        String S = KP.toString();
        String should = "((0.010*K1(1,0.020,0.030))*((0.990*K2(0.040)))#0.8000";
        if (!S.equals(should))
            fail("error: " + S + " <> " + should); 
    }

    private void showChildren( LinkedList children) 
    {
        java.util.Iterator I = children.iterator();
        while (I.hasNext())
        {
            System.err.println(I.next());
        }
    }
    
    
    public void testExpand() {

        String src = "(0.353 K1(2,0.461,0.929)+0.647K2(0.532))#0.0595";
        double vals[] = { 0.353, 0.461, 0.929, 2.0, 0.532, 0.0595 };
        CompositeKernelParams KP = new
        	CompositeKernelParams(true,vals);
        
        LinkedList children = KP.expand();
        if (children.size() != 13) {
            
            showChildren(children);
            fail("size should was " + children.size());
        }
        
		String childrenStr[] = {
		        // negative adjustments
		        "(0.303 K1(2,0.461,0.929)+0.697K2(0.532))#0.0595", // adjust alpha
		        "(0.403 K1(2,0.461,0.929)+0.597K2(0.532))#0.0595", // adjust alpha
		        
		        "(0.353 K1(2,0.451,0.929)+0.647K2(0.532))#0.0595", // adjust s
		        "(0.353 K1(2,0.471,0.929)+0.647K2(0.532))#0.0595", // adjust s
		        
		        "(0.353 K1(2,0.461,0.919)+0.647K2(0.532))#0.0595", // adjust b
		        "(0.353 K1(2,0.461,0.939)+0.647K2(0.532))#0.0595", // adjust b
		        
		        "(0.353 K1(1,0.461,0.929)+0.647K2(0.532))#0.0595", // adjust d
		        "(0.353 K1(3,0.461,0.929)+0.647K2(0.532))#0.0595", // adjust d
		        
		        "(0.353 K1(2,0.461,0.929)+0.647K2(0.530))#0.0595", // adjust gamma
		        "(0.353 K1(2,0.461,0.929)+0.647K2(0.534))#0.0595", // adjust gamma
		        
		        "(0.353 K1(2,0.461,0.929)+0.647K2(0.532))#0.0594", // adjust c
		        "(0.353 K1(2,0.461,0.929)+0.647K2(0.532))#0.0596", // adjust c

		        // flip addition
		        "((0.353*K1(2,0.461,0.929))*((0.647*K2(0.532)))#0.0595" 
		        };
		
		boolean fail = false;
		for (int i = 0; i < children.size(); i++)
		{
		    String s = children.get(i).toString();
		    if (!s.equals(childrenStr[i])) {
		        System.err.println("child: " + s + "\tshould: " + childrenStr[i]);
		        fail = true;
		    }
		}
		        
		if (fail) fail();
		
          
    }

    public void testIsEqual() {
        addition = false;
        c = 0.80;
        double vals[] = { alpha, s, b, d, gamma, c };
        
        addition = false;
        
        CompositeKernelParams KP = new
        	CompositeKernelParams(addition,vals);
        
        checkVars( KP );
        
        CompositeKernelParams KP2 = new
    		CompositeKernelParams(addition,vals);
        
        checkVars(KP2);
        
        if (!KP2.isEqual(KP)) { fail( ); }
        if (!KP.isEqual(KP)) { fail( ); }

        
    }

}
