/*
 * Created on Sep 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package junitTests;

import junit.framework.TestCase;
import kernels.CompositeProduct;
import kernels.CostEstimate;
import kernels.Kernel;
import kernels.Polynomial;
import kernels.RBF;
import data.FileReaderSVM;
import data.VectorTable;
/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class testEstCost extends TestCase {

    public final double DELTA = 0.001;
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(testEstCost.class);
    }
    
    public void testCostBreast( )
    {
        try {
	        String fileName = "/home/tbriggs/svm-work/breast/valid.txt";
	        FileReaderSVM F = new FileReaderSVM(fileName);
			VectorTable T = F.loadTable();
			
			Kernel K = (Kernel) new Polynomial(0.1,0.1,1);
			
			double svmC = 0.0035;
			double C = CostEstimate.est_CostFunction(K,T);
			
			if (Math.abs(C - svmC) > DELTA)
			{
			    fail("failed to match SVM's cost.  computed difference: " + 
			            Math.abs(C-svmC));
			}
        }
        catch(java.io.IOException E)
        {
            fail("could not load file");
        }
    }
    
    
    public void testCostIonosphere1( )
    {
        try {
	        String fileName = "/home/tbriggs/svm-work/ionosphere/valid.txt";
	        FileReaderSVM F = new FileReaderSVM(fileName);
			VectorTable T = F.loadTable();
			
			Kernel K = (Kernel) new Polynomial(0.1,0.1,1);
			
			double svmC = 0.8054;
			double C = CostEstimate.est_CostFunction(K,T);
			
			if (Math.abs(C - svmC) > DELTA)
			{
			    fail("failed to match SVM's cost.  computed difference: " + 
			            Math.abs(C-svmC));
			}
        }
        catch(java.io.IOException E)
        {
            fail("could not load file");
        }
    }

    public void testCostIonosphere2( )
    {
        try {
	        String fileName = "/home/tbriggs/svm-work/ionosphere/valid.txt";
	        FileReaderSVM F = new FileReaderSVM(fileName);
			VectorTable T = F.loadTable();
			
			Kernel K = (Kernel) new CompositeProduct((Kernel) new Polynomial(0.1,0.1,1),(Kernel) new RBF(0.001));;
			        
			
			double svmC = 0.8038;
			double C = CostEstimate.est_CostFunction(K,T);
			
			if (Math.abs(C - svmC) > DELTA)
			{
			    fail("failed to match SVM's cost.  computed difference: " + 
			            Math.abs(C-svmC));
			}
        }
        catch(java.io.IOException E)
        {
            fail("could not load file");
        }
    }

    
}
