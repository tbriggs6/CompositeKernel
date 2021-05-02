/*
 * Created on Sep 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evolutionaryAlgorithm;


import data.VectorTable;
import evaluators.AccuracyEvaluator;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EvolverAccuracy {


	VectorTable T;
	String command;
	int nPopulation, nGeneration;
		
	public static void main(String[] args) {
		
		if (args.length != 3) {
		    throw new RuntimeException("must run as: EvolverAccuracy #pop #gen command");
		}
		EvolverAccuracy E = new EvolverAccuracy(
		        Integer.parseInt(args[0]), Integer.parseInt(args[1]),  args[2]);
		
		E.evolve(  );
	}
	
	public EvolverAccuracy( int nPopulation, int nGen, String command )
	{
		
		this.nPopulation = nPopulation;
		this.nGeneration = nGen;
		this.command = command;
		
	}
	
	public void evolve( )
	{
	
		AccuracyEvaluator F = new AccuracyEvaluator(command);
		TournamentSelection T = new TournamentSelection(8);
		Evolution E = new Evolution( nPopulation,T,F);
		E.search( nGeneration, 1.0);
		
	}
	
}
