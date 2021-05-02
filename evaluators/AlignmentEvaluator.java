/*
 * Created on Sep 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evaluators;

import kernels.KernelParams;
import alignment.Alignment;
import data.GramMatrix;
import data.VectorTable;
import data.*;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AlignmentEvaluator implements FitnessAlgorithm {
	
	VectorTable T;
	GramMatrix YY;
	
	public AlignmentEvaluator(VectorTable T)
	{
		this.T = T;
		this.YY = new GramMatrix(T);
		
	}
	
	public data.TestResult fitness(KernelParams K)
	{
		GramMatrix G = new GramMatrix(K.getKernel(),T);
		double alignment = Alignment.targetAlignment(G,YY);
		
		AlignmentResult R = new AlignmentResult(K.toString(), alignment);
		
		
		return (TestResult) R;
	}
	

}
