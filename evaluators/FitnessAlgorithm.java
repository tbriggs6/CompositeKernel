/*
 * Created on Sep 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evaluators;

import kernels.KernelParams;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface FitnessAlgorithm
{

    public data.TestResult fitness(KernelParams K);
    
}
