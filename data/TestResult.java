/*
 * Created on Oct 7, 2004
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
public interface TestResult {
    public abstract String toString();

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */public abstract int compareTo(Object o);
     public double getSimpleFitness();
     public abstract boolean isValid( );
}