/*
 * Created on Sep 29, 2004
 *
 */
package data;

import java.util.Date;

/**
 * Store the result of a test on a kernel.  Implements the comparable 
 * interface to allow for simple comparisons and natural ordering of 
 * test results.  Implements the TestResult to allow for interchangability
 * with other target metrics. 
 * 
 * @author tbriggs
 */
/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SVMTestResult implements Comparable, TestResult {
    
    /**
     * Comment for <code>accuracy,errors,vcdim,numLines,errEst</code> - 
     * represents the observedtraining accuracy against a training data set.  
     * The observation comes from executing the SVM<sup>Lite</sup> software to 
     * learn and test a model.  The parameters are evaluated by using the 
     * runSVM.Executor test model and test framework  built in ~/svm-work 
     * directories.
     */
    double accuracy;	
    double errors;
    double vcdim;
    double var;
    double dev;
    int numLines;
    double errEst;
    
    /**
     * Comment for <code>badResult</code> is set when either the underlying
     * test-runner failed; or when one of the results exceeds a pre-set limit
     * (eg. accuracy is negative).
     */
    boolean badResult = false;
    
    /**
     * Comment for <code>kernel</code> the string representation of the
     * kernel that generated this result.
     */
    String kernel;
    
    /**
     * Construct test result given all of the parameters (except numLines)
     * @param kernel the kernel that generated the results
     * @param accuracy the observed training accuracy
     * @param errors the observed number of errors during training
     * @param vcdim the observed estimated vc dimension reported by SVM<sup><I>lite</I></sup>
     * @param errEst the estimated XiAlpha upper bound error rate
     */
    public  SVMTestResult(String kernel, double accuracy, double errors, double vcdim, double errEst)
    {
        this.kernel = kernel;
        this.accuracy = accuracy;
        this.errors = errors;
        this.vcdim = vcdim;
        this.errEst = errEst;
        this.var = 0;
        this.dev = 0;
        
        if (accuracy > 1.0) { this.accuracy = accuracy / 100.0; }
        if (errEst > 1.0) { this.errEst = errEst / 100.0; }
        
        if (vcdim <= 0) badResult = true;
        if (accuracy <= 0) badResult = true;
    }
    
    
    /**
     * Construct test result given all of the parameters
     * @param kernel the kernel that generated the results
     * @param accuracy the observed training accuracy
     * @param errors the observed number of errors during training
     * @param vcdim the observed estimated vc dimension reported by SVM<sup><I>lite</I></sup>
     * @param numLines the number of entities in the training file
     * @param errEst the estimated XiAlpha upper bound error rate
     */
    public  SVMTestResult( String kernel, double accuracy, double errors, double vcdim, int numLines, double errEst)
    {
        this.kernel = kernel;
        this.accuracy = accuracy;
        this.errors = errors;
        this.vcdim = vcdim;
        this.numLines = numLines;
        this.errEst = errEst;
        this.var = 0;
        this.dev = 0;
        if (accuracy > 1.0) { this.accuracy = accuracy / 100.0; }
        if (errEst > 1.0) { this.errEst = errEst / 100.0; }
        
        if (errors < 0) badResult = true;
        if (vcdim <= 0) badResult = true;
        if (accuracy <= 0) badResult = true;
    }
    
    /**
     * Construct test result given all of the parameters
     * @param kernel the kernel that generated the results
     * @param accuracy the observed training accuracy
     * @param errors the observed number of errors during training
     * @param vcdim the observed estimated vc dimension reported by SVM<sup><I>lite</I></sup>
     * @param numLines the number of entities in the training file
     * @param errEst the estimated XiAlpha upper bound error rate
     */
    public  SVMTestResult( String kernel, double accuracy, double errors, double vcdim, int numLines, double errEst, double var, double dev)
    {
        this.kernel = kernel;
        this.accuracy = accuracy;
        this.errors = errors;
        this.vcdim = vcdim;
        this.numLines = numLines;
        this.errEst = errEst;
        this.var = var;
        this.dev = dev;
        
        if (accuracy > 1.0) { this.accuracy = accuracy / 100.0; }
        if (errEst > 1.0) { this.errEst = errEst / 100.0; }
        
        if (errors < 0) badResult = true;
        if (vcdim <= 0) badResult = true;
        if (accuracy <= 0) badResult = true;
    }
    

    
    /**
     * Evaluate a simple, scalar value in [ 0, 1.0 ] representing the fitness
     * of the given test result.  The value is computed as a function of
     * the observed accuracy (<i>A</i>) and the estimated error rate (<i>E</i>).
     * The fitness (<i>F</i>) is computed as: <BR>
     * <i>F=(0.5 A) + (0.5 E)
     * 
     * @return fitness value
     * @see data.TestResult#getSimpleFitness()
     */
    public double getSimpleFitness( )
    {
        double fitness = 0;
        double pcntErr = 0.5;
 
        if (isBadResult()) fitness = -1.0;
        else  fitness = ((1.0 - errEst) * pcntErr) + (accuracy * (1 - pcntErr));
        
        return fitness;
    }
    
    
    /**
     * Return the observed training accuracy for the given model
     * @return Returns the accuracy.
     */
    public double getAccuracy() {
        return accuracy;
    }
    /**
     * Set the observed accuracy to a new value
     * @param accuracy The accuracy to set.
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
        
        if (accuracy > 1) { this.accuracy = this.accuracy / 100.0; }
    }
    
    
    /**
     * Determine if the result is a bad result (violates class pre-conditons)
     * @return Returns the badResult.
     */
    public boolean isBadResult() {
        return badResult;
    }
    
    /**
     * Set the bad result status to the indicate value.
     * @param badResult The badResult to set.
     */
    public void setBadResult(boolean badResult) {
        this.badResult = badResult;
    }
    /**
     * @return Returns the errors.
     */
    public double getErrors() {
        return errors;
    }
    /**
     * @param errors The errors to set.
     */
    public void setErrors(double errors) {
        this.errors = errors;
    }
    /**
     * @return Returns the vcdim.
     */
    public double getVcdim() {
        return vcdim;
    }
    /**
     * @param vcdim The vcdim to set.
     */
    public void setVcdim(double vcdim) {
        this.vcdim = vcdim;
    }
    
    public String toString( )
    {
        java.text.DecimalFormat D = new java.text.DecimalFormat("0.0000");
        
        Date now = new Date( );
        String date = now.toString();
        
        String R = "k: " + kernel + 
        "\n(acc: " + D.format(accuracy) + " +/- " + D.format(dev) + ")" + 
        " \terr: " + D.format(errors) +
        " \tvcdim: " + D.format(vcdim) +
        " \tfitness: " + D.format(getSimpleFitness()) +
        " \terrEst: " + D.format(errEst) + 
        " \ntime: " + date;
        
        if (isBadResult()) R = R + "!";
        
        return R;
    }
    
    /**
     * Return the VC complexity penalty estimate for a given delta 
     * @param delta
     * @return
     */
    protected double getVCComplexity(double delta) throws VCDimensionException
    {
        if ((delta == 0) || (numLines == 0)) 
            throw new VCDimensionException("complexity penalty cannot be computed");
        
        double numerator = this.vcdim * Math.log(numLines) + Math.log ( 1.0 / delta);
        double denominator = (double) numLines;
        
        return Math.sqrt( numerator / denominator );
    }
    
    public void setNumLines(int lines)
    {
        this.numLines = lines;
    }
    
    public int compareTo(SVMTestResult other)
    {
        
        if (this.isBadResult() && other.isBadResult()) return 0;
        else if (this.isBadResult() && (!other.isBadResult())) return -1;
        else if ( (!this.isBadResult()) && other.isBadResult()) return 1;
        
        double delta = this.getSimpleFitness() - other.getSimpleFitness();
        
        if (Math.abs(delta) < 0.0005) return 0;
        else if (delta < 0) return -1;
        else return 1;
        
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        
        return compareTo((SVMTestResult) o);
    }
    
    
    /* (non-Javadoc)
     * @see data.TestResult#isValid()
     */
    public boolean isValid() {
        
        return (!(isBadResult( ) || (accuracy <= 0))); 
    }
    
    
}
