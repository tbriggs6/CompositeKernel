/*
 * Created on Oct 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package data;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AlignmentResult implements Comparable, TestResult {

    String kernel;
    Double result;
    
    public AlignmentResult(String kernel, double alignment)
    {
        this.kernel = kernel;
        this.result = new Double(alignment);
    }
    
    public AlignmentResult(String kernel, Double alignment)
    {
        this.kernel = kernel;
        this.result = alignment;
    }
    
    public double getSimpleFitness( )
    {
        if (result == null) return -1;
        return result.doubleValue();
    }
    
    public int compareTo(Object o) {
    
        return result.compareTo((Double) o);
    }

    
    public boolean isValid() {
    
        return  (result.doubleValue() >= 0);
    }

    public String toString( )
    {
        java.text.DecimalFormat D = new java.text.DecimalFormat("#.###");
        return "k: " + kernel +  " a: " + D.format( result.doubleValue());
    }
}
