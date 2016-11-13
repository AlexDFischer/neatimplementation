package neatimplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NEATDriver
{
    private Random r = new Random(System.currentTimeMillis());
    
    public final double c1, c2, c3;
    private Population population;
    private final FitnessFunction fitnessFunction;
    
    private int generationNumber = 0;
    private int innovationNumber;
    
    private List<ConnectionGene> existingMutations = new ArrayList<>();
    
    public double
        proportionMutateWithoutCrossover = Static.DEFAULT_PROPORTION_MUTATE_WITHOUT_CROSSOVER,
        probabilityUniformlyPerturbed = Static.DEFAULT_PROBABILITY_UNIFORMLY_PERTURBED,
        pertubationRange = Static.DEFAULT_PERTUBATION_RANGE,
        newRandomValStdDev = Static.DEFAULT_NEW_RANDOM_VAL_STD_DEV,
        probabilityParentGeneDisabledStillDisabled = Static.DEFAULT_PROBABILITY_PARENT_GENE_DISABLED_STILL_DISABLED,
        probabilityMutate = Static.DEFAULT_PROBABILITY_MUTATE,
        probabilityAddNewNode = Static.DEFAULT_PROBABILITY_ADD_NEW_NODE,
        probabilityNewConnection = Static.DEFAULT_PROBABILITY_NEW_CONNECTION;
    
    private int minNetworksToCopyMostFitNetwork = Static.DEFAULT_MIN_NETWORKS_TO_COPY_MOST_FIT_NETWORK;
    
    public NEATDriver(int populationSize, double thresholdDifference, double c1, double c2, double c3, Genome initialGenome, FitnessFunction fitnessFunction)
    {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.population = new Population(populationSize, thresholdDifference, c1, c2, c3, initialGenome);
        this.fitnessFunction = fitnessFunction;
        this.innovationNumber = initialGenome.connectionGenes.size() - 1;
        for (ConnectionGene c : initialGenome.connectionGenes)
        {
            this.existingMutations.add(c);
        }
    }
    
    public NEATDriver(Genome initialGenome, FitnessFunction fitnessFunction)
    {
        this(Static.DEFAULT_POP_SIZE, Static.DEFAULT_THRESHOLD_DIFFERENCE, Static.DEFAULT_C1, Static.DEFAULT_C2, Static.DEFAULT_C3, initialGenome, fitnessFunction);
    }
    
    private int assignInnovationNumber(int inNode, int outNode)
    {
        for (int i = 0; i < this.existingMutations.size(); i++)
        {
            if (existingMutations.get(i).inNode == inNode && existingMutations.get(i).outNode == outNode)
            {
                return i;
            }
        }
        this.innovationNumber++;
        this.existingMutations.add(new ConnectionGene(inNode, outNode, 1, true, this.innovationNumber));
        return innovationNumber;
    }
    
    /**
     * Index in this.existingMutations of the first equivalent connection
     * @param c the new connection to search for
     * @return the index, or -1 if not found
     */
    private int indexOfEquivalent(ConnectionGene c)
    {
        for (int i = 0; i < this.existingMutations.size(); i++)
        {
            if (c.equivalent(existingMutations.get(i)))
            {
                return i;
            }
        }
        return -1;
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
    
    /**
     * inserts the given gene into the list in sorted order
     * @param connectionGenes
     * @param c
     */
    private void insertConnection(List<ConnectionGene> list, ConnectionGene c)
    {
        if (list.isEmpty() || c.innovationNum < list.get(0).innovationNum)
        {
            list.add(0, c);
            return;
        }
        for (int i = 1; i < list.size(); i++)
        {
            if (c.innovationNum == list.get(i-1).innovationNum)
            {
                return; // don't insert a duplicate
            } else if (list.get(i-1).innovationNum < c.innovationNum && c.innovationNum < list.get(i).innovationNum)
            {
                list.add(i, c);
                return;
            }
        }
        if (c.innovationNum == list.get(list.size()-1).innovationNum)
        {
            return; // don't insert a duplicate
        } else
        {
            list.add(list.size(), c);
        }
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
                newGenomes.add(crossOver(r, sortedGenomes[(j / sortedGenomes.length) % sortedGenomes.length], sortedGenomes[j % sortedGenomes.length]));
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
    
    public void printDetailedStatusReport()
    {
        System.out.println("ON GENERATION " + this.generationNumber);
        System.out.println("NUMBER OF SPECIES: " + this.population.getSpecies().size());
        Genome mostFitGenome = this.mostFitGenome();
        System.out.println("MAX FITNESS: " + fitnessFunction.fitness(mostFitGenome));
        for (Species s : this.population.getSpecies())
        {
            System.out.println("Species " + s.identifier + " has " + s.size() + " genomes and average fitness " + s.averageFitness(fitnessFunction) + " and max fitness " + fitnessFunction.fitness(s.mostFitSpecies(fitnessFunction)));
        }
    }
    
    public void printStatusReport()
    {
        System.out.println("ON GENERATION " + this.generationNumber);
        System.out.println("NUMBER OF SPECIES: " + this.population.getSpecies().size());
        // determine max fitness of all genomes
        Genome mostFitGenome = this.mostFitGenome();
        System.out.println("MAX FITNESS: " + fitnessFunction.fitness(mostFitGenome));
    }
    
    private Genome crossOver(Random r, Genome g1, Genome g2)
    {
        Genome newGenome = crossOverNoMutation(r, g1, g2);
        if (r.nextDouble() < probabilityMutate)
        {
            newGenome.mutateConnectionWeights(r, probabilityUniformlyPerturbed, pertubationRange, newRandomValStdDev);
        }
        if (r.nextDouble() < probabilityAddNewNode && newGenome.connectionGenes.size() > 0) // there also needs to be existing connections to split
        {
            // first find a connection to split, then disable it, then add new node, then add new connections
            ConnectionGene toSplit = newGenome.connectionGenes.get(r.nextInt(newGenome.connectionGenes.size()));
            toSplit.expressed = false;
            newGenome.nodeGenes.add(new NodeGene(NodeType.HIDDEN));
            int newNodeNum = newGenome.nodeGenes.size() - 1;
            ConnectionGene c1 = new ConnectionGene(toSplit.inNode, newNodeNum, 1, true, assignInnovationNumber(toSplit.inNode, newNodeNum));
            ConnectionGene c2 = new ConnectionGene(newNodeNum, toSplit.outNode, toSplit.weight, true, assignInnovationNumber(newNodeNum, toSplit.outNode));
            insertConnection(newGenome.connectionGenes, c1);
            insertConnection(newGenome.connectionGenes, c2);
        }
        if (r.nextDouble() < probabilityNewConnection)
        {
            int inNode = r.nextInt(newGenome.nodeGenes.size());
            // can't have a bias or input node be the output of a connection
            int outNode = r.nextInt(newGenome.nodeGenes.size() - newGenome.getNumInputs() - newGenome.getNumBiases()) + newGenome.getNumInputs() + newGenome.getNumBiases();
            if (!newGenome.wouldMakeRecurrent(inNode, outNode))
            {
                ConnectionGene newConnection = new ConnectionGene(inNode, outNode, 1, true, assignInnovationNumber(inNode, outNode));
                insertConnection(newGenome.connectionGenes, newConnection);
            }
        }
        return newGenome;
    }
    
    private Genome crossOverNoMutation(Random r, Genome g1, Genome g2)
    {
        ArrayList<NodeGene> nodes = new ArrayList<>();
        ArrayList<ConnectionGene> connections = new ArrayList<>();
        // add the input and bias and output nodes of g1 into nodes
        for (NodeGene g : g1.nodeGenes)
        {
            if (g.type == NodeType.INPUT)
            {
                nodes.add(new NodeGene(NodeType.INPUT));
            } else if (g.type == NodeType.BIAS)
            {
                nodes.add(new NodeGene(NodeType.BIAS));
            } else if (g.type == NodeType.OUTPUT)
            {
                nodes.add(new NodeGene(NodeType.OUTPUT));
            } 
            else
            {
                break;
            }
        }
        Genome newGenome = new Genome(nodes, connections);
        int index1 = 0, index2 = 0;
        double f1 = fitnessFunction.fitness(g1);
        double f2 = fitnessFunction.fitness(g2);
        while (index1 < g1.connectionGenes.size() && index2 < g2.connectionGenes.size())
        {
            ConnectionGene c1 = g1.connectionGenes.get(index1);
            ConnectionGene c2 = g2.connectionGenes.get(index2);
            
            
            if (c1.innovationNum == c2.innovationNum)
            {   // we are working with two matching genes. select randomly from either parent
                // but first check to make sure the connection wouldn't make the network recurrent
                ConnectionGene randomlySelected = (r.nextDouble() < 0.5 ? c1 : c2).clone();
                // ensure we have enough NodeGenes for the connections
                // add hidden nodes onto the end of the list of node genes
                int minNeededNode = Math.max(randomlySelected.inNode, randomlySelected.outNode);
                for (int i = newGenome.nodeGenes.size(); i <= minNeededNode; i++)
                {
                    nodes.add(new NodeGene(NodeType.HIDDEN));
                }
                if (!newGenome.wouldMakeRecurrent(randomlySelected))
                {
                    if (c1.expressed && c2.expressed)
                    {   // both enabled
                        connections.add(randomlySelected);
                    } else
                    {
                        // at least one is disabled. there is a chance we reenable them
                        randomlySelected.expressed = !(r.nextDouble() < probabilityParentGeneDisabledStillDisabled);
                        connections.add(randomlySelected);
                    }
                }
                index1++;
                index2++;
            } else
            {   // we are working with disjoint genes
                // inherit disjoint and excess genes from more fit parent (or both if equal fitness)
                if (c1.innovationNum < c2.innovationNum)
                {   // c1 is the gene we should be dealing with
                    if (f1 >= f2)
                    {
                        // ensure we have enough NodeGenes for the connections
                        // add hidden nodes onto the end of the list of node genes
                        int minNeededNode = Math.max(c1.inNode, c1.outNode);
                        for (int i = newGenome.nodeGenes.size(); i <= minNeededNode; i++)
                        {
                            nodes.add(new NodeGene(NodeType.HIDDEN));
                        }
                        if (!newGenome.wouldMakeRecurrent(c1))
                        {
                            connections.add(c1.clone());
                        }
                    }
                    index1++;
                } else
                {   // c2 is the gene we should be dealing with
                    if (f2 >= f1)
                    {
                        // ensure we have enough NodeGenes for the connections
                        // add hidden nodes onto the end of the list of node genes
                        int minNeededNode = Math.max(c2.inNode, c2.outNode);
                        for (int i = newGenome.nodeGenes.size(); i <= minNeededNode; i++)
                        {
                            nodes.add(new NodeGene(NodeType.HIDDEN));
                        }
                        if (!newGenome.wouldMakeRecurrent(c2))
                        {
                            connections.add(c2.clone());
                        }
                    }
                    index2++;
                }
            }
        }
        // now we're dealing with excess genes. inherit disjoint and excess genes from more fit parent (or both if equal fitness)
        if (index1 == g1.connectionGenes.size() && index2 < g2.connectionGenes.size())
        {   // g2 has excess genes
            if (f2 >= f1)
            {
                for (; index2 < g2.connectionGenes.size(); index2++)
                {
                    // ensure we have enough NodeGenes for the connections
                    // add hidden nodes onto the end of the list of node genes
                    ConnectionGene c = g2.connectionGenes.get(index2);
                    int minNeededNode = Math.max(c.inNode, c.outNode);
                    for (int i = newGenome.nodeGenes.size(); i <= minNeededNode; i++)
                    {
                        nodes.add(new NodeGene(NodeType.HIDDEN));
                    }
                    if (!newGenome.wouldMakeRecurrent(c))
                    {
                        connections.add(c.clone());
                    }
                }
            }
        } else if (index2 == g2.connectionGenes.size() && index1 < g1.connectionGenes.size())
        {   // g1 has excess genes
            if (f1 >= f2)
            {
                for (; index1 < g1.connectionGenes.size(); index1++)
                {
                    // ensure we have enough NodeGenes for the connections
                    // add hidden nodes onto the end of the list of node genes
                    ConnectionGene c = g1.connectionGenes.get(index1);
                    int minNeededNode = Math.max(c.inNode, c.outNode);
                    for (int i = newGenome.nodeGenes.size(); i <= minNeededNode; i++)
                    {
                        nodes.add(new NodeGene(NodeType.HIDDEN));
                    }
                    if (!newGenome.wouldMakeRecurrent(c))
                    {
                        connections.add(c.clone());
                    }
                }
            }
            
        } else
        {   // none have any excess genes
            
        }
        return newGenome;
    }
    
    /**
     * Finds a valid new connection to make given the following conditions:
     * * A new connection must not duplicate any other connections
     * * A new connection must not make the network recurrent
     * @param r Random object to use
     * @param g the genome in question
     * @return a ConnectionGene representing the new connection, with weight 1
     */
    private ConnectionGene validNewConnection(Random r, Genome g)
    {
        // TODO
        return null;
    }
}
