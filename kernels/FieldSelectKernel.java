/*
 * Created on Nov 4, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kernels;

import data.SparseVector;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FieldSelectKernel extends Kernel {

    Kernel kernel;
    long bitmask;
    int numFields;
    
    public FieldSelectKernel(long bitmask, int numFields, Kernel kernel) {
        
        this.bitmask = bitmask;
        this.kernel = kernel;
        this.numFields = numFields;
    }

    /* (non-Javadoc)
     * @see kernels.Kernel#eval(data.SparseVector, data.SparseVector)
     *
     */
    public double eval(SparseVector Xi, SparseVector Xj) {

        //TODO see if this code is correct - it may change the only copy
        // of the data that I have available!
        if (! Xi.isDirty()) {
            for (int i = 0; i < numFields; i++)
            {
                if (!isSet(i)) Xi.setValue(i,0);
            }
        }

        if (! Xj.isDirty()) {
            for (int i = 0; i < numFields; i++)
            {
                if (!isSet(i)) Xj.setValue(i,0);
            }
        }
        
        return kernel.eval(Xi, Xj);
        
    }

    
    private boolean isSet(int bit)
    {
        long v = 1;
        
        v = 1 << (bit);
        
        return  ((this.bitmask & v) > 0); 
    }
    
}
