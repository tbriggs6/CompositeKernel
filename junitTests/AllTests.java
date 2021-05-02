/*
 * Created on Sep 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package junitTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AllTests
{

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite()
    {
        TestSuite suite= new TestSuite("Test for junitTests");
        //$JUnit-BEGIN$
             //   suite.addTestSuite(testEstCost.class);
     //   suite.addTestSuite(testCompKernelParam.class);
        //$JUnit-END$
        return suite;
    }
}