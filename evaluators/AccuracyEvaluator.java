/*
 * Created on Sep 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evaluators;

import kernels.KernelParams;
import runSVM.Executor;


/**
 * @author tbriggs (yes it, really is me!)
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AccuracyEvaluator implements FitnessAlgorithm
{

    String command;
    Executor exec;
    
    public AccuracyEvaluator( String command )
    {
        this.command = command;
        this.exec = new Executor(command);
    }
    
    public data.TestResult fitness(KernelParams K)
    {
        return exec.doTest(K.toString());
    }
}
