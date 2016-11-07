package neatimplementation;
import java.util.ArrayList;
import java.util.List;

public class Genome
{
    public final ArrayList<NodeGene> nodeGenes;
    public final ArrayList<ConnectionGene> connectionGenes;
    public final int numOutputs;
    
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
        int numOutputs = 0;
        for (NodeGene nodeGene : nodeGenes)
        {
            if (nodeGene.type == NodeType.OUTPUT)
            {
                numOutputs++;
            }
        }
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
    
    /**
     * Crosses over the two genomes, returning their offspring
     * @param g the other parent to cross with this
     * @return
     */
    public Genome matchMutate(Genome g)
    {
        return null; //TODO
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
        Node[] nodes = new Node[nodeGenes.size()];
        for (int i = 0; i < nodeGenes.size(); i++)
        {
            nodes[i] = new Node(nodeGenes.get(i).type);
            if (nodeGenes.get(i).type == NodeType.INPUT)
            {
                nodes[i].activation = input[i];
            }
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
