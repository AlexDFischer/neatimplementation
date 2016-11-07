package neatimplementation;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PopulationTest
{
    private Population p;

    @Before
    public void setUp()
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
        Genome g = new Genome(nodeGenesList, connectionGenesList);
        this.p = new Population(g);
    }

    @Test
    public void testIdenticalGenomes()
    {
        // TODO
    }

}
