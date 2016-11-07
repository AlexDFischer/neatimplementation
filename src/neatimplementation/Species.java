package neatimplementation;

import java.util.ArrayList;
import java.util.List;

public class Species
{
    private final List<Genome> genomes;
    private final Genome representative;
    
    public Species(Genome representative)
    {
        this.genomes = new ArrayList<>();
        this.representative = representative;
    }
    
    public void add(Genome genome)
    {
        this.genomes.add(genome);
    }
    
    public boolean contains(Genome genome)
    {
        return this.genomes.contains(genome);
    }
    
    public Genome get(int i)
    {
        return this.genomes.get(i);
    }
    
    public boolean inSpecies(Genome genome, double thresholdDifference, double c1, double c2, double c3)
    {
        return this.representative.distance(genome, c1, c2, c3) <= thresholdDifference;
    }
    
    public boolean isEmpty()
    {
        return this.genomes.size() > 0;
    }
    
    public Genome representative()
    {
        return this.representative;
    }
}
