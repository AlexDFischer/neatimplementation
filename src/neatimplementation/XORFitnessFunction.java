package neatimplementation;

public class XORFitnessFunction implements FitnessFunction
{
    @Override
    public double fitness(Genome g)
    {
        double output00 = g.propogate(new double[] {0,0})[0];
        double output01 = g.propogate(new double[] {0,1})[0];
        double output10 = g.propogate(new double[] {1,0})[0];
        double output11 = g.propogate(new double[] {1,1})[0];
        
        return 4.0
             - (output00 - 0.0) * (output00 - 0.0)
             - (output01 - 1.0) * (output01 - 1.0)
             - (output10 - 1.0) * (output10 - 1.0)
             - (output11 - 0.0) * (output11 - 0.0);
    }
}
