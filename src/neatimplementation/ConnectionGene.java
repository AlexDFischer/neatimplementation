package neatimplementation;
public class ConnectionGene
{
    public final int inNode, outNode;
    public double weight;
    public boolean expressed;
    public final int innovationNum;
    
    public ConnectionGene(int inNode, int outNode, double weight, boolean expressed, int innovationNum)
    {
        this.inNode = inNode;
        this.outNode = outNode;
        this.weight = weight;
        this.expressed = expressed;
        this.innovationNum = innovationNum;
    }
    
    @Override
    public ConnectionGene clone()
    {
        return new ConnectionGene(inNode, outNode, weight, expressed, innovationNum);
    }
}
