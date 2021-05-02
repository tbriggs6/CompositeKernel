/*
 */
/*
 * FILE:    Evolution.java
 * PACKAGE: evolutionaryAlgorithm
 * PROJECT:	CSC110
 * AUTHOR:  Tom Briggs (c) 2004
 * DATE:    Sep 14, 2004
 */ 
package evolutionaryAlgorithm;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import data.*;
import data.ResultWriter;
import evaluators.FitnessAlgorithm;


/**
 * @author tbriggs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Evolution {
    
    double mutateProbability = 0.02;
    Gene population[];
    int poolSize;
    SelectionAlgorithm selectAlg;
    FitnessAlgorithm F;
    TestResult bestFitness = null;
    Gene bestGene = null;
    
    public Evolution(int poolSize, SelectionAlgorithm sel, FitnessAlgorithm F)
    {
        this.population = new Gene[poolSize];
        this.poolSize = poolSize;
        this.selectAlg = sel;
        this.F = F;
        
    }
    
    public TestResult search(int maxGenerations, double target)
    {
        System.out.println("========================================================");
        System.out.println("Commencing search maxGen:" + maxGenerations + " pool: " + poolSize);
        System.out.println("========================================================");
        
        DecimalFormat D = new DecimalFormat("#.####");
        int currentGeneration = 0;
        double currentFitness = 0;
        
        this.population = initialPopulation(poolSize);
        
        do {
            
            System.out.println("------------ generation: " + currentGeneration + " -------------");
            // make the next population...
            LinkedList nextPop = new LinkedList( );
            for (int i = 0; i < population.length; i++)
            {
                nextPop.add(population[i]);
            }
            
            int count  = 0;
            // now, enter random selection
            for (int i = 0; i < poolSize; i++)
            {
                count++;
                
                Gene X = selectAlg.select(population);
                Gene Y = selectAlg.select(population);
                
                Gene child = Y.crossOver(X);
                
                if (Math.random() < this.mutateProbability)
                    child.mutate();
                
                child.fitness = F.fitness(child.getParams());
                
                if (!child.fitness.isValid()) {
                    System.err.println("invalid result - discarding. " + count + " " +i);
                    i--;
                    continue;
                }
                    
                
                if ((bestFitness == null) || 
                        (child.fitness.compareTo(bestFitness) > 0))
                {
                    this.bestFitness = child.fitness;
                    this.bestGene = child;
                }
                
                System.out.println("child " + i + "\t" + child.getParams() + "\t" + child.fitness);
                
                if (child.getSimpleFitness() > 0)
                    nextPop.add(population[i]);
            }
            
            // now, population is 2x times the size....
            // prune it down.
            while (nextPop.size() >= poolSize)
            {
                int i = (int)( Math.random() * nextPop.size());
                if (Math.random() > ((Gene)nextPop.get(i)).getSimpleFitness())
                {
                    nextPop.remove(i);
                }
            }
            
            // finally ... remake the population array
            {
                int i = 0;
                Iterator I = nextPop.iterator();
                while (I.hasNext()){
                    Gene G = (Gene) I.next();
                    population[i++] = G;
                }
            }
            
            if (bestGene != null)
                System.out.println(bestGene);
            
        } while ((currentGeneration++ < maxGenerations) && (currentFitness < target));
        
        
        if (bestGene != null) {
            System.out.println("Completed in: " + currentGeneration + " generations");
            System.out.println("Best gene: " + bestGene.getParams().toString());
            System.out.println("Best fitness: " + bestGene.fitness);
            
            ResultWriter R = new ResultWriter(  bestGene.fitness.toString() );
            R.writeResult();
            
        }
        else {
            throw new RuntimeException("There were no reported genes found.");
        }
        
        return bestFitness;
    }
    
    
    private Gene[] initialPopulation(int populationSize)
    {
        DecimalFormat D = new DecimalFormat("#.###");
        System.out.println("Building initial population");
        Gene population[] = new Gene[populationSize];
        
        //TODO  make this a while loop
        int count = 0;
        int i = 0;
        
        while (i < population.length)
        {
            count++;
            
            System.out.print(" " + D.format( (double)count / (double)(i)) + "%  i:" + i + " ");
            Gene G = new Gene( );
            TestResult R = F.fitness(G.getParams());
            if (R.isValid()) {
                G.fitness = R;
                population[i++] = G;
                System.out.println(G);
            }
            else {
                System.out.println("invalid - discarding: " + G);
            }
            
            System.out.println();
        }
        
        return population;
    }
    
    
    
}
