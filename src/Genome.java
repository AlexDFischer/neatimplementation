import java.util.ArrayList;
import java.util.List;

public class Genome
{
    private final List<NodeGene> nodeGenes;
    private final List<ConnectionGene> connectionGenes;
    public final int numOutputs;
    
    /**
     * Creates a new genome with the given genes.
     * @param nodeGenes The node genes of the genome. Precondition: inputs must
     * be first, then outputs. Nodes must be in order.
     * @param connectionGenes the connection genes of the genome.
     */
    public Genome(List<NodeGene> nodeGenes, List<ConnectionGene> connectionGenes)
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

    public double[] propogate(double[] input)
    {
        // build a structure that allows us to calculate it from the output backwards
        Node[] nodes = new Node[nodeGenes.size()];
        for (int i = 0; i < nodeGenes.size(); i++)
        {
            nodes[i] = new Node(nodeGenes.get(i).type);
            if (nodeGenes.get(i).type == NodeType.INPUT)
            {
                nodes[i].output = input[i];
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
        Double output = null;
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
                    return this.output;
                    // break;
                }
                case BIAS:
                {
                    return 1.0;
                    // break
                }
                default: // hidden or output
                {
                    if (this.output == null)
                    {
                        double weightedInputs = 0.0;
                        for (int i = 0; i < this.inputs.size(); i++)
                        {
                            weightedInputs += this.weights.get(i) * this.inputs.get(i).getOutput();
                            Node input = this.inputs.get(i);
                        }
                        this.output = Static.sigmoid(weightedInputs);
                    }
                    return this.output;
                }
            }
        }
    }
}
