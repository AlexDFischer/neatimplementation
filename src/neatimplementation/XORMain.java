package neatimplementation;

public class XORMain
{
    public static void main(String[] args)
    {
        Genome initialGenome = Static.blankXORGenome();
        FitnessFunction f = new XORFitnessFunction();
        NEATDriver driver = new NEATDriver
        (
            100,
            1.5,
            Static.DEFAULT_C1,
            Static.DEFAULT_C2,
            Static.DEFAULT_C3,
            initialGenome, 
            f
        );
        driver.proportionMutateWithoutCrossover = 0.75;
        while (f.fitness(driver.mostFitGenome()) < 3.9)
        {
            driver.printDetailedStatusReport();
            System.out.println("_______________");
            driver.nextGeneration();
        }
        driver.printDetailedStatusReport();
        System.out.println(driver.mostFitGenome().toString());
    }
}
