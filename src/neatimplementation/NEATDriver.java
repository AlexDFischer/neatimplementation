package neatimplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NEATDriver
{
    private Random r = new Random(System.currentTimeMillis());
    
    private final double c1, c2, c3;
    private Population population;
    private final FitnessFunction fitnessFunction;
    
    private int generationNumber = 0;
    private int innovationNumber;
    
    public double
        proportionMutateWithoutCrossover = Static.DEFAULT_PROPORTION_MUTATE_WITHOUT_CROSSOVER,
        probabilityUniformlyPerturbed = Static.DEFAULT_PROBABILITY_UNIFORMLY_PERTURBED,
        pertubationRange = Static.DEFAULT_PERTUBATION_RANGE,
        newRandomValStdDev = Static.DEFAULT_NEW_RANDOM_VAL_STD_DEV;
    
    private int minNetworksToCopyMostFitNetwork = Static.DEFAULT_MIN_NETWORKS_TO_COPY_MOST_FIT_NETWORK;
    
    public NEATDriver(int populationSize, double thresholdDifference, double c1, double c2, double c3, Genome initialGenome, FitnessFunction fitnessFunction)
    {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.population = new Population(populationSize, thresholdDifference, c1, c2, c3, initialGenome);
        this.fitnessFunction = fitnessFunction;
        this.innovationNumber = initialGenome.connectionGenes.size() - 1;
    }
    
    public NEATDriver(Genome initialGenome, FitnessFunction fitnessFunction)
    {
        this(Static.DEFAULT_POP_SIZE, Static.DEFAULT_THRESHOLD_DIFFERENCE, Static.DEFAULT_C1, Static.DEFAULT_C2, Static.DEFAULT_C3, initialGenome, fitnessFunction);
    }
    
    private int indexOfMax(double[] array)
    {
        int index = 0;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > max)
            {
                index = i;
                max = array[i];
            }
        }
        return index;
    }
    
    public Genome mostFitGenome()
    {
        Genome mostFitGenome= null;
        double maxFitness = 0;
        for (Species s : this.population.getSpecies())
        {
            Genome genome = s.mostFitSpecies(fitnessFunction);
            double fitness = fitnessFunction.fitness(genome);
            if (fitness > maxFitness)
            {
                mostFitGenome = genome;
                maxFitness = fitness;
            }
        }
        return mostFitGenome;
    }
    
    public List<Genome> nextGenerationGenomes()
    {
        // first get an array of the average fitness for each Species
        double[] fitnesses = new double[this.population.numSpecies()];
        for (int i = 0; i < fitnesses.length; i++)
        {
            fitnesses[i] = this.population.getSpecies().get(i).averageFitness(this.fitnessFunction);
        }
        
        // calculate how many offspring each species gets
        int runningTotal = 0;
        int[] offspringPerSpecies = new int[this.population.numSpecies()];
        // use the Huntington-Hill method; see https://en.wikipedia.org/wiki/United_States_congressional_apportionment#The_method_of_equal_proportions
        // first assign one offspring to each species
        for (int i = 0; i < population.numSpecies(); i++)
        {
            runningTotal++;
            offspringPerSpecies[i] = 1;
        }
        // assign a priority to each species
        double[] priorities = new double[this.population.numSpecies()];
        for (int i = 0; i < priorities.length; i++)
        {
            priorities[i] = fitnesses[i] / Math.sqrt(offspringPerSpecies[i]*(offspringPerSpecies[i]+1));
        }
        while (runningTotal < this.population.getSize())
        {
            // find the species with the highest priority
            int i = indexOfMax(priorities);
            runningTotal++;
            offspringPerSpecies[i]++;
            priorities[i] = fitnesses[i] / Math.sqrt(offspringPerSpecies[i]*(offspringPerSpecies[i]+1));
        }
        
        // now, for each species, create the Genomes for the next generation
        List<Genome> newGenomes = new ArrayList<>(this.population.getSize());
        for (int i = 0; i < this.population.numSpecies(); i++)
        {
            Species species = this.population.getSpecies().get(i);
            Genome[] sortedGenomes = species.genomesInDescendingFitnessOrder(fitnessFunction);
            int numMutateWithoutCrossover = (int) Math.floor(this.proportionMutateWithoutCrossover * offspringPerSpecies[i]);
            int numMutateWithCrossover = offspringPerSpecies[i] - numMutateWithoutCrossover;
            if (species.size() >= this.minNetworksToCopyMostFitNetwork)
            {
                // in this case, we copy the most fit network in the species into the next generation unchanged
                newGenomes.add(species.mostFitSpecies(this.fitnessFunction));
                if (numMutateWithCrossover > numMutateWithoutCrossover)
                {
                    numMutateWithCrossover--;
                } else
                {
                    numMutateWithoutCrossover--;
                }
            }
            // now do the mutations without crossover
            for (int j = 0; j < numMutateWithoutCrossover; j++)
            {
                newGenomes.add(sortedGenomes[j % sortedGenomes.length].mutateWithoutCrossover(r, probabilityUniformlyPerturbed, pertubationRange, newRandomValStdDev));
                
            }
            // now do the mutations with crossover
            for (int j = 0; j < numMutateWithCrossover; j++)
            {
                // TODO use a better method to determine which genomes to mate
                newGenomes.add(crossOver(sortedGenomes[j % sortedGenomes.length], sortedGenomes[j % sortedGenomes.length]));
            }
        }
        
        return newGenomes;
    }
    
    public void nextGeneration()
    {
        List<Genome> newGenomes = this.nextGenerationGenomes();
        this.population.nextGeneration(newGenomes);
        this.generationNumber++;
    }
    
    public void printStatusReport()
    {
        System.out.println("ON GENERATION " + this.generationNumber);
        System.out.println("NUMBER OF SPECIES: " + this.population.getSpecies().size());
        // determine max fitness of all genomes
        Genome mostFitGenome = this.mostFitGenome();
        System.out.println("MAX FITNESS: " + fitnessFunction.fitness(mostFitGenome));
    }

    private Genome crossOver(Genome genome, Genome genome2)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
