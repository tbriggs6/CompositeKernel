/*
 * Created on Sep 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evolutionaryAlgorithm;

import java.util.LinkedList;
/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentSelection implements SelectionAlgorithm {
	
	int sizeTourney;
	
	public TournamentSelection(int sizeTourney)
	{
		this.sizeTourney = sizeTourney;
	}
	
	
	public Gene select(Gene[] population) {
		
		int populationSize = population.length;
		LinkedList competitors = new LinkedList( );
		for (int j = 0; j < sizeTourney; j++)
		{
			int n = (int) (Math.random() * populationSize);
			competitors.add(population[n]);
		}
		
		return tourney(competitors);
		
	}
	
	private Gene tourney(LinkedList competitors)
	{
		
		while (competitors.size() != 1)
		{
			int position = (int) (Math.random() * competitors.size());
			Gene G = (Gene) competitors.get(position);
			if (Math.random() > G.getSimpleFitness())
			{
				competitors.remove(position);
			}
		}
		
		return (Gene) competitors.getFirst();
	}
	
	
	
}
