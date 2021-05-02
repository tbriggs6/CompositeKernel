/*
 * Created on Sep 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evolutionaryAlgorithm;


import java.io.IOException;

import data.FileReaderSVM;
import data.VectorTable;
import evaluators.AlignmentEvaluator;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EvolverAlignment {


	VectorTable T;
	
		
	public static void main(String[] args) {
		
		
		EvolverAlignment E = new EvolverAlignment(args[0]);
		E.evolve(  );
	}
	
	public EvolverAlignment( String fileName )
	{
		
		try {
			FileReaderSVM F = new FileReaderSVM(fileName);
			T = F.loadTable();
			
		}
		catch(IOException E)
		{
			E.printStackTrace( );
		}
		
		
	}
	
	public void evolve( )
	{
	
		AlignmentEvaluator F = new AlignmentEvaluator(T);
		TournamentSelection T = new TournamentSelection(8);
		Evolution E = new Evolution(200,T,F);
		E.search( 40, 1.0);
		
	}
	
}
