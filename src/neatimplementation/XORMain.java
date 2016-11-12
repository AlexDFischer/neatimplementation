package neatimplementation;

public class XORMain
{
    public static void main(String[] args)
    {
        Genome initialGenome = Static.notYetXORGenome();
        FitnessFunction f = new XORFitnessFunction();
        NEATDriver driver = new NEATDriver
        (
            100,
            1.0,
            Static.DEFAULT_C1,
            Static.DEFAULT_C2,
            Static.DEFAULT_C3,
            initialGenome, 
            f
        );
        driver.proportionMutateWithoutCrossover = 1.0;
        while (f.fitness(driver.mostFitGenome()) < 3.99)
        {
            driver.printStatusReport();
            driver.nextGeneration();
        }
        driver.printStatusReport();
        driver.nextGeneration();
        System.out.println(driver.mostFitGenome().toString());
    }
}
