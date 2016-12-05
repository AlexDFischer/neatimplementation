package neatimplementation;
import java.util.ArrayList;

public class Test
{

    public static void main(String[] args)
    {
        NodeGene[] nodeGenes = new NodeGene[]
        {
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.INPUT),
            new NodeGene(NodeType.HIDDEN),
            new NodeGene(NodeType.OUTPUT),
            new NodeGene(NodeType.OUTPUT)
        };
        ArrayList<NodeGene> nodeGenesList = new ArrayList<>();
        for (NodeGene g : nodeGenes)
        {
            nodeGenesList.add(g);
        }
        ConnectionGene[] connectionGenes = new ConnectionGene[]
        {
            new ConnectionGene(0, 2, 1.0, true, 0),
            new ConnectionGene(1, 2, -2.0, true, 1),
            new ConnectionGene(2, 3, 0.1, true, 2),
            new ConnectionGene(2, 4, -1.0, true, 3),
            new ConnectionGene(1, 3, -0.01, true, 4)
        };
        ArrayList<ConnectionGene> connectionGenesList = new ArrayList<>();
        for (ConnectionGene g : connectionGenes)
        {
            connectionGenesList.add(g);
        }
        Genome genome = new Genome(nodeGenesList, connectionGenesList);
        double[] outputs = genome.propogate(new double[] {1.0, 0.5});
        for (double o : outputs)
        {
            System.out.println(o);
        }
    }

}
