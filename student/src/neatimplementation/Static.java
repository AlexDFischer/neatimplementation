package neatimplementation;

import java.util.ArrayList;

public class Static
{
    public static final int DEFAULT_POP_SIZE = 150;
    public static final double DEFAULT_THRESHOLD_DIFFERENCE = 3.0;
    public static final double DEFAULT_C1 = 1.0, DEFAULT_C2 = 1.0, DEFAULT_C3 = 0.4;
    
    public static final double
        DEFAULT_PROPORTION_MUTATE_WITHOUT_CROSSOVER = 0.5,
        DEFAULT_PROBABILITY_UNIFORMLY_PERTURBED = 0.9,
        DEFAULT_PERTUBATION_RANGE = 1.0,
        DEFAULT_NEW_RANDOM_VAL_STD_DEV = 0.5,
        DEFAULT_PROBABILITY_PARENT_GENE_DISABLED_STILL_DISABLED = 0.75,
        DEFAULT_PROBABILITY_MUTATE = 0.8,
        DEFAULT_PROBABILITY_ADD_NEW_NODE = 0.05,
        DEFAULT_PROBABILITY_NEW_CONNECTION = 0.3;
    
    public static final int
        DEFAULT_MIN_NETWORKS_TO_COPY_MOST_FIT_NETWORK = 5;
    
    public static double sigmoid(double x)
    {
        //return Math.tanh(4.9*x);
        return 1/(1+Math.exp(-4.9*x));
    }
    
    public static Genome blankXORGenome()
    {
        NodeGene[] nodeGenes = new NodeGene[]
        {
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.OUTPUT)
        };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[]
        {
            new ConnectionGene(0, 2, 0.0, true, 0),
            new ConnectionGene(1, 2, 0.0, true, 1)
        };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        return new Genome(nodeGenesList, connectionGenesList);
    }
    
    public static Genome testGenome()
    {
        NodeGene[] nodeGenes = new NodeGene[]
        {
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.OUTPUT),
            new NodeGene(NodeType.OUTPUT),
            new NodeGene(NodeType.HIDDEN)
        };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[]
        {
            new ConnectionGene(0, 4, 1.0, true, 0),
            new ConnectionGene(1, 4, -2.0, true, 1),
            new ConnectionGene(4, 2, -1.0, true, 3),
            new ConnectionGene(1, 2, -0.01, true, 4),
            new ConnectionGene(4, 3, 0.1, true, 2)
        };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        return new Genome(nodeGenesList, connectionGenesList);
    }
    
    public static Genome xorGenome()
    {
        NodeGene[] nodeGenes = new NodeGene[]
        {
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.BIAS),
            new NodeGene(NodeType.OUTPUT),
            new NodeGene(NodeType.HIDDEN)
        };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[]
        {
            new ConnectionGene(0, 3, 3, true, 0),
            new ConnectionGene(1, 3, 3, true, 1),
            new ConnectionGene(0, 4, 4, true, 2),
            new ConnectionGene(1, 4, 4, true, 3),
            new ConnectionGene(2, 4, -6, true, 4),
            new ConnectionGene(2, 3, -2, true, 6),
            new ConnectionGene(3, 3, -6, true, 5)
        };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        return new Genome(nodeGenesList, connectionGenesList);
    }
    
    public static Genome notYetXORGenome()
    {
        NodeGene[] nodeGenes = new NodeGene[]
        {
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.HIDDEN),
            new NodeGene(NodeType.HIDDEN),
            new NodeGene(NodeType.OUTPUT)
        };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[]
        {
            new ConnectionGene(0, 4, 3, true, 0),
            new ConnectionGene(1, 4, 3, true, 1),
            new ConnectionGene(0, 2, 4, true, 2),
            new ConnectionGene(1, 2, 4, true, 3),
            new ConnectionGene(0, 3, 4, true, 4),
            new ConnectionGene(1, 3, 4, true, 5),
            new ConnectionGene(2, 4, -6, true, 6),
            new ConnectionGene(3, 4, -6, true, 7)
        };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        return new Genome(nodeGenesList, connectionGenesList);
    }
}