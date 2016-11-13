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
    
    /**
     * Tells whether two connections are equivalent, ie, have the same in/out nodes
     * @param that the other ConnectionGene to compare this to
     * @return true if they are equivalent, false otherwise
     */
    public boolean equivalent(ConnectionGene that)
    {
        return this.inNode == that.inNode && this.outNode == that.outNode;
    }
}
