package neatimplementation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PopulationTest
{
    private Population p;
    private Genome g;

    @Before
    public void setUp()
    {
        this.g = Static.testGenome();
        this.p = new Population(g);
    }

    @Test
    public void testIdenticalGenomes()
    {
        setUp();
        assertEquals(1, p.numSpecies());
        List<Genome> newGenomes = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            newGenomes.add(g.clone());
        }
        p.nextGeneration(newGenomes);
        assertEquals(1, p.numSpecies());
    }
    
    @Test
    public void testDifferentGenomesButSameSpecies()
    {
        setUp();
        assertEquals(1, p.numSpecies());
        Genome g2 = this.g.clone();
        Genome g3 = this.g.clone();
        Genome g4 = this.g.clone();
        g2.connectionGenes.get(0).weight = 0.5;
        g3.connectionGenes.get(1).weight = -1.5;
        g4.connectionGenes.add(new ConnectionGene(0, 4, 1.0, true, 5));
        
        System.out.println(distance(g2, g));
        System.out.println(distance(g3, g));
        System.out.println(distance(g4, g));
        
        List<Genome> newGenomes = new ArrayList<>();
        for (int i = 0; i < 97; i++)
        {
            newGenomes.add(g.clone());
        }
        newGenomes.add(g2);
        newGenomes.add(g3);
        newGenomes.add(g4);
        this.p.nextGeneration(newGenomes);
        assertEquals(1, p.numSpecies());
    }
    
    @Test
    public void testTwoSpecies()
    {
        setUp();
        Genome g2 = this.g.clone();
        g2.connectionGenes.get(0).weight = 101.0;
        
        assertTrue(Math.abs(8.0 - distance(g, g2)) < 0.01);
        
        List<Genome> newGenomes = new ArrayList<>();
        for (int i = 0; i < 50; i++)
        {
            newGenomes.add(g.clone());
        }
        for (int i = 0; i < 50; i++)
        {
            newGenomes.add(g2.clone());
        }
        this.p.nextGeneration(newGenomes);
        assertEquals(2, this.p.numSpecies());
    }
    
    private double distance(Genome g1, Genome g2)
    {
        return g1.distance(g2, Static.DEFAULT_C1, Static.DEFAULT_C2, Static.DEFAULT_C3);
    }
}
