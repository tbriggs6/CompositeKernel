/*
/*
 * FILE:    VectorTable.java
 * PACKAGE: data
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class VectorTable {
	
	ArrayList table;
	int numRows = 0;
	
	public VectorTable( )
	{
		table = new ArrayList( );
	}
	
	public void addRow(int Y, String pairs[])
	{
		SparseVector V = new SparseVector(Y, pairs);
		table.add(numRows++,V);
	}
	
	public SparseVector getRow(int i)
	{
		return (SparseVector) table.get(i);
	}
	
	public int getNumRows( )
	{
		return numRows;
	}
	
	public int getY( int row )
	{
		return ((SparseVector) table.get(row)).getY();
	}
	
	public int[] getY( )
	{
		int Y[] = new int[table.size()];
		for (int i = 0; i < table.size(); i++)
		{
			Y[i] = ((SparseVector) table.get(i)).getY();
		}
		
		return Y;
	}
	 
	public Iterator iterator( )
	{
	    return table.iterator();
	}
}
