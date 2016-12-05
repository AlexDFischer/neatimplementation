package neatimplementation;
public class NodeGene
{
    public final NodeType type;
    
    public NodeGene(NodeType type)
    {
        this.type = type;
    }
    
    @Override
    public NodeGene clone()
    {
        return new NodeGene(type);
    }
}
