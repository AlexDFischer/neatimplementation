package neatimplementation;

import java.util.ArrayList;

public class Static
{
    public static final int DEFAULT_POP_SIZE = 100;
    public static final double DEFAULT_THRESHOLD_DIFFERENCE = 3.0;
    public static final double DEFAULT_C1 = 1.0, DEFAULT_C2 = 1.0, DEFAULT_C3 = 0.4;
    
    public static double sigmoid(double x)
    {
        return Math.tanh(4.9*x);
    }
    
    public static Genome testGenome()
    {
        NodeGene[] nodeGenes = new NodeGene[] { new NodeGene(NodeType.INPUT), new NodeGene(NodeType.INPUT),
                new NodeGene(NodeType.HIDDEN), new NodeGene(NodeType.OUTPUT), new NodeGene(NodeType.OUTPUT) };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[] { new ConnectionGene(0, 2, 1.0, true, 0),
                new ConnectionGene(1, 2, -2.0, true, 1), new ConnectionGene(2, 3, 0.1, true, 2),
                new ConnectionGene(2, 4, -1.0, true, 3), new ConnectionGene(1, 3, -0.01, true, 4) };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        return new Genome(nodeGenesList, connectionGenesList);
    }
}