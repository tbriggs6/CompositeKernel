/*
/*
 * FILE:    FileReaderSVM.java
 * PACKAGE: data
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FileReaderSVM {
	
	String fileName;
	
	public FileReaderSVM( String fileName )
	{
		this.fileName = fileName;
	}
	
	
	public VectorTable loadTable( ) throws IOException
	{
		VectorTable T = new VectorTable( );
		
		File F = new File(fileName);
		BufferedReader in = new BufferedReader(new FileReader(F));
		
		String line;
		while ( (line = in.readLine()) != null) 
		{
			
			String fields[] = line.split("\\s+");
			String tmpFields[] = new String[fields.length - 1];
			int k = 0;
			for (int i = 1; i < fields.length; i++)
				if (fields[i] != null)
				tmpFields[k++] = fields[i];
			
			int y;
			if (fields[0].equals("+1")) {
				y = 1;
			}
			else {
				y = Integer.parseInt(fields[0]);
			}
			
			T.addRow(y, tmpFields);
		}
		
		
		return T;
		
	}
	
	
}
