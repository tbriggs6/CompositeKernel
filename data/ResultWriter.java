/*
 * Created on Oct 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package data;
import java.io.*;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ResultWriter {

    String resultFile, testName, dataSet;
    String result;
    boolean writeResults = true;
    
    public ResultWriter(String result )
    {
        
        this.resultFile = System.getProperty("result.outFile");
        this.testName = System.getProperty("result.testName");
        this.dataSet = System.getProperty("result.dataSet");
        
        if ((resultFile == null) || (testName == null) || (dataSet == null))
        {
            System.err.println("Cannot write results : -D's aren't set.");
            writeResults = false;
        }
        
        this.result = result;
    }
    

    public void writeResult( )
    {
        
        if (!writeResults) {
            System.err.println(this.result.toString());
            return; 
        }
        
        try {
            File F = new File(this.resultFile);
            F.createNewFile( );
            
	        PrintWriter out = new PrintWriter(  
	                new FileOutputStream( F , true ));
	        
	        String resultStr = this.result.toString();
	        
	        out.println("=====================================");
	        out.println("Test: \t" + this.testName);
	        out.println("DataSet: " + this.dataSet);
	        	        out.println(resultStr);
	        out.println("=====================================\n\n");
	        
	        out.close();
        }
        catch(Exception E)
        {
            System.err.println(this.result.toString());
            System.err.println(this.resultFile);
            E.printStackTrace();
            System.exit(-1);
        }
        
        
    }
    
    
    public static void main(String args[])
    {

         ResultWriter W = new ResultWriter( "result" );
    }
    
}
