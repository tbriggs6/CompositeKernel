/*
 * Created on Sep 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kernels;

import java.util.Iterator;

import data.SparseVector;
import data.VectorTable;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CostEstimate {

    public static double est_deltaR_average(Kernel K, VectorTable X)
    {
        double sum = 0;
        
        SparseVector nullVector = new SparseVector(0);
        double kNN = K.eval(nullVector, nullVector);
        double numRows = X.getNumRows();
        
        Iterator rowIterator = X.iterator();
        while (rowIterator.hasNext())
        {
            SparseVector xRow = (SparseVector) rowIterator.next();
            double kXX = K.eval(xRow, xRow);
            double kXN = K.eval(xRow, nullVector);
            
            double l = kXX - 2*kXN + kNN;
            sum += Math.sqrt(l);
        }
        
        return  sum / numRows;
    }
    
    public static double est_CostFunction(Kernel K, VectorTable X)
    {
        double avg_delta_r = est_deltaR_average(K,X);
        
        return 1.0 / ( avg_delta_r * avg_delta_r);
    }
    
    
}
