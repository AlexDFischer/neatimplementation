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
        
        Genome g = Static.notYetXORGenome();
        System.out.println(g.propogate(new double[] {1,1})[0]);
        System.out.println(new XORFitnessFunction().fitness(g));
        
        Genome g2 = g.clone();
        while (new XORFitnessFunction().fitness(g2) <= 3.99)
        {
            g2 = defaultMutate(r, g2);
        }
        System.out.println(g2);
        System.out.println(new XORFitnessFunction().fitness(g2));
    }
    
    private static Genome defaultMutate(Random r, Genome g)
    {
        return g.mutateWithoutCrossover(r, 0.5, 3, 1);
        
    }

}
