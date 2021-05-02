/*
/*
 * FILE:    SparseVector.java
 * PACKAGE: data
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 12, 2004
 */ 
package data;

import java.text.DecimalFormat;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SparseVector {
	
	int y;
	double values[];
	int fields[];
	int maxField = 0;
	int last = -1;
	private boolean dirty = false;
	
	public SparseVector(int y)
	{
		this.y = y;
		this.values = new double[0];
		this.fields = new int[0];
		this.maxField = 0;
		this.last = -1;
	}
	
	public SparseVector(int y, double values[], int fields[] )
	{
		this.y = y;
		this.values = values;
		this.fields = fields;
		
		verifyFields( );
	}
	
	/**
	 * Construct a sparse vector from the values given in the pairs[] array.
	 * Each entry is expected to be an entry in "field_num:value", which
	 * is the standard form for SVMLite.
	 *   
	 * @param pairs an array of values in the form "field_num:value" 
	 */
	public SparseVector(int y, String pairs[])
	{
		this(y, pairs,":");
	}
	
	/**
	 * Construct a sparse vector from the values given in the pairs[] array,
	 * where the values are split up by the given delimeter string.
	 * @param pairs an array of values in the form "field_num:value"
	 * @param delimeter a string that represents the delimeter characcter.
	 */
	public SparseVector(int y , String pairs[],String delimeter)
	{
		this.y = y;
		this.values = new double[pairs.length];
		this.fields = new int[pairs.length];
		
		for (int i = 0; i < pairs.length; i++)
		{
			String pair = pairs[i];
			String subfields[] = pair.split(delimeter);
			if (subfields.length != 2) {
				throw new RuntimeException("Error, malformed pair: " + pairs[i]);
			}
			
			fields[i] = Integer.parseInt(subfields[0]);
			values[i] = Double.parseDouble(subfields[1]);
			
			if (Double.isInfinite(values[i]))
			{
				throw new RuntimeException("Error: pair is infinite: " + pairs[i]);
			}
			
			if (Double.isNaN(values[i]))
			{
				throw new RuntimeException("Error: pair is NaN: " + pairs[i]);
			}
		}
		
		maxField = fields[fields.length-1];
		verifyFields( );
	}
	
	/**
	 * Ensure that the fields given in the array are in stricty non-decreasing order.
	 *
	 */
	private void verifyFields( )
	{
		int f = fields[0];
		for (int i = 1; i < fields.length; i++) 
			if (f >= fields[i]) 
				throw new RuntimeException("Fields must be in strictly non-decreasing order.");
			else
				f = fields[i];
	}
	
	
	public int maxField( )
	{
		return fields[fields.length - 1 ];
	}
	
	public double get(int f)
	{
		if ((last == -1) || (last > maxField( )) || (last < f))
		{
			last = 0;
		}
		
		while (fields[last] < f)
			last++;
		
		if  (fields[last] == f) return values[last];
		else return 0;
		
	}
	
	public int[] getFields( )
	{
		return this.fields;
	}
	
	public double getValue(int i)
	{
		return values[i];
	}
	
	public void setValue(int i, double v)
	{
	    if (i < values.length)
	        values[i] = v;
	}
	
	public int getY( )
	{
		return y;
	}
	
	public double norm2()
	{
		double sum = 0;
		
		for (int i = 0; i < values.length; i++)
			sum += values[i] * values[i];
		
		double tmp = Math.sqrt(sum);
		
		return tmp;
	}
	
	
	public double norm2sub(SparseVector Xj )
	{
		int sum = 0;
		
		int idxI = 0;
		
		int idxJ = 0;
		int fieldsJ[] = Xj.getFields();
		
		
		while ((idxI < fields.length) && (idxJ < fieldsJ.length))
		{
			double t,v;
			
			if (fields[idxI] < fieldsJ[idxJ]) {
				t = getValue(idxI++);
				v = 0;
			}
			else if (fields[idxI] == fieldsJ[idxJ]) {
				t = getValue(idxI++);
				v = Xj.getValue(idxJ++);
			}
			else {
				t = 0;
				v = Xj.getValue(idxJ++);
			}
			
			sum += Math.pow( (t - v), 2);
			
			if (Double.isNaN(t) || Double.isNaN(v) || Double.isNaN(sum))
			{
				System.err.println("error: t: " + t + "v: " + v + " sum: " + sum);
				throw new RuntimeException("found NaN.");

			}
			
		}
		
		for ( ; idxI < fields.length; idxI++)
			sum += Math.pow(values[idxI],2);
		
		for ( ; idxJ < fieldsJ.length; idxJ++)
			sum += Math.pow(Xj.getValue(idxJ),2);
		
		
		return Math.sqrt(sum);
		
	}
	
	public SparseVector subtract(SparseVector Xj)
	{
		int sum = 0;
		
		int idxI = 0;
		
		int idxJ = 0;
		int fieldsJ[] = Xj.getFields();
		
		SparseVector Xk = new SparseVector(this.y);
		
		
		while ((idxI < fields.length) && (idxJ < fieldsJ.length))
		{
			double t,v;
			
			if (fields[idxI] < fieldsJ[idxJ]) {
				t = getValue(idxI++);
				v = 0;
			}
			else if (fields[idxI] == fieldsJ[idxJ]) {
				t = getValue(idxI++);
				v = Xj.getValue(idxJ++);
			}
			else {
				t = 0;
				v = Xj.getValue(idxJ++);
			}

			
			
			Xk.add(fields[idxI],t-v);
			
		}
		
		for ( ; idxI < fields.length; idxI++)
		{
			Xk.add(fields[idxI], values[idxI]);
		}
		
		for ( ; idxJ < fieldsJ.length; idxJ++)
		{
			Xk.add(fieldsJ[idxJ], Xj.getValue(idxJ));
		}
		
		return Xk;
	}

	public void add(int indx, double val)
	{
		int n = fields.length;
		
		growArrays( );
		fields[n] = indx;
		values[n] = val;
		maxField = Math.max(maxField, indx);
	}

	private void growArrays( )
	{
		int nF[] = new int[fields.length + 1];
		double nV[] = new double[fields.length+1];
		
		for (int i = 0; i < fields.length; i++) {
			nF[i] = fields[i];			   
			nV[i] = values[i];
		}
		
		this.fields = nF;
		this.values = nV;
	}
	
	
	public double dotProduct(SparseVector Xj )
	{
		double sum = 0;
		
		int idxI = 0;
		int idxJ = 0;
		
		
		while ((idxI < fields.length) && (idxJ < Xj.fields.length))
		{
			double t,v;
			
			if (fields[idxI] < Xj.fields[idxJ]) {
				idxI++;
				continue;
			}
			else if (fields[idxI] == Xj.fields[idxJ]) {
				t = values[idxI++];
				v = Xj.values[idxJ++];
			}
			else {
				idxJ++;
				continue;
			}
			
			sum += t * v;
			
			if (Double.isNaN(t) || Double.isNaN(v) || Double.isNaN(sum))
			{
				System.err.println("error: t: " + t + "v: " + v + " sum: " + sum);
				throw new RuntimeException("found NaN.");

			}

		}
		
		return sum;
	}
	
	public String toString( )
	{
		StringBuffer B = new StringBuffer( );
		DecimalFormat D = new DecimalFormat("#.###");
		B.append(y);
		for (int i = 0; i < values.length; i++) {
			B.append("\t");
			B.append("(" + D.format(values[i]) + ")" );
			
		}
		return B.toString();
	}
	
	public void setDirty( )
	{
	    dirty = true;
	}
	
	public void setPurify( )
	{
	    dirty = false;
	}
	
	public boolean isDirty( )
	{ 
	    return dirty;
	}
	
	
}
