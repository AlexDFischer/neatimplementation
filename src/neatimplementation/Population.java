package neatimplementation;

import java.util.ArrayList;
import java.util.List;

public class Population
{
    private final double thresholdDifference, c1, c2, c3;
    private final int size;
    private List<Species> species;
    private int maxSpeciesIdentifier = -1;
    
    public Population(Genome initialGenome)
    {
        this(Static.DEFAULT_POP_SIZE, Static.DEFAULT_THRESHOLD_DIFFERENCE, Static.DEFAULT_C1, Static.DEFAULT_C2, Static.DEFAULT_C3, initialGenome);
    }
    
    public Population(int size, double thresholdDifference, double c1, double c2, double c3, Genome initialGenome)
    {
        this.size = size;
        this.thresholdDifference = thresholdDifference;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.species = new ArrayList<>();
        this.maxSpeciesIdentifier++;
        this.species.add(new Species(initialGenome.clone(), this.maxSpeciesIdentifier));
        for (int i = 0; i < this.size; i++)
        {
            this.species.get(0).add(initialGenome);
        }
    }
    
    /**
     * Returns the number of genomes in this population
     * @return the size
     */
    public int getSize()
    {
        return this.size;
    }
    
    /**
     * Returns the List of species in this population
     * @return the list of species
     */
    public List<Species> getSpecies()
    {
        return this.species;
    }
    
    /**
     * Updates this population to speciate the next generation of genomes
     * @param genomes the genomes of the next generation to speciate
     */
    public void nextGeneration(List<Genome> genomes)
    {
        List<Species> newSpecies = new ArrayList<>();
        // create the new species based off the first genome in each old species
        for (Species s : this.species)
        {
            if (!s.isEmpty())
            {
                // use the first genome in previous generation's species as representative
                newSpecies.add(new Species(s.get(0), s.identifier));
            }
        }
placingGenomes:for (Genome g : genomes)
        {
            for (Species s : newSpecies)
            {
                if (s.inSpecies(g, thresholdDifference, c1, c2, c3))
                {
                    s.add(g);
                    continue placingGenomes;
                }
            }
            // g is not in any existing species
            this.maxSpeciesIdentifier++;
            Species s = new Species(g, this.maxSpeciesIdentifier);
            s.add(g);
            newSpecies.add(s);
        }
        // remove empty species
        for (int i = 0; i < newSpecies.size(); i++)
        {
            if (newSpecies.get(i).isEmpty())
            {
                newSpecies.remove(i);
                i--;
            }
        }
        this.species = newSpecies;
    }
    
    /**
     * Number of species in the population
     * @return the number of species
     */
    public int numSpecies()
    {
        return this.species.size();
    }
    
    /**
     * Calculates the sum of the fitness of each species
     * @return the computed sum
     */
    public double sumFitness(FitnessFunction f)
    {
        double result = 0.0;
        for (Species s : this.species)
        {
            result += s.averageFitness(f);
        }
        return result;
    }
}
