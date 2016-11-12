package neatimplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Species
{
    private final List<Genome> genomes;
    private final Genome representative;
    public final int identifier;
    
    public Species(Genome representative, int identifier)
    {
        this.genomes = new ArrayList<>();
        this.representative = representative;
        this.identifier = identifier;
    }
    
    public void add(Genome genome)
    {
        this.genomes.add(genome);
    }
    
    /**
     * Computes the adjusted fitness, or the average fitness of all genomes in
     * the species
     * @param f the fitness function to use
     * @return the average fitness
     * @throws IllegalStateException if the species is empty
     */
    public double averageFitness(FitnessFunction f)
    {
        if (this.isEmpty())
        {
            throw new IllegalStateException("Can't get adjusted fitness of empty species");
        }
        double avg = 0.0;
        for (Genome g : this.genomes)
        {
            avg += f.fitness(g);
        }
        avg /= this.genomes.size();
        return avg;
    }
    
    public boolean contains(Genome genome)
    {
        return this.genomes.contains(genome);
    }
    
    public Genome[] genomesInDescendingFitnessOrder(FitnessFunction f)
    {
        Genome[] sortedGenomes = new Genome[this.genomes.size()];
        for (int i = 0; i < sortedGenomes.length; i++)
        {
            sortedGenomes[i] = this.genomes.get(i);
        }
        Arrays.sort(sortedGenomes,
            new Comparator<Genome>()
            {
                @Override
                public int compare(Genome arg0, Genome arg1)
                {
                    double f0 = f.fitness(arg0), f1 = f.fitness(arg1);
                    if (f0 == f1)
                    {
                        return 0;
                    } else
                    {
                        return f.fitness(arg1) - f.fitness(arg0) > 0.0 ? 1 : -1; // reversing arg1 and arg0 makes it in descending order
                    }
                }
        
            }
        );
        return sortedGenomes;
    }
    
    public Genome get(int i)
    {
        return this.genomes.get(i);
    }
    
    public boolean inSpecies(Genome genome, double thresholdDifference, double c1, double c2, double c3)
    {
        return this.representative.distance(genome, c1, c2, c3) <= thresholdDifference;
    }
    
    public boolean isEmpty()
    {
        return this.genomes.size() == 0;
    }
    
    /**
     * Finds the most fit genome in the species. Requires the species to be not
     * empty.
     * @param f the fitness function to use
     * @return the most fit genome in the species
     * @throws IllegalStateException if the species is empty
     */
    public Genome mostFitSpecies(FitnessFunction f)
    {
        if (this.isEmpty())
        {
            throw new IllegalStateException("Can't get the fittest genome in an empty species");
        }
        Genome result = null;
        double maxFitness = -Double.MAX_VALUE;
        for (Genome g : this.genomes)
        {
            double fitness = f.fitness(g);
            if (fitness > maxFitness)
            {
                result = g;
                maxFitness = fitness;
            }
        }
        return result;
    }
    
    public Genome representative()
    {
        return this.representative;
    }
    
    /**
     * Returns the number of genomes in this species
     * @return the number of genomes in this species
     */
    public int size()
    {
        return this.genomes.size();
    }
}
