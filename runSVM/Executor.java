/*
 * Created on Mar 31, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package runSVM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import data.SVMTestResult;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Executor extends Thread {
    StringBuffer output;
    Process child;
    String command;
    boolean showOutput = false;
    boolean showError = true;
    
    /**
     * Thread to "watch" execution 
     */
    public Executor( String command ) {
        super();
        this.output = new StringBuffer();
        this.command = command;
    }
    
    public String getOutput( )
    {
        return this.output.toString();
    }
    
    //
    public  SVMTestResult doTest(String kernel)
    {
        String result = null;
        
        if (showOutput) System.out.println("Starting test on kernel " + kernel);
        
        try {
            boolean finished = false;
            String args[] = { command, kernel };
            Process child = Runtime.getRuntime().exec(args);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(child.getInputStream()));
            String str;
            
            while (!finished &&
                    (str = in.readLine()) != null) {
                if ((str.startsWith("TEST RESULT")) ||
                     (str.startsWith("TRAIN RESULT"))) {
                    if (showOutput)
                        System.err.println(str);
                }
                else if (str.startsWith("kernel\t")) {
                    ;
                }
                else if (str.startsWith(kernel + "\t")) {
                    result = str;
                    finished = true;
                }
                else {
                    if (showOutput || showError) {
                        System.err.println("unknown(" + str + ")");
                        System.err.println("kernel: " + kernel);
                    }
                    result = null;
                }
            }
            
            child.waitFor();
            
        }
        catch(IOException E)
        {
            E.printStackTrace( );
            throw new RuntimeException("Error");
        }
        catch(InterruptedException E)
        {
            E.printStackTrace( );
            throw new RuntimeException("Error");
        }
        catch(Exception E) 
        {
            return new SVMTestResult(kernel, -1,-1,-1, -1);
        }
        
        // result string needs to be processed
        return strToResult(kernel, result);
    }
    
    private SVMTestResult strToResult(String kernel, String result)
    {
        double error = 0,vcdim = 0,num = 0, errEst = 0, var = 0, dev = 0;
        int numLines;
        
        if (result == null) { 
            System.out.println("Warning: received a NULL result from handler.  ");
            return new SVMTestResult(kernel, -1,-1,-1,-1);  
        }
        
        String fields[] = result.split("\t");
        
        if ((fields.length < 4) || (fields.length > 8)) {
            System.err.println("MALFORMED RESULTS:(" + result + ") " + fields.length);
            return new SVMTestResult(kernel, -1,-1,-1, -1);
        }
        
        try {
            error = Double.parseDouble(fields[1]);
            num = Double.parseDouble(fields[2]);
            vcdim = Double.parseDouble(fields[3]);
            numLines = Integer.MAX_VALUE;
            
            if (fields.length == 5) {
                numLines = Integer.parseInt(fields[4]);
            }
            
            if (fields.length == 6) {
                errEst = Double.parseDouble(fields[5]);
            }
            
            if (fields.length == 8) {
                var = Double.parseDouble(fields[6]);
                dev = Double.parseDouble(fields[7]);
            }
            
            SVMTestResult R = new SVMTestResult(kernel, error, num, vcdim,numLines,errEst, var, dev);
            if (showOutput)
                System.err.println(R.toString());
            return R;
        }
        catch(Exception E)
        {
            System.err.println("MALFORMED ERROR FIELD: (" + result + ") (" + fields[1] + ")");
            //E.printStackTrace();
            return new SVMTestResult(kernel, -1,-1,-1, -1);
        }
        
        
        
    }
    
    
    public Process getProcess( )
    {
        return child;
    } 
    
    public synchronized void terminate( )
    {
        if (child == null) throw new RuntimeException( );
        
        child.destroy();
        this.interrupt();
        
    }
}
