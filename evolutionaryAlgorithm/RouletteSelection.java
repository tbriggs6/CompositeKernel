/*
 * Created on Sep 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package evolutionaryAlgorithm;

/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class RouletteSelection implements SelectionAlgorithm {

	
	public Gene select(Gene[] population) {

		double probs[] = new double[population.length];
		double sum = sum(population);
		
		probs[0] = population[0].getSimpleFitness() / sum;
		for (int i = 1; i < population.length; i++)
		{
			probs[i] = probs[i-1] + (population[i].getSimpleFitness() / sum); 
		}
		
		double select = Math.random();
		
		int selected = population.length - 1;
		for (int i = 0; i < population.length; i++)
		{
			if (probs[i] > select)
			{
				selected = i;
				break;
			}
		}
		
		
		return population[selected];
	}
	
	
	private double sum(Gene[] population)
	{
		double sum = 0;
		for (int i = 0; i < population.length; i++)
			sum += population[i].getSimpleFitness();
		return sum;
	}
	
	 
	

}
