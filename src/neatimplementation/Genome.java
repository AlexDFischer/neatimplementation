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
        ArrayList<NodeGene> newNodeGenes = new ArrayList<>();
        for (NodeGene nodeGene : this.nodeGenes)
        {
            newNodeGenes.add(nodeGene.clone());
        }
        ArrayList<ConnectionGene> newConnectionGenes = new ArrayList<>();
        for (ConnectionGene connectionGene : this.connectionGenes)
        {
            newConnectionGenes.add(connectionGene.clone());
        }
        return new Genome(newNodeGenes, newConnectionGenes);
    }
    
    public double distance(Genome that, double c1, double c2, double c3)
    {
        double E, // number of excess genes
               D = 0, // number of disjoint genes
               N, // number of genes in the larger genome
               W = 0; // average weight difference between matching genes
        int numMatchingGenes = 0;
        N = Math.max(this.connectionGenes.size(), that.connectionGenes.size());
        int thisGeneIndex = 0,thatGeneIndex = 0;
        while (thisGeneIndex < this.connectionGenes.size() && thatGeneIndex < that.connectionGenes.size())
        {
            if (this.connectionGenes.get(thisGeneIndex).innovationNum == that.connectionGenes.get(thatGeneIndex).innovationNum)
            {
                numMatchingGenes++;
                W += Math.abs(this.connectionGenes.get(thisGeneIndex).weight - that.connectionGenes.get(thatGeneIndex).weight);
                thisGeneIndex++;
                thatGeneIndex++;
                // TODO what if we used squared instead of abs here? seems like it might be better. What if we didn't even use abs? The paper is ambiguous
            } else
            {
                if (this.connectionGenes.get(thisGeneIndex).innovationNum < that.connectionGenes.get(thatGeneIndex).innovationNum)
                {
                    thisGeneIndex++;
                } else
                {
                    thatGeneIndex++;
                }
                D++;
            }
        }
        // now that we've counted the matching and disjoint genes, count excess genes.
        if (thisGeneIndex == this.connectionGenes.size() && thatGeneIndex == that.connectionGenes.size()) // no excess genes
        {
            E = 0.0;
        } else if (thisGeneIndex == this.connectionGenes.size())
        {
            // that has excess genes
            E = that.connectionGenes.size() - thatGeneIndex;
        } else
        {
            // this has excess genes;
            E = this.connectionGenes.size() - thisGeneIndex;
        }
        W = (numMatchingGenes != 0) ? W / numMatchingGenes : 0.0;
        //System.out.println("W = " + W);
        //System.out.println("numMatchingGenes = " + numMatchingGenes);
        return (c1 * E + c2 * D)/N + c3 * W;
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
        // build a structure that allows us to calculate it from the output backwards
        Node[] nodes = usableNetwork();
        for (int i = 0; i < nodeGenes.size(); i++)
        {
            if (nodeGenes.get(i).type == NodeType.INPUT)
            {
                nodes[i].activation = input[i];
            }
        }
        // set up output array
        ArrayList<Double> outputs = new ArrayList<>();
        
        // now calculate each output
        for (Node node : nodes)
        {
            if (node.type == NodeType.OUTPUT)
            {
                outputs.add(node.getOutput());
            }
        }
        double[] outputDoubles = new double[this.numOutputs];
        for (int i = 0; i < outputDoubles.length; i++)
        {
            outputDoubles[i] = (double)outputs.get(i);
        }
        return outputDoubles;
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
     * Creates a more usable linked graph structure
     * @return a list of {@link Node} instances complete with their connections
     */
    private Node[] usableNetwork()
    {
        Node[] nodes = new Node[nodeGenes.size()];
        for (int i = 0; i < nodeGenes.size(); i++)
        {
            nodes[i] = new Node(nodeGenes.get(i).type);
        }
        for (ConnectionGene c : connectionGenes)
        {
            if (c.expressed)
            {
                Node outNode = nodes[c.outNode];
                Node inNode = nodes[c.inNode];
                outNode.inputs.add(inNode);
                outNode.weights.add(c.weight);
            }
        }
        return nodes;
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
     * Tells whether or not a new connection with the given inputs and outputs
     * would make the network recurrent
     * @param inNode the input of the hypothetical connection
     * @param outNode the output of the hypothetical connection
     * @return true if it would make the network recurrent, false otherwise
     */
    public boolean wouldMakeRecurrent(int inNode, int outNode)
    {
        // see if we can get from c.inNode to c.outNode going backwards via inputs
        Node[] nodes = usableNetwork();
        // perform a depth first search: if we get to an input, that's the end of this branch
        return wouldMakeRecurrentHelper(nodes[inNode], nodes[outNode]);
    }
    
    private boolean wouldMakeRecurrentHelper(Node currentNode, Node nodeToSearchFor)
    {
        if (currentNode == nodeToSearchFor)
        {
            return true;
        } else if (currentNode.type == NodeType.INPUT && currentNode.type == NodeType.BIAS)
        {
            return false;
        } else
        {
            for (Node n : currentNode.inputs)
            {
                if (wouldMakeRecurrentHelper(n, nodeToSearchFor))
                {
                    return true;
                }
            }
            return false;
        }
    }

    private static class Node
    {
        double activation = 0;
        boolean hasActivation = false;
        final NodeType type;
        List<Node> inputs;
        List<Double> weights;

        public Node(NodeType type)
        {
            this.type = type;
            this.inputs = new ArrayList<Node>();
            this.weights = new ArrayList<Double>();
        }

        public double getOutput()
        {
            switch (this.type)
            {
                case INPUT:
                {
                    return this.activation;
                    // break;
                }
                case BIAS:
                {
                    return 1.0;
                    // break
                }
                default: // hidden or output
                {
                    if (!hasActivation)
                    {
                        double weightedInputs = 0.0;
                        for (int i = 0; i < this.inputs.size(); i++)
                        {
                            weightedInputs += this.weights.get(i) * this.inputs.get(i).getOutput();
                        }
                        this.activation = Static.sigmoid(weightedInputs);
                        this.hasActivation = true;
                    }
                    return this.activation;
                }
            }
        }
    }
}
