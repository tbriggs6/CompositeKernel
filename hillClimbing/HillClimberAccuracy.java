/*
 */
/*
 * FILE:    HillClimber.java
 * PACKAGE: hillClimbing
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package hillClimbing;

import java.util.Iterator;
import java.util.LinkedList;

import kernels.CompositeKernelParams;
import kernels.SimpleKernelParams;
import kernels.CostEstimate;
import kernels.Kernel;
import kernels.KernelParams;
import data.FileReaderSVM;
import data.ResultWriter;
import data.SVMTestResult;
import data.TestResult;
import data.VectorTable;
import evaluators.AccuracyEvaluator;


/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class HillClimberAccuracy {
    
    static double EPSILON =0.0000000001; 
    VectorTable T;
    SVMTestResult bestResult = null;
    KernelParams bestState = null;
    
    String fileName;
    
    int kernelEvals = 0;
    int cacheHits = 0;
    AccuracyEvaluator E;	
    
    double estCostTradeoff;
    
    
    public static void main(String[] args) {
        
        if (args.length != 3)
        {
            System.err.println("Error: run as java hillClimbing/HillClimberAccuracy command DIR/valid-train.txt nRestarts");
            return;
        }
        HillClimberAccuracy HC = new HillClimberAccuracy(args[0],args[1]);
        TestResult result = HC.climbHills( Integer.parseInt(args[2]));
    }
    
    public HillClimberAccuracy( String command , String fileName )
    {
	System.out.println("Using " + command + " for estimation.");
        E = new AccuracyEvaluator(command);
        this.fileName = fileName;
    }
    
    /* 
     * Hill Climbing agent:
     * (alpha1 * K1(s,b,d)) + (1-alpha1) * K2(g))
     */
    public TestResult climbHills(int maxIterations )
    {
        java.text.DecimalFormat D = new java.text.DecimalFormat("0.0000");
                
        bestState = null;
        bestResult = null;
        
        loadTable( );
        
        for (int i = 0; i < maxIterations; i++)
        {
            System.out.println("Starting iteration " + i +
                    " evals so far: " + kernelEvals);
            if (i > 0) {
                System.out.println("The best kernel so far: " + bestState.toString());
                System.out.println("The best alignment so far: " + bestResult);
            }
            
            climbHillsIteration( );
        }
        
        System.out.println("The best kernel: " + bestState.toString());
        System.out.println("The best alignment: " +bestResult);
        
        ResultWriter R = new ResultWriter(bestResult.toString());
        R.writeResult();
        
        return bestResult;
    }
    
    /**
     * display the current results from an iteration of the hill climber
     * @param currResult - the current results
     * @param curr - the current kernel
     * @param iterBest - the iteration's best results
     * @param overallBest - the overall best results
     * @param delta - the difference in accuracy
     */
    private void showState( SVMTestResult currResult, KernelParams curr, 
            SVMTestResult iterBest, SVMTestResult overallBest)
    {
        java.text.DecimalFormat D = new java.text.DecimalFormat("0.000000");    
        System.out.print("  ");
        System.out.print(currResult);
    
        if (currResult.isBadResult()) { System.out.print("!"); }
        System.out.print("\t");
        System.out.print(curr);
        System.out.print(" ");
        
        if (iterBest == null)
        {
            System.out.println("(first)");
        }
        else {
            if (currResult.compareTo(iterBest) > 0) 
                System.out.print("(i!)");
        }
        
        if ((overallBest != null) &&
            (currResult.compareTo(overallBest) > 0))  
            System.out.print("(o!)");
            
        System.out.println("");
    }
    
    
    
    /**
     * Handle a single iteratioin of the hill climbing algorithm.
     * A random state is selected, and the search commences by expanding
     * the random state into its neighbors (KernelParams.expand( )).  
     * 
     * @see KernelParams.randomize()
     * 
     * @return SVMResult bestResult - the results of the best kernel
     */
    public TestResult climbHillsIteration( )
    {
        boolean continueRunning = false;
        
        KernelParams state = randomStart( );
        
        SVMTestResult bestIterationResult = null;
        KernelParams bestIterationState = null;
        KernelParams lastState = state;
        
        boolean done = true;
        
        
        
        do {
            System.out.println("======================================");
            System.out.println("best overall:\t" + bestState + "\t" + bestResult);
            System.out.println("best iteteration:\t" + bestIterationState + "\t" + bestIterationResult);
            System.out.println("expanding: " + lastState);
            
            done = true;	// set to end the loop - will restart if found better one
            
            LinkedList children = lastState.expand( );
            
            Iterator I = children.iterator();
            while (I.hasNext())
            {
                KernelParams tmpState = (KernelParams) I.next();
                SVMTestResult tmpResult = (data.SVMTestResult) E.fitness(tmpState);
                
                this.kernelEvals++;
                
                if (bestIterationResult == null) {
                    bestIterationResult = tmpResult;
                    bestIterationState = tmpState;
                    
                    if (bestResult == null) {
                        bestResult = tmpResult;
                        bestState = tmpState;
                    } // end if bestAlignment
                    done = false;
                    continue;
                } // end if bestIterAlignment
                
                if (tmpResult.compareTo(bestIterationResult) > 0)
                {
                    bestIterationResult = tmpResult;
                    bestIterationState = tmpState;
                    done = false;	// keep the loop alive.
                    
                    if (tmpResult.compareTo(bestResult) > 0)
                    {
                        bestResult = tmpResult;
                        bestState = tmpState;
                    }
                }
                
                this.showState(tmpResult,tmpState,bestIterationResult, bestResult);
            } // end while Iterator loop
            
        } while( ! done );  // end do while loop
        
        return bestResult;
    }
    
    
    private double estimateC( Kernel K, VectorTable T)
    {
        return CostEstimate.est_CostFunction(K,T);
    }
    
    private void loadTable( )
    {
        try {
            FileReaderSVM F = new FileReaderSVM(fileName);
            this.T = F.loadTable();
        }
        catch(java.io.IOException E)
        {
            E.printStackTrace( );
            System.exit(-1);
        }
    }
    
    public KernelParams randomStart( )
    {
        
	CompositeKernelParams state = CompositeKernelParams.randomStart();
	//SimpleKernelParams state = new SimpleKernelParams(Math.random() * 3);

        double c = estimateC(state.getKernel(),T);
        state.setC(c);
        
        return (KernelParams) state;
    }
}
