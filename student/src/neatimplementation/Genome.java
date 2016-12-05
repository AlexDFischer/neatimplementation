package neatimplementation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome
{
    public final ArrayList<NodeGene> nodeGenes;
    public final ArrayList<ConnectionGene> connectionGenes;
    private final int numBiases, numInputs, numOutputs;
    
    /**
     * Creates a new genome with the given genes.
     * @param nodeGenes The node genes of the genome. Precondition: inputs must
     * be first, then outputs. Nodes must be in order.
     * @param connectionGenes the connection genes of the genome.
     */
    public Genome(ArrayList<NodeGene> nodeGenes, ArrayList<ConnectionGene> connectionGenes)
    {
        this.nodeGenes = nodeGenes;
        this.connectionGenes = connectionGenes;
        int numBiases = 0, numInputs = 0, numOutputs = 0;
        for (NodeGene nodeGene : nodeGenes)
        {
            if (nodeGene.type == NodeType.BIAS)
            {
                numBiases++;
            } else if (nodeGene.type == NodeType.INPUT)
            {
                numInputs++;
            } else if (nodeGene.type == NodeType.OUTPUT)
            {
                numOutputs++;
            }
        }
        this.numBiases = numBiases;
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
    }
    
    /**
     * Makes a deep copy of the Genome, ie, one with the connection genes and
     * node genes being new objects.
     */
    @Override
    public Genome clone()
    {
        // TODO
    }
    
    /**
     * Computes the distance, as specified in the documentation
     * @param that the genome to compute the distance from
     * @param c1 first constant in documentation
     * @param c2 second constant in documentation
     * @param c3 third constant in documentation
     * @return the computed distance
     */
    public double distance(Genome that, double c1, double c2, double c3)
    {
        // TODO
    }
    
    public int getNumBiases()
    {
        return this.numBiases;
    }
    
    public int getNumInputs()
    {
        return this.numInputs;
    }
    
    public int getNumOutputs()
    {
        return this.numOutputs;
    }
    
    /**
     * Mutates the connection weights in this genome.
     * @param r the {@link Math.Random} object to use
     * @param probabilityUniformlyPerturbed the probability that a weight will
     * be uniformly perturbed
     * @param pertubationRange if a weight is uniformly perturbed, the range in
     * which it will be perturbed. The new weight will be in the set
     * [weight - pertubationRange, weight + pertubationRange]
     * @param newRandomValStdDev if a weight is assigned a new random value
     * instead of uniformly perturbed, the standard deviation to use to assign
     * a new weight to the connection
     */
    public void mutateConnectionWeights(Random r, double probabilityUniformlyPerturbed, double pertubationRange, double newRandomValStdDev)
    {
        for (ConnectionGene c : this.connectionGenes)
        {
            double p = r.nextDouble();
            if (p < probabilityUniformlyPerturbed)
            {
                // we are doing uniform pertubation, not new random value
                c.weight += (r.nextDouble() * 2 - 1) * pertubationRange;
            } else
            {
                // we are assigning a new random value, not uniformly perturbing
                c.weight = r.nextGaussian() * newRandomValStdDev;
            }
        }
    }
    
    /**
     * Returns a new genome with mutated connection weights
     * @param r the {@link Math.Random} object to use
     * @param probabilityUniformlyPerturbed the probability that a weight will
     * be uniformly perturbed
     * @param pertubationRange if a weight is uniformly perturbed, the range in
     * which it will be perturbed. The new weight will be in the set
     * [weight - pertubationRange, weight + pertubationRange]
     * @param newRandomValStdDev if a weight is assigned a new random value
     * instead of uniformly perturbed, the standard deviation to use to assign
     * a new weight to the connection
     * @return the new Genome
     */
    public Genome mutateWithoutCrossover(Random r, double probabilityUniformlyPerturbed, double pertubationRange, double newRandomValStdDev)
    {
        Genome g = this.clone();
        g.mutateConnectionWeights(r, probabilityUniformlyPerturbed, pertubationRange, newRandomValStdDev);
        return g;
    }
    
    /**
     * Returns the output of the network with the given input
     * @param input the inputs to the network. Array must have same length as
     * number of inputs
     * @return outputs of the network. Array will have same length as number of
     * outputs of the network
     */
    public double[] propogate(double[] input)
    {
        // TODO
    }
    
    /**
     * Returns a String representation of this Genome. The format will be a
     * newline-separated list of connections as follows:
     * 0: 0 -> 2 (0.05)
     * 1: 0 -> 3 (-0.5)
     * 2: 2 -> 3 (0.078)
     * 5: 1 -> 1 (-1.429)
     * ...
     * Where the first number is the innovation number, the second is the in
     * node, the third is the out node, and the fourth is the connection weight
     * for each connection.
     * @return the String in the above format
     */
    @Override
    public String toString()
    {
        String result = "";
        for (ConnectionGene c : this.connectionGenes)
        {
            result += c.innovationNum + ": " + c.inNode + " -> " + c.outNode + " (weight: " + c.weight + ", enabled: " + c.expressed + ")\n";
        }
        return result;
    }
    
    /**
     * Tells whether or not this connection gene would make the network recurrent
     * @param c the connection in questions
     * @return true if c would make the network recurrent, false otherwise
     */
    public boolean wouldMakeRecurrent(ConnectionGene c)
    {
        return wouldMakeRecurrent(c.inNode, c.outNode);
    }
    
    /**
     * Tells whether adding a connection from inNode to outNode would make this connection recurrent
     * @param inNode the inNode of a hypothetical connection
     * @param outNode the outNode of a hypothetical connection
     * @return true if the hypothetical connection would make the network recurrent, false otherwise
     */
    public boolean wouldMakeRecurrent(int inNode, int outNode)
    {
        // TODO
    }
}
