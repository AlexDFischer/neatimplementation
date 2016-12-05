package neatimplementation;

import java.util.Random;

public class MainTest
{

    public static void main(String[] args)
    {
        Random r = new Random(System.currentTimeMillis());
        
        /*Genome g = Static.testGenome();
        System.out.println(g);
        Random r = new Random(System.currentTimeMillis());
        Genome g2 = g.mutateWithoutCrossover(r, Static.DEFAULT_PROBABILITY_UNIFORMLY_PERTURBED, Static.DEFAULT_PERTUBATION_RANGE, Static.DEFAULT_NEW_RANDOM_VAL_STD_DEV);
        System.out.println(g2);*/
        
        Genome g = Static.blankXORGenome();
        g.connectionGenes.add(new ConnectionGene(0, 3, 1, true, 2));
        g.nodeGenes.add(new NodeGene(NodeType.HIDDEN));
        
        Genome g2 = g.clone();
        NEATDriver driver = new NEATDriver(g, new XORFitnessFunction());
        System.out.println(g.wouldMakeRecurrent(2, 3));
        driver.probabilityNewConnection = 1.0;
        System.out.println(driver.crossOver(r, g, g2));
    }
    
    private static Genome defaultMutate(Random r, Genome g)
    {
        return g.mutateWithoutCrossover(r, 0.5, 3, 1);
        
    }

}
